package com.yunkang.chat.study.model;

/**
 * 作者：凌涛 on 2019/5/6 21:17
 * 邮箱：771548229@qq..com
 */
public class RecordVideo {


    /**
     * channelId : 307758
     * url : http://oss-live-1.videocc.net/record/record/recordf/b67d34962120190408115457751/2019-05-06-11-39-32_2019-05-06-14-06-39.mp4
     * startTime : 20190506113932
     * endTime : 20190506140544
     * fileSize : 157202716
     * duration : 8772
     * bitrate : 0
     * resolution : 1280x720
     * channelSessionId : fby9tco8en
     */
    private  String photo;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    private int channelId;
    private String url;
    private String startTime;
    private String endTime;
    private String fileSize;
    private int duration;
    private String bitrate;
    private String resolution;
    private String channelSessionId;

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getBitrate() {
        return bitrate;
    }

    public void setBitrate(String bitrate) {
        this.bitrate = bitrate;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getChannelSessionId() {
        return channelSessionId;
    }

    public void setChannelSessionId(String channelSessionId) {
        this.channelSessionId = channelSessionId;
    }
}
