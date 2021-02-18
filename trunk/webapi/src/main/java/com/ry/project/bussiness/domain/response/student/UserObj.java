package com.ry.project.bussiness.domain.response.student;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

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
public class UserObj implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户类型，1学生，2老师
     */
    private Integer userTypeId;

    /**
     * 用户来源，1云平台，2注册
     */
    private Integer userFrom;

    /**
     * 用户真名
     */
    private String userTrueName;

    /**
     * 性别，1男，2女，0不详
     */
    private Integer userSex;

    /**
     * 用户头像
     */
    private String userFace;

    /**
     * 邮箱
     */
    private String emailAddress;
    /**
     * 学校id
     */
    private String schoolId;
    /**
     * 学校code
     */
    private String schoolCode;

    /**
     * 学校名
     */
    private String schoolName;

    /**
     * 班级名
     */
    private String className;

    /**
     * 个人简介
     */
    private String comment;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 年级ID
     */
    private String gradeId;

    /**
     * 用户来源类型，1云平台，2导入学生，3注册
     */
    private Integer sourceTypeId;

    /**
     * 用户来源id
     */
    private String sourceId;

    /**
     * 状态，0删除，1未删除
     */
    private Integer status;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    /**
     * 学籍号
     */
    private String studentCode;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 最后一次登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime lastLoginTime;

}
