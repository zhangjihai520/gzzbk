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
public class GetByBannerIdBody {
    @NotBlank(message = "课程ID为空")
    private String bannerId;
}
