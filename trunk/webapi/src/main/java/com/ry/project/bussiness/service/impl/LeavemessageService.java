package com.ry.project.bussiness.service.impl;

import com.ry.common.utils.StringUtils;
import com.ry.project.bussiness.domain.bo.Leavemessage;
import com.ry.project.bussiness.domain.bo.LeavemessageVO;
import com.ry.project.bussiness.mapper.LeavemessageMapper;
import com.ry.project.bussiness.service.ILeavemessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 * 留言表 服务实现类
 * </p>
 *
 * @author xrq
 * @since 2020-12-01
 */
@Service
public class LeavemessageService extends ServiceImpl<LeavemessageMapper, Leavemessage> implements ILeavemessageService {

    @Override
    public List<LeavemessageVO> getChildrensByRootIds(String rootIds, int roleId) {
        return this.baseMapper.getChildrensByRootIds(rootIds, roleId);
    }

    @Override
    public List<LeavemessageVO> buildSelectTree(List<LeavemessageVO> messages) {
        List<LeavemessageVO> returnList = new ArrayList<LeavemessageVO>();
        List<Long> tempList = new ArrayList<Long>();
        for (LeavemessageVO message : messages) {
            tempList.add(message.getId());
        }
        for (Iterator<LeavemessageVO> iterator = messages.iterator(); iterator.hasNext(); ) {
            LeavemessageVO message = (LeavemessageVO) iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(message.getReplyId())) {
                recursionFn(messages, message);
                returnList.add(message);
            }
        }
        if (returnList.isEmpty()) {
            returnList = messages;
        }
        return returnList;
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<LeavemessageVO> list, LeavemessageVO t) {
        // 得到子节点列表
        List<LeavemessageVO> childList = getChildList(list, t);
        t.setChildren(childList);
        for (LeavemessageVO tChild : childList) {
            if (hasChild(list, tChild)) {
                // 判断是否有子节点
                Iterator<LeavemessageVO> it = childList.iterator();
                while (it.hasNext()) {
                    LeavemessageVO n = (LeavemessageVO) it.next();
                    recursionFn(list, n);
                }
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<LeavemessageVO> getChildList(List<LeavemessageVO> list, LeavemessageVO t) {
        List<LeavemessageVO> tlist = new ArrayList<LeavemessageVO>();
        Iterator<LeavemessageVO> it = list.iterator();
        while (it.hasNext()) {
            LeavemessageVO n = (LeavemessageVO) it.next();
            if (StringUtils.isNotNull(n.getReplyId()) && n.getReplyId().longValue() == t.getId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<LeavemessageVO> list, LeavemessageVO t) {
        return getChildList(list, t).size() > 0 ? true : false;
    }
}
