package com.ry.project.bussiness.controller.action.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ry.common.constant.Constants;
import com.ry.common.enums.*;
import com.ry.common.utils.CommonUtil;
import com.ry.common.utils.DateUtils;
import com.ry.common.utils.UrlUtil;
import com.ry.framework.manager.AsyncManager;
import com.ry.framework.manager.factory.AsyncFactory;
import com.ry.framework.redis.RedisCache;
import com.ry.framework.security.service.TokenService;
import com.ry.framework.web.domain.AjaxResult;
import com.ry.project.bussiness.controller.action.IStudentActionService;
import com.ry.project.bussiness.domain.bo.*;
import com.ry.project.bussiness.domain.cache.TeacherRankItem;
import com.ry.project.bussiness.domain.request.GetBySubjectIdBody;
import com.ry.project.bussiness.domain.request.student.*;
import com.ry.project.bussiness.domain.request.teacher.GetLeavemessageBody;
import com.ry.project.bussiness.domain.response.PageResponse;
import com.ry.project.bussiness.domain.response.student.*;
import com.ry.project.bussiness.service.*;
import com.ry.project.common.vo.IdName;
import com.ry.project.system.domain.SysDictData;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 学生
 * </p>
 *
 * @author xrq
 * @since 2020-04-26
 */
@Slf4j
@Service
public class StudentActionService implements IStudentActionService {

    @Autowired
    ISubjectService subjectService;

    @Autowired
    IUserService userService;

    @Autowired
    RedisCache redisCache;

    @Autowired
    TokenService tokenService;

    @Autowired
    IChapterlessonService chapterlessonService;

    @Autowired
    ISubjectstudentService subjectstudentService;

    @Autowired
    IEvaluaterecordService evaluaterecordService;

    @Autowired
    ILeavemessageService leavemessageService;

    @Override
    public AjaxResult getUserInfo(HttpServletRequest request) throws Exception {
        Long userId = tokenService.getUserId(request);
        User user = userService.getByKey(userId, true);
        var response = new UserObj();
        if (user != null) {
            response.setUserId(UrlUtil.encrypt(user.getUserId()))
                    .setUserName(user.getUserName())
                    .setUserTrueName(user.getUserTrueName())
                    .setUserFace(user.getUserFace())
                    .setStudentCode(user.getStudentCode())
                    .setComment(user.getComment())
                    .setIdCard(user.getIdCard())
                    .setSchoolId(user.getSchoolId() != null ? UrlUtil.encrypt(user.getSchoolId()) : null)
                    .setSchoolCode(user.getSchoolCode())
                    .setSchoolName(user.getSchoolName())
                    .setClassName(user.getClassName())
                    .setUserSex(user.getUserSex())
                    .setPhone(user.getPhone())
                    .setGradeId(user.getGradeId() != null ? UrlUtil.encrypt(user.getGradeId()) : null);
        }
        return AjaxResult.success(response);
    }

    @Override
    public AjaxResult getLiveList(HttpServletRequest request) {
        Long userId = tokenService.getUserId(request);
        List<Long> registerSubjectIds = new ArrayList<>();
        List<LiveObj> response = new LinkedList<>();
        var subjectWrapper = new QueryWrapper<Subject>();
        subjectWrapper.lambda().eq(Subject::getCategory, Category.LIVE.getId()).eq(Subject::getStatus, SubjectStatus.PUBLISHED.getId()).ne(Subject::getClassState, ClassState.END.getId()).orderByDesc(Subject::getBeginTime);
        IPage<Subject> page = new Page<>(1, 4);
        IPage<Subject> pages = subjectService.page(page, subjectWrapper);
        List<Subject> subjects = pages.getRecords();
        if (CollectionUtils.isNotEmpty(subjects)) {
            var subRegIds = getRegisterIds(subjects, userId);
            if (CollectionUtils.isNotEmpty(subRegIds)) {
                registerSubjectIds.addAll(subRegIds);
            }
        }
        for (Subject subject : subjects) {
            var live = new LiveObj()
                    .setId(UrlUtil.encrypt(subject.getId()))
                    .setName(subject.getName())
                    .setAllowSignUpCount(subject.getAllowSignUpCount())
                    .setImagePath(subject.getImagePath())
                    .setSignUpCounts(subject.getSignUpCount() + "/" + subject.getAllowSignUpCount())
                    .setSignUpCount(subject.getSignUpCount())
                    .setBeginTime(subject.getBeginTime())
                    .setClassStatus(CommonUtil.getClassStatus(subject));
            var sub = subjectService.getByKey(subject.getId(), true);
            if (CollectionUtils.isNotEmpty(sub.getChapterlessonList())) {
                live.setTeacherName(sub.getChapterlessonList().get(0).getTeacherName());
            }
            if (registerSubjectIds.contains(sub.getId())) {
                live.setRegisterStatus(1);
            } else {
                live.setRegisterStatus(0);
            }
            response.add(live);
        }

        return AjaxResult.success(response);
    }

