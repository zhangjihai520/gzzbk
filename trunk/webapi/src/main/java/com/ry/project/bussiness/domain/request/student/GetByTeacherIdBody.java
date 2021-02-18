package com.ry.project.bussiness.domain.request.student;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * 根据课程ID获取数据入参
 */
@Data
@Accessors(chain = true)
public class GetByTeacherIdBody {
    @NotBlank
    private String teacherId;
}
