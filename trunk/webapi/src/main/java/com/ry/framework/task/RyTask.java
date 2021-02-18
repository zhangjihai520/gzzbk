package com.ry.framework.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ry.common.constant.Constants;
import com.ry.common.core.dledc.response.SchoolInfo;
import com.ry.common.enums.*;
import com.ry.common.utils.CommonUtil;
import com.ry.common.utils.DateUtils;
import com.ry.framework.manager.AsyncManager;
import com.ry.framework.manager.factory.AsyncFactory;
import com.ry.framework.redis.RedisCache;
import com.ry.project.bussiness.domain.bo.*;
import com.ry.project.bussiness.domain.cache.TeacherRankItem;
import com.ry.project.bussiness.service.*;
import com.ry.project.bussiness.service.impl.SubjectService;
import com.ry.project.system.domain.SysDept;
import com.ry.project.system.service.ISysDeptService;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 定时任务调度测试
 *
 * @author
 */
@Component("ryTask")
@Slf4j
public class RyTask {
    @Autowired
    IDledcService dledcService;
    @Autowired
    ISubjectService subjectService;
    @Autowired
    ISysDeptService sysDeptService;
    @Autowired
    IUserService userService;
    @Autowired
    RedisCache redisCache;
    @Autowired
    IChapterlessonService chapterlessonService;
    @Autowired
    ISubjectstudentService subjectstudentService;
    @Autowired
    IStudentstudyrecordService studentstudyrecordService;
    @Autowired
    IEvaluaterecordService evaluaterecordService;

    public void calPlayCount(Integer params) {
        var studentstudyrecordQueryWrapper = new QueryWrapper<Studentstudyrecord>();
        String start_date = DateUtils.getBeforeDaysDate(params);
        studentstudyrecordQueryWrapper.lambda().eq(Studentstudyrecord::getStudyRecordType, StudyRecordType.PLAY.getId()).apply("date_format (create_time,'%Y-%m-%d') >= date_format('" + start_date + "','%Y-%m-%d')");
        List<Studentstudyrecord> list = studentstudyrecordService.list(studentstudyrecordQueryWrapper);
        if (CollectionUtils.isEmpty(list) || list.size() == 0) {
            return;
        }
        var subjectIds = list.stream().map(e -> e.getSubjectId()).distinct().collect(Collectors.toList());
        var ssrWrapper = new QueryWrapper<Studentstudyrecord>();
        ssrWrapper.lambda().eq(Studentstudyrecord::getStudyRecordType, StudyRecordType.PLAY.getId()).in(Studentstudyrecord::getSubjectId, subjectIds);
        list = studentstudyrecordService.list(ssrWrapper);
        var sMap = list.stream().collect(Collectors.groupingBy(Studentstudyrecord::getSubjectId));
        for (Map.Entry<Long, List<Studentstudyrecord>> entry : sMap.entrySet()) {
            var sid = entry.getKey();
            var eList = entry.getValue();
            var count = eList.size();
            var subject = new Subject().setId(sid);
            subject.setPlayCount(count);
            subject.updateById();
            AsyncManager.me().execute(AsyncFactory.reflashSubjectCache(sid));
        }
    }

    public void calEvalAvgScore(Integer params) {
        var evaluaterecordQueryWrapper = new QueryWrapper<Evaluaterecord>();
        String start_date = DateUtils.getBeforeDaysDate(params);
        evaluaterecordQueryWrapper.lambda().eq(Evaluaterecord::getStatus, 1)
                .apply("date_format (create_time,'%Y-%m-%d') >= date_format('" + start_date + "','%Y-%m-%d')");
        List<Evaluaterecord> list = evaluaterecordService.list(evaluaterecordQueryWrapper);
        if (CollectionUtils.isEmpty(list) || list.size() == 0) {
            return;
        }
        var subjectIds = list.stream().map(e -> e.getSubjectId()).distinct().collect(Collectors.toList());
        var erqWapper = new QueryWrapper<Evaluaterecord>();
        erqWapper.lambda().in(Evaluaterecord::getSubjectId, subjectIds).eq(Evaluaterecord::getStatus, 1);
        list = evaluaterecordService.list(erqWapper);
        var sMap = list.stream().collect(Collectors.groupingBy(Evaluaterecord::getSubjectId));
        for (Map.Entry<Long, List<Evaluaterecord>> entry : sMap.entrySet()) {
            var sid = entry.getKey();
            var eList = entry.getValue();
            var count = eList.size();
            var totalScore = eList.stream().mapToInt(p -> p.getEvaluateLevel()).sum();
            var avgScore = CommonUtil.division(totalScore, count);
            var subject = new Subject().setId(sid).setCommentScore(avgScore).setCommentCount(count);
            subject.updateById();
            AsyncManager.me().execute(AsyncFactory.reflashSubjectCache(sid));
        }
    }