    @Override
    public AjaxResult getHotSubjectList() {
        var subjectWrapper = new QueryWrapper<Subject>();
        subjectWrapper.lambda().eq(Subject::getStatus, SubjectStatus.PUBLISHED.getId()).eq(Subject::getClassState, ClassState.END.getId()).orderByDesc(Subject::getPlayCount);
        IPage<Subject> page = new Page<>(1, 4);
        IPage<Subject> pages = subjectService.page(page, subjectWrapper);
        List<Subject> subjects = pages.getRecords();
        List<SubjectObj> response = new LinkedList<>();
        for (Subject subject : subjects) {
            var hotSubjectObj = new SubjectObj()
                    .setId(UrlUtil.encrypt(subject.getId()))
                    .setName(subject.getName())
                    .setImagePath(subject.getImagePath())
                    .setClassHour(subject.getClassHour())
                    .setPlayCount(subject.getPlayCount());
            response.add(hotSubjectObj);
        }
        return AjaxResult.success(response);
    }

    @Override
    public AjaxResult getPreLiveList(GetPreLiveListBody body, HttpServletRequest request) {
        Long userId = tokenService.getUserId(request);
        List<Long> registerSubjectIds = new ArrayList<>();
        List<PreLiveObj> response = new LinkedList<>();
        var subjectWrapper = new QueryWrapper<Subject>();
        String start_date = DateUtils.getBeforeDaysDate(-1);
        if (StringUtils.isNotBlank(body.getName())) {
            subjectWrapper.lambda().like(Subject::getName, body.getName());
        }
        subjectWrapper.lambda().eq(Subject::getStatus, SubjectStatus.PUBLISHED.getId()).eq(Subject::getCategory, Category.LIVE.getId())
                .apply(StringUtils.isNotBlank(body.getBeginTime()) && StringUtils.isNotBlank(body.getEndTime()),
                        "date_format (begin_time,'%Y-%m-%d') >= date_format('" + body.getBeginTime() + "','%Y-%m-%d') and date_format (begin_time,'%Y-%m-%d') <= date_format('" + body.getEndTime() + "','%Y-%m-%d')")
                .apply(StringUtils.isNotBlank(body.getBeginTime()) && StringUtils.isBlank(body.getEndTime()),
                        "date_format (begin_time,'%Y-%m-%d') >= date_format('" + body.getBeginTime() + "','%Y-%m-%d')")
                .apply(StringUtils.isBlank(body.getBeginTime()) && StringUtils.isNotBlank(body.getEndTime()),
                        "date_format (begin_time,'%Y-%m-%d') >= date_format('" + start_date + "','%Y-%m-%d') and date_format (begin_time,'%Y-%m-%d') <= date_format('" + body.getEndTime() + "','%Y-%m-%d')")
                .apply(StringUtils.isBlank(body.getBeginTime()) && StringUtils.isBlank(body.getEndTime()),
                        "date_format (begin_time,'%Y-%m-%d') >= date_format('" + start_date + "','%Y-%m-%d')")
                .orderByDesc(Subject::getBeginTime);
        List<Subject> subjects = subjectService.list(subjectWrapper);
        if (CollectionUtils.isNotEmpty(subjects)) {
            var subRegIds = getRegisterIds(subjects, userId);
            if (CollectionUtils.isNotEmpty(subRegIds)) {
                registerSubjectIds.addAll(subRegIds);
            }
        }
        subjects.sort(Comparator.comparing(Subject::getBeginTime).reversed());
        var preLiveDateMap = subjects.stream().collect(Collectors.groupingBy(x -> DateUtils.getFormatDate(x.getBeginTime(), "yyyy-MM-dd")));
        for (Map.Entry<String, List<Subject>> entry : preLiveDateMap.entrySet()) {
            var preLiveObj = new PreLiveObj().setLiveDate(entry.getKey());
            var items = new ArrayList<PreLiveItem>();
            for (var sub : entry.getValue()) {
                var subject = subjectService.getByKey(sub.getId(), true);
                var chapterlesson = subject.getChapterlessonList();
                var item = new PreLiveItem().setSubjectId(UrlUtil.encrypt(subject.getId()))
                        .setName(sub.getName())
                        .setImagePath(sub.getImagePath())
                        .setSignUpCount(sub.getSignUpCount() + "/" + sub.getAllowSignUpCount())
                        .setClassState(CommonUtil.getClassStatus(sub))
                        .setLiveTimeRange(DateUtils.convertTimeRange(sub.getBeginTime(), sub.getEndTime()));
                if (CollectionUtils.isNotEmpty(chapterlesson)) {
                    item.setTeacherName(chapterlesson.get(0).getTeacherName());
                }
                if (registerSubjectIds.contains(sub.getId())) {
                    item.setRegisterStatus(1);
                } else {
                    item.setRegisterStatus(0);
                }
                items.add(item);
            }
            preLiveObj.getLiveList().addAll(items.stream().sorted(Comparator.comparing(PreLiveItem::getLiveTimeRange).reversed()).collect(Collectors.toList()));
            response.add(preLiveObj);
        }
        return AjaxResult.success(response.stream().sorted(Comparator.comparing(PreLiveObj::getLiveDate).reversed()).collect(Collectors.toList()));
    }


