package com.ry.common.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * @Auther: 幸仁强
 * @Date: 2019/7/16 15:34
 * @Description: 用户类型
 */
public enum UserType {

    /**
     * 后端用户
     */
    Manager(0),

    /**
     * 学生
     */
    Student(1),

    /**
     * 老师
     */
    Teacher(2);

    @Getter
    @Setter
    private int id;

    // 构造方法
    UserType(int id) {
        this.id = id;
    }

}
