package com.ry.common.enums;

import java.util.Arrays;

/**
 * 用户状态
 *
 * @author
 */

public enum StudyRecordType {
    REGISTER(1, "报名"),
    PLAY(2, "观看");


    private final Integer id;
    private final String name;

    StudyRecordType(Integer id, String name) {
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
    public static String getNameById(int id) {
        StudyRecordType admissionStatus = Arrays.asList(StudyRecordType.values()).stream()
                .filter(alarmGrade -> alarmGrade.getId().equals(id))
                .findFirst().orElse(null);
        if (admissionStatus != null) {
            return admissionStatus.getName();
        } else {
            return "暂无";
        }
    }
}
