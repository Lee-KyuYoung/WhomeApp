package com.lky.whome.vo;

import java.io.Serializable;
import java.util.List;

public class WhomeVO implements Serializable {

    private String homeId;
    private String homeTitle;
    private String homeType;
    private String homeBedCount;
    private String homeRate;
    private String homeRateCount;
    private String homePay;
    private String homeImg;
    private String homeMaxGuest;
    private String homeBathCount;
    private String userId;
    private String userImg;
    private String userJoinDate;
    private String userIntro;
    private String homeAddr1;
    private String homeAddr2;
    private String homeRevStart;
    private String homeRevEnd;
    private String homeComm;
    private String homeRange;
    private String latitude;
    private String longitude;
    private List<String> homeImgList;
    private List<String> homeFacilityList;
    private List<String> homeGuestRuleList;
    private List<String> homeTypeList;
    private List<String> homePrecautionList;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getUserIntro() {
        return userIntro;
    }

    public void setUserIntro(String userIntro) {
        this.userIntro = userIntro;
    }

    public String getHomeId() {
        return homeId;
    }

    public void setHomeId(String homeId) {
        this.homeId = homeId;
    }

    public String getHomeTitle() {
        return homeTitle;
    }

    public void setHomeTitle(String homeTitle) {
        this.homeTitle = homeTitle;
    }

    public String getHomeType() {
        return homeType;
    }

    public void setHomeType(String homeType) {
        this.homeType = homeType;
    }

    public String getHomePay() {
        return homePay;
    }

    public void setHomePay(String homePay) {
        this.homePay = homePay;
    }

    public String getHomeBedCount() {
        return homeBedCount;
    }

    public void setHomeBedCount(String homeBedCount) {
        this.homeBedCount = homeBedCount;
    }

    public String getHomeRate() {
        return homeRate;
    }

    public void setHomeRate(String homeRate) {
        this.homeRate = homeRate;
    }

    public String getHomeImg() {
        return homeImg;
    }

    public void setHomeImg(String homeImg) {
        this.homeImg = homeImg;
    }

    public String getHomeMaxGuest() {
        return homeMaxGuest;
    }

    public void setHomeMaxGuest(String homeMaxGuest) {
        this.homeMaxGuest = homeMaxGuest;
    }

    public String getHomeBathCount() {
        return homeBathCount;
    }

    public void setHomeBathCount(String homeBathCount) {
        this.homeBathCount = homeBathCount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHomeAddr1() {
        return homeAddr1;
    }

    public void setHomeAddr1(String homeAddr1) {
        this.homeAddr1 = homeAddr1;
    }

    public String getHomeAddr2() {
        return homeAddr2;
    }

    public void setHomeAddr2(String homeAddr2) {
        this.homeAddr2 = homeAddr2;
    }

    public String getHomeRevStart() {
        return homeRevStart;
    }

    public void setHomeRevStart(String homeRevStart) {
        this.homeRevStart = homeRevStart;
    }

    public String getHomeRevEnd() {
        return homeRevEnd;
    }

    public void setHomeRevEnd(String homeRevEnd) {
        this.homeRevEnd = homeRevEnd;
    }

    public String getHomeComm() {
        return homeComm;
    }

    public void setHomeComm(String homeComm) {
        this.homeComm = homeComm;
    }

    public String getHomeRange() {
        return homeRange;
    }

    public void setHomeRange(String homeRange) {
        this.homeRange = homeRange;
    }

    public List<String> getHomeImgList() {
        return homeImgList;
    }

    public void setHomeImgList(List<String> homeImgList) {
        this.homeImgList = homeImgList;
    }

    public List<String> getHomeFacilityList() {
        return homeFacilityList;
    }

    public void setHomeFacilityList(List<String> homeFacilityList) {
        this.homeFacilityList = homeFacilityList;
    }

    public List<String> getHomeGuestRuleList() {
        return homeGuestRuleList;
    }

    public void setHomeGuestRuleList(List<String> homeGuestRuleList) {
        this.homeGuestRuleList = homeGuestRuleList;
    }

    public List<String> getHomeTypeList() {
        return homeTypeList;
    }

    public void setHomeTypeList(List<String> homeTypeList) {
        this.homeTypeList = homeTypeList;
    }

    public List<String> getHomePrecautionList() {
        return homePrecautionList;
    }

    public void setHomePrecautionList(List<String> homePrecautionList) {
        this.homePrecautionList = homePrecautionList;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getHomeRateCount() {
        return homeRateCount;
    }

    public void setHomeRateCount(String homeRateCount) {
        this.homeRateCount = homeRateCount;
    }

    public String getUserJoinDate() {
        return userJoinDate;
    }

    public void setUserJoinDate(String userJoinDate) {
        this.userJoinDate = userJoinDate;
    }
}
