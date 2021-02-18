package com.ry.framework.manager.factory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimerTask;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ry.common.enums.ClassState;
import com.ry.common.enums.StudyRecordType;
import com.ry.common.utils.DateUtils;
import com.ry.project.bussiness.domain.bo.Evaluaterecord;
import com.ry.project.bussiness.domain.bo.Studentstudyrecord;
import com.ry.project.bussiness.domain.bo.Subjectstudent;
import com.ry.project.bussiness.service.IStudentstudyrecordService;
import com.ry.project.bussiness.service.ISubjectService;
import com.ry.project.bussiness.service.ISubjectstudentService;
import lombok.var;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ry.common.constant.Constants;
import com.ry.common.utils.LogUtils;
import com.ry.common.utils.ServletUtils;
import com.ry.common.utils.ip.AddressUtils;
import com.ry.common.utils.ip.IpUtils;
import com.ry.common.utils.spring.SpringUtils;
import com.ry.project.monitor.domain.SysLogininfor;
import com.ry.project.monitor.domain.SysOperLog;
import com.ry.project.monitor.service.ISysLogininforService;
import com.ry.project.monitor.service.ISysOperLogService;
import eu.bitwalker.useragentutils.UserAgent;

/**
 * 异步工厂（产生任务用）
 *
 * @author
 */
public class AsyncFactory {
    private static final Logger sys_user_logger = LoggerFactory.getLogger("sys-user");

    /**
     * 记录登陆信息
     *
     * @param username 用户名
     * @param status   状态
     * @param message  消息
     * @param args     列表
     * @return 任务task
     */
    public static TimerTask recordLogininfor(final String username, final int logintype, final int usertype, final String status, final String message,
                                             final Object... args) {
        final UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        final String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
        return new TimerTask() {
            @Override
            public void run() {
                String address = AddressUtils.getRealAddressByIP(ip);
                StringBuilder s = new StringBuilder();
                s.append(LogUtils.getBlock(ip));
                s.append(address);
                s.append(LogUtils.getBlock(username));
                s.append(LogUtils.getBlock(status));
                s.append(LogUtils.getBlock(message));
                // 打印信息到日志
                sys_user_logger.info(s.toString(), args);
                // 获取客户端操作系统
                String os = userAgent.getOperatingSystem().getName();
                // 获取客户端浏览器
                String browser = userAgent.getBrowser().getName();
                // 封装对象
                SysLogininfor logininfor = new SysLogininfor();
                logininfor.setUserName(username);
                logininfor.setIpaddr(ip);
                logininfor.setLoginLocation(address);
                logininfor.setBrowser(browser);
                logininfor.setOs(os);
                logininfor.setMsg(message);
                logininfor.setLoginType(logintype);
                logininfor.setUserType(usertype);
                // 日志状态
                if (Constants.LOGIN_SUCCESS.equals(status) || Constants.LOGOUT.equals(status)) {
                    logininfor.setStatus(Constants.SUCCESS);
                } else if (Constants.LOGIN_FAIL.equals(status)) {
                    logininfor.setStatus(Constants.FAIL);
                }
                // 插入数据
                SpringUtils.getBean(ISysLogininforService.class).insertLogininfor(logininfor);
            }
        };
    }

    /**
     * 学习轨迹记录
     *
     * @param
     * @return 任务task
     */
    public static TimerTask recordStudyLog(final Long subjectId, final Long userId, final StudyRecordType studyRecordType) {
        return new TimerTask() {
            @Override
            public void run() {
                var studentstudyrecordService = SpringUtils.getBean(IStudentstudyrecordService.class);
                var subjectService = SpringUtils.getBean(ISubjectService.class);
                var subject = subjectService.getByKey(subjectId, true);
                var record = new Studentstudyrecord().setSubjectId(subjectId).setUserId(userId).setStudyRecordType(studyRecordType.getId()).setContent(String.format("%s“%s”", studyRecordType.getName(), subject.getName()));
                if (studyRecordType.getId().equals(StudyRecordType.PLAY.getId()) && subject.getClassState() > ClassState.BEFORE.getId()) {
                    var queryWapper = new QueryWrapper<Studentstudyrecord>();
                    String start_date = LocalDateTime.now().plusMinutes(-10).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    queryWapper.lambda().eq(Studentstudyrecord::getUserId, userId).eq(Studentstudyrecord::getSubjectId, subjectId)
                            .apply("date_format (create_time,'%Y-%m-%d %H:%i:%s') > date_format('" + start_date + "','%Y-%m-%d %H:%i:%s')");
                    var lastData = studentstudyrecordService.getOne(queryWapper);
                    if (lastData == null) {
                        studentstudyrecordService.save(record);
                    }
                    var subjectstudentService = SpringUtils.getBean(ISubjectstudentService.class);
                    var queryWrapper = new QueryWrapper<Subjectstudent>();
                    queryWrapper.lambda().eq(Subjectstudent::getSubjectId, subjectId).eq(Subjectstudent::getUserId, userId);
                    var result = subjectstudentService.getOne(queryWrapper);
                    if (result == null) {
                        var subjectstudent = new Subjectstudent().setUserId(userId).setSubjectId(subjectId).setIsRegister(false);
                        subjectstudentService.save(subjectstudent);
                    }
                } else if (studyRecordType.getId().equals(StudyRecordType.REGISTER.getId())) {
                    studentstudyrecordService.save(record);
                }
            }
        };
    }


    /**
     * 学习轨迹记录
     *
     * @param subjectId 学习轨迹信息
     * @return 任务task
     */
    public static TimerTask reflashSubjectCache(final Long subjectId) {
        return new TimerTask() {
            @Override
            public void run() {
                SpringUtils.getBean(ISubjectService.class).getByKey(subjectId, false);
            }
        };
    }


    /**
     * 操作日志记录
     *
     * @param operLog 操作日志信息
     * @return 任务task
     */
    public static TimerTask recordOper(final SysOperLog operLog) {
        return new TimerTask() {
            @Override
            public void run() {
                // 远程查询操作地点
                operLog.setOperLocation(AddressUtils.getRealAddressByIP(operLog.getOperIp()));
                SpringUtils.getBean(ISysOperLogService.class).insertOperlog(operLog);
            }
        };
    }
}
