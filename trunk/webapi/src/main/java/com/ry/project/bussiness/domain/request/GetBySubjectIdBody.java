package com.ry.project.bussiness.domain.request;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * 根据课程ID获取数据入参
 */
@Data
@Accessors(chain = true)
public class GetBySubjectIdBody {
    @NotBlank(message = "入参不能为空")
    private String subjectId;
}
