package com.ry.project.bussiness.domain.response.teacher;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
public class SubjectObjTeacher {

    private String id;

    private String name;

    private String imagePath;

    private Integer category;

    private String gradeName;

    private String courseName;

    private String signUpCounts;

    private Integer status;

    private BigDecimal commentScore;

    private String classTime;

    private Integer classState;

    private Integer createrFlag;
}
