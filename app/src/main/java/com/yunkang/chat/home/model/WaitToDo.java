package com.yunkang.chat.home.model;

import java.util.List;

public class WaitToDo {


    public static List<WaitChild> getWaitChildren() {
        return waitChildren;
    }

    public static void setWaitChildren(List<WaitChild> waitChildren) {
        WaitToDo.waitChildren = waitChildren;
    }

    static  List<WaitChild> waitChildren;
    public  static class WaitChild{

    }
}
