package com.ry.project.bussiness.service.impl;

import com.ry.project.bussiness.domain.bo.Banner;
import com.ry.project.bussiness.mapper.BannerMapper;
import com.ry.project.bussiness.service.IBannerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 首页banner表 服务实现类
 * </p>
 *
 * @author xrq
 * @since 2020-12-29
 */
@Service
public class BannerService extends ServiceImpl<BannerMapper, Banner> implements IBannerService {

}
