package com.ry.project.bussiness.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ry.common.constant.Constants;
import com.ry.common.enums.SourceTypeEnum;
import com.ry.framework.redis.RedisCache;
import com.ry.project.bussiness.domain.bo.User;
import com.ry.project.bussiness.mapper.UserMapper;
import com.ry.project.bussiness.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 学生、教师表。都是来源于云平台 服务实现类
 * </p>
 *
 * @author xrq
 * @since 2020-12-01
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    RedisCache redisCache;

    @Override
    public User getByKey(Long userId, Boolean useCache) {
        if (useCache == null) {
            useCache = true;
        }
        User cache = null;
        if (useCache) {
            cache = redisCache.getCacheObject(new User().setUserId(userId).key());
        }
        if (cache == null) {
            cache = super.getById(userId);
            if (cache != null) {
                redisCache.setCacheObject(cache.key(), cache, Constants.MINUTE_480, TimeUnit.MINUTES);
            }
        }
        return cache;
    }

    @Override
    public User readByStudentCode(String studentCode) {
        var userQuery = new QueryWrapper<User>();
        userQuery.lambda().eq(User::getStudentCode, studentCode);
        User user = super.getOne(userQuery);
        return user;
    }

    @Override
    public User readBySource(int sourceTypeId, String sourceId) {
        var userQuery = new QueryWrapper<User>();
        userQuery.lambda().eq(User::getSourceTypeId, SourceTypeEnum.JiaoFuPingTai.getCode()).eq(User::getSourceId, sourceId);
        User user = super.getOne(userQuery);
        return user;
    }
}
