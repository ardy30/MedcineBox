package com.newthread.medicinebox.bean;

/**
 * Created by 张浩 on 2016/3/9.
 */
public class UserInfo {
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getUserInfoJson() {
        return UserInfoJson;
    }

    public void setUserInfoJson(String userInfoJson) {
        UserInfoJson = userInfoJson;
    }

    public String getLoginMessage() {
        return loginMessage;
    }

    public void setLoginMessage(String loginMessage) {
        this.loginMessage = loginMessage;
    }

    String sessionId;
    boolean login;
    String response;
    String nickname;
    String age;
    String headImgUrl;
    String UserInfoJson;
    String loginMessage;

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    boolean updated;

    public boolean isFileUpLoaded() {
        return fileUpLoaded;
    }

    public void setFileUpLoaded(boolean fileUpLoaded) {
        this.fileUpLoaded = fileUpLoaded;
    }

    boolean fileUpLoaded;
}
