package com.yunkang.chat.study.model;

import fj.dropdownmenu.lib.pojo.DropdownItemObject;

/**
 * 作者：凌涛 on 2019/4/23 14:19
 * 邮箱：771548229@qq..com
 */
public class VideoLabel {

    /**
     * createBy :
     * createDate : 2019-04-23T01:33:01.446Z
     * delStatus : string
     * id :
     * num : 0
     * remark : string
     * updateBy :
     * updateDate : 2019-04-23T01:33:01.446Z
     * videoLabelName : string
     */

    private String createBy;
    private String createDate;
    private String delStatus;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private int num;
    private String remark;
    private String updateBy;
    private String updateDate;
    private String videoLabelName;

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

    public String getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(String delStatus) {
        this.delStatus = delStatus;
    }


    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getVideoLabelName() {
        return videoLabelName;
    }

    public void setVideoLabelName(String videoLabelName) {
        this.videoLabelName = videoLabelName;
    }
}
