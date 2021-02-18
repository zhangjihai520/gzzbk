package com.ry.project.bussiness.domain.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 留言表
 * </p>
 *
 * @author xrq
 * @since 2020-12-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Leavemessage extends Model<Leavemessage> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 课程id
     */
    private Long subjectId;

    /**
     * 课程章节id
     */
    private Long chapterlessonId;

    /**
     * 回复id
     */
    private Long replyId;

    /**
     * 内容
     */
    private String content;

    /**
     * 状态，0隐藏，1展示
     */
    private Integer status;

    /**
     * 发布者id
     */
    private Long userId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 回复标识 0未回复 1已回复
     */
    private Integer replayState;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
