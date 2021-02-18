package com.ry.project.bussiness.domain.request.manager;

import com.ry.project.bussiness.domain.request.PageRequest;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author
 * @since 2020-06-01
 */
@Data
@Accessors(chain = true)
public class GetBannerListBody extends PageRequest {
    /**
     * 课程状态 -1全部 0删除 1上架  2下架
     */
    private int status;
    /**
     * 课程名称
     */
    private String name;

    /**
     * 创建时间范围 开始
     */
    private String beginTime;
    /**
     * 创建时间范围 结束
     */
    private String endTime;
}
