package com.yunkang.chat.tools;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    public static void showToast(Context context,String toast){
        Toast.makeText(context,toast,Toast.LENGTH_SHORT).show();
    }
}