    private List<Long> getRegisterIds(List<Subject> subjects, Long userId) {
        if (userId != null && userId > 0) {
            var subjectIds = subjects.stream().map(e -> e.getId()).distinct().collect(Collectors.toList());
            return getRegisterIdsByUserId(subjectIds, userId);
        }
        return null;
    }

    private List<Long> getRegisterIdsByUserId(List<Long> subjectIds, Long userId) {
        var subjectstudentQueryWrapper = new QueryWrapper<Subjectstudent>();
        subjectstudentQueryWrapper.lambda().eq(Subjectstudent::getUserId, userId).in(Subjectstudent::getSubjectId, subjectIds).eq(Subjectstudent::getIsRegister, true).eq(Subjectstudent::getStatus, 1);
        var registerSubjects = subjectstudentService.list(subjectstudentQueryWrapper);
        if (CollectionUtils.isNotEmpty(registerSubjects)) {
            var regIds = registerSubjects.stream().map(e -> e.getSubjectId()).distinct().collect(Collectors.toList());
            return regIds;
        }
        return null;
    }

    @Override
    public AjaxResult getSubjectList(GetSubjectListBody body, HttpServletRequest request) {
        Long userId = tokenService.getUserId(request);
        var subjectWrapper = new QueryWrapper<Subject>();
        subjectWrapper.lambda().eq(Subject::getClassState, ClassState.END.getId()).eq(Subject::getStatus, SubjectStatus.PUBLISHED.getId());
        if (StringUtils.isNotBlank(body.getName())) {
            subjectWrapper.lambda().like(Subject::getName, body.getName());
        }
        if (StringUtils.isNotBlank(body.getGradeId())) {
            subjectWrapper.lambda().eq(Subject::getGradeId, UrlUtil.decrypt(body.getGradeId(), Long.class));
        }
        if (StringUtils.isNotBlank(body.getCourseId())) {
            subjectWrapper.lambda().eq(Subject::getCourseId, UrlUtil.decrypt(body.getCourseId(), Long.class));
        }
        subjectWrapper.lambda().orderByDesc(Subject::getId);
        IPage<Subject> page = new Page<>(body.getPageIndex(), body.getPageSize());
        IPage<Subject> pages = subjectService.page(page, subjectWrapper);
        var list = pages.getRecords();
        Map<Long, Boolean> ssMap = null;
        if (CollectionUtils.isNotEmpty(list)) {
            var subjectIds = list.stream().map(e -> e.getId()).distinct().collect(Collectors.toList());
            var subjectStudentWrapper = new QueryWrapper<Subjectstudent>();
            subjectStudentWrapper.lambda().eq(Subjectstudent::getUserId, userId).in(Subjectstudent::getSubjectId, subjectIds);
            var subjectstudentList = subjectstudentService.list(subjectStudentWrapper);
            ssMap = subjectstudentList.stream().collect(Collectors.toMap(x -> x.getSubjectId(), Subjectstudent::getIsEvaluate));
        }
        var subjectList = new ArrayList<SubjectObj>();
        for (var item : list) {
            var obj = new SubjectObj().setId(UrlUtil.encrypt(item.getId()))
                    .setClassHour(item.getClassHour())
                    .setCommentScore(item.getCommentScore())
                    .setImagePath(item.getImagePath())
                    .setName(item.getName())
                    .setPlayCount(item.getPlayCount());
            if (ssMap != null && ssMap.containsKey(item.getId()) && !ssMap.get(item.getId())) {
                obj.setEvaluateStatus(1);
            }
            subjectList.add(obj);
        }
        var response = new PageResponse<SubjectObj>().setTotal(pages.getTotal()).setDataList(subjectList);
        return AjaxResult.success(response);
    }

