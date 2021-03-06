package com.ry.project.bussiness.domain.request;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Auther: 幸仁强
 * @Date: 2019/7/16 15:34
 * @Description: 入参实体基类（分页）
 */
@Data
@Accessors(chain = true)
public class PageRequest {
    //页码从1开始
    private int pageIndex = 1;//开始页
    //页码大小
    private int pageSize = 25;//条数

    /// <summary>
    /// 跳过行数
    /// </summary>
    public int getPageSkip() {
        if (pageSize == 0) {
            pageSize = 25;
        }
        if (pageIndex == 0) {
            pageIndex = 1;
        }
        return (pageIndex - 1) * pageSize;
    }
}
