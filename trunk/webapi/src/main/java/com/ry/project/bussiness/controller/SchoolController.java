package com.ry.project.bussiness.controller;

import com.alibaba.fastjson.JSON;
import com.ry.framework.web.domain.AjaxResult;
import com.ry.project.bussiness.controller.action.ISchoolActionService;
import com.ry.project.bussiness.domain.request.GetBySubjectIdBody;
import com.ry.project.bussiness.domain.request.school.ChangeBindStatusBody;
import com.ry.project.bussiness.domain.request.school.GetSubjectListBody;
import com.ry.project.bussiness.domain.request.school.GetTeacherListBody;
import com.ry.project.bussiness.domain.request.school.SaveSubjectBody;
import com.ry.project.bussiness.domain.request.ChangeBySubjectIdBody;
import com.ry.project.bussiness.domain.request.teacher.GetByIdBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/school")
@Api(value = "学校模块接口", tags = {"学校模块接口"})
@Slf4j
public class SchoolController {
    @Autowired
    ISchoolActionService schoolActionService;

    @ApiOperation("获取教师下拉选项")
    @PostMapping("/teacherOptions")
    public AjaxResult teacherOptions(HttpServletRequest request) {
        try {
            return schoolActionService.teacherOptions(request);
        } catch (Exception ex) {
            log.error("获取教师下拉选项异常", ex);
            return AjaxResult.error("获取教师下拉选项失败");
        }
    }

    @ApiOperation("【学校】获取课程列表信息")
    @PostMapping("/getSubjectList")
    public AjaxResult getSubjectList(@RequestBody GetSubjectListBody body, HttpServletRequest request) {
        try {
            return schoolActionService.getSubjectList(body, request);
        } catch (Exception ex) {
            log.error(String.format("【学校】获取课程列表信息异常，参数：%s", JSON.toJSONString(body)), ex);
            return AjaxResult.error("获取课程列表信息失败");
        }
    }

    @ApiOperation("【学校】获取教师列表信息")
    @PostMapping("/getTeacherList")
    public AjaxResult getTeacherList(@RequestBody GetTeacherListBody body, HttpServletRequest request) {
        try {
            return schoolActionService.getTeacherList(body, request);
        } catch (Exception ex) {
            log.error(String.format("【学校】获取课程列表信息异常，参数：%s", JSON.toJSONString(body)), ex);
            return AjaxResult.error("获取课程列表信息失败");
        }
    }

    @ApiOperation("【学校】获取教师详细信息")
    @PostMapping("/getTeacherDetail")
    public AjaxResult getTeacherDetail(@RequestBody GetByIdBody body) {
        try {
            return schoolActionService.getTeacherDetail(body);
        } catch (Exception ex) {
            log.error(String.format("【学校】获取教师详细信息异常，参数：%s", JSON.toJSONString(body)), ex);
            return AjaxResult.error("获取教师详细信息失败");
        }
    }

    @ApiOperation("【学校】更新教师绑定状态")
    @PostMapping("/changeTeacherBindStatus")
    public AjaxResult changeTeacherBindStatus(@Validated @RequestBody ChangeBindStatusBody body) {
        try {
            return schoolActionService.changeTeacherBindStatus(body);
        } catch (Exception ex) {
            log.error(String.format("【学校】更新教师绑定状态异常，参数：%s", JSON.toJSONString(body)), ex);
            return AjaxResult.error("更新教师绑定状态失败");
        }
    }


    @ApiOperation("【学校】获取待办课程列表信息")
    @PostMapping("/getToDoSubjectList")
    public AjaxResult getToDoSubjectList(@RequestBody GetSubjectListBody body, HttpServletRequest request) {
        try {
            return schoolActionService.getToDoSubjectList(body, request);
        } catch (Exception ex) {
            log.error(String.format("【学校】获取课程列表信息异常，参数：%s", JSON.toJSONString(body)), ex);
            return AjaxResult.error("获取课程列表信息失败");
        }
    }

    @ApiOperation("【学校】学校创建录播课程")
    @PostMapping("/saveSubject")
    public AjaxResult saveSubject(@Validated @RequestBody SaveSubjectBody body, HttpServletRequest request) {
        try {
            return schoolActionService.saveSubject(body, request);
        } catch (Exception ex) {
            log.error(String.format("【学校】学校创建录播课程异常，参数：%s", JSON.toJSONString(body)), ex);
            return AjaxResult.error("学校创建录播课程失败");
        }
    }

    @ApiOperation("【学校】获取课程详细信息")
    @PostMapping("/getSubjectDetail")
    public AjaxResult getSubjectDetail(@RequestBody GetBySubjectIdBody body) {
        try {
            return schoolActionService.getSubjectDetail(body);
        } catch (Exception ex) {
            log.error(String.format("【学校】获取课程详细信息异常，参数：%s", JSON.toJSONString(body)), ex);
            return AjaxResult.error("获取课程详细信息失败");
        }
    }

    @ApiOperation("【学校】更新课程状态")
    @PostMapping("/changeStatus")
    public AjaxResult changeStatus(@Validated @RequestBody ChangeBySubjectIdBody body) {
        try {
            return schoolActionService.changeStatus(body);
        } catch (Exception ex) {
            log.error(String.format("【学校】更新课程状态异常，参数：%s", JSON.toJSONString(body)), ex);
            return AjaxResult.error("更新课程状态失败");
        }
    }

}
