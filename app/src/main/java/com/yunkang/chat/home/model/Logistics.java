package com.yunkang.chat.home.model;

import java.util.List;

/**
 * 作者：凌涛 on 2019/1/21 11:48
 * 邮箱：771548229@qq..com
 */
public class Logistics {
    public static List<Child> getChildList() {
        return sChildList;
    }

    public static void setChildList(List<Child> childList) {
        sChildList = childList;
    }

    static List<Child> sChildList;
    public  static class Child{

    }
}
