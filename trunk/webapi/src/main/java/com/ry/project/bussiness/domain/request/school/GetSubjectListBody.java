package com.ry.project.bussiness.domain.request.school;

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
    private String schoolId;
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
    private String beginTime;

    private String endTime;

}
