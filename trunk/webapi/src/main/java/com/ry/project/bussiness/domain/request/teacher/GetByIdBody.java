package com.ry.project.bussiness.domain.request.teacher;

import com.ry.project.bussiness.domain.request.PageRequest;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * @author yjx
 */
@Data
@Accessors(chain = true)
public class GetByIdBody {
    @NotBlank
    private String id;
}
