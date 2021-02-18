package com.ry.project.bussiness.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ry.common.constant.Constants;
import com.ry.framework.redis.RedisCache;
import com.ry.project.bussiness.domain.bo.Sensiword;
import com.ry.project.bussiness.mapper.SensiwordMapper;
import com.ry.project.bussiness.service.ISensiwordService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 敏感字枚举表 服务实现类
 * </p>
 *
 * @author xrq
 * @since 2020-12-01
 */
@Service
public class SensiwordService extends ServiceImpl<SensiwordMapper, Sensiword> implements ISensiwordService {

    @Autowired
    RedisCache redisCache;
    @Override
    public List<Sensiword> loadSensiWord() {
        List<Sensiword> list = redisCache.getCacheObject(Constants.SENSIWORDLIST_KEY);
        if (CollectionUtils.isNotEmpty(list))
        {
            return list;
        }
        list = super.list();
        if (CollectionUtils.isNotEmpty(list)) {
            redisCache.setCacheObject(Constants.SENSIWORDLIST_KEY, list, Constants.MINUTE_120, TimeUnit.MINUTES);
        }
        return list;
    }
}
