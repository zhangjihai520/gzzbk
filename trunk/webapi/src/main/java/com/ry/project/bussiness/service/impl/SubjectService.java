package com.ry.project.bussiness.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ry.common.constant.Constants;
import com.ry.framework.redis.RedisCache;
import com.ry.project.bussiness.domain.bo.Chapterlesson;
import com.ry.project.bussiness.domain.bo.Subject;
import com.ry.project.bussiness.domain.bo.User;
import com.ry.project.bussiness.mapper.SubjectMapper;
import com.ry.project.bussiness.service.ISubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 学科/专题表 服务实现类
 * </p>
 *
 * @author xrq
 * @since 2020-12-02
 */
@Service
public class SubjectService extends ServiceImpl<SubjectMapper, Subject> implements ISubjectService {
    @Autowired
    RedisCache redisCache;
    @Autowired
    ChapterlessonService chapterlessonService;

    @Override
    public Subject getByKey(Long subjectId, Boolean useCache) {
        if (useCache == null) {
            useCache = true;
        }
        Subject cache = null;
        if (useCache) {
            cache = redisCache.getCacheObject(new Subject().setId(subjectId).key());
        }
        if (cache == null) {
            cache = super.getById(subjectId);
            if (cache != null) {
                var queryWapper = new QueryWrapper<Chapterlesson>();
                queryWapper.lambda().eq(Chapterlesson::getSubjectId, subjectId).ne(Chapterlesson::getStatus, 0);
                var queryList = chapterlessonService.list(queryWapper);
                cache.setChapterlessonList(queryList);
                redisCache.setCacheObject(cache.key(), cache, Constants.MINUTE_480, TimeUnit.MINUTES);
            }
        }
        return cache;
    }
}
