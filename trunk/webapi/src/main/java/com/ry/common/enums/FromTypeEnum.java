package com.ry.common.enums;

import lombok.Getter;

public enum FromTypeEnum {
    PC(1),
    H5(2);

    @Getter
    private int index;

    private FromTypeEnum(int index) {
        this.index = index;
    }
}