    public void calRegCount(Integer params) {
        var studentstudyrecordQueryWrapper = new QueryWrapper<Studentstudyrecord>();
        String start_date = DateUtils.getBeforeDaysDate(params);
        studentstudyrecordQueryWrapper.lambda().eq(Studentstudyrecord::getStudyRecordType, StudyRecordType.REGISTER.getId()).apply("date_format (create_time,'%Y-%m-%d') >= date_format('" + start_date + "','%Y-%m-%d')");
        List<Studentstudyrecord> list = studentstudyrecordService.list(studentstudyrecordQueryWrapper);
        if (CollectionUtils.isEmpty(list) || list.size() == 0) {
            return;
        }
        var subjectIds = list.stream().map(e -> e.getSubjectId()).distinct().collect(Collectors.toList());
        var ssrWrapper = new QueryWrapper<Studentstudyrecord>();
        ssrWrapper.lambda().eq(Studentstudyrecord::getStudyRecordType, StudyRecordType.REGISTER.getId()).in(Studentstudyrecord::getSubjectId, subjectIds);
        list = studentstudyrecordService.list(ssrWrapper);
        var sMap = list.stream().collect(Collectors.groupingBy(Studentstudyrecord::getSubjectId));
        for (Map.Entry<Long, List<Studentstudyrecord>> entry : sMap.entrySet()) {
            var sid = entry.getKey();
            var eList = entry.getValue();
            var count = eList.size();
            var subject = new Subject().setId(sid);
            subject.setSignUpCount(count);
            subject.updateById();
            AsyncManager.me().execute(AsyncFactory.reflashSubjectCache(sid));
        }
    }

    public void syncSchool() {
        List<SchoolInfo> schoolInfos = dledcService.getSchoolList();
        List<SchoolInfo> hschoolInfos = schoolInfos.stream().filter(x -> StringUtils.isNotBlank(x.getCategoryId()) && x.getCategoryId().equals("School") && StringUtils.isNotBlank(x.getPhase()) && x.getPhase().substring(0, 2).equals("34") && x.getDeleteMark() != null && !x.getDeleteMark() && x.getEnabledMark() != null && x.getEnabledMark()).collect(Collectors.toList());
        List<SysDept> schools = sysDeptService.selectSchoolList();
        for (SchoolInfo x : hschoolInfos) {
            if (schools.stream().filter(y -> y.getCode().equals(x.getOrganizeId())).count() > 0) {
                continue;
            }
            SysDept dept = new SysDept();
            dept.setAncestors("100");
            dept.setParentId(100L);
            dept.setCode(x.getOrganizeId());
            dept.setDelFlag("0");
            dept.setStatus("0");
            dept.setDeptName(x.getSchoolName());
            dept.setLeader("admin");
            dept.setManageFlag(0);
            dept.setOrderNum("1");
            dept.setCreateBy("admin");
            dept.setPhone("18888888888");
            sysDeptService.insertDept(dept);
        }
    }

    public void sychroUser() {
        var userWrapper = new QueryWrapper<User>();
        userWrapper.lambda().eq(User::getStudentCode, "等待同步");
        var userList = userService.list(userWrapper);
        if (CollectionUtils.isEmpty(userList) || userList.size() == 0) {
            return;
        }
        log.info(String.format("用户同步开始，共%s条用户数据待同步", userList.size()));
        int successCount = 0;
        int failCount = 0;
        for (var user : userList) {
            try {
                dledcService.sychroUser(user.getSourceId());
                successCount++;
            } catch (Exception e) {
                failCount++;
                log.error(String.format("%s更新失败", user.getSourceId()), e);
            }
        }
        log.info(String.format("本次用户同步完成，成功更新%s条用户数据,%s条数据更新失败", successCount, failCount));
    }

    public void genTeacherRankingList() {
        var cacheObj = new ArrayList<TeacherRankItem>();
        var userWrapper = new QueryWrapper<User>();
        userWrapper.lambda().eq(User::getStatus, 1).eq(User::getUserTypeId, UserType.Teacher.getId()).isNotNull(User::getSchoolId).eq(User::getFamousFlag, 1);
        List<User> teachers = userService.list(userWrapper);
        for (var t : teachers) {
            var item = new TeacherRankItem().setId(t.getUserId()).setSchoolName(t.getSchoolName()).setUserTrueName(t.getUserTrueName()).setUserFace(t.getUserFace()).setComment(t.getComment());
            var chapterlessonQueryWrapper = new QueryWrapper<Chapterlesson>();
            chapterlessonQueryWrapper.lambda().eq(Chapterlesson::getStatus, 1).eq(Chapterlesson::getTeacherId, t.getUserId());
            var cps = chapterlessonService.list(chapterlessonQueryWrapper);
            var cpsMap = cps.stream().collect(Collectors.groupingBy(Chapterlesson::getSubjectId));
            for (Long key : cpsMap.keySet()) {
                var subject = subjectService.getByKey(key, true);
                if (subject.getStatus().equals(SubjectStatus.PUBLISHED.getId())) {
                    item.getSubjectList().add(subject);
                }
            }
            item.setOpusCount(item.getSubjectList().size());
            cacheObj.add(item);
        }
        if (CollectionUtils.isNotEmpty(cacheObj)) {
            redisCache.setCacheObject(Constants.TEACHERRANKLIST_KEY, cacheObj, Constants.MINUTE_120, TimeUnit.MINUTES);
        }
    }
}
