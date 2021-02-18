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
public class GetSubjectListBody extends PageRequest {
    /**
     * 课程名称
     */
    private String name;

    /**
     * 年级id
     */
    private String gradeId;

    /**
     * 科目ID
     */
    private String courseId;


}
