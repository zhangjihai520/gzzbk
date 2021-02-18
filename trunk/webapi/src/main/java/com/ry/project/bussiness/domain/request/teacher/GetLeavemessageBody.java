package com.ry.project.bussiness.domain.request.teacher;

import com.ry.project.bussiness.domain.request.PageRequest;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author yjx
 */
@Data
@Accessors(chain = true)
public class GetLeavemessageBody extends PageRequest {
    private String boundaryId;
    private String subjectId;
    private String chapterlessonId;
}
