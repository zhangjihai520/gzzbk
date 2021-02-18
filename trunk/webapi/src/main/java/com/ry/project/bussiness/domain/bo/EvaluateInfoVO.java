package com.ry.project.bussiness.domain.bo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 学生课程关系表
 * </p>
 *
 * @author xrq
 * @since 2020-12-01
 */
@Data
@Accessors(chain = true)
public class EvaluateInfoVO implements Serializable {
    @NotBlank
    private String subjectId;
    @Range(min = 1, max = 6)
    private int evaluateLevel;
    @NotBlank
    private String content;
}
