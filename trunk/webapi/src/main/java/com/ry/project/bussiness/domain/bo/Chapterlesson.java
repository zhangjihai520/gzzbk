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
 * 章节表
 * </p>
 *
 * @author xrq
 * @since 2020-12-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Chapterlesson extends Model<Chapterlesson> {

    private static final long serialVersionUID = 1L;

    /**
     * 章节ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 章节名称
     */
    private String name;

    /**
     * 所属课程id
     */
    private Long subjectId;

    /**
     * 授课教师ID
     */
    private Long teacherId;

    /**
     * 授课教师姓名
     */
    private String teacherName;

    /**
     * 视频地址
     */
    private String videoPath;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态，0无效，有效
     */
    private Integer status;

    /**
     * 简介/说明，存放富文本
     */
    private String remark;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
