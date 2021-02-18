package com.ry.project.bussiness.domain.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ry.framework.annotation.IRedisStoredObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 学生、教师表。都是来源于云平台
 * </p>
 *
 * @author xrq
 * @since 2020-12-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User extends Model<User> implements IRedisStoredObject {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

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
    private Long schoolId;
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
    private Integer gradeId;

    /**
     * 学科ID
     */
    private Integer courseId;

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
     * 用户角色(1学生，2老师，3管理员，4家长),多个角色以英文逗号分隔
     */
    private String userRoles;

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

    /**
     * 绑定状态，教师关联学校需要学校管理员审核，学校管理员可解绑已绑定的教师。 0已解绑  1待学校审核    2绑定成功
     */
    private Integer bindStatus;

    /**
     * 名师标注  0否  1是
     */
    private Integer famousFlag;

    @Override
    protected Serializable pkVal() {
        return this.userId;
    }

    private static String key = "User:UserId_%s";

    public String key() {
        return String.format(key, this.userId);
    }

}
