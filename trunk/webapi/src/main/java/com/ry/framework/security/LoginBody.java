package com.ry.framework.security;

import lombok.Data;

/**
 * 用户登录对象
 *
 * @author
 */
@Data
public class LoginBody {
    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 验证码
     */
    private String code;

    /**
     * 0学校/管理员  1学生/教师
     */
    private int loginType;

    /**
     * 唯一标识
     */
    private String uuid = "";
}
