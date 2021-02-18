package com.ry.project.bussiness.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ry.project.bussiness.domain.bo.StudentsubjectVO;
import com.ry.project.bussiness.domain.bo.Subjectstudent;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 学生报名课程表 服务类
 * </p>
 *
 * @author xrq
 * @since 2020-12-01
 */
public interface ISubjectstudentService extends IService<Subjectstudent> {
    IPage<StudentsubjectVO> mySubjectPage(Page page, QueryWrapper<StudentsubjectVO> queryWrapper);
}
