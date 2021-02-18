package com.ry.project.bussiness.controller.action.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ry.common.enums.*;
import com.ry.common.utils.CommonUtil;
import com.ry.common.utils.DateUtils;
import com.ry.common.utils.UrlUtil;
import com.ry.framework.manager.AsyncManager;
import com.ry.framework.manager.factory.AsyncFactory;
import com.ry.framework.redis.RedisCache;
import com.ry.framework.security.LoginUser;
import com.ry.framework.security.service.TokenService;
import com.ry.framework.web.domain.AjaxResult;
import com.ry.project.bussiness.controller.action.ISchoolActionService;
import com.ry.project.bussiness.domain.bo.Chapterlesson;
import com.ry.project.bussiness.domain.bo.Subject;
import com.ry.project.bussiness.domain.bo.User;
import com.ry.project.bussiness.domain.request.ChangeBySubjectIdBody;
import com.ry.project.bussiness.domain.request.GetBySubjectIdBody;
import com.ry.project.bussiness.domain.request.school.ChangeBindStatusBody;
import com.ry.project.bussiness.domain.request.school.GetSubjectListBody;
import com.ry.project.bussiness.domain.request.school.GetTeacherListBody;
import com.ry.project.bussiness.domain.request.school.SaveSubjectBody;
import com.ry.project.bussiness.domain.request.teacher.GetByIdBody;
import com.ry.project.bussiness.domain.response.PageResponse;
import com.ry.project.bussiness.domain.response.school.SubjectObj;
import com.ry.project.bussiness.domain.response.school.TeacherObj;
import com.ry.project.bussiness.domain.response.student.ChapterLessonObj;
import com.ry.project.bussiness.domain.response.student.GetSubjectDetailResponse;
import com.ry.project.bussiness.domain.response.teacher.GetUserInfo;
import com.ry.project.bussiness.service.IChapterlessonService;
import com.ry.project.bussiness.service.ISubjectService;
import com.ry.project.bussiness.service.IUserService;
import com.ry.project.common.vo.IdName;
import com.ry.project.system.domain.SysDept;
import com.ry.project.system.domain.SysDictData;
import com.ry.project.system.service.ISysDeptService;
import com.ry.project.system.service.ISysDictDataService;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户
 * </p>
 *
 * @author xrq
 * @since 2020-04-26
 */
@Slf4j
@Service
public class SchoolActionService implements ISchoolActionService {
    @Autowired
    ISubjectService subjectService;

    @Autowired
    TokenService tokenService;

    @Autowired
    RedisCache redisCache;

    @Autowired
    ISysDictDataService sysDictDataService;

    @Autowired
    IUserService userService;

    @Autowired
    ISysDeptService sysDeptService;

    @Autowired
    IChapterlessonService chapterlessonService;

    @Override
    public AjaxResult teacherOptions(HttpServletRequest request) {
        var response = new ArrayList<IdName>();
        var schoolId = tokenService.getLoginUser(request).getUser().getDeptId();
        if (schoolId != null && schoolId.intValue() > 0) {
            var userQueryWrapper = new QueryWrapper<User>();
            userQueryWrapper.lambda().eq(User::getStatus, 1).eq(User::getUserTypeId, UserType.Teacher.getId()).eq(User::getSchoolId, schoolId).eq(User::getBindStatus, BindStatus.BINDING.getId());
            var list = userService.list(userQueryWrapper);
            list.forEach(x -> response.add(new IdName().setId(UrlUtil.encrypt(x.getUserId())).setName(x.getUserTrueName() + " " + x.getPhone())));
        }
        return AjaxResult.success(response);
    }

