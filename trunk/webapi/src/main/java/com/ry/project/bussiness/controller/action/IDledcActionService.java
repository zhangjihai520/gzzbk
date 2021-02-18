package com.ry.project.bussiness.controller.action;

import com.ry.common.model.dledc.request.DledcConfigApiRequest;
import com.ry.common.model.dledc.request.DledcLoginApiRequest;
import com.ry.common.model.dledc.request.DledcMobileLoginApiRequest;
import com.ry.framework.web.domain.AjaxResult;

/**
 * <p>
 * 消息 服务类
 * </p>
 *
 * @author xrq
 * @since 2020-04-26
 */
public interface IDledcActionService {
    AjaxResult DledcLogin(DledcLoginApiRequest request) throws Exception;

    AjaxResult DledcMobileLogin(DledcMobileLoginApiRequest request) throws Exception;

    AjaxResult DledcConfig(DledcConfigApiRequest request);
}
