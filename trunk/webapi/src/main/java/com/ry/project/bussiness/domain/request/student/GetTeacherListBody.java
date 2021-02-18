package com.ry.project.bussiness.domain.request.student;

import com.ry.project.bussiness.domain.request.PageRequest;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author
 * @since 2020-06-01
 */
@Data
@Accessors(chain = true)
public class GetTeacherListBody extends PageRequest {
    /**
     * 教师姓名
     */
    private String userTrueName;


}
