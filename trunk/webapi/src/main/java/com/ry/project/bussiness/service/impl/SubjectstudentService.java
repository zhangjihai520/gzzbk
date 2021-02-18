package com.ry.project.bussiness.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ry.project.bussiness.domain.bo.StudentsubjectVO;
import com.ry.project.bussiness.domain.bo.Subjectstudent;
import com.ry.project.bussiness.mapper.SubjectstudentMapper;
import com.ry.project.bussiness.service.ISubjectstudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 学生报名课程表 服务实现类
 * </p>
 *
 * @author xrq
 * @since 2020-12-01
 */
@Service
public class SubjectstudentService extends ServiceImpl<SubjectstudentMapper, Subjectstudent> implements ISubjectstudentService {

    @Override
    public IPage<StudentsubjectVO> mySubjectPage(Page page, QueryWrapper<StudentsubjectVO> queryWrapper) {
        return this.baseMapper.mySubjectPage(page, queryWrapper);
    }
}
