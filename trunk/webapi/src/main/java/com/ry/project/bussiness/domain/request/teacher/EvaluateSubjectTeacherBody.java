package com.ry.project.bussiness.domain.request.teacher;

import com.ry.project.bussiness.domain.bo.EvaluateInfoVO;
import com.ry.project.bussiness.domain.request.PageRequest;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 根据课程ID获取数据入参
 */
@Data
@Accessors(chain = true)
public class EvaluateSubjectTeacherBody extends PageRequest {

    private String subjectId;
}
