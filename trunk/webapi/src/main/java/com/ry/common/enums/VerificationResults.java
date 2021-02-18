package com.ry.common.enums;

import lombok.var;

import java.util.Arrays;

/**
 * student表用
 *
 * @author
 */

public enum VerificationResults {
    NOVERIFIED(-1, "未核验"),
    WAITVERIFIED(0, "待核验"),
    PASS(1, "核验通过"),
    NOTPASS(2, "核验未通过");

    private final Integer id;
    private final String name;

    VerificationResults(Integer id, String name) {
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
        var verificationResults = Arrays.asList(VerificationResults.values()).stream()
                .filter(x -> x.getId().equals(id))
                .findFirst().orElse(null);
        if (verificationResults != null) {
            return verificationResults.getName();
        } else {
            return "填报中";
        }
    }
}
