package com.ry.project.bussiness.controller;

import com.alibaba.fastjson.JSON;
import com.ry.framework.web.domain.AjaxResult;
import com.ry.project.bussiness.controller.action.IManagerActionService;
import com.ry.project.bussiness.domain.request.manager.ChangeByBannerIdBody;
import com.ry.project.bussiness.domain.request.ChangeBySubjectIdBody;
import com.ry.project.bussiness.domain.request.manager.GetByBannerIdBody;
import com.ry.project.bussiness.domain.request.GetBySubjectIdBody;
import com.ry.project.bussiness.domain.request.manager.GetBannerListBody;
import com.ry.project.bussiness.domain.request.school.*;
import com.ry.project.bussiness.domain.request.manager.SaveBannerBody;
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
@RequestMapping("/manager")
@Api(value = "管理员模块接口", tags = {"管理员模块接口"})
@Slf4j
public class ManagerController {
    @Autowired
    IManagerActionService managerActionService;

    @ApiOperation("【管理员】获取教师列表信息")
    @PostMapping("/getTeacherList")
    public AjaxResult getTeacherList(@RequestBody GetTeacherListBody body, HttpServletRequest request) {
        try {
            return managerActionService.getTeacherList(body, request);
        } catch (Exception ex) {
            log.error(String.format("【管理员】获取课程列表信息异常，参数：%s", JSON.toJSONString(body)), ex);
            return AjaxResult.error("【管理员】获取课程列表信息失败");
        }
    }

    @ApiOperation("【管理员】获取教师详细信息")
    @PostMapping("/getTeacherDetail")
    public AjaxResult getTeacherDetail(@RequestBody GetByIdBody body) {
        try {
            return managerActionService.getTeacherDetail(body);
        } catch (Exception ex) {
            log.error(String.format("【管理员】获取教师详细信息异常，参数：%s", JSON.toJSONString(body)), ex);
            return AjaxResult.error("【管理员】获取教师详细信息失败");
        }
    }

    @ApiOperation("【管理员】更新名师状态")
    @PostMapping("/changeTeacherFamousFlag")
    public AjaxResult changeTeacherFamousFlag(@Validated @RequestBody ChangeFamousFlagBody body) {
        try {
            return managerActionService.changeTeacherFamousFlag(body);
        } catch (Exception ex) {
            log.error(String.format("【学校】更新名师状态异常，参数：%s", JSON.toJSONString(body)), ex);
            return AjaxResult.error("【管理员】更新更新名师状态失败");
        }
    }

    @ApiOperation("【管理员】获取课程列表信息")
    @PostMapping("/getSubjectList")
    public AjaxResult getSubjectList(@RequestBody GetSubjectListBody body, HttpServletRequest request) {
        try {
            return managerActionService.getSubjectList(body, request);
        } catch (Exception ex) {
            log.error(String.format("【管理员】获取课程列表信息异常，参数：%s", JSON.toJSONString(body)), ex);
            return AjaxResult.error("获取课程列表信息失败");
        }
    }

    @ApiOperation("【管理员】获取待办课程列表信息")
    @PostMapping("/getToDoSubjectList")
    public AjaxResult getToDoSubjectList(@RequestBody GetSubjectListBody body, HttpServletRequest request) {
        try {
            return managerActionService.getToDoSubjectList(body, request);
        } catch (Exception ex) {
            log.error(String.format("【管理员】获取课程列表信息异常，参数：%s", JSON.toJSONString(body)), ex);
            return AjaxResult.error("获取课程列表信息失败");
        }
    }

    @ApiOperation("【管理员】保存录播课程")
    @PostMapping("/saveSubject")
    public AjaxResult saveSubject(@Validated @RequestBody SaveSubjectBody body, HttpServletRequest request) {
        try {
            return managerActionService.saveSubject(body, request);
        } catch (Exception ex) {
            log.error(String.format("【管理员】保存录播课程异常，参数：%s", JSON.toJSONString(body)), ex);
            return AjaxResult.error("保存录播课程失败");
        }
    }

    @ApiOperation("【管理员】获取课程详细信息")
    @PostMapping("/getSubjectDetail")
    public AjaxResult getSubjectDetail(@RequestBody GetBySubjectIdBody body) {
        try {
            return managerActionService.getSubjectDetail(body);
        } catch (Exception ex) {
            log.error(String.format("【管理员】获取课程详细信息异常，参数：%s", JSON.toJSONString(body)), ex);
            return AjaxResult.error("获取课程详细信息失败");
        }
    }

    @ApiOperation("【管理员】更新课程状态")
    @PostMapping("/changeStatus")
    public AjaxResult changeStatus(@Validated @RequestBody ChangeBySubjectIdBody body) {
        try {
            return managerActionService.changeStatus(body);
        } catch (Exception ex) {
            log.error(String.format("【管理员】更新课程状态异常，参数：%s", JSON.toJSONString(body)), ex);
            return AjaxResult.error("更新课程状态失败");
        }
    }

    @ApiOperation("【管理员】保存Banner")
    @PostMapping("/saveBanner")
    public AjaxResult saveBanner(@Validated @RequestBody SaveBannerBody body) {
        try {
            return managerActionService.saveBanner(body);
        } catch (Exception ex) {
            log.error(String.format("【管理员】保存Banner异常，参数：%s", JSON.toJSONString(body)), ex);
            return AjaxResult.error("保存Banner失败");
        }
    }

    @ApiOperation("【管理员】更新banner状态")
    @PostMapping("/changeBannerStatus")
    public AjaxResult changeBannerStatus(@Validated @RequestBody ChangeByBannerIdBody body) {
        try {
            return managerActionService.changeBannerStatus(body);
        } catch (Exception ex) {
            log.error(String.format("【管理员】更新banner状态异常，参数：%s", JSON.toJSONString(body)), ex);
            return AjaxResult.error("更新banner状态失败");
        }
    }

    @ApiOperation("【管理员】获取banner详情")
    @PostMapping("/getBannerDetail")
    public AjaxResult getBannerDetail(@Validated @RequestBody GetByBannerIdBody body) {
        try {
            return managerActionService.getBannerDetail(body);
        } catch (Exception ex) {
            log.error(String.format("【管理员】获取banner详情异常，参数：%s", JSON.toJSONString(body)), ex);
            return AjaxResult.error("获取banner详情失败");
        }
    }

    @ApiOperation("【管理员】获取Banner列表信息")
    @PostMapping("/getBannerList")
    public AjaxResult getBannerList(@RequestBody GetBannerListBody body) {
        try {
            return managerActionService.getBannerList(body);
        } catch (Exception ex) {
            log.error(String.format("【管理员】获取Banner列表信息异常，参数：%s", JSON.toJSONString(body)), ex);
            return AjaxResult.error("获取Banner列表信息失败");
        }
    }
}
