package com.ry.project.bussiness.service;

import com.ry.common.core.dledc.DledcDefaultClient;
import com.ry.common.core.dledc.response.SchoolInfo;
import com.ry.common.model.TwoTuple;

import java.util.List;

/**
 * 云平台业务层接口
 */
public interface IDledcService {
    /// <summary>
    /// 同步教辅平台用户
    /// </summary>
    /// <returns></returns>
    TwoTuple<Long, Integer> sychroUser(String uid);

    List<SchoolInfo> getSchoolList();



    /// <summary>
    /// 获取教辅平台对接参数
    /// </summary>
    /// <param name="accessTypeId">1教辅双师课堂,2在线互动实验室</param>
    /// <returns></returns>
    DledcDefaultClient getDledcConfig(int accessTypeId);
}
