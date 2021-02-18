package com.ry.project.bussiness.domain.response.teacher;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Auther: 幸仁强
 * @Date: 2020/4/16 15:34
 * @Description: 返回实体对象基类
 */
@Data
@Accessors(chain = true)
public class ChapterLessonObjTeacher {
    private String id;

    private String name;

    private String teacherId;

    private String videoPath;

    private String teacherName;

    private int sort;
}
