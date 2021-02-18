package com.ry.common.enums;

import lombok.var;

import java.util.Arrays;

/**
 * 创建者标识
 *
 * @author
 */

public enum CreaterFlag {

    TEACHER(0, "教师"), SCHOOL(1, "学校"), MANAGER(2, "管理员");

    private final Integer id;
    private final String name;

    CreaterFlag(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    /**
     * @param id
     * @return
     */
    public static String getNameById(Integer id) {
        var recruitType = Arrays.asList(CreaterFlag.values()).stream()
                .filter(alarmGrade -> alarmGrade.getId().equals(id))
                .findFirst().orElse(null);
        if (recruitType != null) {
            return recruitType.getName();
        } else {
            return "";
        }

    }
}
