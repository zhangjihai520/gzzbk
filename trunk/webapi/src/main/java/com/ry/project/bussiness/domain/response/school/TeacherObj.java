package com.ry.project.bussiness.domain.response.school;

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
public class TeacherObj {

    private String id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户真名
     */
    private String userTrueName;

    /**
     * 学校名称
     */
    private String schoolName;

    /**
     * 性别，1男，2女，0不详
     */
    private Integer userSex;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 绑定状态，教师关联学校需要学校管理员审核，学校管理员可解绑已绑定的教师。 0已解绑  1待学校审核    2绑定成功
     */
    private Integer bindStatus;

    /**
     * 名师标注  0否  1是
     */
    private Integer famousFlag;

}
