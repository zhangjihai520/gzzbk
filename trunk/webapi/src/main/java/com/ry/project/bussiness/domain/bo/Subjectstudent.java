package com.ry.project.bussiness.domain.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 学生课程关系表
 * </p>
 *
 * @author xrq
 * @since 2020-12-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Subjectstudent extends Model<Subjectstudent> {

    private static final long serialVersionUID = 1L;

    /**
     * 学生报名课程id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 课程id
     */
    private Long subjectId;

    /**
     * 学生id
     */
    private Long userId;

    /**
     * 记录学生当前章节的进度,暂时不用
     */
    private Integer chapterlessonId;

    /**
     * 是否评价
     */
    private Boolean isEvaluate;

    /**
     * 是否报名  true是报名的，false是回看的
     */
    private Boolean isRegister;
    /**
     * 状态，0删除，1正常
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
