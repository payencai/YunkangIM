package com.yunkang.chat.JPush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.service.PushService;

public class JpushReceiver extends BroadcastReceiver{
    public static final String TAG="JpushReceiver";
    private JpushPresenter jpushPresenter = null;
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("rrr","rrr");
        this.mContext=context;
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            return;
        }
        if (jpushPresenter == null) {
            jpushPresenter = new JpushPresenter();
        }
        jpushPresenter.setContext(context);
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            // SDK 向 JPush Server 注册所得到的注册 全局唯一的 ID
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.e("regid",regId);
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.e(TAG, "-推送消息: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            jpushPresenter.doProcessPushMessage(bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.e(TAG, "-推送通知: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //LogUtil.e(TAG, "-推送通知: " + bundle.getString(JPushInterface.EXTRA_ALERT));
            jpushPresenter.doProcessPusNotify(bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.e(TAG, "-点击推送的通知: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            jpushPresenter.doOpenPusNotify(bundle);
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.e(TAG, "-点击推送的callback: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //mContext.startActivity(new Intent(mContext, NotifyActivity.class));
            //LogUtil.e(TAG,"-用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            //Log.e( "-" + intent.getAction() + " connected state change to " + connected);
        } else {
            //LogUtil.e(TAG, "-Unhandled intent - " + intent.getAction());
        }
    }
//
//    作者：奔跑的佩恩
//    链接：https://www.jianshu.com/p/1b1dd62b2d13
//    來源：简书
//    简书著作权归作者所有，任何形式的转载都请联系作者获得授权并注明出处。
}
