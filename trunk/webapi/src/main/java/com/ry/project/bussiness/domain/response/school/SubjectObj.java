package com.ry.project.bussiness.domain.response.school;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

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
public class SubjectObj {

    private String id;

    private String name;

    private String schoolName;

    private String teacherName;

    private int category;

    private int status;

    private String gradeName;

    private String classTime;

    private Integer createrFlag;

    private String signUpCounts;
}
