package com.ry.project.bussiness.domain.response.teacher;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GetEvaluatecord {

    private String evaluateId;
    private String userTrueName;
    private String userFace;
    private int evaluateLevel;
    private String content;
}
