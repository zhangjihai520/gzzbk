package com.ry.project.bussiness.domain.bo;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ry.framework.annotation.IRedisStoredObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 学科/专题表
 * </p>
 *
 * @author xrq
 * @since 2020-12-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Subject extends Model<Subject> implements IRedisStoredObject {

    private static final long serialVersionUID = 1L;

    /**
     * 学科/专题id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 学科/专题名称
     */
    private String name;

    /**
     * 封面地址
     */
    private String imagePath;

    /**
     * 类型 1公共  2校本
     */
    private Integer type;

    /**
     * 类别 1直播  2录播
     */
    private Integer category;

    /**
     * 年级id
     */
    private Integer gradeId;

    /**
     * 科目ID
     */
    private Integer courseId;

    /**
     * 所属学校名称，校本课程时用
     */
    private String schoolName;

    /**
     * 所属学校id，校本课程时用
     */
    private Long schoolId;

    /**
     * 总课时数
     */
    private Integer classHour;

    /**
     * 播放量
     */
    private Integer playCount;

    /**
     * 实际报名人数
     */
    private Integer signUpCount;

    /**
     * 允许报名人数  0表示不限名额
     */
    private Integer allowSignUpCount;

    /**
     * 评分
     */
    private BigDecimal commentScore;

    /**
     * 评分人数
     */
    private Integer commentCount;

    /**
     * 简介/说明 存放富文本
     */
    private String remark;

    /**
     * 教学计划
     */
    private String plan;

    /**
     * 报名开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime signUpBeginTime;

    /**
     * 报名结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime signUpEndTime;

    /**
     * 课程标签，读字典表
     */
    private String tag;

    /**
     * 状态 0删除 1草稿 2待学校审核 3待中心审核  4驳回  5审核通过（待上架）  6已上架   7已下架
     */
    private Integer status;

    /**
     * 上课状态，1待上课,2上课中,3已下课
     */
    private Integer classState;

    /**
     * 见枚举 CreaterFlag
     */
    private Integer createrFlag;
    /**
     * 审核意见 学校和管理员之间的审核意见用&&分割开    如   “ 学校同意&&中心同意”
     */
    private String auditOpinion;
    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime beginTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;
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
    @TableField(exist = false)
    private List<Chapterlesson> chapterlessonList;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    private static String key = "Subject:SubjectId_%s";

    public String key() {
        return String.format(key, this.id);
    }
}
