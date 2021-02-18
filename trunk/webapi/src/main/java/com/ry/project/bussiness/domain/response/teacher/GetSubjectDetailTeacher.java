package com.ry.project.bussiness.domain.response.teacher;

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
public class GetSubjectDetailTeacher {

    private String id;

    private String name;

    private Integer category;

    private Integer status;

    private String gradeId;

    private String courseId;

    private String gradeName;

    private String courseName;

    private String imagePath;

    private int classHour;

    private int playCount;

    private int signUpCount;

    private int allowSignUpCount;

    private BigDecimal commentScore;

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

    private List<ChapterLessonObjTeacher> chapterLessons;

    public GetSubjectDetailTeacher() {
        chapterLessons = new ArrayList<ChapterLessonObjTeacher>();
    }
}