    @Override
    public AjaxResult getSubjectDetail(GetBySubjectIdBody body, HttpServletRequest request) {
        var subject = subjectService.getByKey(UrlUtil.decrypt(body.getSubjectId(), Long.class), true);
        GetSubjectDetailResponse response = new GetSubjectDetailResponse();
        response.setCategory(subject.getCategory());
        response.setStatus(subject.getStatus());
        response.setRemark(subject.getRemark());
        response.setPlan(subject.getPlan());
        response.setAllowSignUpCount(subject.getAllowSignUpCount());
        response.setSignUpCount(subject.getSignUpCount());
        response.setSignUpBeginTime(subject.getSignUpBeginTime());
        response.setSignUpEndTime(subject.getSignUpEndTime());
        if (subject.getCategory() == Category.LIVE.getId()) {
            response.setBeginTime(subject.getBeginTime());
            response.setEndTime(subject.getEndTime());
        }
        response.setClassState(CommonUtil.getClassStatus(subject));
        response.setClassHour(subject.getClassHour());
        response.setCommentScore(subject.getCommentScore());
        response.setPlayCount(subject.getPlayCount());
        response.setId(body.getSubjectId());
        response.setName(subject.getName());
        response.setImagePath(subject.getImagePath());
        for (var chapterlesson : subject.getChapterlessonList()) {
            response.getChapterLessons().add(new ChapterLessonObj().setId(UrlUtil.encrypt(chapterlesson.getId())).setName(chapterlesson.getName()).setTeacherName(chapterlesson.getTeacherName()).setVideoPath(chapterlesson.getVideoPath()).setSort(chapterlesson.getSort()));
        }
        Long userId = tokenService.getUserId(request);
        AsyncManager.me().execute(AsyncFactory.recordStudyLog(subject.getId(), userId, StudyRecordType.PLAY));
        return AjaxResult.success(response);
    }

    @Override
    public AjaxResult moreSubjectList(GetBySubjectIdBody body) {
        var sub = subjectService.getByKey(UrlUtil.decrypt(body.getSubjectId(), Long.class), true);
        var subjectWrapper = new QueryWrapper<Subject>();
        subjectWrapper.lambda().eq(Subject::getStatus, SubjectStatus.PUBLISHED.getId()).eq(Subject::getClassState, ClassState.END.getId())
                .eq(Subject::getGradeId, sub.getGradeId()).eq(Subject::getCourseId, sub.getCourseId()).ne(Subject::getId, sub.getId()).orderByDesc(Subject::getPlayCount);
        IPage<Subject> page = new Page<>(1, 4);
        IPage<Subject> pages = subjectService.page(page, subjectWrapper);
        List<Subject> subjects = pages.getRecords();
        List<SubjectObj> response = new LinkedList<>();
        for (Subject subject : subjects) {
            var hotSubjectObj = new SubjectObj()
                    .setId(UrlUtil.encrypt(subject.getId()))
                    .setName(subject.getName())
                    .setImagePath(subject.getImagePath())
                    .setClassHour(subject.getClassHour())
                    .setPlayCount(subject.getPlayCount());
            response.add(hotSubjectObj);
        }
        return AjaxResult.success(response);
    }

