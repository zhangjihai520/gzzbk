package com.ry.common.core.dledc.response;

import com.ry.common.core.dledc.DledcResponse;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CheckUserAccountResponse extends DledcResponse {
    /// <summary>
    /// 处理状态
    /// </summary>
    private int StatusCode;

    /// <summary>
    /// 处理结果信息
    /// </summary>
    private String Message;

    /// <summary>
    /// 返回参数
    /// </summary>
    private CheckUserAccountData Data;
}
