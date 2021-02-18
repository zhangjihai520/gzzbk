package com.ry.project.bussiness.domain.response.student;

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
public class GetTeacherDetailResponse{

    private String id;

    private String userTrueName;

    private String userFace;

    private String schoolName;

    private String comment;

    private List<SubjectObj> opusList;

    public GetTeacherDetailResponse() {
        opusList = new ArrayList<SubjectObj>();
    }
}
