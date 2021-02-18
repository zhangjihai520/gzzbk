package com.ry.project.bussiness.domain.cache;

import com.ry.framework.annotation.IRedisStoredObject;
import com.ry.project.bussiness.domain.bo.Subject;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class TeacherRankItem implements Serializable {

    private Long id;

    private String userTrueName;

    private String userFace;

    private String schoolName;

    private String comment;

    private int opusCount;

    private List<Subject> subjectList;

    public TeacherRankItem(){
        subjectList = new ArrayList<>();
    }

}
