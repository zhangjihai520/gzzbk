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
 * 课程评价表
 * </p>
 *
 * @author xrq
 * @since 2020-12-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Evaluaterecord extends Model<Evaluaterecord> {

    private static final long serialVersionUID = 1L;

    /**
     * 评价记录id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 课程id
     */
    private Long subjectId;

    /**
     * 评价人id
     */
    private Long sourceUserId;

    /**
     * 评价人姓名
     */
    private String sourceUserName;

    /**
     * 评价星级
     */
    private Integer evaluateLevel;

    /**
     * 评价内容
     */
    private String content;

    /**
     * 状态，0删除，1未删除
     */
    private Integer status;

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
