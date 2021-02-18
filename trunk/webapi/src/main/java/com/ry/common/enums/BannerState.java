package com.ry.common.enums;

import lombok.var;

import java.util.Arrays;

/**
 * Banner状态
 *
 * @author
 */

public enum BannerState {

    DELETED(0, "已删除"), PUBLISHED(1, "已上架"), TOBEPUBLISH(2, "待下架");

    private final Integer id;
    private final String name;

    BannerState(Integer id, String name) {
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
        var recruitType = Arrays.asList(BannerState.values()).stream()
                .filter(alarmGrade -> alarmGrade.getId().equals(id))
                .findFirst().orElse(null);
        if (recruitType != null) {
            return recruitType.getName();
        } else {
            return "";
        }

    }
}
