package com.ry.project.bussiness.domain.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ry.project.bussiness.domain.request.PageRequest;
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
public class GetSubjectListBody extends PageRequest {

    /**
     * 类型 0不限 1公共  2校本
     */
    private int type;
    /**
     * 类别 1直播  2录播
     */
    private int category;
    /**
     * 课程状态 -1全部
     */
    private int status;
    /**
     * 课程名称
     */
    private String name;
    /**
     *报名时间
     */
    private String signUpBeginTime;

    private String signUpEndTime;

    /**
     * 年级id
     */
    private int gradeId;

    /**
     * 科目ID
     */
    private int courseId;

    /**
     * 排序
     */
    private String orderBy;

}
