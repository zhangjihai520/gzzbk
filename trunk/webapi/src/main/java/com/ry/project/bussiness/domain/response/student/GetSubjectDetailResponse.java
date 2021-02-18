package com.ry.project.bussiness.domain.response.student;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ry.project.bussiness.domain.response.ReviewRecord;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: 幸仁强
 * @Date: 2020/4/16 15:34
 * @Description: 返回实体对象基类
 */
@Data
@Accessors(chain = true)
public class GetSubjectDetailResponse {

    private String id;
    private String name;
    private String imagePath;
    private int playCount;
    private int registerStatus;
    private int classHour;
    private int category;
    private Integer classState;
    private int status;
    private int signUpCount;
    private int allowSignUpCount;
    private BigDecimal commentScore;
    private String gradeId;
    private String courseId;
    private String gradeName;
    private String courseName;
    private String remark;
    private String plan;

    private List<ReviewRecord> reviewRecords;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime beginTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime signUpBeginTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime signUpEndTime;
    private List<ChapterLessonObj> chapterLessons;


    public GetSubjectDetailResponse() {
        chapterLessons = new ArrayList<ChapterLessonObj>();
    }
}
