package com.ry.project.bussiness.domain.request.school;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 根据课程ID获取数据入参
 */
@Data
@Accessors(chain = true)
public class ChangeBindStatusBody {
    @NotBlank(message = "用户ID为空")
    private String userId;

    /**
     * 0解除绑定  2绑定成功
     */
    @NotNull(message = "绑定状态不能为空")
    private Integer bindStatus;
}
