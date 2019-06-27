package com.yunkang.chat.start.model;

import java.io.Serializable;

/**
 * 作者：凌涛 on 2019/1/21 20:34
 * 邮箱：771548229@qq..com
 */
public class Account implements Serializable{
    String phone;
    String password;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
