package com.ry.common.enums;

import lombok.var;

import java.util.Arrays;

/**
 * 用户状态
 *
 * @author
 */

public enum Sex {

    MALE(0, "男"),
    FEMALE(1, "女");

    private final Integer id;
    private final String name;

    Sex(Integer id, String name) {
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
        var distinctAddress = Arrays.asList(Sex.values()).stream()
                .filter(x -> x.getId().equals(id))
                .findFirst().orElse(null);
        if (distinctAddress != null) {
            return distinctAddress.getName();
        } else {
            return Sex.MALE.getName();
        }
    }
}
