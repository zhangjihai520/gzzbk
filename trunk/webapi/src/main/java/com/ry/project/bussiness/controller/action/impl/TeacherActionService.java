package com.ry.project.bussiness.controller.action.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ry.common.constant.Constants;
import com.ry.common.enums.Category;
import com.ry.common.enums.CreaterFlag;
import com.ry.common.enums.SubjectStatus;
import com.ry.common.enums.UserType;
import com.ry.common.utils.CommonUtil;
import com.ry.common.utils.UrlUtil;
import com.ry.common.utils.sensi.SensitiveFilter;
import com.ry.framework.manager.AsyncManager;
import com.ry.framework.manager.factory.AsyncFactory;
import com.ry.framework.redis.RedisCache;
import com.ry.framework.security.LoginUser;
import com.ry.framework.security.service.TokenService;
import com.ry.framework.task.RyTask;
import com.ry.framework.web.domain.AjaxResult;
import com.ry.project.bussiness.controller.action.ITeacherActionService;
import com.ry.project.bussiness.domain.bo.*;
import com.ry.project.bussiness.domain.cache.AuthCodeCache;
import com.ry.project.bussiness.domain.request.ChangeBySubjectIdBody;
import com.ry.project.bussiness.domain.request.GetBySubjectIdBody;
import com.ry.project.bussiness.domain.request.teacher.*;
import com.ry.project.bussiness.domain.response.PageResponse;
import com.ry.project.bussiness.domain.response.teacher.*;
import com.ry.project.bussiness.service.*;
import com.ry.project.common.vo.IdName;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 教师
 * </p>
 */
@Slf4j
@Service
public class TeacherActionService implements ITeacherActionService {

    @Autowired
    ISubjectService subjectService;

    @Autowired
    IUserService userService;

    @Autowired
    IEvaluaterecordService evaluaterecordService;

    @Autowired
    TokenService tokenService;

    @Autowired
    RedisCache redisCache;

    @Autowired
    ILeavemessageService leavemessageService;

    @Autowired
    IChapterlessonService chapterlessonService;

    @Autowired
    ISysDictDataService sysDictDataService;

    @Autowired
    ISysDeptService sysDeptService;

    @Autowired
    RyTask ryTask;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult saveSubject(SaveSubjectBody body, HttpServletRequest request) {
        AjaxResult ajax = AjaxResult.success();
        Subject oldSubject = null;
        Long userId = tokenService.getUserId(request);
        User user = userService.getByKey(userId, true);
        Subject subject = new Subject().setName(body.getName())
                .setImagePath(body.getImagePath())
                .setGradeId(UrlUtil.decrypt(body.getGradeId(), Integer.class))
                .setCourseId(UrlUtil.decrypt(body.getCourseId(), Integer.class))
                .setAllowSignUpCount(body.getAllowSignUpCount())
                .setRemark(body.getRemark())
                .setPlan(body.getPlan())
                .setClassHour(1)
                .setSchoolId(user.getSchoolId())
                .setSchoolName(user.getSchoolName())
                .setSignUpBeginTime(body.getSignUpBeginTime())
                .setSignUpEndTime(body.getSignUpEndTime())
                .setBeginTime(body.getBeginTime())
                .setEndTime(body.getEndTime())
                .setTag(body.getTag())
                .setStatus(body.getStatus())
                .setCreaterFlag(CreaterFlag.TEACHER.getId());
        if (StringUtils.isNotBlank(body.getId())) {
            Long subjectId = UrlUtil.decrypt(body.getId(), Long.class);
            if (subjectId.intValue() > 0) {
                oldSubject = subjectService.getByKey(subjectId, true);
                subject.setId(subjectId);
            }
        }
        var result = subjectService.saveOrUpdate(subject);
        if (result) {
            Chapterlesson chapterlesson = new Chapterlesson()
                    .setName(body.getName())
                    .setSubjectId(subject.getId())
                    .setVideoPath(body.getVideoPath())
                    .setTeacherId(user.getUserId())
                    .setTeacherName(user.getUserTrueName());
            if (oldSubject != null && CollectionUtils.isNotEmpty(oldSubject.getChapterlessonList())) {
                chapterlesson.setId(oldSubject.getChapterlessonList().get(0).getId());
            }
            result = chapterlessonService.saveOrUpdate(chapterlesson);
            AsyncManager.me().execute(AsyncFactory.reflashSubjectCache(subject.getId()));
        }
        ajax.put("status", result ? 1 : 2);
        return ajax;
    }

