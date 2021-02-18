package com.ry.project.bussiness.domain.request;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 根据课程ID获取数据入参
 */
@Data
@Accessors(chain = true)
public class ChangeBySubjectIdBody {
    @NotBlank(message = "课程ID为空")
    private String subjectId;
    @NotNull(message = "状态不能为空")
    private Integer status;

    private String auditOpinion;
}