    @Override
    public AjaxResult getTeacherList(GetTeacherListBody body) {
        List<TeacherRankItem> list = redisCache.getCacheObject(Constants.TEACHERRANKLIST_KEY);
        if (StringUtils.isNotBlank(body.getUserTrueName())) {
            list = list.stream().filter(x -> x.getUserTrueName().contains(body.getUserTrueName())).collect(Collectors.toList());
        }
        list.stream().sorted(Comparator.comparing(TeacherRankItem::getOpusCount).reversed())
                .skip((body.getPageIndex() - 1) * body.getPageSize()).limit(body.getPageSize()).collect(Collectors.toList());
        List<TeacherObj> responseList = new ArrayList<>();
        for (var item : list) {
            var obj = new TeacherObj().setSchoolName(item.getSchoolName())
                    .setUserFace(item.getUserFace())
                    .setUserTrueName(item.getUserTrueName())
                    .setId(UrlUtil.encrypt(item.getId()))
                    .setOpusCount(item.getOpusCount());
            var newOpusSize = item.getSubjectList().size() > 3 ? 3 : item.getSubjectList().size();
            for (int i = 0; i < newOpusSize; i++) {
                var o = item.getSubjectList().get(i);
                obj.getNewestOpusList().add(new IdName(UrlUtil.encrypt(o.getId()), o.getName()));
            }
            responseList.add(obj);
        }
        var response = new PageResponse<TeacherObj>().setTotal(list.size()).setDataList(responseList);
        return AjaxResult.success(response);
    }

