package com.ry.project.bussiness.domain.response.student;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
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
public class SubjectObj implements Serializable {

    private String id;

    private String name;

    private String imagePath;

    private int playCount;
    private int registerStatus;
    private int evaluateStatus;
    private int classHour;
    private int category;
    private Integer classState;
    private int status;
    private int signUpCount;
    private String signUpCounts;
    private int allowSignUpCount;
    private BigDecimal commentScore;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime beginTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;
}
