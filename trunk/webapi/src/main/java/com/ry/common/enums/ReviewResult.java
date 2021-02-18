package com.ry.common.enums;

import lombok.var;

import java.util.Arrays;

/**
 * 用户状态
 *
 * @author
 */

public enum ReviewResult {
    SCHOOLPASS(3, "学校：审核通过"),
    SCHOOLNOTPASS(4, "学校：驳回"),
    MANAGERNOTPASS(5, "中心：驳回"),
    MANAGERPASS(6, "中心：审核通过");
    private final Integer id;
    private final String name;

    ReviewResult(Integer id, String name) {
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
        var reviewResult = Arrays.asList(ReviewResult.values()).stream()
                .filter(alarmGrade -> alarmGrade.getId().equals(id))
                .findFirst().orElse(null);
        if (reviewResult != null) {
            return reviewResult.getName();
        } else {
            return "——";
        }

    }
}
