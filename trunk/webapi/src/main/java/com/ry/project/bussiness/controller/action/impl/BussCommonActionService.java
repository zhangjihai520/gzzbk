package com.ry.project.bussiness.controller.action.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ry.common.enums.UserType;
import com.ry.common.utils.UrlUtil;
import com.ry.framework.web.domain.AjaxResult;
import com.ry.project.bussiness.controller.action.IBussCommonActionService;
import com.ry.project.bussiness.domain.bo.*;
import com.ry.project.bussiness.domain.response.school.BannerObj;
import com.ry.project.bussiness.service.*;
import com.ry.project.common.vo.IdName;
import com.ry.project.system.service.ISysDeptService;
import com.ry.project.system.service.ISysDictDataService;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * <p>
 * 教师
 * </p>
 */
@Slf4j
@Service
public class BussCommonActionService implements IBussCommonActionService {
    @Autowired
    IUserService userService;
    @Autowired
    ISysDictDataService sysDictDataService;
    @Autowired
    ISysDeptService sysDeptService;
    @Autowired
    IBannerService bannerService;

    @Override
    public AjaxResult teacherOptions() {
        var userQueryWrapper = new QueryWrapper<User>();
        userQueryWrapper.lambda().eq(User::getStatus, 1).eq(User::getUserTypeId, UserType.Teacher.getId());
        var list = userService.list(userQueryWrapper);
        var response = new ArrayList<IdName>();
        list.forEach(x -> response.add(new IdName().setId(UrlUtil.encrypt(x.getUserId())).setName(x.getUserTrueName() + " " + x.getPhone() + "(" + x.getSchoolName() + ")")));
        return AjaxResult.success(response);
    }

    @Override
    public AjaxResult gradeOptions() {
        var dataList = sysDictDataService.selectDictDataByType("sys_grade");
        var response = new ArrayList<IdName>();
        dataList.forEach(x -> response.add(new IdName().setId(UrlUtil.encrypt(x.getDictValue())).setName(x.getDictLabel())));
        return AjaxResult.success(response);
    }

    @Override
    public AjaxResult courseOptions() {
        var dataList = sysDictDataService.selectDictDataByType("sys_course");
        var response = new ArrayList<IdName>();
        dataList.forEach(x -> response.add(new IdName().setId(UrlUtil.encrypt(x.getDictValue())).setName(x.getDictLabel())));
        return AjaxResult.success(response);
    }

    @Override
    public AjaxResult schoolOptions() {
        var dataList = sysDeptService.selectSchoolList();
        var response = new ArrayList<IdName>();
        dataList.forEach(x -> response.add(new IdName().setId(UrlUtil.encrypt(x.getDeptId())).setName(x.getDeptName())));
        return AjaxResult.success(response);
    }

    @Override
    public AjaxResult banners() {
        var bannerQueryWrapper = new QueryWrapper<Banner>();
        bannerQueryWrapper.lambda().eq(Banner::getStatus, 1);
        IPage<Banner> page = new Page<>(1, 5);
        IPage<Banner> pages = bannerService.page(page, bannerQueryWrapper);
        var list = new ArrayList<BannerObj>();
        for (var banner : pages.getRecords()) {
            var item = new BannerObj()
                    .setId(UrlUtil.encrypt(banner.getId()))
                    .setName(banner.getName())
                    .setStatus(banner.getStatus())
                    .setPicUrl(banner.getPicUrl())
                    .setLinkUrl(banner.getLinkUrl())
                    .setCreateTime(banner.getCreateTime())
                    .setUpdateTime(banner.getUpdateTime());
            list.add(item);
        }
        return AjaxResult.success(list);
    }
}
