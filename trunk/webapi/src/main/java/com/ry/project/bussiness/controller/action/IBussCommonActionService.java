package com.ry.project.bussiness.controller.action;


import com.ry.framework.web.domain.AjaxResult;
import com.ry.project.bussiness.domain.request.ChangeBySubjectIdBody;
import com.ry.project.bussiness.domain.request.GetBySubjectIdBody;
import com.ry.project.bussiness.domain.request.teacher.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 公共接口相关控制层业务
 * </p>
 *
 * @author yjx
 */
public interface IBussCommonActionService {

    AjaxResult teacherOptions();

    AjaxResult gradeOptions();

    AjaxResult courseOptions();

    AjaxResult schoolOptions();

    AjaxResult banners();

}
