package com.ry.project.bussiness.domain.response.student;

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
public class LiveObj {

    private String id;

    private String name;

    private String imagePath;

    private String teacherName;

    private int registerStatus;

    private Integer signUpCount;

    private String signUpCounts;

    private Integer allowSignUpCount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime beginTime;

    private Integer classStatus;

}
