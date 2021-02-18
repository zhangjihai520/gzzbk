package com.ry.project.bussiness.domain.request.manager;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class SaveBannerBody {
    private String bannerId;

    @NotBlank
    private String name;
    @NotBlank
    private String picUrl;
    @NotBlank
    private String linkUrl;
}
