package com.ry.framework.security;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoginResponse {

    /// <summary>
    /// 用户类型  0管理员/学校 ,  1学生  2老师
    /// </summary>
    private int userType;

    /// <summary>
    /// 用户token
    /// </summary>
    private String token;

    public LoginResponse() {
    }

    public LoginResponse(int userType, String token) {
        this.userType = userType;
        this.token = token;
    }
}

