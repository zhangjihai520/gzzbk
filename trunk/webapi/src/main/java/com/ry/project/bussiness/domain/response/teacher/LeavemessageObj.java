package com.ry.project.bussiness.domain.response.teacher;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

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
public class LeavemessageObj {
    private String id;

    private String userName;

    private String userFace;

    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    private List<LeavemessageChild> leavemessageChild;

    public LeavemessageObj() {
        leavemessageChild = new ArrayList<LeavemessageChild>();
    }

}
