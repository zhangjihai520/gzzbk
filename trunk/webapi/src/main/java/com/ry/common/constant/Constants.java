package com.ry.common.constant;

import io.jsonwebtoken.Claims;

import java.time.format.DateTimeFormatter;

/**
 * 通用常量信息
 *
 * @author
 */
public class Constants {
    public static final String UidKey = "RuanYun_UserIdKey";
    public static final String IDDESKEY = "lQa9_&skzly%!9fs2@*UNA($ck_^:)'aI9e.^2Lbx9,5lf!j+~Hz@^hakuJ^crOb";
    public static final String DUPLICATE_TOKEN_KEY = "duplicate_post:cache";
    public static final int MINUTE_10 = 60 * 10;
    public static final int MINUTE_30 = 60 * 30;
    public static final int MINUTE_60 = 60 * 60;
    public static final int MINUTE_120 = 60 * 60 * 2;
    public static final int MINUTE_360 = 60 * 60 * 6;
    public static final int YEAR_3= 60 * 60 * 24 * 365 * 3;
    public static final int HOUR_24= 60 * 60 * 24;
    public static final int MINUTE_480 = 60 * 60 * 8;
    public static final int HALF_YEAR = 60 * 60 * 24 * 180;
    public static final DateTimeFormatter datefomat_nomal = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final DateTimeFormatter datefomat_nomal2 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    public static final DateTimeFormatter Datefomat_yyyyMMddHHmm = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

    public static final DateTimeFormatter Datefomat_yyyyMMdd = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static final DateTimeFormatter Datefomat_yyyyMMdd2 = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";

    /**
     * 通用成功标识
     */
    public static final String SUCCESS = "0";

    /**
     * 通用失败标识
     */
    public static final String FAIL = "1";

    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS = "Success";

    /**
     * 注销
     */
    public static final String LOGOUT = "Logout";

    /**
     * 登录失败
     */
    public static final String LOGIN_FAIL = "Error";
    /**
     * 验证码 redis key
     */
    public static final String DEPT_KEY = "dept:";
    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";
    /**
     * 验证码 redis key
     */
    public static final String CHANGEPWD_CODE_KEY = "changepwd_codes:";

    /**
     * 验证码 redis key
     */
    public static final String REGISTER_CODE_KEY = "register_codes:";

    /**
     * 登录用户 redis key
     */
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";
    /**
     * 区县 redis key
     */
    public static final String DEPTMANAGER_KEY = "DeptManager:";


    /**
     * 中学 redis key
     */
    public static final String HIGHSCHOOLS_KEY = "high_schools";

    /**
     * 字典 redis key
     */
    public static final String TEACHERRANKLIST_KEY = "TeacherRankListCache";

    /**
     * 字典 redis key
     */
    public static final String SENSIWORDLIST_KEY = "SensiWordListCache";
    /**
     * 业务数据缓存
     */
    public static final Integer BUSSINESS_DATA_CACHE = 30;

    /**
     * 验证码有效期（分钟）
     */
    public static final Integer CAPTCHA_EXPIRATION = 5;
    /**
     * 令牌
     */
    public static final String TOKEN = "token";

    /**
     * 令牌前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 令牌前缀
     */
    public static final String LOGIN_USER_KEY = "login_user_key";

    public static final String LOGIN_USER_TYPE = "login_user_type";

    /**
     * 用户ID
     */
    public static final String JWT_USERID = "userid";

    /**
     * 用户名称
     */
    public static final String JWT_USERNAME = Claims.SUBJECT;

    /**
     * 用户头像
     */
    public static final String JWT_AVATAR = "avatar";

    /**
     * 创建时间
     */
    public static final String JWT_CREATED = "created";

    /**
     * 用户权限
     */
    public static final String JWT_AUTHORITIES = "authorities";

    /**
     * 资源映射路径 前缀
     */
    public static final String RESOURCE_PREFIX = "/profile";

    /// <summary>
    /// 文件最大长度(长度字节)
    /// </summary>
    public static final long FileMaxLength = 104857600;
    // 年月日JGPRD
    public static final int YEAR = 3;
    public static final int MONTH = 2;
    public static final int DAY = 1;
}
