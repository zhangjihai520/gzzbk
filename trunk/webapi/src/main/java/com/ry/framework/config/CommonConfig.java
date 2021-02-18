package com.ry.framework.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取项目相关配置
 *
 * @author
 */
@Component
@ConfigurationProperties(prefix = "common")
public class CommonConfig {
    /**
     * 项目名称
     */
    private String name;

    /**
     * 版本
     */
    private String version;

    /**
     * 版权年份
     */
    private String copyrightYear;

    /**
     * 实例演示开关
     */
    private boolean demoEnabled;
    /**
     * 上传路径
     */
    private static String profile;

    /**
     * 获取地址开关
     */
    private static boolean addressEnabled;


    public static String dledcApiUrl;
    public static String offLineRedirectUri;
    public static String offLineClientId;
    public static String offLineClientSecret;
    public static String offLineScope;
    public static String offLinePostLogoutRedirectUri;
    public static String onlineRedirectUri;
    public static String onlineClientId;
    public static String onlineClientSecret;
    public static String onlineScope;
    public static String onlinePostLogoutRedirectUri;
    public static String dledcCorpId;
    public static String dledcToken;
    public static String dledcEncodingAESKey;
    public static String dledcPlatformUrl;
    public static boolean useSynchronizationUse;
    public static String unSynchronizationUser;

    public static String livePath;
    public static String recordPath;

    public static String ftpHost;
    public static String ftpPort;
    public static String ftpUserName;
    public static String ftpPassword;
    public static String ftpBasePath;
    public static String ftpHttpPath;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCopyrightYear() {
        return copyrightYear;
    }

    public void setCopyrightYear(String copyrightYear) {
        this.copyrightYear = copyrightYear;
    }

    public boolean isDemoEnabled() {
        return demoEnabled;
    }

    public void setDemoEnabled(boolean demoEnabled) {
        this.demoEnabled = demoEnabled;
    }

