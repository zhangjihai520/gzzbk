package com.ry.project.bussiness.domain.request.student;

import com.ry.project.bussiness.domain.bo.EvaluateInfoVO;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * 根据课程ID获取数据入参
 */
@Data
@Accessors(chain = true)
public class LeavemessageBody {
    @NotBlank
    private String subjectId;
    @NotBlank
    private String chapterlessonId;
    private String replyId;
    @NotBlank
    private String content;
}
