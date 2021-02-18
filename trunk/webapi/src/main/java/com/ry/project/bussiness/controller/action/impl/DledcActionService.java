package com.ry.project.bussiness.controller.action.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ry.common.constant.Constants;
import com.ry.common.core.dledc.DledcDefaultClient;
import com.ry.common.core.dledc.response.ConnectTokenResponse;
import com.ry.common.core.dledc.response.ConnectUserInfoResponse;
import com.ry.common.core.dledc.utils.DledcConvertHelper;
import com.ry.common.enums.SourceTypeEnum;
import com.ry.common.model.TwoTuple;
import com.ry.common.model.dledc.request.DledcConfigApiRequest;
import com.ry.common.model.dledc.request.DledcLoginApiRequest;
import com.ry.common.model.dledc.request.DledcMobileLoginApiRequest;
import com.ry.common.model.dledc.response.DledcConfigResponse;
import com.ry.common.model.dledc.response.DledcLoginResponse;
import com.ry.common.model.dledc.response.DledcMobileLoginResponse;
import com.ry.common.utils.MessageUtils;
import com.ry.framework.config.CommonConfig;
import com.ry.framework.manager.AsyncManager;
import com.ry.framework.manager.factory.AsyncFactory;
import com.ry.framework.redis.RedisCache;
import com.ry.framework.security.LoginUser;
import com.ry.framework.security.service.TokenService;
import com.ry.framework.web.domain.AjaxResult;
import com.ry.project.bussiness.controller.action.IDledcActionService;
import com.ry.project.bussiness.domain.bo.User;
import com.ry.project.bussiness.service.IDledcService;
import com.ry.project.bussiness.service.IUserService;
import com.ry.project.system.domain.SysUser;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * <p>
 * 消息 服务实现类
 * </p>
 *
 * @author xrq
 * @since 2020-04-26
 */
@Slf4j
@Service
public class DledcActionService implements IDledcActionService {
    @Autowired
    IUserService userService;
    @Autowired
    IDledcService dledcService;
    @Autowired
    private TokenService tokenService;

