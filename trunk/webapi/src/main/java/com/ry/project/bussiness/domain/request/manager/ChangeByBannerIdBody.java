package com.ry.project.bussiness.domain.request.manager;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 根据BannerID获取数据入参
 */
@Data
@Accessors(chain = true)
public class ChangeByBannerIdBody {
    @NotBlank(message = "BannerID为空")
    private String bannerId;
    @NotNull(message = "状态不能为空")
    private Integer status;
}
