package com.ry.project.bussiness.domain.response.manager;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ry.project.bussiness.domain.bo.Banner;
import com.ry.project.bussiness.domain.response.student.ChapterLessonObj;
import com.ry.project.bussiness.domain.response.student.SubjectObj;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: 幸仁强
 * @Date: 2020/4/16 15:34
 * @Description: 返回实体对象基类
 */
@Data
@Accessors(chain = true)
public class GetBannerDetailResponse {
    private String bannerId;

    private String name;

    private String picUrl;

    private String linkUrl;
}
