package com.ry.project.bussiness.domain.response.school;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

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
public class BannerObj {

    private String id;

    /**
     * bannel名称
     */
    private String name;

    /**
     * bannel图片链接
     */
    private String picUrl;

    /**
     * 外链地址
     */
    private String linkUrl;

    /**
     * 状态  0删除 1上架  2下架
     */
    private Integer status;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
}