    @Override
    public AjaxResult getSubjectList(GetSubjectListBody body, HttpServletRequest request) {
        var response = new PageResponse<SubjectObjTeacher>();
        Long userId = tokenService.getUserId(request);
        var chapterlessonQueryWrapper = new QueryWrapper<Chapterlesson>();
        chapterlessonQueryWrapper.lambda().eq(Chapterlesson::getStatus, 1).eq(Chapterlesson::getTeacherId, userId).orderByDesc(Chapterlesson::getId);
        var cpl = chapterlessonService.list(chapterlessonQueryWrapper);
        if (CollectionUtils.isEmpty(cpl) || cpl.size() == 0) {
            return AjaxResult.success(response);
        }
        var responseList = new ArrayList<SubjectObjTeacher>();
        var gradeList = sysDictDataService.selectDictDataByType("sys_grade");
        Map<Integer, String> gradeMap = gradeList.stream().collect(Collectors.toMap(x -> Integer.parseInt(x.getDictValue()), SysDictData::getDictLabel));
        var courseList = sysDictDataService.selectDictDataByType("sys_course");
        Map<Integer, String> courseMap = courseList.stream().collect(Collectors.toMap(x -> Integer.parseInt(x.getDictValue()), SysDictData::getDictLabel));
        var subjectWrapper = new QueryWrapper<Subject>();
        var subjectIds = cpl.stream().map(e -> e.getSubjectId()).distinct().collect(Collectors.toList());
        subjectWrapper.lambda().in(Subject::getId, subjectIds);
        if (body.getStatus() == SubjectStatus.DRAFT.getId().intValue() || body.getStatus() == SubjectStatus.MANAGERNOTPASS.getId().intValue()) {
            subjectWrapper.lambda().eq(Subject::getStatus, body.getStatus()).eq(Subject::getCreaterFlag, CreaterFlag.TEACHER.getId());
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
                    .or(wp -> wp.in(Subject::getStatus, statusList).eq(Subject::getCreaterFlag, CreaterFlag.TEACHER.getId())));
        }
        if (body.getCategory() > 0) {
            subjectWrapper.lambda().eq(Subject::getCategory, body.getCategory());
        }
        if (body.getStatus() > 0) {
            subjectWrapper.lambda().eq(Subject::getStatus, body.getStatus());
        }
        if (StringUtils.isNotBlank(body.getName())) {
            subjectWrapper.lambda().like(Subject::getName, body.getName());
        }
        subjectWrapper.lambda().apply(StringUtils.isNotBlank(body.getBeginTime()) && StringUtils.isNotBlank(body.getEndTime()),
                "date_format (begin_time,'%Y-%m-%d') >= date_format('" + body.getBeginTime() + "','%Y-%m-%d') and date_format (begin_time,'%Y-%m-%d') <= date_format('" + body.getEndTime() + "','%Y-%m-%d')")
                .apply(StringUtils.isNotBlank(body.getBeginTime()) && StringUtils.isBlank(body.getEndTime()),
                        "date_format (begin_time,'%Y-%m-%d') >= date_format('" + body.getBeginTime() + "','%Y-%m-%d')")
                .apply(StringUtils.isBlank(body.getBeginTime()) && StringUtils.isNotBlank(body.getEndTime()),
                        "date_format (begin_time,'%Y-%m-%d') <= date_format('" + body.getEndTime() + "','%Y-%m-%d')").orderByDesc(Subject::getId);
        IPage<Subject> page = new Page<>(body.getPageIndex(), body.getPageSize());
        IPage<Subject> pages = subjectService.page(page, subjectWrapper);
        List<Subject> subjects = pages.getRecords();
        for (var sub : subjects) {
            var item = new SubjectObjTeacher();
            item.setId(UrlUtil.encrypt(sub.getId())).setName(sub.getName())
                    .setImagePath(sub.getImagePath()).setCategory(sub.getCategory())
                    .setSignUpCounts(sub.getSignUpCount() + "/" + sub.getAllowSignUpCount())
                    .setClassTime(CommonUtil.convertClassTime(sub)).setClassState(sub.getClassState())
                    .setStatus(sub.getStatus()).setCommentScore(sub.getCommentScore())
                    .setCreaterFlag(sub.getCreaterFlag());
            ;
            if (gradeMap.containsKey(sub.getGradeId())) {
                item.setGradeName(gradeMap.get(sub.getGradeId()));
            } else {
                item.setGradeName("不限");
            }
            if (courseMap.containsKey(sub.getCourseId())) {
                item.setCourseName(courseMap.get(sub.getCourseId()));
            } else {
                item.setCourseName("不限");
            }
            responseList.add(item);
        }
        response.setTotal(pages.getTotal()).setDataList(responseList);
        return AjaxResult.success(response);
    }

    @Override
    public AjaxResult getSubjectDetail(GetBySubjectIdBody body) {
        var subjectId = UrlUtil.decrypt(body.getSubjectId(), Long.class);
        var subject = subjectService.getByKey(subjectId, true);
        var gradeList = sysDictDataService.selectDictDataByType("sys_grade");
        Map<Integer, String> gradeMap = gradeList.stream().collect(Collectors.toMap(x -> Integer.parseInt(x.getDictValue()), SysDictData::getDictLabel));
        var courseList = sysDictDataService.selectDictDataByType("sys_course");
        Map<Integer, String> courseMap = courseList.stream().collect(Collectors.toMap(x -> Integer.parseInt(x.getDictValue()), SysDictData::getDictLabel));
        GetSubjectDetailTeacher response = new GetSubjectDetailTeacher()
                .setId(UrlUtil.encrypt(subject.getId()))
                .setName(subject.getName())
                .setCategory(subject.getCategory())
                .setImagePath(subject.getImagePath())
                .setClassHour(subject.getClassHour())
                .setPlayCount(subject.getPlayCount())
                .setSignUpCount(subject.getSignUpCount())
                .setAllowSignUpCount(subject.getAllowSignUpCount())
                .setCommentScore(subject.getCommentScore())
                .setRemark(subject.getRemark())
                .setPlan(subject.getPlan())
                .setSignUpBeginTime(subject.getSignUpBeginTime())
                .setSignUpEndTime(subject.getSignUpEndTime())
                .setBeginTime(subject.getBeginTime())
                .setEndTime(subject.getEndTime())
                .setStatus(subject.getStatus())
                .setReviewRecords(CommonUtil.getReviewRecords(subject.getAuditOpinion()));
        if (gradeMap.containsKey(subject.getGradeId())) {
            response.setGradeId(UrlUtil.encrypt(subject.getGradeId())).setGradeName(gradeMap.get(subject.getGradeId()));
        }
        if (courseMap.containsKey(subject.getCourseId())) {
            response.setCourseId(UrlUtil.encrypt(subject.getCourseId())).setCourseName(courseMap.get(subject.getCourseId()));
        }
        if (CollectionUtils.isNotEmpty(subject.getChapterlessonList())) {
            var chapterlessons = subject.getChapterlessonList().stream().filter(x -> x.getStatus().intValue() > 0).sorted(Comparator.comparing(Chapterlesson::getSort)).collect(Collectors.toList());
            chapterlessons.forEach(x -> {
                var chapterLessonObjTeacher = new ChapterLessonObjTeacher()
                        .setId(UrlUtil.encrypt(x.getId()))
                        .setName(x.getName())
                        .setVideoPath(x.getVideoPath())
                        .setSort(x.getSort());
                if (x.getTeacherId() > 0) {
                    chapterLessonObjTeacher.setTeacherName(x.getTeacherName()).setTeacherId(UrlUtil.encrypt(x.getTeacherId()));
                }
                response.getChapterLessons().add(chapterLessonObjTeacher);
            });
        }
        return AjaxResult.success(response);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult changeStatus(ChangeBySubjectIdBody body) {
        AjaxResult ajax = AjaxResult.success();
        var subjectId = UrlUtil.decrypt(body.getSubjectId(), Long.class);
        Subject sub = new Subject().setStatus(body.getStatus()).setId(subjectId);
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

    @Override
    public AjaxResult getEvaluateDetail(EvaluateSubjectTeacherBody body) {
        Long subjectId = UrlUtil.decrypt(body.getSubjectId(), Long.class);
        SensitiveFilter filter = SensitiveFilter.DEFAULT;
        var evaluaterecordWrapper = new QueryWrapper<Evaluaterecord>();
        evaluaterecordWrapper.lambda().eq(Evaluaterecord::getSubjectId, subjectId).eq(Evaluaterecord::getStatus, 1).orderByDesc(Evaluaterecord::getId);
        IPage<Evaluaterecord> page = new Page<Evaluaterecord>(body.getPageIndex(), body.getPageSize());
        IPage<Evaluaterecord> pages = evaluaterecordService.page(page, evaluaterecordWrapper);
        var evaluaterecordList = new ArrayList<GetEvaluatecord>();
        for (var item : pages.getRecords()) {
            User user = userService.getByKey(item.getSourceUserId(), true);
            evaluaterecordList.add(new GetEvaluatecord().setEvaluateId(UrlUtil.encrypt(item.getId()))
                    .setUserTrueName(user.getUserTrueName())
                    .setContent(filter.filter(item.getContent(), '*'))
                    .setUserFace(user.getUserFace())
                    .setEvaluateLevel(item.getEvaluateLevel()));
        }
        var response = new PageResponse<GetEvaluatecord>().setTotal(pages.getTotal()).setDataList(evaluaterecordList);
        return AjaxResult.success(response);
    }

    @Override
    public AjaxResult getUserInfo(HttpServletRequest request) {
        LoginUser loginUser = tokenService.getLoginUser(request);
        User user = userService.getByKey(loginUser.getUserId(), true);
        var gradeList = sysDictDataService.selectDictDataByType("sys_grade");
        Map<Integer, String> gradeMap = gradeList.stream().collect(Collectors.toMap(x -> Integer.parseInt(x.getDictValue()), SysDictData::getDictLabel));
        var courseList = sysDictDataService.selectDictDataByType("sys_course");
        Map<Integer, String> courseMap = courseList.stream().collect(Collectors.toMap(x -> Integer.parseInt(x.getDictValue()), SysDictData::getDictLabel));
        GetUserInfo userInfo = new GetUserInfo();
        userInfo.setUserTrueName(user.getUserTrueName())
                .setUserFace(user.getUserFace())
                .setSchoolId(user.getSchoolId() != null ? UrlUtil.encrypt(user.getSchoolId()) : null)
                .setBindStatus(user.getBindStatus())
                .setSchoolName(user.getSchoolName())
                .setPhone(user.getPhone())
                .setComment(user.getComment());
        if (gradeMap.containsKey(user.getGradeId())) {
            userInfo.setGradeId(UrlUtil.encrypt(user.getGradeId())).setGradeName(gradeMap.get(user.getGradeId()));
        }
        if (courseMap.containsKey(user.getCourseId())) {
            userInfo.setCourseId(UrlUtil.encrypt(user.getCourseId())).setCourseName(courseMap.get(user.getCourseId()));
        }
        return AjaxResult.success(userInfo);
    }

    @Override
    public AjaxResult updateUserInfo(GetUserInfoBody body, HttpServletRequest request) {
        AjaxResult ajax = AjaxResult.success();
        Long userId = tokenService.getUserId(request);
        User user = new User();
        user.setUserId(userId);
        if (StringUtils.isNotBlank(body.getUserFace())) {
            user.setUserFace(body.getUserFace());
        }
        if (StringUtils.isNotBlank(body.getGradeId())) {
            user.setGradeId(UrlUtil.decrypt(body.getGradeId(), Integer.class));
        }
        if (StringUtils.isNotBlank(body.getCourseId())) {
            user.setCourseId(UrlUtil.decrypt(body.getCourseId(), Integer.class));
        }
        if (StringUtils.isNotBlank(body.getComment())) {
            user.setComment(body.getComment());
        }
        if (StringUtils.isNotBlank(body.getPhone())) {
            user.setPhone(body.getPhone());
        }
        if (StringUtils.isNotBlank(body.getSchoolId())) {
            var schoolId = UrlUtil.decrypt(body.getSchoolId(), Long.class);
            User ouInfo = userService.getByKey(userId, true);
            if (schoolId != ouInfo.getSchoolId()) {
                var schoolList = sysDeptService.selectSchoolList();
                var school = schoolList.stream().filter(x -> x.getDeptId().equals(schoolId)).findFirst().orElse(null);
                if (school != null) {
                    user.setSchoolId(schoolId)
                            .setSchoolCode(school.getCode())
                            .setSchoolName(school.getDeptName());
                }
            }
        }
        var result = user.updateById();
        userService.getByKey(userId, false);
        ajax.put("status", result ? 1 : 2);
        return ajax;
    }

    @Override
    public AjaxResult saveMessage(SaveMessageBody body, HttpServletRequest request) {
        AjaxResult ajax = AjaxResult.success();
        Long subjectId = UrlUtil.decrypt(body.getSubjectId(), Long.class);
        Long chapterlessonId = UrlUtil.decrypt(body.getChapterlessonId(), Long.class);
        Long replyId = UrlUtil.decrypt(body.getReplyId(), Long.class);
        Long userId = tokenService.getUserId(request);
        var leavemessage = new Leavemessage().setUserId(userId)
                .setContent(body.getContent())
                .setReplyId(replyId)
                .setChapterlessonId(chapterlessonId)
                .setSubjectId(subjectId);
        var result = leavemessageService.save(leavemessage);
        if (replyId.intValue() > 0 && result) {
            var replyMessage = new Leavemessage().setId(replyId).setReplayState(1);
            replyMessage.updateById();
        }
        ajax.put("status", result ? 1 : 2);
        return ajax;
    }

    @Override
    public AjaxResult getChaptersBySubjectId(GetBySubjectIdBody body) {
        Long subjectId = UrlUtil.decrypt(body.getSubjectId(), Long.class);
        var subject = subjectService.getByKey(subjectId, true);
        var chapterlessons = subject.getChapterlessonList().stream().filter(x -> x.getStatus().intValue() > 0).sorted(Comparator.comparing(Chapterlesson::getSort)).collect(Collectors.toList());
        var response = new ArrayList<IdName>();
        chapterlessons.forEach(x -> response.add(new IdName().setId(UrlUtil.encrypt(x.getId())).setName(x.getName())));
        return AjaxResult.success(response);
    }

    @Override
    public AjaxResult getLeavemessage(GetLeavemessageBody body) {
        Long subjectId = UrlUtil.decrypt(body.getSubjectId(), Long.class);
        Long chapterlessonId = UrlUtil.decrypt(body.getChapterlessonId(), Long.class);
        var messageWrapper = new QueryWrapper<Leavemessage>();
        messageWrapper.lambda().eq(Leavemessage::getSubjectId, subjectId).eq(Leavemessage::getChapterlessonId, chapterlessonId).eq(Leavemessage::getStatus, 1).eq(Leavemessage::getReplyId, 0L).orderByAsc(Leavemessage::getReplayState);
        IPage<Leavemessage> page = new Page<Leavemessage>(body.getPageIndex(), body.getPageSize());
        IPage<Leavemessage> pages = leavemessageService.page(page, messageWrapper);
        List<Long> rootids = pages.getRecords().stream().map(e -> e.getId()).collect(Collectors.toList());
        String rootidsStr = StringUtils.join(rootids.toArray(), ",");
        List<LeavemessageVO> messageList = leavemessageService.getChildrensByRootIds(rootidsStr, UserType.Teacher.getId());
        List<LeavemessageVO> selectTrees = leavemessageService.buildSelectTree(messageList);
        var leavemessageTrees = selectTrees.stream().map(LeavemessageTree::new).collect(Collectors.toList());
        var response = new PageResponse<LeavemessageTree>().setTotal(pages.getTotal()).setDataList(leavemessageTrees);
        return AjaxResult.success(response);
    }

    @Override
    public AjaxResult changeLeaveMessageStatus(GetByIdBody body) {
        AjaxResult ajax = AjaxResult.success();
        var messageId = UrlUtil.decrypt(body.getId(), Long.class);
        var message = new Leavemessage().setStatus(0).setId(messageId);
        var result = message.updateById();
        ajax.put("status", result ? 1 : 2);
        return ajax;
    }

    @Override
    public AjaxResult changeEvaluateStatus(GetByIdBody body) {
        AjaxResult ajax = AjaxResult.success();
        var evaluateId = UrlUtil.decrypt(body.getId(), Long.class);
        var evaluate = new Evaluaterecord().setStatus(0).setId(evaluateId);
        var result = evaluate.updateById();
        ajax.put("status", result ? 1 : 2);
        return ajax;
    }


    @Override
    public AjaxResult getAuthCode(GetBySubjectIdBody body, HttpServletRequest request) {
        var user = tokenService.getLoginUser(request);
        Long subjectId = UrlUtil.decrypt(body.getSubjectId(), Long.class);
        var subject = subjectService.getByKey(subjectId, true);
        if (!subject.getCategory().equals(Category.LIVE.getId()) || user.getUserType() != 2) {
            return AjaxResult.error("用户权限不足！");
        }
        var userId = user.getUserId();
        AuthCodeCache authCodeCache = new AuthCodeCache().setTeacherId(userId).setSubjectId(subjectId);
        AuthCodeCache cache = redisCache.getCacheObject(authCodeCache.key());
        if (cache == null) {
            cache = new AuthCodeCache();
            String code = CommonUtil.getRandomString(5);
            cache.setTeacherId(userId).setSubjectId(subjectId).setCode(code);
            redisCache.setCacheObject(cache.key(), cache, Constants.MINUTE_10, TimeUnit.SECONDS);
        }
        String authStr = String.format("%s/%s/%s", userId, subjectId, cache.getCode());
        return AjaxResult.success(String.format("R_%s%s?auth=%s", subject.getBeginTime().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")), userId, UrlUtil.encrypt(authStr)));
    }

}
