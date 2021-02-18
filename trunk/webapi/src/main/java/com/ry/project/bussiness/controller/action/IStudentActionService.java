package com.ry.project.bussiness.controller.action;

import com.ry.framework.web.domain.AjaxResult;
import com.ry.project.bussiness.domain.request.GetBySubjectIdBody;
import com.ry.project.bussiness.domain.request.student.*;
import com.ry.project.bussiness.domain.request.teacher.GetLeavemessageBody;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 学生相关控制层业务
 * </p>
 *
 * @author xrq
 * @since 2020-04-26
 */
public interface IStudentActionService {

    AjaxResult getUserInfo(HttpServletRequest request) throws Exception;

    AjaxResult getLiveList(HttpServletRequest request);

    AjaxResult getHotSubjectList();

    AjaxResult getPreLiveList(GetPreLiveListBody body, HttpServletRequest request);

    AjaxResult getSubjectList(GetSubjectListBody body, HttpServletRequest request);

    AjaxResult getSubjectDetail(GetBySubjectIdBody body, HttpServletRequest request);

    AjaxResult moreSubjectList(GetBySubjectIdBody body);

    AjaxResult getTeacherList(GetTeacherListBody body);

    AjaxResult getTeacherDetail(GetByTeacherIdBody body, HttpServletRequest request);

    AjaxResult mySubjectList(GetSubjectListBody body, HttpServletRequest request);

    AjaxResult evaluateSubject(EvaluateSubjectBody body, HttpServletRequest request);

    AjaxResult registSubject(GetBySubjectIdBody body, HttpServletRequest request);

    AjaxResult leavemessage(LeavemessageBody body, HttpServletRequest request);

    AjaxResult getLeavemessage(GetLeavemessageBody body);

    AjaxResult getLeavemessageList(GetLeavemessageBody body,HttpServletRequest request);

}
