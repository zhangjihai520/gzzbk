package com.ry.project.bussiness.domain.cache;

import com.ry.framework.annotation.IRedisStoredObject;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class AuthCodeCache implements Serializable, IRedisStoredObject {

    private Long teacherId;

    private Long subjectId;

    private String code;

    private LocalDateTime pushTime;

    private static String key = "OnLineClassAuthCode:TeacherId_%s:SubjectId_%s";

    public String key() {
        return String.format(key, teacherId, subjectId);
    }
}
