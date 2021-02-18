package com.ry.project.bussiness.domain.request.school;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class SaveSubjectBody {
    private String id;
    @NotBlank
    private String name;

    private String imagePath;

    private String gradeId;

    private String courseId;

    private String remark;

    private String plan;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime beginTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;

    private String tag;

    private int status;

    private List<ChapterLessonObj> chapterLessons;

    public SaveSubjectBody() {
        chapterLessons = new ArrayList<>();
    }
}
