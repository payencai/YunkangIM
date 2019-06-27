package com.yunkang.chat.home.Js;

import android.util.Log;
import android.webkit.JavascriptInterface;

/**
 * 作者：凌涛 on 2019/1/22 11:50
 * 邮箱：771548229@qq..com
 */
public class JsToNative {
    // 没有返回结果
    @JavascriptInterface
    public void jsMethod(String paramFromJS) {
        Log.e("result",paramFromJS);
    }

    // 有返回结果
    @JavascriptInterface
    public String jsMethodReturn(String paramFromJS) {

        return "your result";
    }
}

