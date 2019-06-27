package com.yunkang.chat.home.model;

import java.io.Serializable;

/**
 * 作者：凌涛 on 2019/1/22 14:31
 * 邮箱：771548229@qq..com
 */
public class HomeCategory implements Serializable{


    /**
     * id : 246354116960899072
     * name : 更换
     * icon : 548435543535
     * rank : 1
     * parentId : -1
     * delStatus : 0
     * createDate : 2019-01-14 10:34:17
     * createBy : 242489189247111168
     */

    private String id;
    private String name;
    private String icon;
    private int rank;
    private String parentId;
    private int delStatus;
    private String createDate;
    private String createBy;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public int getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(int delStatus) {
        this.delStatus = delStatus;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
}
