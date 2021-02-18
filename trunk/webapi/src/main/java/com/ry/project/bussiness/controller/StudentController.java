package com.ry.project.bussiness.controller;

import com.alibaba.fastjson.JSON;
import com.ry.framework.web.domain.AjaxResult;
import com.ry.project.bussiness.controller.action.IStudentActionService;
import com.ry.project.bussiness.domain.request.GetBySubjectIdBody;
import com.ry.project.bussiness.domain.request.student.*;
import com.ry.project.bussiness.domain.request.teacher.GetLeavemessageBody;
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

/**
 * <p>
 * 学生相关API
 * </p>
 *
 * @author
 * @since 2020-05-29
 */
@RestController
@RequestMapping("/student")
@Api(value = "学生相关接口", tags = {"学生相关接口"})
@Slf4j
public class StudentController {

    @Autowired
    IStudentActionService studentActionService;

    @ApiOperation("【学生】获取用户信息")
    @PostMapping("/getUserInfo")
    public AjaxResult getUserInfo(HttpServletRequest httprequest) {
        try {
            return studentActionService.getUserInfo(httprequest);
        } catch (Exception ex) {
            log.error("【学生】获取用户信息异常", ex);
            return AjaxResult.error("获取用户信息失败");
        }
    }

    @ApiOperation("【学生】获取首页直播列表")
    @PostMapping("/getLiveList")
    public AjaxResult getLiveList(HttpServletRequest request) {
        try {
            return studentActionService.getLiveList(request);
        } catch (Exception ex) {
            log.error("【学生】获取首页直播列表异常", ex);
            return AjaxResult.error("获取首页直播列表失败");
        }
    }

    @ApiOperation("【学生】获取首页热门课程列表")
    @PostMapping("/getHotSubjectList")
    public AjaxResult getHotSubjectList() {
        try {
            return studentActionService.getHotSubjectList();
        } catch (Exception ex) {
            log.error("【学生】获取首页热门课程列表异常", ex);
            return AjaxResult.error("获取首页热门课程列表失败");
        }
    }

    @ApiOperation("【学生】获取直播预告列表")
    @PostMapping("/getPreLiveList")
    public AjaxResult getPreLiveList(@RequestBody GetPreLiveListBody body, HttpServletRequest request) {
        try {
            return studentActionService.getPreLiveList(body, request);
        } catch (Exception ex) {
            log.error(String.format("【学生】获取直播预告列表异常，参数：%s", JSON.toJSONString(body)), ex);
            return AjaxResult.error("获取直播预告列表失败");
        }
    }

    @ApiOperation("【学生】获取课程列表信息")
    @PostMapping("/getSubjectList")
    public AjaxResult getSubjectList(@RequestBody GetSubjectListBody body, HttpServletRequest request) {
        try {
            return studentActionService.getSubjectList(body, request);
        } catch (Exception ex) {
            log.error(String.format("【学生】获取课程列表信息异常，参数：%s", JSON.toJSONString(body)), ex);
            return AjaxResult.error("获取课程列表信息失败");
        }
    }

    @ApiOperation("【学生】获取课程详情信息")
    @PostMapping("/getSubjectDetail")
    public AjaxResult getSubjectDetail(@Validated @RequestBody GetBySubjectIdBody body, HttpServletRequest request) {
        try {
            return studentActionService.getSubjectDetail(body, request);
        } catch (Exception ex) {
            log.error(String.format("【学生】获取课程详情信息异常，参数：%s", JSON.toJSONString(body)), ex);
            return AjaxResult.error("获取课程详情信息失败");
        }
    }

    @ApiOperation("【学生】获取更多视频")
    @PostMapping("/moreSubjectList")
    public AjaxResult moreSubjectList(@Validated @RequestBody GetBySubjectIdBody body) {
        try {
            return studentActionService.moreSubjectList(body);
        } catch (Exception ex) {
            log.error(String.format("【学生】获取更多视频异常，参数：%s", JSON.toJSONString(body)), ex);
            return AjaxResult.error("获取更多视频失败");
        }
    }

