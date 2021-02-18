package com.ry.framework.security.service;

import com.ry.common.constant.Constants;
import com.ry.common.core.dledc.request.CheckUserAccountRequest;
import com.ry.common.core.dledc.response.CheckUserAccountResponse;
import com.ry.common.enums.SourceTypeEnum;
import com.ry.common.exception.CustomException;
import com.ry.common.exception.user.CaptchaException;
import com.ry.common.exception.user.CaptchaExpireException;
import com.ry.common.exception.user.UserPasswordNotMatchException;
import com.ry.common.model.TwoTuple;
import com.ry.common.utils.MessageUtils;
import com.ry.framework.config.CommonConfig;
import com.ry.framework.manager.AsyncManager;
import com.ry.framework.manager.factory.AsyncFactory;
import com.ry.framework.redis.RedisCache;
import com.ry.framework.security.LoginBody;
import com.ry.framework.security.LoginResponse;
import com.ry.framework.security.LoginUser;
import com.ry.framework.web.domain.AjaxResult;
import com.ry.project.bussiness.domain.bo.User;
import com.ry.project.bussiness.service.IDledcService;
import com.ry.project.bussiness.service.IUserService;
import com.ry.project.system.domain.SysUser;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 登录校验方法
 *
 * @author
 */
@Component
public class SysLoginService {
    @Autowired
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    IUserService userService;

    @Autowired
    private RedisCache redisCache;
    @Autowired
    IDledcService dledcService;

    /**
     * 登录验证
     *
     * @return 结果
     */
    public AjaxResult login(LoginBody loginBody) {
        String verifyKey = Constants.CAPTCHA_CODE_KEY + loginBody.getUuid();
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        LoginUser loginUser;
        LoginResponse response;
        if (captcha == null) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(loginBody.getUsername(), 0, loginBody.getLoginType(), Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
            throw new CaptchaExpireException();
        }
        if (!loginBody.getCode().equalsIgnoreCase(captcha)) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(loginBody.getUsername(), 0, loginBody.getLoginType(), Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
            throw new CaptchaException();
        }
        if (loginBody.getLoginType() == 0) {
            // 用户验证
            Authentication authentication;
            try {
                // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
                authentication = authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(loginBody.getUsername(), loginBody.getPassword()));
            } catch (Exception e) {
                if (e instanceof BadCredentialsException) {
                    AsyncManager.me().execute(AsyncFactory.recordLogininfor(loginBody.getUsername(), 0, loginBody.getLoginType(), Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                    throw new UserPasswordNotMatchException();
                } else {
                    AsyncManager.me().execute(AsyncFactory.recordLogininfor(loginBody.getUsername(), 0, loginBody.getLoginType(), Constants.LOGIN_FAIL, e.getMessage()));
                    throw new CustomException(e.getMessage());
                }
            }
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(loginBody.getUsername(), 0, loginBody.getLoginType(), Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
            loginUser = (LoginUser) authentication.getPrincipal();
            loginUser.setUserType(0);
            loginUser.setUserId(loginUser.getUser().getUserId());
            response = new LoginResponse(0, tokenService.createToken(loginUser));
        } else {
            User user;
            var userId = 0L;
            try {
                var client = dledcService.getDledcConfig(1);
                var checkUserAccountRequest = new CheckUserAccountRequest(loginBody.getUsername(), loginBody.getPassword());
                var checkUserAccountResponse = client.Execute(CheckUserAccountResponse.class, checkUserAccountRequest);
                if (checkUserAccountResponse == null
                        || checkUserAccountResponse.getStatusCode() != 1
                        || checkUserAccountResponse.getData() == null
                        || checkUserAccountResponse.getData().getSingleData() == null) {
                    throw new UserPasswordNotMatchException();
                }
                var uId = checkUserAccountResponse.getData().getSingleData().getUid();
                if (CommonConfig.isUseSynchronizationUse()) {
                    TwoTuple<Long, Integer> twoTuple = dledcService.sychroUser(uId);
                    userId = twoTuple.first;
                } else {
                    user = userService.readBySource(SourceTypeEnum.JiaoFuPingTai.getCode(), uId);
                    if (user == null) {
                        user = new User();
                    }
                    if (user.getUserId() == null || user.getUserId().intValue() == 0) {
                        TwoTuple<Long, Integer> twoTuple = dledcService.sychroUser(uId);
                        userId = twoTuple.first;
                    } else {
                        userId = user.getUserId();
                        if (user.getStudentCode() != "等待同步") {
                            user.setStudentCode("等待同步");
                            //if (user.getUserId() > 0) {
                            userService.updateById(user);
                            //} else {
                            //    userService.save(user);
                            //}
                        }
                    }
                }
            } catch (Exception e) {
                if (e instanceof UserPasswordNotMatchException) {
                    AsyncManager.me().execute(AsyncFactory.recordLogininfor(loginBody.getUsername(), 0, loginBody.getLoginType(), Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                    throw new UserPasswordNotMatchException();
                } else {
                    AsyncManager.me().execute(AsyncFactory.recordLogininfor(loginBody.getUsername(), 0, loginBody.getLoginType(), Constants.LOGIN_FAIL, e.getMessage()));
                    throw new CustomException(e.getMessage());
                }
            }
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(loginBody.getUsername(), 0, loginBody.getLoginType(), Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
            user = userService.getByKey(userId, true);
            loginUser = new LoginUser();
            loginUser.setUser(new SysUser(user.getPassword(), user.getUserTrueName()));
            loginUser.setUserType(user.getUserTypeId());
            loginUser.setUserId(user.getUserId());
            response = new LoginResponse(user.getUserTypeId(), tokenService.createToken(loginUser));
        }

        return AjaxResult.success(response);
    }
}