    @Autowired
    private RedisCache redisCache;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult DledcLogin(DledcLoginApiRequest request) throws Exception {
        String name = URLDecoder.decode(request.getName(), "UTF-8");
        String uid = request.getUid();
        String t = request.getT();
        Long userid = 0L;
        if (StringUtils.isBlank(uid) || CommonConfig.isUseSynchronizationUse()) {
            //同步处理
            TwoTuple<Long, AjaxResult> useridAndResponse = SuccessResponse(request);
            if (useridAndResponse.first == 0) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(request.getName(), 1, 1, Constants.LOGIN_FAIL, useridAndResponse.second.get("msg").toString()));
                return useridAndResponse.second;
            }
            userid = useridAndResponse.first;
        } else {
            User user = User(request, uid, name, userService);
            userid = user.getUserId();
        }
        User user = userService.getByKey(userid, true);
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(user.getUserName(), 1, 1, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = new LoginUser();
        loginUser.setUser(new SysUser(user.getPassword(), user.getUserTrueName()));
        loginUser.setUserType(user.getUserTypeId());
        loginUser.setUserId(user.getUserId());
        DledcLoginResponse response = new DledcLoginResponse(user.getUserTypeId(), tokenService.createToken(loginUser));
        return AjaxResult.success(response);
    }

    /**
     * TODO
     *
     * @param request
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult DledcMobileLogin(DledcMobileLoginApiRequest request) throws Exception {
        TwoTuple<Long, Integer> twoTuple = dledcService.sychroUser(request.getUid());
        User user = userService.getByKey(twoTuple.first, true);
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(user.getUserName(), 2, 1, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = new LoginUser();
        loginUser.setUser(new SysUser(user.getPassword(), user.getUserTrueName()));
        loginUser.setUserType(user.getUserTypeId());
        loginUser.setUserId(user.getUserId());
        DledcMobileLoginResponse response = new DledcMobileLoginResponse(user.getUserTypeId(), tokenService.createToken(loginUser));
        return AjaxResult.success(response);
    }

    @Override
    public AjaxResult DledcConfig(DledcConfigApiRequest request) {
        DledcConfigResponse response = new DledcConfigResponse();
        if (request.getAccessTypeId() == 1) {
            response.setRedirectUri(CommonConfig.getOnlineRedirectUri())
                    .setClientId(CommonConfig.getOnlineClientId())
                    .setClientSecret(CommonConfig.getOnlineClientSecret())
                    .setPostLogoutRedirectUri(CommonConfig.getOnlinePostLogoutRedirectUri());
        } else {
            response.setRedirectUri(CommonConfig.getOffLineRedirectUri())
                    .setClientId(CommonConfig.getOffLineClientId())
                    .setClientSecret(CommonConfig.getOffLineClientSecret())
                    .setPostLogoutRedirectUri(CommonConfig.getOffLinePostLogoutRedirectUri());
        }
        response.setDledcApiUrl(CommonConfig.getDledcApiUrl());
        response.setDledcPlatformUrl(CommonConfig.getDledcPlatformUrl());
        return AjaxResult.success(response);
    }

    public static User User(DledcLoginApiRequest apiRequest, String uid, String name, IUserService userService) throws Exception {
        log.info(String.format("开始新华云快捷登录%s", uid));
        var userQuery = new QueryWrapper<User>();
        userQuery.lambda().eq(User::getSourceTypeId, SourceTypeEnum.JiaoFuPingTai.getCode()).eq(User::getSourceId, uid);
        User user = userService.getOne(userQuery);
        log.info(String.format("查询用户是否存在%s", uid));
        if (user.getUserId() == null || user.getUserId().intValue() == 0) {
            user.setUserTrueName(name);
            user.setCreateTime(LocalDateTime.now());
            user.setSourceTypeId(SourceTypeEnum.JiaoFuPingTai.getCode());
            user.setSourceId(uid);
            user.setStatus(1);
            user.setUserTypeId(DledcConvertHelper.ConvertUserType(apiRequest.getT().toLowerCase()));
            user.setUserName(UUID.randomUUID().toString());
            user.setPassword("");
            user.setUserSex(1);
            user.setUserFace("");
            user.setEmailAddress("");
            user.setSchoolName("");
            user.setComment("");
            user.setPhone("");
            user.setStudentCode("等待同步");
            user.setUserRoles(DledcConvertHelper.ConvertUserType(apiRequest.getT().toLowerCase()) + "");
            user.setIdCard("");
            user.setUpdateTime(LocalDateTime.now());
            if (user.getUserId() != null && user.getUserId().intValue() > 0) {
                userService.updateById(user);
                userService.getByKey(user.getUserId(), false);
            } else {
                userService.save(user);
            }
            log.info(String.format("保存首次登录用户%s", uid));
        }
        return user;
    }

    private TwoTuple<Long, AjaxResult> SuccessResponse(DledcLoginApiRequest apiRequest) throws Exception {
        log.info("开始普通南昌云平台登录接口");
        DledcDefaultClient client = dledcService.getDledcConfig(apiRequest.getAccessTypeId());
        log.info("获取配置成功");
        log.info("code:" + apiRequest.getCode());
        ConnectTokenResponse connectTokenResponse = client.ConnectTokenByCode(apiRequest.getCode());
        log.info("获取令牌成功:", connectTokenResponse.getAccess_token());
        if (connectTokenResponse == null) {
            return new TwoTuple<Long, AjaxResult>(0L, AjaxResult.error("从云平台获取token失败"));
        }
        ConnectUserInfoResponse connectUserInfoResponse = client.ConnectUserInfo(connectTokenResponse.getAccess_token());
        log.info("获取用户信息成功");
        if (connectUserInfoResponse == null) {
            return new TwoTuple<Long, AjaxResult>(0L, AjaxResult.error("从云平台获取uid失败"));
        }
        String uid = connectUserInfoResponse.getSub();
        TwoTuple<Long, Integer> twoTuple = dledcService.sychroUser(uid);
        log.info("用户同步完成");
        return new TwoTuple<Long, AjaxResult>(twoTuple.first, null);
    }
}