    @Override
    public AjaxResult getTeacherList(GetTeacherListBody body, HttpServletRequest request) {
        var schoolId = tokenService.getLoginUser(request).getUser().getDeptId();
        var userQueryWrapper = new QueryWrapper<User>();
        userQueryWrapper.lambda().eq(User::getStatus, 1).eq(User::getUserTypeId, UserType.Teacher.getId()).eq(User::getSchoolId, schoolId);
        if (body.getBindStatus() > -1) {
            userQueryWrapper.lambda().eq(User::getBindStatus, body.getBindStatus());
        }
        if (StringUtils.isNotBlank(body.getTeacherName())) {
            userQueryWrapper.lambda().like(User::getUserTrueName, body.getTeacherName());
        }
        IPage<User> page = new Page<>(body.getPageIndex(), body.getPageSize());
        IPage<User> pages = userService.page(page, userQueryWrapper);

        var list = new ArrayList<TeacherObj>();
        for (var user : pages.getRecords()) {
            var item = new TeacherObj()
                    .setId(UrlUtil.encrypt(user.getUserId()))
                    .setUserName(user.getUserName())
                    .setUserTrueName(user.getUserTrueName())
                    .setBindStatus(user.getBindStatus())
                    .setFamousFlag(user.getFamousFlag())
                    .setPhone(user.getPhone())
                    .setUserSex(user.getUserSex());
            list.add(item);
        }
        var response = new PageResponse<TeacherObj>().setTotal(pages.getTotal()).setDataList(list);
        return AjaxResult.success(response);
    }

