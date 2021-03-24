package com.lky.whome.vo;

import java.io.Serializable;

public class UserInfoVO implements Serializable {
    private String userId;
    private String userPw;
    private String userName;
    private String userImg;
    private String userEmail;
    private String userAddr1;
    private String userAddr2;
    private String userPhone;
    private String userIntro;
    private String userAuth;
    private String userBirth;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPw() {
        return userPw;
    }

    public void setUserPw(String userPw) {
        this.userPw = userPw;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserAddr1() {
        return userAddr1;
    }

    public void setUserAddr1(String userAddr1) {
        this.userAddr1 = userAddr1;
    }

    public String getUserAddr2() {
        return userAddr2;
    }

    public void setUserAddr2(String userAddr2) {
        this.userAddr2 = userAddr2;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserIntro() {
        return userIntro;
    }

    public void setUserIntro(String userIntro) {
        this.userIntro = userIntro;
    }

    public String getUserAuth() {
        return userAuth;
    }

    public void setUserAuth(String userAuth) {
        this.userAuth = userAuth;
    }

    public String getUserBirth() {
        return userBirth;
    }

    public void setUserBirth(String userBirth) {
        this.userBirth = userBirth;
    }
}
