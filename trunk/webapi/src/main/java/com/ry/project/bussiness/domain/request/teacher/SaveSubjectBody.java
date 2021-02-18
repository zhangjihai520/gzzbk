package com.ry.project.bussiness.domain.request.teacher;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class SaveSubjectBody {

    private String id;
    @NotBlank
    private String name;

    private String imagePath;

    private String gradeId;

    private String courseId;

    private int allowSignUpCount;

    private String remark;

    private String plan;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime signUpBeginTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime signUpEndTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime beginTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;

    private String tag;

    private int status;

    private String lessonsName;

    private String videoPath;
}
