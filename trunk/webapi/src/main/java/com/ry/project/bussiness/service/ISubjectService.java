package com.ry.project.bussiness.service;

import com.ry.project.bussiness.domain.bo.Subject;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 学科/专题表 服务类
 * </p>
 *
 * @author xrq
 * @since 2020-12-02
 */
public interface ISubjectService extends IService<Subject> {
    Subject getByKey(Long subjectId, Boolean useCache);
}
