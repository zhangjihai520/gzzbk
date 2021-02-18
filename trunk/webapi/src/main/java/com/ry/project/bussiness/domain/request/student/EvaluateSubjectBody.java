package com.ry.project.bussiness.domain.request.student;

import com.ry.project.bussiness.domain.bo.EvaluateInfoVO;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 根据课程ID获取数据入参
 */
@Data
@Accessors(chain = true)
public class EvaluateSubjectBody extends EvaluateInfoVO {
}
