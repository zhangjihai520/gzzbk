package com.ry.project.bussiness.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ry.project.bussiness.domain.bo.Sensiword;

import java.util.List;

/**
 * <p>
 * 敏感字枚举表 服务类
 * </p>
 *
 * @author xrq
 * @since 2020-12-01
 */
public interface ISensiwordService extends IService<Sensiword> {
    List<Sensiword> loadSensiWord();
}
