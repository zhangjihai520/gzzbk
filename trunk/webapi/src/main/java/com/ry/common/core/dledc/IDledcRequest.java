package com.ry.common.core.dledc;

public interface IDledcRequest<T extends DledcResponse> {
    /// <summary>
    /// 请求api路径
    /// </summary>
    /// <returns>api路径</returns>
    String GetApiPath();
}
