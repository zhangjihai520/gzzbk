package com.ry.project.bussiness.controller;

import com.ry.common.exception.file.InvalidExtensionException;
import com.ry.common.utils.CommonUtil;
import com.ry.common.utils.DateUtils;
import com.ry.common.utils.FtpUtil;
import com.ry.common.utils.IdUtils;
import com.ry.common.utils.file.FileUploadUtils;
import com.ry.common.utils.file.MimeTypeUtils;
import com.ry.framework.aspectj.lang.annotation.Log;
import com.ry.framework.aspectj.lang.enums.BusinessType;
import com.ry.framework.config.CommonConfig;
import com.ry.framework.security.service.TokenService;
import com.ry.framework.web.domain.AjaxResult;
import com.ry.project.bussiness.controller.action.IBussCommonActionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * <p>
 * 教师相关API
 * </p>
 *
 * @author
 * @since 2020-05-29
 */
@RestController
@RequestMapping("/busscommon")
@Api(value = "公用接口", tags = {"公用接口"})
@Slf4j
public class BussCommonController {

    @Autowired
    IBussCommonActionService commonActionService;

    @Autowired
    private TokenService tokenService;


    @ApiOperation("获取教师下拉选项")
    @PostMapping("/teacherOptions")
    public AjaxResult teacherOptions() {
        try {
            return commonActionService.teacherOptions();
        } catch (Exception ex) {
            log.error("获取教师下拉选项异常", ex);
            return AjaxResult.error("获取教师下拉选项失败");
        }
    }

    @ApiOperation("获取年级下拉选项")
    @PostMapping("/gradeOptions")
    public AjaxResult gradeOptions() {
        try {
            return commonActionService.gradeOptions();
        } catch (Exception ex) {
            log.error("获取年级下拉选项异常", ex);
            return AjaxResult.error("获取年级下拉选项失败");
        }
    }

    @ApiOperation("获取学科下拉选项")
    @PostMapping("/courseOptions")
    public AjaxResult courseOptions() {
        try {
            return commonActionService.courseOptions();
        } catch (Exception ex) {
            log.error("获取学科下拉选项异常", ex);
            return AjaxResult.error("获取学科下拉选项失败");
        }
    }

    @ApiOperation("获取学校下拉选项")
    @PostMapping("/schoolOptions")
    public AjaxResult schoolOptions() {
        try {
            return commonActionService.schoolOptions();
        } catch (Exception ex) {
            log.error("获取学校下拉选项异常", ex);
            return AjaxResult.error("获取学校下拉选项失败");
        }
    }

    @ApiOperation("获取首页banner")
    @PostMapping("/banners")
    public AjaxResult banners() {
        try {
            return commonActionService.banners();
        } catch (Exception ex) {
            log.error("获取首页banner异常", ex);
            return AjaxResult.error("获取首页banner失败");
        }
    }

    /**
     * @param file
     * @param fileType  文件类型  0未分类（如富文本框里的图片）, 1 用户头像,2 封面, 3 视频, 4 课件, 5 banner
     * @param request
     * @return
     */
    @Log(title = "文件上传", businessType = BusinessType.UPLOAD)
    @PostMapping(value = "/upload")
    public AjaxResult upload(MultipartFile file, int fileType, HttpServletRequest request) {
        try {
            var loginUser = tokenService.getLoginUser(request);
            var userType = loginUser.getUserType();
            var userId = loginUser.getUserId();
            if (fileType <= 0) {
                return AjaxResult.error("参数错误！");
            }
            if (userId <= 0) {
                return AjaxResult.error("用户权限不足！");
            }
            var mimeType = MimeTypeUtils.getAllowMimeTypeByFileType(fileType);
            FileUploadUtils.assertAllowed(file, mimeType);
            String oldFileName = file.getOriginalFilename();
            String suffix = oldFileName.substring(oldFileName.lastIndexOf("."));
            var filePath = userType + "/" + userId + "/" + fileType;
            String newFileName = DateUtils.dateTimeNow() + "_" + IdUtils.fastSimpleUUID() + suffix;

            Boolean resultBoolean = FtpUtil.uploadFile(CommonConfig.getFtpHost(), Integer.parseInt(CommonConfig.getFtpPort()), CommonConfig.getFtpUserName(), CommonConfig.getFtpPassword(), CommonConfig.getFtpBasePath(), filePath, newFileName, file.getInputStream());
            if (resultBoolean) {
                String url = CommonConfig.getFtpHttpPath() + CommonConfig.getFtpBasePath() + "/" + filePath + "/" + newFileName;
                var ajaxResult = AjaxResult.success();
                ajaxResult.put("url", url);
                return ajaxResult;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResult.error("文件上传失败");
    }

}
