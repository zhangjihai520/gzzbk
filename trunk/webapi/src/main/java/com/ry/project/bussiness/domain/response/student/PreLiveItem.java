package com.ry.project.bussiness.domain.response.student;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class PreLiveItem {
    private String subjectId;

    private String name;

    private String liveTimeRange;

    private String teacherName;
    //-1待报名,0报名中,1待上课,2上课中,3已下课
    private Integer classState;
    /**
     * 封面
     */
    private String imagePath;
    /**
     * 报名状态 0未报名 1已报名
     */
    private int registerStatus;

    /**
     * 允许报名人数
     */
    private String signUpCount;

}
