package com.ry.project.bussiness.controller.action;

import com.ry.framework.web.domain.AjaxResult;
import com.ry.project.bussiness.domain.request.GetBySubjectIdBody;
import com.ry.project.bussiness.domain.request.school.ChangeBindStatusBody;
import com.ry.project.bussiness.domain.request.school.GetSubjectListBody;
import com.ry.project.bussiness.domain.request.school.GetTeacherListBody;
import com.ry.project.bussiness.domain.request.school.SaveSubjectBody;
import com.ry.project.bussiness.domain.request.ChangeBySubjectIdBody;
import com.ry.project.bussiness.domain.request.teacher.GetByIdBody;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 课程
 * </p>
 *
 * @author xrq
 * @since 2020-04-26
 */
public interface ISchoolActionService {
    AjaxResult teacherOptions(HttpServletRequest request);

    AjaxResult getTeacherList(GetTeacherListBody body, HttpServletRequest request);

    AjaxResult getTeacherDetail(GetByIdBody body);

    AjaxResult changeTeacherBindStatus(ChangeBindStatusBody body);

    AjaxResult getSubjectList(GetSubjectListBody body, HttpServletRequest request);

    AjaxResult getToDoSubjectList(GetSubjectListBody body, HttpServletRequest request);

    AjaxResult saveSubject(SaveSubjectBody body, HttpServletRequest request);

    AjaxResult getSubjectDetail(GetBySubjectIdBody body);

    AjaxResult changeStatus(ChangeBySubjectIdBody body);
}
