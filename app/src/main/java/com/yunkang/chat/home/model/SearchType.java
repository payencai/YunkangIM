package com.yunkang.chat.home.model;

import java.util.List;

/**
 * 作者：凌涛 on 2019/1/24 11:35
 * 邮箱：771548229@qq..com
 */
public class SearchType {
    String name;
    List<String> tagList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }
}
