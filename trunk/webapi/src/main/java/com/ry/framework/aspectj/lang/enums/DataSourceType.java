package com.ry.framework.aspectj.lang.enums;

import lombok.Getter;

/**
 * 数据源
 *
 * @author
 */
public enum DataSourceType {
    /**
     * 主库
     */
    MASTER(("master"));

    /**
     * 从库
     */
    //SLAVE("slave");

    @Getter
    private String value;

    DataSourceType(String value) {
        this.value = value;
    }
}
