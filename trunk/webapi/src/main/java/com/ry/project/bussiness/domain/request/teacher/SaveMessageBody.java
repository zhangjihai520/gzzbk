package com.ry.project.bussiness.domain.request.teacher;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * @author yjx
 */
@Data
@Accessors(chain = true)
public class SaveMessageBody {
    @NotBlank
    private String subjectId;

    private String chapterlessonId;
    @NotBlank
    private String replyId;
    @NotBlank
    private String content;
}
