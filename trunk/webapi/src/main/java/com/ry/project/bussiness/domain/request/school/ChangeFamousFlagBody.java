package com.ry.project.bussiness.domain.request.school;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 根据课程ID获取数据入参
 */
@Data
@Accessors(chain = true)
public class ChangeFamousFlagBody {
    @NotBlank(message = "用户ID为空")
    private String userId;

    /**
     * 0解除名师  1设置名师
     */
    @Range(min = 0,max = 1,message = "名师状态不能只能为0和1")
    private Integer famousFlag;
}
