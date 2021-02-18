package com.ry.project.bussiness.controller.action;

import com.ry.framework.web.domain.AjaxResult;
import com.ry.project.bussiness.domain.request.manager.ChangeByBannerIdBody;
import com.ry.project.bussiness.domain.request.ChangeBySubjectIdBody;
import com.ry.project.bussiness.domain.request.manager.GetByBannerIdBody;
import com.ry.project.bussiness.domain.request.GetBySubjectIdBody;
import com.ry.project.bussiness.domain.request.manager.GetBannerListBody;
import com.ry.project.bussiness.domain.request.school.*;
import com.ry.project.bussiness.domain.request.manager.SaveBannerBody;
import com.ry.project.bussiness.domain.request.teacher.GetByIdBody;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 课程
 * </p>
 *
 * @author xrq
 * @since 2020-04-26
 */
public interface IManagerActionService {

    AjaxResult getTeacherList(GetTeacherListBody body, HttpServletRequest request);

    AjaxResult getTeacherDetail(GetByIdBody body);

    AjaxResult changeTeacherFamousFlag(ChangeFamousFlagBody body);

    AjaxResult getSubjectList(GetSubjectListBody body, HttpServletRequest request);

    AjaxResult getToDoSubjectList(GetSubjectListBody body, HttpServletRequest request);

    AjaxResult saveSubject(SaveSubjectBody body, HttpServletRequest request);

    AjaxResult getSubjectDetail(GetBySubjectIdBody body);

    AjaxResult changeStatus(ChangeBySubjectIdBody body);

    AjaxResult saveBanner(SaveBannerBody body);

    AjaxResult changeBannerStatus(ChangeByBannerIdBody body);

    AjaxResult getBannerDetail(GetByBannerIdBody body);

    AjaxResult getBannerList(GetBannerListBody body);
}
