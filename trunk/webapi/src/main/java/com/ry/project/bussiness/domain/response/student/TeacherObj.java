package com.ry.project.bussiness.domain.response.student;

import com.ry.project.common.vo.IdName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author
 * @since 2020-06-01
 */
@Data
@Accessors(chain = true)
public class TeacherObj {

    private String id;

    private String userTrueName;

    private String userFace;

    private String schoolName;

    private int opusCount;

    private List<IdName> newestOpusList;

    public TeacherObj(){
        newestOpusList = new ArrayList<>();
    }
}
