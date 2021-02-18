package com.ry.common.core.dledc.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CheckUserAccountData {
    /// <summary>
    /// 用户信息
    /// </summary>
    private CheckUserAccountUserInfo SingleData;
}
