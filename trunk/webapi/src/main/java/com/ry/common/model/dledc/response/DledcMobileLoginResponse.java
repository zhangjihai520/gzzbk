package com.ry.common.model.dledc.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DledcMobileLoginResponse {

    /// <summary>
    /// 用户类型  1学生  2老师
    /// </summary>
    private int userType;

    /// <summary>
    /// 用户token
    /// </summary>
    private String token;

    public DledcMobileLoginResponse() {
    }

    public DledcMobileLoginResponse(int userType, String token) {
        this.userType = userType;
        this.token = token;
    }
}

