package com.ry.project.bussiness.domain.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ry.project.bussiness.domain.bo.Subject;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
public class StudentsubjectVO extends Subject implements Serializable {

    private Integer evaluateLevel;

    private String content;

    private int chapterlessonId;
}
