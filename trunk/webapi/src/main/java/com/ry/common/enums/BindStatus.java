package com.ry.common.enums;

import lombok.var;

import java.util.Arrays;

/**
 * 教师绑定状态
 *
 * @author
 */

public enum BindStatus {

    UNBINDING(0, "已解除绑定"), TOBEREVIEWED(1, "待审核"), BINDING(2, "已绑定");

    private final Integer id;
    private final String name;

    BindStatus(Integer id, String name) {
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
        var bindStatus = Arrays.asList(BindStatus.values()).stream()
                .filter(alarmGrade -> alarmGrade.getId().equals(id))
                .findFirst().orElse(null);
        if (bindStatus != null) {
            return bindStatus.getName();
        } else {
            return "";
        }

    }
}
