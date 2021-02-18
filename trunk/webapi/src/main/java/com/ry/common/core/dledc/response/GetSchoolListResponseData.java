package com.ry.common.core.dledc.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
@Data
@Accessors(chain = true)
public class GetSchoolListResponseData {
    /// <summary>
    /// 班级列表
    /// </summary>
    @JSONField(name = "ListData")
    private List<SchoolInfo> listData;
}
