package com.ry.common.enums;

import lombok.var;

import java.util.Arrays;

/**
 * 用户状态
 *
 * @author
 */

public enum ClassState {

    BEFORE(1, "待上课"), IN(2, "上课中"), END(3, "已结课");

    private final Integer id;
    private final String name;

    ClassState(Integer id, String name) {
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
        var recruitType = Arrays.asList(ClassState.values()).stream()
                .filter(alarmGrade -> alarmGrade.getId().equals(id))
                .findFirst().orElse(null);
        if (recruitType != null) {
            return recruitType.getName();
        } else {
            return "";
        }

    }
}
