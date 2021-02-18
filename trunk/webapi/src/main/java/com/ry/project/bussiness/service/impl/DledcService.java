package com.ry.project.bussiness.service.impl;

import com.ry.common.core.dledc.DledcDefaultClient;
import com.ry.common.core.dledc.request.GetClassListByCodeRequest;
import com.ry.common.core.dledc.request.GetSchoolListRequest;
import com.ry.common.core.dledc.request.GetUserBaseInfoRequest;
import com.ry.common.core.dledc.response.*;
import com.ry.common.core.dledc.utils.DledcConvertHelper;
import com.ry.common.enums.SourceTypeEnum;
import com.ry.common.enums.UserType;
import com.ry.common.model.TwoTuple;
import com.ry.framework.config.CommonConfig;
import com.ry.project.bussiness.domain.bo.User;
import com.ry.project.bussiness.service.IDledcService;
import com.ry.project.bussiness.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DledcService implements IDledcService {

    /// <summary>
    /// 缓存dledcClient
    /// </summary>
    private static Map<Integer, DledcDefaultClient> _dledcClientDir = new HashMap<Integer, DledcDefaultClient>();
    private static Lock lock = new ReentrantLock();

    @Autowired
    IUserService userService;

    @Override
    public TwoTuple<Long, Integer> sychroUser(String uid) {
        DledcDefaultClient client = getDledcConfig(1);
        GetUserBaseInfoRequest getUserBaseInfoRequest = new GetUserBaseInfoRequest(uid);
        GetUserBaseInfoResponse getUserBaseInfoResponse = client.Execute(GetUserBaseInfoResponse.class, getUserBaseInfoRequest);
        if (getUserBaseInfoResponse == null || getUserBaseInfoResponse.getData() == null || getUserBaseInfoResponse.getData().getSingleData() == null) {
            log.error(String.format("同步云平台用户异常，参数：%s", uid));
        }
        UserBaseInfo userBaseInfo = getUserBaseInfoResponse.getData().getSingleData();
        if (StringUtils.isBlank(userBaseInfo.getGradeCode())) {
            GetClassListByCodeResponse getClassListByCodeResponse = client.Execute(GetClassListByCodeResponse.class, new GetClassListByCodeRequest(userBaseInfo.getOrganizeId()));
            if (getClassListByCodeResponse != null && getClassListByCodeResponse.getData() != null) {
                userBaseInfo.setGradeCode(DledcConvertHelper.GetGradeCode(userBaseInfo.getDepartmentId(), getClassListByCodeResponse.getData().getListData()));
            }
        }
        User user = userService.readBySource(SourceTypeEnum.JiaoFuPingTai.getCode(), uid);
        var userType = DledcConvertHelper.ConvertUserType(userBaseInfo.getUserType());
        if (user == null) {
            user = new User().setCreateTime(LocalDateTime.now()).setSourceTypeId(SourceTypeEnum.JiaoFuPingTai.getCode()).setSourceId(userBaseInfo.getUid()).setStatus(1);
        } else {
            if (StringUtils.isNotBlank(CommonConfig.getUnSynchronizationUser())) {
                int[] unSynchronizationUserId = Arrays.stream(CommonConfig.getUnSynchronizationUser().split(",")).mapToInt(Integer::valueOf).toArray();
                List<Integer> userIdList = Arrays.stream(unSynchronizationUserId).boxed().collect(Collectors.toList());
                if (userIdList.contains(user.getUserId())) {
                    return new TwoTuple(user.getUserId(), userType);
                }
            }
        }

        user.setUserName(StringUtils.isNotBlank(userBaseInfo.getAccount()) ? userBaseInfo.getAccount() : UUID.randomUUID().toString())
                .setPassword(StringUtils.isNotBlank(userBaseInfo.getPassword()) ? userBaseInfo.getPassword() : StringUtils.EMPTY)
                .setUserTypeId(userType)
                .setUserTrueName(StringUtils.isNotBlank(userBaseInfo.getRealName()) ? userBaseInfo.getRealName() : StringUtils.EMPTY)
                .setUserSex(((userBaseInfo.getGender() != null && userBaseInfo.getGender())) ? 1 : 2)
                .setUserFace(StringUtils.isNotBlank(userBaseInfo.getHeadIcon()) ? userBaseInfo.getHeadIcon() : StringUtils.EMPTY)
                .setEmailAddress(StringUtils.isNotBlank(userBaseInfo.getEmail()) ? userBaseInfo.getEmail() : StringUtils.EMPTY)
                .setComment(StringUtils.isNotBlank(userBaseInfo.getDescription()) ? userBaseInfo.getDescription() : StringUtils.EMPTY)
                .setPhone(StringUtils.isNotBlank(userBaseInfo.getMobilePhone()) ? userBaseInfo.getMobilePhone() : StringUtils.EMPTY)
                .setGradeId(DledcConvertHelper.ConvertGradeId(userBaseInfo.getGradeCode()))
                .setUserRoles(DledcConvertHelper.ConvertUserRoles(userBaseInfo.getUserRoles()))
                .setStudentCode(StringUtils.isNotBlank(userBaseInfo.getStudentCode()) ? userBaseInfo.getStudentCode() : StringUtils.EMPTY)
                .setIdCard(StringUtils.isNotBlank(userBaseInfo.getIdCard()) ? userBaseInfo.getIdCard() : StringUtils.EMPTY)
                .setUpdateTime(LocalDateTime.now());
        if (userType != UserType.Teacher.getId()) {
            user.setSchoolCode(userBaseInfo.getOrganizeId())
                    .setSchoolName(StringUtils.isNotBlank(userBaseInfo.getOrganizeName()) ? userBaseInfo.getOrganizeName() : StringUtils.EMPTY);
        }
        if (user.getUserId() != null && user.getUserId().intValue() > 0) {
            userService.updateById(user);
            userService.getByKey(user.getUserId(), false);
        } else {
            userService.save(user);
        }
        return new TwoTuple(user.getUserId(), userType);
    }

    @Override
    public List<SchoolInfo> getSchoolList() {
        DledcDefaultClient client = getDledcConfig(1);
        GetSchoolListResponse getSchoolListResponse = client.Execute(GetSchoolListResponse.class, new GetSchoolListRequest());
        if (getSchoolListResponse != null && getSchoolListResponse.getData() != null) {
            return getSchoolListResponse.getData().getListData();
        }
        return null;
    }

    @Override
    public DledcDefaultClient getDledcConfig(int accessTypeId) {
        if (!_dledcClientDir.containsKey(accessTypeId)) {
            lock.lock();
            try {
                if (!_dledcClientDir.containsKey(accessTypeId)) {
                    if (accessTypeId == 1) {
                        _dledcClientDir.put(accessTypeId, new DledcDefaultClient(CommonConfig.getDledcApiUrl(), CommonConfig.getOnlineRedirectUri(), CommonConfig.getOnlineClientId(), CommonConfig.getOnlineClientSecret(), CommonConfig.getOnlinePostLogoutRedirectUri()));
                    } else {
                        _dledcClientDir.put(accessTypeId, new DledcDefaultClient(CommonConfig.getDledcApiUrl(), CommonConfig.getOffLineRedirectUri(), CommonConfig.getOffLineClientId(), CommonConfig.getOffLineClientSecret(), CommonConfig.getOffLinePostLogoutRedirectUri()));
                    }
                }
            } catch (Exception e) {
                log.error("获取教辅平台对接Client异常", e);
            } finally {
                lock.unlock();
            }
        }
        return _dledcClientDir.get(accessTypeId);
    }
}
