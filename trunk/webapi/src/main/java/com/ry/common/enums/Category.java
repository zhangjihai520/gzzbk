package com.ry.common.enums;

import lombok.var;

import java.util.Arrays;

/**
 * 用户状态
 *
 * @author
 */

public enum Category {

    LIVE(1, "直播"), RECORD(2, "录播");

    private final Integer id;
    private final String name;

    Category(Integer id, String name) {
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
        var category = Arrays.asList(Category.values()).stream()
                .filter(alarmGrade -> alarmGrade.getId().equals(id))
                .findFirst().orElse(null);
        if (category != null) {
            return category.getName();
        } else {
            return "";
        }

    }
}