    @Override
    public AjaxResult getTeacherDetail(GetByIdBody body) {
        User user = userService.getByKey(UrlUtil.decrypt(body.getId(), Long.class), true);
        var gradeList = sysDictDataService.selectDictDataByType("sys_grade");
        Map<Integer, String> gradeMap = gradeList.stream().collect(Collectors.toMap(x -> Integer.parseInt(x.getDictValue()), SysDictData::getDictLabel));
        var courseList = sysDictDataService.selectDictDataByType("sys_course");
        Map<Integer, String> courseMap = courseList.stream().collect(Collectors.toMap(x -> Integer.parseInt(x.getDictValue()), SysDictData::getDictLabel));
        GetUserInfo userInfo = new GetUserInfo();
        userInfo.setUserTrueName(user.getUserTrueName())
                .setUserName(user.getUserName())
                .setUserFace(user.getUserFace())
                .setBindStatus(user.getBindStatus())
                .setFamousFlag(user.getFamousFlag())
                .setSchoolName(user.getSchoolName())
                .setPhone(user.getPhone())
                .setComment(user.getComment());
        if (gradeMap.containsKey(user.getGradeId())) {
            userInfo.setGradeName(gradeMap.get(user.getGradeId()));
        }
        if (courseMap.containsKey(user.getCourseId())) {
            userInfo.setCourseName(courseMap.get(user.getCourseId()));
        }
        return AjaxResult.success(userInfo);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult changeTeacherBindStatus(ChangeBindStatusBody body) {
        AjaxResult ajax = AjaxResult.success();
        var userId = UrlUtil.decrypt(body.getUserId(), Long.class);
        if (body.getBindStatus() == BindStatus.BINDING.getId() || body.getBindStatus() == BindStatus.UNBINDING.getId()) {
            User user = new User().setUserId(userId).setBindStatus(body.getBindStatus());
            var result = user.updateById();
            if (result) {
                ajax.put("status", 1);
                userService.getByKey(userId, false);
            } else {
                ajax.put("status", 2);
            }
        } else {
            ajax.put("status", 3);
        }
        return ajax;
    }

    @Override
    public AjaxResult getSubjectList(GetSubjectListBody body, HttpServletRequest request) {
        var schoolId = tokenService.getLoginUser(request).getUser().getDeptId();
        if (schoolId <= 100) {
            return AjaxResult.success(new PageResponse<SubjectObj>());
        }
        var subjectWrapper = new QueryWrapper<Subject>();
        subjectWrapper.lambda().eq(Subject::getSchoolId, schoolId);
        if (body.getCategory() > 0) {
            subjectWrapper.lambda().eq(Subject::getCategory, body.getCategory());
        }
        if (body.getStatus() == SubjectStatus.DRAFT.getId().intValue() || body.getStatus() == SubjectStatus.MANAGERNOTPASS.getId().intValue()) {
            subjectWrapper.lambda().eq(Subject::getStatus, body.getStatus()).eq(Subject::getCreaterFlag, CreaterFlag.SCHOOL.getId());
        } else if (body.getStatus() > SubjectStatus.DELETE.getId()) {
            subjectWrapper.lambda().eq(Subject::getStatus, body.getStatus());
        } else {
            var statusList = new ArrayList<Integer>();
            statusList.add(SubjectStatus.DRAFT.getId().intValue());
            statusList.add(SubjectStatus.MANAGERNOTPASS.getId().intValue());

            var statusList2 = new ArrayList<Integer>();
            statusList2.add(SubjectStatus.DELETE.getId().intValue());
            statusList2.add(SubjectStatus.DRAFT.getId().intValue());
            statusList2.add(SubjectStatus.MANAGERNOTPASS.getId().intValue());
            subjectWrapper.lambda().and(wrapper -> wrapper.notIn(Subject::getStatus, statusList2)
                    .or(wp -> wp.in(Subject::getStatus, statusList).eq(Subject::getCreaterFlag, CreaterFlag.SCHOOL.getId())));
        }
        if (StringUtils.isNotBlank(body.getName())) {
            subjectWrapper.lambda().like(Subject::getName, body.getName());
        }
        subjectWrapper.lambda().apply(StringUtils.isNotBlank(body.getBeginTime()) && StringUtils.isNotBlank(body.getEndTime()),
                "date_format (begin_time,'%Y-%m-%d') >= date_format('" + body.getBeginTime() + "','%Y-%m-%d') and date_format (begin_time,'%Y-%m-%d') <= date_format('" + body.getEndTime() + "','%Y-%m-%d')")
                .apply(StringUtils.isNotBlank(body.getBeginTime()) && StringUtils.isBlank(body.getEndTime()),
                        "date_format (begin_time,'%Y-%m-%d') >= date_format('" + body.getBeginTime() + "','%Y-%m-%d')")
                .apply(StringUtils.isBlank(body.getBeginTime()) && StringUtils.isNotBlank(body.getEndTime()),
                        "date_format (begin_time,'%Y-%m-%d') <= date_format('" + body.getEndTime() + "','%Y-%m-%d')")
                .orderByDesc(Subject::getId);
        IPage<Subject> page = new Page<>(body.getPageIndex(), body.getPageSize());
        IPage<Subject> pages = subjectService.page(page, subjectWrapper);
        var list = new ArrayList<SubjectObj>();
        var dataList = sysDictDataService.selectDictDataByType("sys_grade");
        Map<Integer, String> depMap = dataList.stream().collect(Collectors.toMap(x -> Integer.parseInt(x.getDictValue()), SysDictData::getDictLabel));
        for (var subject : pages.getRecords()) {
            var item = new SubjectObj()
                    .setId(UrlUtil.encrypt(subject.getId()))
                    .setName(subject.getName())
                    .setCategory(subject.getCategory())
                    .setStatus(subject.getStatus())
                    .setSignUpCounts(subject.getSignUpCount() + "/" + subject.getAllowSignUpCount())
                    .setCreaterFlag(subject.getCreaterFlag());
            var sub = subjectService.getByKey(subject.getId(), true);
            if (CollectionUtils.isNotEmpty(sub.getChapterlessonList())) {
                item.setTeacherName(sub.getChapterlessonList().get(0).getTeacherName());
            }
            if (depMap.containsKey(subject.getGradeId())) {
                item.setGradeName(depMap.get(subject.getGradeId()));
            } else {
                item.setGradeName("不限");
            }
            item.setClassTime(CommonUtil.convertClassTime(subject));
            list.add(item);
        }
        var response = new PageResponse<SubjectObj>().setTotal(pages.getTotal()).setDataList(list);
        return AjaxResult.success(response);
    }

    @Override
    public AjaxResult getToDoSubjectList(GetSubjectListBody body, HttpServletRequest request) {
        var schoolId = tokenService.getLoginUser(request).getUser().getDeptId();
        var subjectWrapper = new QueryWrapper<Subject>();
        subjectWrapper.lambda().eq(Subject::getSchoolId, schoolId);
        if (body.getCategory() > 0) {
            subjectWrapper.lambda().eq(Subject::getCategory, body.getCategory());
        }
        var statusList = new ArrayList<Integer>();
        statusList.add(SubjectStatus.TOBEPUBLISH.getId().intValue());
        statusList.add(SubjectStatus.MANAGERNOTPASS.getId().intValue());
        subjectWrapper.lambda().and(wrapper -> wrapper.eq(Subject::getStatus, SubjectStatus.TOBEREVIEWED1.getId())
                .or(wp -> wp.in(Subject::getStatus, statusList).eq(Subject::getCreaterFlag, CreaterFlag.SCHOOL.getId())));
        if (StringUtils.isNotBlank(body.getName())) {
            subjectWrapper.lambda().like(Subject::getName, body.getName());
        }
        subjectWrapper.lambda().apply(StringUtils.isNotBlank(body.getBeginTime()) && StringUtils.isNotBlank(body.getEndTime()),
                "date_format (begin_time,'%Y-%m-%d') >= date_format('" + body.getBeginTime() + "','%Y-%m-%d') and date_format (begin_time,'%Y-%m-%d') <= date_format('" + body.getEndTime() + "','%Y-%m-%d')")
                .apply(StringUtils.isNotBlank(body.getBeginTime()) && StringUtils.isBlank(body.getEndTime()),
                        "date_format (begin_time,'%Y-%m-%d') >= date_format('" + body.getBeginTime() + "','%Y-%m-%d')")
                .apply(StringUtils.isBlank(body.getBeginTime()) && StringUtils.isNotBlank(body.getEndTime()),
                        "date_format (begin_time,'%Y-%m-%d') <= date_format('" + body.getEndTime() + "','%Y-%m-%d')")
                .orderByDesc(Subject::getId);
        IPage<Subject> page = new Page<>(body.getPageIndex(), body.getPageSize());
        IPage<Subject> pages = subjectService.page(page, subjectWrapper);
        var list = new ArrayList<SubjectObj>();
        var dataList = sysDictDataService.selectDictDataByType("sys_grade");
        Map<Integer, String> depMap = dataList.stream().collect(Collectors.toMap(x -> Integer.parseInt(x.getDictValue()), SysDictData::getDictLabel));
        for (var subject : pages.getRecords()) {
            var item = new SubjectObj()
                    .setId(UrlUtil.encrypt(subject.getId()))
                    .setName(subject.getName())
                    .setCategory(subject.getCategory())
                    .setStatus(subject.getStatus())
                    .setCreaterFlag(subject.getCreaterFlag());
            var sub = subjectService.getByKey(subject.getId(), true);
            if (CollectionUtils.isNotEmpty(sub.getChapterlessonList())) {
                item.setTeacherName(sub.getChapterlessonList().get(0).getTeacherName());
            }
            if (depMap.containsKey(subject.getGradeId())) {
                item.setGradeName(depMap.get(subject.getGradeId()));
            } else {
                item.setGradeName("不限");
            }
            item.setClassTime(CommonUtil.convertClassTime(subject));
            list.add(item);
        }
        var response = new PageResponse<SubjectObj>().setTotal(pages.getTotal()).setDataList(list);
        return AjaxResult.success(response);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult saveSubject(SaveSubjectBody body, HttpServletRequest request) {
        var schoolId = tokenService.getLoginUser(request).getUser().getDeptId();
        List<SysDept> schools = sysDeptService.selectSchoolList();
        var school = schools.stream().filter(x -> x.getDeptId().equals(schoolId)).findFirst().orElse(null);
        AjaxResult ajax = AjaxResult.success();
        Subject subject = new Subject();
        subject.setName(body.getName())
                .setCategory(Category.RECORD.getId())
                .setClassState(ClassState.END.getId())
                .setClassHour(body.getChapterLessons().size())
                .setSchoolId(schoolId)
                .setImagePath(body.getImagePath())
                .setGradeId(UrlUtil.decrypt(body.getGradeId(), Integer.class))
                .setCourseId(UrlUtil.decrypt(body.getCourseId(), Integer.class))
                .setAllowSignUpCount(0)
                .setRemark(body.getRemark())
                .setPlan(body.getPlan())
                .setBeginTime(body.getBeginTime())
                .setEndTime(body.getEndTime())
                .setTag(body.getTag())
                .setStatus(body.getStatus())
                .setCreaterFlag(CreaterFlag.SCHOOL.getId());
        if (StringUtils.isNotBlank(body.getId())) {
            Long subjectId = UrlUtil.decrypt(body.getId(), Long.class);
            if (subjectId.intValue() > 0) {
                subject.setId(subjectId);
            }
        }
        if (school != null) {
            subject.setSchoolName(school.getDeptName());
        }
        var result = subjectService.saveOrUpdate(subject);
        if (result) {
            var chapterlessonWapper = new QueryWrapper<Chapterlesson>();
            chapterlessonWapper.lambda().eq(Chapterlesson::getSubjectId, subject.getId()).ne(Chapterlesson::getStatus, 0);
            var oldChapterlessons = chapterlessonService.list(chapterlessonWapper);
            var chapterlessonIds = body.getChapterLessons().stream().filter(x -> x.getId() != null).map(e -> UrlUtil.decrypt(e.getId(), Long.class)).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(oldChapterlessons) && CollectionUtils.isNotEmpty(chapterlessonIds)) {
                oldChapterlessons.forEach(x -> {
                    if (!chapterlessonIds.contains(x.getId())) {
                        x.setStatus(0);
                        x.updateById();
                    }
                });
            }
            var chapterLessonObjs = body.getChapterLessons();
            for (var o : chapterLessonObjs) {
                Chapterlesson chapterlesson = new Chapterlesson()
                        .setName(o.getName())
                        .setSubjectId(subject.getId())
                        .setVideoPath(o.getVideoPath())
                        .setSort(o.getSort());
                if (StringUtils.isNotBlank(o.getTeacherId())) {
                    var teacherId = UrlUtil.decrypt(o.getTeacherId(), Long.class);
                    User teacher = userService.getByKey(teacherId, true);
                    chapterlesson.setTeacherName(teacher.getUserTrueName()).setTeacherId(teacherId);
                }
                if (StringUtils.isNotBlank(o.getId())) {
                    Long chapterlessonId = UrlUtil.decrypt(o.getId(), Long.class);
                    if (chapterlessonId.intValue() > 0) {
                        chapterlesson.setId(chapterlessonId);
                    }
                }
                result = chapterlessonService.saveOrUpdate(chapterlesson);
                AsyncManager.me().execute(AsyncFactory.reflashSubjectCache(subject.getId()));
            }
        }
        ajax.put("status", result ? 1 : 2);
        return ajax;
    }

    @Override
    public AjaxResult getSubjectDetail(GetBySubjectIdBody body) {
        var subject = subjectService.getByKey(UrlUtil.decrypt(body.getSubjectId(), Long.class), true);
        var gradeList = sysDictDataService.selectDictDataByType("sys_grade");
        Map<Integer, String> gradeMap = gradeList.stream().collect(Collectors.toMap(x -> Integer.parseInt(x.getDictValue()), SysDictData::getDictLabel));
        var courseList = sysDictDataService.selectDictDataByType("sys_course");
        Map<Integer, String> courseMap = courseList.stream().collect(Collectors.toMap(x -> Integer.parseInt(x.getDictValue()), SysDictData::getDictLabel));
        GetSubjectDetailResponse response = new GetSubjectDetailResponse();
        response.setRemark(subject.getRemark()).setPlan(subject.getPlan())
                .setSignUpBeginTime(subject.getSignUpBeginTime()).setSignUpEndTime(subject.getSignUpEndTime())
                .setAllowSignUpCount(subject.getAllowSignUpCount()).setSignUpCount(subject.getSignUpCount())
                .setBeginTime(subject.getBeginTime()).setEndTime(subject.getEndTime())
                .setClassState(CommonUtil.getClassStatus(subject)).setClassHour(subject.getClassHour())
                .setCommentScore(subject.getCommentScore()).setPlayCount(subject.getPlayCount())
                .setId(body.getSubjectId()).setName(subject.getName()).setStatus(subject.getStatus())
                .setCategory(subject.getCategory()).setImagePath(subject.getImagePath())
                .setReviewRecords(CommonUtil.getReviewRecords(subject.getAuditOpinion()));
        if (gradeMap.containsKey(subject.getGradeId())) {
            response.setGradeId(UrlUtil.encrypt(subject.getGradeId())).setGradeName(gradeMap.get(subject.getGradeId()));
        }
        if (courseMap.containsKey(subject.getCourseId())) {
            response.setCourseId(UrlUtil.encrypt(subject.getCourseId())).setCourseName(courseMap.get(subject.getCourseId()));
        }
        var chapterlessons = subject.getChapterlessonList().stream().filter(x -> x.getStatus().intValue() > 0).sorted(Comparator.comparing(Chapterlesson::getSort)).collect(Collectors.toList());
        for (var chapterlesson : chapterlessons) {
            var chapterLessonObj = new ChapterLessonObj().setId(UrlUtil.encrypt(chapterlesson.getId())).setName(chapterlesson.getName()).setVideoPath(chapterlesson.getVideoPath()).setSort(chapterlesson.getSort());
            if (chapterlesson.getTeacherId() > 0) {
                chapterLessonObj.setTeacherId(UrlUtil.encrypt(chapterlesson.getTeacherId())).setTeacherName(chapterlesson.getTeacherName());
            }
            response.getChapterLessons().add(chapterLessonObj);
        }
        return AjaxResult.success(response);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult changeStatus(ChangeBySubjectIdBody body) {
        AjaxResult ajax = AjaxResult.success();
        var subjectId = UrlUtil.decrypt(body.getSubjectId(), Long.class);
        var subject = subjectService.getByKey(subjectId, true);
        Subject sub = new Subject().setId(subjectId).setStatus(body.getStatus());
        if (body.getStatus() == ReviewResult.SCHOOLPASS.getId().intValue() || body.getStatus() == ReviewResult.SCHOOLNOTPASS.getId().intValue()) {
            var str = DateUtils.getCurrentDateTime() + "@@@" + ReviewResult.getNameById(body.getStatus()) + "@@@" + (StringUtils.isNotBlank(body.getAuditOpinion()) ? body.getAuditOpinion() : "");
            if (StringUtils.isNotBlank(subject.getAuditOpinion())) {
                sub.setAuditOpinion(subject.getAuditOpinion() + "&&&" + str);
            } else {
                sub.setAuditOpinion(str);
            }
        }
        var result = sub.updateById();
        if (result && body.getStatus().equals(SubjectStatus.DELETE.getId())) {
            var updateWrapper = new UpdateWrapper<Chapterlesson>();
            updateWrapper.lambda().set(Chapterlesson::getStatus, 0).eq(Chapterlesson::getSubjectId, subjectId);
            chapterlessonService.update(updateWrapper);
        }
        AsyncManager.me().execute(AsyncFactory.reflashSubjectCache(subjectId));
        ajax.put("status", result ? 1 : 2);
        return ajax;
    }
}
