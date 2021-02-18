package com.ry.project.bussiness.domain.response.student;

import com.ry.project.bussiness.domain.bo.EvaluateInfoVO;
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
public class StudentSubjectObj {

    private String id;

    private String name;

    private String imagePath;

    private String teacherName;
    /**
     * 类别 1直播  2录播
     */
    private Integer category;

    private int classState;

    private EvaluateInfoVO evaluateInfo;
}
