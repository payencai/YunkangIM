package com.yunkang.chat.start.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：凌涛 on 2019/1/21 20:08
 * 邮箱：771548229@qq..com
 */
public class UserInfo implements Serializable{



    private double integral;
    private String id;
    private String custId;
    private String userId;
    private String type;
    private String nickName;
    private String username;
    private String telephone;
    private String cerType;
    private String realNameStatus;
    private String banStatus;
    private String newStatus;
    private String delStatus;
    private String createBy;
    private String createDate;
    private String token;
    private String headPortrait;
    private String zfbAccount;
    private String name;
    private double balance;
    private String updateBy;
    private String updateDate;
    private int age;
    private List<CouponDetails> mCouponDetails;


    public static class CouponDetails implements Serializable{


        /**
         * id : 267834999913963520
         * custId : 267834990266699777
         * couponId : 265253085352943616
         * couponName : 测试一下优惠券
         * couponPrice : 100.0
         * startTermValidity : 2019-03-01 00:00:00
         * endTermValidity : 2019-04-30 00:00:00
         * termValidityStatus : 1
         * satisfy : 0.0
         * getType : 新用户券
         * couponStatus : 0
         * createBy : 267834990266699777
         * createDate : 2019-03-14 17:11:39
         * nickName : 新用户132029
         */
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
        public int getRegularTime() {
            return regularTime;
        }

        public void setRegularTime(int regularTime) {
            this.regularTime = regularTime;
        }
    }


    public List<CouponDetails> getCouponDetails() {
        return mCouponDetails;
    }

    public void setCouponDetails(List<CouponDetails> couponDetails) {
        mCouponDetails = couponDetails;
    }

    public double getIntegral() {
        return integral;
    }

    public void setIntegral(double integral) {
        this.integral = integral;
    }
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    private boolean gender;
    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public String getZfbAccount() {
        return zfbAccount;
    }

    public void setZfbAccount(String zfbAccount) {
        this.zfbAccount = zfbAccount;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCerType() {
        return cerType;
    }

    public void setCerType(String cerType) {
        this.cerType = cerType;
    }

    public String getRealNameStatus() {
        return realNameStatus;
    }

    public void setRealNameStatus(String realNameStatus) {
        this.realNameStatus = realNameStatus;
    }

    public String getBanStatus() {
        return banStatus;
    }

    public void setBanStatus(String banStatus) {
        this.banStatus = banStatus;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }

    public String getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(String delStatus) {
        this.delStatus = delStatus;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
