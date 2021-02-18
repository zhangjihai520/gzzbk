package com.ry.common.core.dledc.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.common.core.dledc.IDledcGetRequest;
import com.ry.common.core.dledc.response.GetUserBaseInfoResponse;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Data
public class GetUserBaseInfoRequest implements IDledcGetRequest<GetUserBaseInfoResponse> {
    /// <summary>
    /// 用户统一身份 uid
    /// </summary>
    @JSONField(name = "UId")
    private String uId;

    /// <summary>
    /// 0后台用户（管理员、学校），1学生  2老师
    /// </summary>
    @JSONField(name = "UserType")
    private String userType;

    public GetUserBaseInfoRequest() {
    }

    public GetUserBaseInfoRequest(String uId) {
        this.uId = uId;
        this.userType = StringUtils.EMPTY;
    }

    public GetUserBaseInfoRequest(String uId, String userType) {
        this.uId = uId;
        this.userType = userType;
    }

    @Override
    public Map<String, String> GetParameters() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("uid", getUId());
        map.put("UserType", getUserType());
        return map;
    }

    @Override
    public String GetApiPath() {
        return "api/api/users/getbaseinfo";
    }
}
