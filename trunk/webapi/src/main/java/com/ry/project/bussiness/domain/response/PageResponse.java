package com.ry.project.bussiness.domain.response;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther: 幸仁强
 * @Date: 2019/7/16 15:34
 * @Description: 返回实体分页对象基类
 */
@Data
@Accessors(chain = true)
public class PageResponse<T> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private List<T> dataList;//数据PageRequest

    private long pageIndex;//当前页

    private long pageSize;//每页条数

    private long total;//总条数

    private long pages;//总页面数目
}