    public static String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        CommonConfig.profile = profile;
    }

    public static boolean isAddressEnabled() {
        return addressEnabled;
    }

    public void setAddressEnabled(boolean addressEnabled) {
        CommonConfig.addressEnabled = addressEnabled;
    }

    public static String getDledcApiUrl() {
        return dledcApiUrl;
    }

    public void setDledcApiUrl(String dledcApiUrl) {
        CommonConfig.dledcApiUrl = dledcApiUrl;
    }

    public static String getOffLineRedirectUri() {
        return offLineRedirectUri;
    }

    public void setOffLineRedirectUri(String offLineRedirectUri) {
        CommonConfig.offLineRedirectUri = offLineRedirectUri;
    }

    public static String getOffLineClientId() {
        return offLineClientId;
    }

    public void setOffLineClientId(String offLineClientId) {
        CommonConfig.offLineClientId = offLineClientId;
    }

    public static String getOffLineClientSecret() {
        return offLineClientSecret;
    }

    public void setOffLineClientSecret(String offLineClientSecret) {
        CommonConfig.offLineClientSecret = offLineClientSecret;
    }

    public static String getOffLineScope() {
        return offLineScope;
    }

    public void setOffLineScope(String offLineScope) {
        CommonConfig.offLineScope = offLineScope;
    }

    public static String getOffLinePostLogoutRedirectUri() {
        return offLinePostLogoutRedirectUri;
    }

    public void setOffLinePostLogoutRedirectUri(String offLinePostLogoutRedirectUri) {
        CommonConfig.offLinePostLogoutRedirectUri = offLinePostLogoutRedirectUri;
    }

    public static String getOnlineRedirectUri() {
        return onlineRedirectUri;
    }

    public void setOnlineRedirectUri(String onlineRedirectUri) {
        CommonConfig.onlineRedirectUri = onlineRedirectUri;
    }

    public static String getOnlineClientId() {
        return onlineClientId;
    }

    public void setOnlineClientId(String onlineClientId) {
        CommonConfig.onlineClientId = onlineClientId;
    }

    public static String getOnlineClientSecret() {
        return onlineClientSecret;
    }

    public void setOnlineClientSecret(String onlineClientSecret) {
        CommonConfig.onlineClientSecret = onlineClientSecret;
    }

    public static String getOnlineScope() {
        return onlineScope;
    }

    public void setOnlineScope(String onlineScope) {
        CommonConfig.onlineScope = onlineScope;
    }

    public static String getOnlinePostLogoutRedirectUri() {
        return onlinePostLogoutRedirectUri;
    }

    public void setOnlinePostLogoutRedirectUri(String onlinePostLogoutRedirectUri) {
        CommonConfig.onlinePostLogoutRedirectUri = onlinePostLogoutRedirectUri;
    }

    public static String getDledcCorpId() {
        return dledcCorpId;
    }

    public void setDledcCorpId(String dledcCorpId) {
        CommonConfig.dledcCorpId = dledcCorpId;
    }

    public static String getDledcToken() {
        return dledcToken;
    }

    public void setDledcToken(String dledcToken) {
        CommonConfig.dledcToken = dledcToken;
    }

    public static String getDledcEncodingAESKey() {
        return dledcEncodingAESKey;
    }

    public void setDledcEncodingAESKey(String dledcEncodingAESKey) {
        CommonConfig.dledcEncodingAESKey = dledcEncodingAESKey;
    }

    public static String getDledcPlatformUrl() {
        return dledcPlatformUrl;
    }

    public void setDledcPlatformUrl(String dledcPlatformUrl) {
        CommonConfig.dledcPlatformUrl = dledcPlatformUrl;
    }

    public static boolean isUseSynchronizationUse() {
        return useSynchronizationUse;
    }

    public void setUseSynchronizationUse(boolean useSynchronizationUse) {
        CommonConfig.useSynchronizationUse = useSynchronizationUse;
    }

    public static String getUnSynchronizationUser() {
        return unSynchronizationUser;
    }

    public void setUnSynchronizationUser(String unSynchronizationUser) {
        CommonConfig.unSynchronizationUser = unSynchronizationUser;
    }

    public static String getLivePath() {
        return livePath;
    }

    public void setLivePath(String livePath) {
        CommonConfig.livePath = livePath;
    }

    public static String getRecordPath() {
        return recordPath;
    }

    public void setRecordPath(String recordPath) {
        CommonConfig.recordPath = recordPath;
    }

    public static String getFtpHost() {
        return ftpHost;
    }

    public void setFtpHost(String ftpHost) {
        CommonConfig.ftpHost = ftpHost;
    }

    public static String getFtpPort() {
        return ftpPort;
    }

    public void setFtpPort(String ftpPort) {
        CommonConfig.ftpPort = ftpPort;
    }

    public static String getFtpUserName() {
        return ftpUserName;
    }

    public void setFtpUserName(String ftpUserName) {
        CommonConfig.ftpUserName = ftpUserName;
    }

    public static String getFtpPassword() {
        return ftpPassword;
    }

    public void setFtpPassword(String ftpPassword) {
        CommonConfig.ftpPassword = ftpPassword;
    }

    public static String getFtpBasePath() {
        return ftpBasePath;
    }

    public void setFtpBasePath(String ftpBasePath) {
        CommonConfig.ftpBasePath = ftpBasePath;
    }

    public static String getFtpHttpPath() {
        return ftpHttpPath;
    }

    public void setFtpHttpPath(String ftpHttpPath) {
        CommonConfig.ftpHttpPath = ftpHttpPath;
    }

    /**
     * 获取头像上传路径
     */
    public static String getAvatarPath() {
        return getProfile() + "/avatar";
    }

    /**
     * 获取下载路径
     */
    public static String getDownloadPath() {
        return getProfile() + "/download/";
    }

    /**
     * 获取上传路径
     */
    public static String getUploadPath() {
        return getProfile() + "/upload";
    }
}