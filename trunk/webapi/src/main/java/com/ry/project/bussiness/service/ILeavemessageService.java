package com.ry.project.bussiness.service;

import com.ry.project.bussiness.domain.bo.Leavemessage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ry.project.bussiness.domain.bo.LeavemessageVO;

import java.util.List;

/**
 * <p>
 * 留言表 服务类
 * </p>
 *
 * @author xrq
 * @since 2020-12-01
 */
public interface ILeavemessageService extends IService<Leavemessage> {
    List<LeavemessageVO> getChildrensByRootIds(String rootIds, int roleId);

    List<LeavemessageVO> buildSelectTree(List<LeavemessageVO> messages);
}
