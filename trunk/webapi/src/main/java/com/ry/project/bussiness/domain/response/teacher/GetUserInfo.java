package com.ry.project.bussiness.domain.response.teacher;


import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author yjx
 */
@Data
@Accessors(chain = true)
public class GetUserInfo {

    private String userId;

    private String userName;

    private String userTrueName;

    private String userFace;

    private String schoolId;

    private String schoolName;

    private String gradeId;

    private String gradeName;

    private String courseId;

    private String courseName;

    private String phone;

    private String comment;
    /**
     * 绑定状态，教师关联学校需要学校管理员审核，学校管理员可解绑已绑定的教师。 0已解绑  1待学校审核    2绑定成功
     */
    private Integer bindStatus;

    /**
     * 名师标注  0否  1是
     */
    private Integer famousFlag;
}
