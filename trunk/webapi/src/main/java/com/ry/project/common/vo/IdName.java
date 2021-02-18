package com.ry.project.common.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class IdName {
    private String id;
    private String name;
    public IdName(){}
    public IdName(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
