package com.yunkang.chat.start.model;

/**
 * 作者：凌涛 on 2019/3/20 14:55
 * 邮箱：771548229@qq..com
 */
public class Coupon {
    private int regularTime;
    private String id;
    private String custId;
    private String couponId;
    private String couponName;
    private double couponPrice;
    private String startTermValidity;
    private String endTermValidity;
    private String termValidityStatus;
    private double satisfy;
    private String getType;
    private String couponStatus;
    private String createBy;
    private String createDate;
    private String nickName;

    public int getRegularTime() {
        return regularTime;
    }

    public void setRegularTime(int regularTime) {
        this.regularTime = regularTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public double getCouponPrice() {
        return couponPrice;
    }

    public void setCouponPrice(double couponPrice) {
        this.couponPrice = couponPrice;
    }

    public String getStartTermValidity() {
        return startTermValidity;
    }

    public void setStartTermValidity(String startTermValidity) {
        this.startTermValidity = startTermValidity;
    }

    public String getEndTermValidity() {
        return endTermValidity;
    }

    public void setEndTermValidity(String endTermValidity) {
        this.endTermValidity = endTermValidity;
    }

    public String getTermValidityStatus() {
        return termValidityStatus;
    }

    public void setTermValidityStatus(String termValidityStatus) {
        this.termValidityStatus = termValidityStatus;
    }

    public double getSatisfy() {
        return satisfy;
    }

    public void setSatisfy(double satisfy) {
        this.satisfy = satisfy;
    }

    public String getGetType() {
        return getType;
    }

    public void setGetType(String getType) {
        this.getType = getType;
    }

    public String getCouponStatus() {
        return couponStatus;
    }

    public void setCouponStatus(String couponStatus) {
        this.couponStatus = couponStatus;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
