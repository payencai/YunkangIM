package com.yunkang.chat.study.model;

import java.io.Serializable;

/**
 * 作者：凌涛 on 2019/4/22 11:18
 * 邮箱：771548229@qq..com
 */
public class VideoComment implements Serializable {

    /**
     * id : 282686829897224192
     * videoId : 282298377609973760
     * custId : 269189962954723328
     * comment : 买房买车吗
     * parent : 282681338253053952
     * typeStatus : 1
     * comments : 0
     * delStatus : 0
     * createBy : 269189962954723328
     * createDate : 2019-04-24 16:47:31
     * nickName : 嗷嗷嗷
     * heading : http://jzylapp.oss-cn-hangzhou.aliyuncs.com/busImage/2019/03/28/272921905377202176?Expires=4707367512&OSSAccessKeyId=LTAIXDLmmnPp2xUJ&Signature=9YnFGSjkRVpFg8bc80Cy%2F1GI%2Fys%3D
     */

    private String id;
    private String videoId;
    private String custId;
    private String comment;
    private String parent;
    private String typeStatus;
    private String comments;
    private String delStatus;
    private String createBy;
    private String createDate;
    private String nickName;
    private String heading;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getTypeStatus() {
        return typeStatus;
    }

    public void setTypeStatus(String typeStatus) {
        this.typeStatus = typeStatus;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }
}
