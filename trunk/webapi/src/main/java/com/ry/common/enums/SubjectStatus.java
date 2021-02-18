package com.ry.common.enums;

import lombok.var;

import java.util.Arrays;

/**
 * 用户状态
 *
 * @author
 */

public enum SubjectStatus {

    DELETE(0, "删除"),
    DRAFT(1, "草稿"),
    TOBEREVIEWED1(2, "待学校审核"),
    TOBEREVIEWED2(3, "待中心审核"),
    SCHOOLNOTPASS(4, "学校驳回"),
    MANAGERNOTPASS(5, "中心驳回"),
    TOBEPUBLISH(6, "待上架"),
    PUBLISHED(7, "已上架"),
    UNPUBLISH(8, "已下架");
    private final Integer id;
    private final String name;

    SubjectStatus(Integer id, String name) {
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
        var subjectStatus = Arrays.asList(SubjectStatus.values()).stream()
                .filter(alarmGrade -> alarmGrade.getId().equals(id))
                .findFirst().orElse(null);
        if (subjectStatus != null) {
            return subjectStatus.getName();
        } else {
            return "暂无";
        }

    }
}
