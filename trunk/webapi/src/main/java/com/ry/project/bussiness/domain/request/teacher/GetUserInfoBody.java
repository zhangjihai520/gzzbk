package com.ry.project.bussiness.domain.request.teacher;


import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 *
 * @author yjx
 */
@Data
@Accessors(chain = true)
public class GetUserInfoBody {

    private String userFace;

    @NotBlank
    private String schoolId;

    private String gradeId;

    private String courseId;

    private String phone;

    private String comment;
}
