package com.ry.common.enums;

/**
 * 用户状态
 *
 * @author
 */

public enum StudentStatus {

    NOTFILL(1, "未填报"),
    FILLING(2, "填报中"),
    VERIFICATION(3, "信息核验中"),
    NOT_PASS(4, "未通过"),
    PASS(5, "通过");

    private final Integer id;
    private final String name;

    StudentStatus(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
