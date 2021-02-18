package com.ry.common.core.dledc.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.ry.common.core.dledc.IDledcGetRequest;
import com.ry.common.core.dledc.response.CheckUserAccountResponse;
import com.ry.common.core.dledc.response.GetSchoolListResponse;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class GetSchoolListRequest implements IDledcGetRequest<GetSchoolListResponse> {

    public GetSchoolListRequest() {
    }


    @Override
    public Map<String, String> GetParameters() {
        Map<String, String> map = new HashMap<String, String>();
        return map;
    }

    @Override
    public String GetApiPath() {
        return "api/api/Organize/GetOrgList";
    }
}
