package com.ry.project.bussiness.controller.action.impl;

import com.alibaba.fastjson.JSON;
import com.ry.common.constant.Constants;
import com.ry.common.utils.UrlUtil;
import com.ry.framework.config.CommonConfig;
import com.ry.framework.manager.AsyncManager;
import com.ry.framework.manager.factory.AsyncFactory;
import com.ry.framework.redis.RedisCache;
import com.ry.project.bussiness.controller.action.IRtmpNotifyActionService;
import com.ry.project.bussiness.domain.cache.AuthCodeCache;
import com.ry.project.bussiness.domain.bo.Chapterlesson;
import com.ry.project.bussiness.service.IChapterlessonService;
import com.ry.project.bussiness.service.ISubjectService;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * <p>
 * rtmp 服务实现类
 * </p>
 *
 * @author xrq
 * @since 2020-04-26
 */
@Slf4j
@Service
public class RtmpNotifyActionService implements IRtmpNotifyActionService {
    @Autowired
    ISubjectService subjectService;
    @Autowired
    IChapterlessonService chapterlessonService;
    @Autowired
    RedisCache redisCache;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean pushAuth(Map<String, String> params) throws Exception {
        var act = params.get("act");
        String auth = params.get("auth");
        if (StringUtils.isNotBlank(auth)) {
            String authStr = UrlUtil.decrypt(auth, String.class);
            String[] auths = authStr.split("/");
            var teacherId = Long.parseLong(auths[0]);
            var subjectId = Long.parseLong(auths[1]);
            var authCode = auths[2];
            var domain = params.get("domain");
            var name = params.get("name");
            var app = params.get("app");
            if ("done".equals(act)) {
                log.info(String.format("结束推流，参数%s", JSON.toJSONString(params)));
                var subject = subjectService.getByKey(subjectId, true);
                subject.setClassState(3);
                for (Chapterlesson chapterlesson : subject.getChapterlessonList()) {
                    chapterlesson.setVideoPath(String.format("%s/%s.mp4", CommonConfig.getRecordPath(), name));
                    boolean result = chapterlessonService.updateById(chapterlesson);
                    if (!result) {
                        throw new Exception();
                    }
                }
                subjectService.updateById(subject);
                //更新缓存
                AsyncManager.me().execute(AsyncFactory.reflashSubjectCache(subjectId));
                return true;
            } else {
                log.info(String.format("开始推流，参数%s", JSON.toJSONString(params)));
                if (null != auths && auths.length == 3) {
                    AuthCodeCache authCodeCache = new AuthCodeCache().setTeacherId(teacherId).setSubjectId(subjectId);
                    AuthCodeCache cache = redisCache.getCacheObject(authCodeCache.key());
                    if (cache != null && cache.getCode().equals(authCode)) {
                        var subject = subjectService.getByKey(subjectId, true);
                        subject.setClassState(2);
                        for (Chapterlesson chapterlesson : subject.getChapterlessonList()) {
                            chapterlesson.setVideoPath(String.format("%s/%s.m3u8", CommonConfig.getLivePath(), name));
                            boolean result = chapterlessonService.updateById(chapterlesson);
                            if (!result) {
                                throw new Exception();
                            }
                        }
                        subjectService.updateById(subject);
                        //更新缓存
                        AsyncManager.me().execute(AsyncFactory.reflashSubjectCache(subjectId));
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
