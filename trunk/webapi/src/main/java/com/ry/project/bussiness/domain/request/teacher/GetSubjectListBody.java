package com.ry.project.bussiness.domain.request.teacher;

import com.ry.project.bussiness.domain.request.PageRequest;
import lombok.Data;
import lombok.experimental.Accessors;

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
     * 课程名称
     */
    private String name;

    /**
     * 课程类型
     */
    private int category;
    /**
     * 课程状态
     */
    private int status;

    /**
     * 课程时间范围 结束
     */
    private String endTime;



    /**
     * 课程时间范围 开始
     */
    private String beginTime;


}
