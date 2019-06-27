package com.yunkang.chat.home.model;

import java.util.List;

public class Record {

    public static List<ReCordChild> getReCordChildren() {
        return reCordChildren;
    }

    public static void setReCordChildren(List<ReCordChild> reCordChildren) {
        Record.reCordChildren = reCordChildren;
    }

    static  List<ReCordChild> reCordChildren;
    public  static class ReCordChild{

    }
}