    @Override
    public AjaxResult getTeacherDetail(GetByTeacherIdBody body, HttpServletRequest request) {
        Long userId = tokenService.getUserId(request);
        List<Long> registerSubjectIds = new ArrayList<>();
        Long teacherId = UrlUtil.decrypt(body.getTeacherId(), Long.class);
        var response = new GetTeacherDetailResponse().setId(body.getTeacherId());
        var teacher = userService.getByKey(teacherId, true);
        response.setComment(teacher.getComment())
                .setUserTrueName(teacher.getUserTrueName())
                .setSchoolName(teacher.getSchoolName())
                .setUserFace(teacher.getUserFace());
        var chapterlessonQueryWrapper = new QueryWrapper<Chapterlesson>();
        chapterlessonQueryWrapper.lambda().eq(Chapterlesson::getStatus, 1).eq(Chapterlesson::getTeacherId, teacherId);
        var cps = chapterlessonService.list(chapterlessonQueryWrapper);
        var cpsMap = cps.stream().collect(Collectors.groupingBy(Chapterlesson::getSubjectId));
        var subjectIds = cpsMap.keySet().stream().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(subjectIds)) {
            var subRegIds = getRegisterIdsByUserId(subjectIds, userId);
            if (CollectionUtils.isNotEmpty(subRegIds)) {
                registerSubjectIds.addAll(subRegIds);
            }
        }
        for (Long key : cpsMap.keySet()) {
            var sub = subjectService.getByKey(key, true);
            if (sub.getStatus().equals(SubjectStatus.PUBLISHED.getId())) {
                var item = new SubjectObj().setId(UrlUtil.encrypt(sub.getId()))
                        .setName(sub.getName())
                        .setCategory(sub.getCategory())
                        .setClassState(CommonUtil.getClassStatus(sub))
                        .setSignUpCount(sub.getSignUpCount())
                        .setSignUpCounts(sub.getSignUpCount()+"/"+sub.getAllowSignUpCount())
                        .setAllowSignUpCount(sub.getAllowSignUpCount())
                        .setCommentScore(sub.getCommentScore()).setImagePath(sub.getImagePath())
                        .setEndTime(sub.getEndTime()).setBeginTime(sub.getBeginTime());
                if (registerSubjectIds.contains(sub.getId())) {
                    item.setRegisterStatus(1);
                } else {
                    item.setRegisterStatus(0);
                }
                response.getOpusList().add(item);
            }
        }
        return AjaxResult.success(response);
    }

    @Override
    public AjaxResult mySubjectList(GetSubjectListBody body, HttpServletRequest request) {
        Long userId = tokenService.getUserId(request);
        var subjectStudentWrapper = new QueryWrapper<StudentsubjectVO>();
        subjectStudentWrapper.eq("ss.user_id", userId)
                .eq("s.status", SubjectStatus.PUBLISHED.getId())
                .eq("ss.status", 1);
        if (StringUtils.isNotBlank(body.getName())) {
            subjectStudentWrapper.like("s.name", body.getName());
        }
        Page<StudentsubjectVO> page = new Page<>(body.getPageIndex(), body.getPageSize());
        IPage<StudentsubjectVO> pages = subjectstudentService.mySubjectPage(page, subjectStudentWrapper);
        var studentSubjectObjs = new ArrayList<StudentSubjectObj>();
        for (var item : pages.getRecords()) {
            var obj = new StudentSubjectObj().setId(UrlUtil.encrypt(item.getId()))
                    .setCategory(item.getCategory())
                    .setImagePath(item.getImagePath())
                    .setName(item.getName())
                    .setClassState(CommonUtil.getClassStatus(item));
            if (item.getCategory().equals(Category.LIVE.getId())) {
                var subject = subjectService.getByKey(item.getId(), true);
                obj.setTeacherName(subject.getChapterlessonList().get(0).getTeacherName());
            }
            if (null != item.getEvaluateLevel() && item.getEvaluateLevel().intValue() > 0) {
                obj.setEvaluateInfo(new EvaluateInfoVO().setEvaluateLevel(item.getEvaluateLevel()).setContent(item.getContent()).setSubjectId(UrlUtil.encrypt(item.getId())));
            }
            studentSubjectObjs.add(obj);
        }
        var response = new PageResponse<StudentSubjectObj>().setTotal(pages.getTotal()).setDataList(studentSubjectObjs);
        return AjaxResult.success(response);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult evaluateSubject(EvaluateSubjectBody body, HttpServletRequest request) {
        AjaxResult ajax = AjaxResult.success();
        Long subjectId = UrlUtil.decrypt(body.getSubjectId(), Long.class);
        Long userId = tokenService.getUserId(request);
        User user = userService.getByKey(userId, true);
        Evaluaterecord evaluaterecord = new Evaluaterecord()
                .setSourceUserId(userId)
                .setSourceUserName(user.getUserTrueName())
                .setContent(body.getContent())
                .setSubjectId(UrlUtil.decrypt(body.getSubjectId(), Long.class))
                .setEvaluateLevel(body.getEvaluateLevel());
        var recordResult = evaluaterecordService.save(evaluaterecord);
        if (recordResult) {
            // 计算课程平均评分,已用定时任务去做
            var queryWrapper = new QueryWrapper<Subjectstudent>();
            queryWrapper.lambda().eq(Subjectstudent::getSubjectId, subjectId).eq(Subjectstudent::getUserId, userId);
            var subjectstudent = subjectstudentService.getOne(queryWrapper);
            subjectstudent.setIsEvaluate(true);
            var evalStatus = subjectstudent.updateById();
            ajax.put("status", evalStatus ? 1 : 2);
        } else {
            ajax.put("status", 2);
        }
        return ajax;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult registSubject(GetBySubjectIdBody body, HttpServletRequest request) {
        AjaxResult ajax = AjaxResult.success();
        Long subjectId = UrlUtil.decrypt(body.getSubjectId(), Long.class);
        Long userId = tokenService.getUserId(request);
        var queryWrapper = new QueryWrapper<Subjectstudent>();
        queryWrapper.lambda().eq(Subjectstudent::getSubjectId, subjectId).eq(Subjectstudent::getUserId, userId);
        var list = subjectstudentService.list(queryWrapper);
        if (CollectionUtils.isNotEmpty(list)) {
            ajax.put("status", 0);
        } else {
            Subject subject = subjectService.getByKey(subjectId, true);
            if(subject.getAllowSignUpCount()!=0 && subject.getSignUpCount()>=subject.getAllowSignUpCount()){
                ajax.put("status", 3);
                ajax.put("msg","报名人数已满");
            }else{
                var subjectstudent = new Subjectstudent().setUserId(userId).setSubjectId(subjectId).setIsRegister(true);
                var result = subjectstudentService.save(subjectstudent);
                if (result) {
                    subject.setSignUpCount(subject.getSignUpCount()+1);
                    var res = subject.updateById();
                    if(res){
                        ajax.put("status", 1);
                        AsyncManager.me().execute(AsyncFactory.recordStudyLog(subjectId, userId, StudyRecordType.REGISTER));
                    }else{
                        ajax.put("status", 2);
                    }
                } else {
                    ajax.put("status", 2);
                }
            }
        }
        return ajax;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult leavemessage(LeavemessageBody body, HttpServletRequest request) {
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
        ajax.put("status", result ? 1 : 2);
        return ajax;
    }

    @Override
    public AjaxResult getLeavemessage(GetLeavemessageBody body) {
        Long subjectId = UrlUtil.decrypt(body.getSubjectId(), Long.class);
        Long chapterlessonId = UrlUtil.decrypt(body.getChapterlessonId(), Long.class);
        Long boundaryId = UrlUtil.decrypt(body.getBoundaryId(), Long.class);
        var messageWrapper = new QueryWrapper<Leavemessage>();
        messageWrapper.lambda().eq(Leavemessage::getSubjectId, subjectId).eq(Leavemessage::getChapterlessonId, chapterlessonId).eq(Leavemessage::getStatus, 1).eq(Leavemessage::getReplyId, 0L).orderByDesc(Leavemessage::getId);
        var allMessage = leavemessageService.list(messageWrapper);
        if (boundaryId > 0) {

            allMessage = allMessage.stream().filter(m -> m.getId() < boundaryId).collect(Collectors.toList());
        }
        var list = allMessage.stream().limit(body.getPageSize()).collect(Collectors.toList());
        List<Long> rootids = list.stream().map(e -> e.getId()).collect(Collectors.toList());
        String rootidsStr = com.ry.common.utils.StringUtils.join(rootids.toArray(), ",");
        List<LeavemessageVO> messageList = leavemessageService.getChildrensByRootIds(rootidsStr, UserType.Student.getId());
        List<LeavemessageVO> selectTrees = leavemessageService.buildSelectTree(messageList);
        var leavemessageTrees = selectTrees.stream().map(LeavemessageTree::new).collect(Collectors.toList());
        var response = new PageResponse<LeavemessageTree>().setTotal(list.size()).setDataList(leavemessageTrees);
        return AjaxResult.success(response);
    }

    @Override
    public AjaxResult getLeavemessageList(GetLeavemessageBody body,HttpServletRequest request) {
        Long userId = tokenService.getUserId(request);
        Long subjectId = UrlUtil.decrypt(body.getSubjectId(), Long.class);
        Long chapterlessonId = UrlUtil.decrypt(body.getChapterlessonId(), Long.class);
        var messageWrapper = new QueryWrapper<Leavemessage>();
        messageWrapper.lambda().eq(Leavemessage::getSubjectId, subjectId).eq(Leavemessage::getChapterlessonId, chapterlessonId).eq(Leavemessage::getUserId,userId).eq(Leavemessage::getStatus, 1).eq(Leavemessage::getReplyId, 0L).orderByAsc(Leavemessage::getReplayState);
        IPage<Leavemessage> page = new Page<Leavemessage>(body.getPageIndex(), body.getPageSize());
        IPage<Leavemessage> pages = leavemessageService.page(page, messageWrapper);
        List<Long> rootids = pages.getRecords().stream().map(e -> e.getId()).collect(Collectors.toList());
        String rootidsStr = StringUtils.join(rootids.toArray(), ",");
        List<LeavemessageVO> messageList = leavemessageService.getChildrensByRootIds(rootidsStr,UserType.Student.getId());
        List<LeavemessageVO> selectTrees = leavemessageService.buildSelectTree(messageList);
        var leavemessageTrees = selectTrees.stream().map(LeavemessageTree::new).collect(Collectors.toList());
        var response = new PageResponse<LeavemessageTree>().setTotal(pages.getTotal()).setDataList(leavemessageTrees);
        return AjaxResult.success(response);
    }
}
