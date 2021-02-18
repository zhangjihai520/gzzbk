package com.ry.project.bussiness.controller.action;


import com.ry.framework.web.domain.AjaxResult;
import com.ry.project.bussiness.domain.request.ChangeBySubjectIdBody;
import com.ry.project.bussiness.domain.request.GetBySubjectIdBody;
import com.ry.project.bussiness.domain.request.teacher.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 教师相关控制层业务
 * </p>
 *
 * @author yjx
 */
public interface ITeacherActionService {

    AjaxResult saveSubject(SaveSubjectBody body, HttpServletRequest request);

    AjaxResult getSubjectList(GetSubjectListBody body, HttpServletRequest request);

    AjaxResult getSubjectDetail(GetBySubjectIdBody body);

    AjaxResult changeStatus(ChangeBySubjectIdBody body);

    AjaxResult getEvaluateDetail(EvaluateSubjectTeacherBody body);

    AjaxResult getUserInfo(HttpServletRequest request);

    AjaxResult updateUserInfo(GetUserInfoBody body, HttpServletRequest request);

    AjaxResult saveMessage(SaveMessageBody body, HttpServletRequest request);

    AjaxResult getChaptersBySubjectId(GetBySubjectIdBody body);

    AjaxResult getLeavemessage(GetLeavemessageBody body);

    AjaxResult changeLeaveMessageStatus(GetByIdBody body);

    AjaxResult changeEvaluateStatus(GetByIdBody body);

    AjaxResult getAuthCode(GetBySubjectIdBody body, HttpServletRequest request);
}
