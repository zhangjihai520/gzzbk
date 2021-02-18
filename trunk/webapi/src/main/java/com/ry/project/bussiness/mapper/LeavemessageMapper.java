package com.ry.project.bussiness.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ry.project.bussiness.domain.bo.Leavemessage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ry.project.bussiness.domain.bo.LeavemessageVO;
import com.ry.project.bussiness.domain.bo.StudentsubjectVO;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;

/**
 * <p>
 * 留言表 Mapper 接口
 * </p>
 *
 * @author xrq
 * @since 2020-12-01
 */
public interface LeavemessageMapper extends BaseMapper<Leavemessage> {

    @Select("Call Gzzbk_Leavemessage_GetChildrenMessage(#{rootIds},#{roleId})")
    @Options(statementType = StatementType.CALLABLE)
    List<LeavemessageVO> getChildrensByRootIds(@Param("rootIds") String rootIds, @Param("roleId") int roleId);
}
