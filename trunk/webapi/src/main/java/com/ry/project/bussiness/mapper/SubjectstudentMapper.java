package com.ry.project.bussiness.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ry.project.bussiness.domain.bo.StudentsubjectVO;
import com.ry.project.bussiness.domain.bo.Subjectstudent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ry.project.bussiness.domain.request.student.GetSubjectListBody;
import javafx.scene.control.Pagination;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

/**
 * <p>
 * 学生报名课程表 Mapper 接口
 * </p>
 *
 * @author xrq
 * @since 2020-12-01
 */
public interface SubjectstudentMapper extends BaseMapper<Subjectstudent> {
    /**
     * @param page 翻页对象，可以作为 xml 参数直接使用，传递参数 Page 即自动分页
     * @return
     */
    @Select("SELECT s.*,ss.chapterlesson_id as chapterlessonId,e.evaluate_level as evaluateLevel, e.content FROM subjectstudent ss left join subject s on ss.subject_id=s.id left join evaluaterecord e on e.subject_id=ss.subject_id and e.source_user_id = ss.user_id ${ew.customSqlSegment} order by ss.update_time desc")
    IPage<StudentsubjectVO> mySubjectPage(Page page, @Param(Constants.WRAPPER) QueryWrapper<StudentsubjectVO> queryWrapper);
}
