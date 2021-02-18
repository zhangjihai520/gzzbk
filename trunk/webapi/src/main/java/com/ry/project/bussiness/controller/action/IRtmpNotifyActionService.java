package com.ry.project.bussiness.controller.action;

import java.util.Map;

/**
 * <p>
 * 消息 服务类
 * </p>
 *
 * @author xrq
 * @since 2020-04-26
 */
public interface IRtmpNotifyActionService {
    //推流权限校验
    boolean pushAuth(Map<String, String> map) throws Exception;
}
