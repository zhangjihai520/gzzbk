package com.ry.project.bussiness.service;

import com.ry.project.bussiness.domain.bo.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 学生、教师表。都是来源于云平台 服务类
 * </p>
 *
 * @author xrq
 * @since 2020-12-01
 */
public interface IUserService extends IService<User> {

    /// <summary>
    /// 根据主键获取一条User表数据
    /// </summary>
    /// <param name="userId">用户id</param>
    /// <returns>查询到的表实体对象</returns>
    User getByKey(Long userId, Boolean useCache);

    /**
     * 根据学籍号获取用户
     * @param studentCode
     * @return
     */
    User readByStudentCode(String studentCode);

    /// <summary>
    /// 批量保存User对象
    /// </summary>
    /// <param name="dataModels"></param>
    //int BatchInsertUser(List<User> dataModels);

    /// <summary>
    /// 根据来源类型和来源id获取用户
    /// </summary>
    /// <param name="sourceTypeId"></param>
    /// <param name="sourceId"></param>
    /// <returns></returns>
    User readBySource(int sourceTypeId, String sourceId);

}