    @ApiOperation("【学生】获取名师风采列表")
    @PostMapping("/getTeacherList")
    public AjaxResult getTeacherList(@RequestBody GetTeacherListBody body) {
        try {
            return studentActionService.getTeacherList(body);
        } catch (Exception ex) {
            log.error(String.format("【学生】获取名师风采列表异常，参数：%s", JSON.toJSONString(body)), ex);
            return AjaxResult.error("获取名师风采列表失败");
        }
    }

    @ApiOperation("【学生】获取教师详细信息")
    @PostMapping("/getTeacherDetail")
    public AjaxResult getTeacherDetail(@Validated @RequestBody GetByTeacherIdBody body, HttpServletRequest request) {
        try {
            return studentActionService.getTeacherDetail(body, request);
        } catch (Exception ex) {
            log.error(String.format("【学生】获取教师详细信息异常，参数：%s", JSON.toJSONString(body)), ex);
            return AjaxResult.error("获取教师详细信息失败");
        }
    }

    @ApiOperation("【学生】获取我的课程列表信息")
    @PostMapping("/mySubjectList")
    public AjaxResult mySubjectList(@RequestBody GetSubjectListBody body, HttpServletRequest request) {
        try {
            return studentActionService.mySubjectList(body, request);
        } catch (Exception ex) {
            log.error(String.format("【学生】获取我的课程列表信息异常，参数：%s", JSON.toJSONString(body)), ex);
            return AjaxResult.error("获取我的课程列表信息失败");
        }
    }

    @ApiOperation("评价课程")
    @PostMapping("/evaluateSubject")
    public AjaxResult evaluateSubject(@RequestBody EvaluateSubjectBody body, HttpServletRequest request) {
        try {
            return studentActionService.evaluateSubject(body, request);
        } catch (Exception ex) {
            log.error(String.format("评价课程异常，参数：%s", JSON.toJSONString(body)), ex);
            return AjaxResult.error("评价课程失败");
        }
    }

    @ApiOperation("【学生】报名课程")
    @PostMapping("/registSubject")
    public AjaxResult registSubject(@Validated @RequestBody GetBySubjectIdBody body, HttpServletRequest request) {
        try {
            return studentActionService.registSubject(body, request);
        } catch (Exception ex) {
            log.error(String.format("【学生】报名课程异常，参数：%s", JSON.toJSONString(body)), ex);
            return AjaxResult.error("报名课程失败");
        }
    }

    @ApiOperation("【学生】留言")
    @PostMapping("/leavemessage")
    public AjaxResult leavemessage(@Validated @RequestBody LeavemessageBody body, HttpServletRequest request) {
        try {
            return studentActionService.leavemessage(body, request);
        } catch (Exception ex) {
            log.error(String.format("【学生】留言异常，参数：%s", JSON.toJSONString(body)), ex);
            return AjaxResult.error("留言失败");
        }
    }

    @ApiOperation("【学生】获取留言信息列表")
    @PostMapping("/getLeavemessage")
    public AjaxResult getLeavemessage(@Validated @RequestBody GetLeavemessageBody body) {
        try {
            return studentActionService.getLeavemessage(body);
        } catch (Exception ex) {
            log.error(String.format("【学生】获取留言信息列表，参数：%s", JSON.toJSONString(body)), ex);
            return AjaxResult.error("获取留言信息列表失败");
        }
    }

    @ApiOperation("【学生】获取留言信息回复列表")
    @PostMapping("/getLeavemessageList")
    public AjaxResult getLeavemessageList(@RequestBody GetLeavemessageBody body,HttpServletRequest request) {
        try {
            return studentActionService.getLeavemessageList(body,request);
        } catch (Exception ex) {
            log.error(String.format("【学生】获取留言信息回复列表异常，参数：%s", JSON.toJSONString(body)), ex);
            return AjaxResult.error("获取留言信息回复列表失败");
        }
    }

}
