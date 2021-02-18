package com.ry.project.bussiness.controller;

import com.alibaba.fastjson.JSON;
import com.ry.framework.web.domain.AjaxResult;
import com.ry.project.bussiness.controller.action.IStudentActionService;
import com.ry.project.bussiness.controller.action.ITeacherActionService;
import com.ry.project.bussiness.domain.request.ChangeBySubjectIdBody;
import com.ry.project.bussiness.domain.request.GetBySubjectIdBody;
import com.ry.project.bussiness.domain.request.teacher.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
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
 * 教师相关API
 * </p>
 *
 * @author
 * @since 2020-05-29
 */
@RestController
@RequestMapping("/teacher")
@Api(value = "教师相关接口", tags = {"教师相关接口"})
@Slf4j
public class TeacherController {

    @Autowired
    IStudentActionService studentActionService;

    @Autowired
    ITeacherActionService teacherActionService;

    @ApiOperation("【教师】创建直播课程")
    @PostMapping("/saveSubject")
    public AjaxResult saveSubject(@Validated @RequestBody SaveSubjectBody body, HttpServletRequest request) {
        try {
            return teacherActionService.saveSubject(body, request);
        } catch (Exception ex) {
            log.error(String.format("【教师】创建直播课程异常，参数：%s", JSON.toJSONString(body)), ex);
            return AjaxResult.error("创建直播课程失败");
        }
    }

    @ApiOperation("【教师】获取老师课程列表")
    @PostMapping("/getSubjectList")
    public AjaxResult getSubjectList(@RequestBody GetSubjectListBody body, HttpServletRequest request) {
        try {
            return teacherActionService.getSubjectList(body, request);
        } catch (Exception ex) {
            log.error(String.format("【教师】老师课程列表异常，参数：%s", JSON.toJSONString(body)), ex);
            return AjaxResult.error("老师课程列表失败");
        }
    }

    @ApiOperation("【教师】获取课程详细信息")
    @PostMapping("/getSubjectDetail")
    public AjaxResult getSubjectDetail(@RequestBody GetBySubjectIdBody body) {
        try {
            return teacherActionService.getSubjectDetail(body);
        } catch (Exception ex) {
            log.error(String.format("【教师】获取课程详细信息异常，参数：%s", JSON.toJSONString(body)), ex);
            return AjaxResult.error("获取课程详细信息失败");
        }
    }

    @ApiOperation("【教师】更新课程状态")
    @PostMapping("/changeStatus")
    public AjaxResult changeStatus(@Validated @RequestBody ChangeBySubjectIdBody body) {
        try {
            return teacherActionService.changeStatus(body);
        } catch (Exception ex) {
            log.error(String.format("【教师】更新课程状态异常，参数：%s", JSON.toJSONString(body)), ex);
            return AjaxResult.error("更新课程状态失败");
        }
    }

    @ApiOperation("【教师】根据课程ID获取评价列表")
    @PostMapping("/getEvaluateDetail")
    public AjaxResult getEvaluateDetail(@RequestBody EvaluateSubjectTeacherBody body) {
        try {
            return teacherActionService.getEvaluateDetail(body);
        } catch (Exception ex) {
            log.error(String.format("【教师】根据课程ID获取评价列表异常，参数：%s", JSON.toJSONString(body)), ex);
            return AjaxResult.error("根据课程ID获取评价列表失败");
        }
    }

    @ApiOperation("【教师】获取个人信息")
    @PostMapping("/getUserInfo")
    public AjaxResult getUserInfo(HttpServletRequest request) {
        try {
            return teacherActionService.getUserInfo(request);
        } catch (Exception ex) {
            log.error("【教师】获取个人信息异常", ex);
            return AjaxResult.error("获取个人信息失败");
        }
    }

    @ApiOperation("【教师】修改个人信息")
    @PostMapping("/updateUserInfo")
    public AjaxResult updateUserInfo(@Validated @RequestBody GetUserInfoBody body, HttpServletRequest request) {
        try {
            return teacherActionService.updateUserInfo(body, request);
        } catch (Exception ex) {
            log.error(String.format("【教师】修改个人信息异常，参数：%s", JSON.toJSONString(body)), ex);
            return AjaxResult.error("修改个人信息失败");
        }
    }

    @ApiOperation("【教师】回复留言信息")
    @PostMapping("/saveMessage")
    public AjaxResult saveMessage(@Validated @RequestBody SaveMessageBody body, HttpServletRequest request) {
        try {
            return teacherActionService.saveMessage(body, request);
        } catch (Exception ex) {
            log.error(String.format("【教师】回复留言信息异常，参数：%s", JSON.toJSONString(body)), ex);
            return AjaxResult.error("回复留言信息失败");
        }
    }

    @ApiOperation("【教师】根据课程获取章节列表")
    @PostMapping("/getChaptersBySubjectId")
    public AjaxResult getChaptersBySubjectId(@RequestBody GetBySubjectIdBody body) {
        try {
            return teacherActionService.getChaptersBySubjectId(body);
        } catch (Exception ex) {
            log.error("【教师】根据课程获取章节列表异常", ex);
            return AjaxResult.error("【教师】根据课程获取章节列表失败");
        }
    }


    @ApiOperation("【教师】获取留言信息列表")
    @PostMapping("/getLeavemessage")
    public AjaxResult getLeavemessage(@RequestBody GetLeavemessageBody body) {
        try {
            return teacherActionService.getLeavemessage(body);
        } catch (Exception ex) {
            log.error(String.format("【教师】获取留言信息列表异常，参数：%s", JSON.toJSONString(body)), ex);
            return AjaxResult.error("获取留言信息列表失败");
        }
    }

    @ApiOperation("【教师】更新留言状态")
    @PostMapping("/changeLeaveMessageStatus")
    public AjaxResult changeLeaveMessageStatus(@Validated @RequestBody GetByIdBody body) {
        try {
            return teacherActionService.changeLeaveMessageStatus(body);
        } catch (Exception ex) {
            log.error(String.format("【教师】更新留言状态异常，参数：%s", JSON.toJSONString(body)), ex);
            return AjaxResult.error("更新留言状态失败");
        }
    }


    @ApiOperation("【教师】更新评价状态")
    @PostMapping("/changeEvaluateStatus")
    public AjaxResult changeEvaluateStatus(@Validated @RequestBody GetByIdBody body) {
        try {
            return teacherActionService.changeEvaluateStatus(body);
        } catch (Exception ex) {
            log.error(String.format("【教师】更新评价状态异常，参数：%s", JSON.toJSONString(body)), ex);
            return AjaxResult.error("更新评价状态失败");
        }
    }


    @ApiOperation("【教师】获取授权号码")
    @ApiImplicitParam(name = "request", dataType = "GetBySubjectIdBody", paramType = "body", required = true, value = "获取授权号码入参实体")
    @PostMapping("/getAuthCode")
    public AjaxResult getAuthCode(@RequestBody GetBySubjectIdBody body, HttpServletRequest request) {
        try {
            return teacherActionService.getAuthCode(body, request);
        } catch (Exception ex) {
            log.error(String.format("【教师】获取授权号码报错，参数：%s", JSON.toJSONString(body)), ex);
            return AjaxResult.error("获取授权号码失败");
        }
    }
}
