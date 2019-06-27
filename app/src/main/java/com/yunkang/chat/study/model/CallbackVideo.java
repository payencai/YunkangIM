package com.yunkang.chat.study.model;

import java.io.Serializable;
import java.util.Set;

/**
 * 作者：凌涛 on 2019/4/23 14:35
 * 邮箱：771548229@qq..com
 */
public class CallbackVideo implements Serializable {


    /**
     * id : 282298377609973760
     * videoTittle : 动画视频
     * videoUrl : http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4
     * videoPhoto : http://d.5857.com/wmmtf_140507/001.jpg
     * videoLabel : 282288261620731904
     * videoDescription : 哈哈哈哈哈
     * useStatus : 1
     * delStatus : 0
     * type : 2
     * createBy : 242382893168611328
     * createDate : 2019-04-23 15:03:56
     * comments : 5
     */
    private int playNum;

    public int getPlayNum() {
        return playNum;
    }

    public void setPlayNum(int playNum) {
        this.playNum = playNum;
    }

    private String id;
    private String videoTittle;
    private String videoUrl;
    private String videoPhoto;
    private String videoLabel;
    private String videoDescription;
    private String useStatus;
    private String delStatus;
    private String type;
    private String createBy;
    private String createDate;
    private String comments;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVideoTittle() {
        return videoTittle;
    }

    public void setVideoTittle(String videoTittle) {
        this.videoTittle = videoTittle;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoPhoto() {
        return videoPhoto;
    }

    public void setVideoPhoto(String videoPhoto) {
        this.videoPhoto = videoPhoto;
    }

    public String getVideoLabel() {
        return videoLabel;
    }

    public void setVideoLabel(String videoLabel) {
        this.videoLabel = videoLabel;
    }

    public String getVideoDescription() {
        return videoDescription;
    }

    public void setVideoDescription(String videoDescription) {
        this.videoDescription = videoDescription;
    }

    public String getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(String useStatus) {
        this.useStatus = useStatus;
    }

    public String getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(String delStatus) {
        this.delStatus = delStatus;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
