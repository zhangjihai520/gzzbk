package com.ry.project.bussiness.domain.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ReviewRecord {
    String timestamp;
    String content;
    String remark;
}
