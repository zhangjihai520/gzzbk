package com.ry.project.bussiness.domain.request.school;

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
     * 绑定状态 -1全部  0已解除  1待审核  2已绑定
     */
    private Integer bindStatus;
    /**
     * 教师姓名
     */
    private String teacherName;

    private String schoolId;

}
