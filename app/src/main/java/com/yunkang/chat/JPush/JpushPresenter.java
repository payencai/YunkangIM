package com.yunkang.chat.JPush;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.text.TextUtils;




import com.yunkang.chat.MyApplication;
import com.yunkang.chat.R;

import com.yunkang.chat.home.activity.MsgActivity;



import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

public class JpushPresenter implements JpushContract {
    private Context mContext;
    public static final String TAG="JpushPresenter";
    public JpushPresenter() {

    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    /**
     * 接收到自定义的消息，调用自定义的通知显示出来
     */

    @Override
    public void doProcessPushMessage(Bundle bundle) {
        showNotify(bundle);
    }

    /**
     * 发送通知
     * @param bundle
     */
    @Override
    public void doProcessPusNotify(Bundle bundle) {
        showNotify(bundle);
    }


    /**
     * 使用Jpush内置的样式构建通知
     */

    public void showNotify(Bundle bundle){

        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(mContext);
        builder.statusBarDrawable = R.mipmap.ic_logo;
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL
                | Notification.FLAG_SHOW_LIGHTS;  //设置为自动消失和呼吸灯闪烁
        builder.notificationDefaults = Notification.DEFAULT_SOUND
                | Notification.DEFAULT_VIBRATE
                | Notification.DEFAULT_LIGHTS;  // 设置为铃声、震动、呼吸灯闪烁都要
        JPushInterface.setPushNotificationBuilder(0, builder);
        // LogUtil.d(TAG,"=====doProcessPusNotify=======");
    }
    /**
     * 点击通知之后的操作在这里
     * @param bundle
     */
    @Override
    public void doOpenPusNotify(Bundle bundle) {
        if(!TextUtils.isEmpty(MyApplication.token)){
            Intent intent2=new Intent(mContext, MsgActivity.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent2);
        }

//            return;
//        }
//        try {
//            Intent intent=new Intent(mContext, H5CommomActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            JSONObject result=new JSONObject(msg);
//            if(result==null){
//                return;
//            }
//            JSONObject android=result.getJSONObject("android");
//            if(android==null){
//                return;
//            }
//            JSONObject extras=android.getJSONObject("extras");
//            if(extras==null){
//                return;
//            }
//            JSONObject data=extras.getJSONObject("Result");
//            if(data==null){
//                return;
//            }
//            Msg item= new Gson().fromJson(data.toString(),Msg.class);
//            switch (item.getPushNode()){
//                case "41":
//                    intent.putExtra("url", Constant.Commom.orderDetail+MyApplication.token+"&id="+item.getBelongId());
//                    //tv_title.setText("订单已付款");
//                    mContext.startActivity(intent);
//                case "33":
//                    intent.putExtra("url", Constant.Commom.newDetail+"type="+33);
//                    mContext.startActivity(intent);
//                    //startActivity(new Intent(MsgDetailActivity.this, H5CommomActivity.class).putExtra("url", Constant.Commom.newDetail));
//                    //认证详情
//                    break;
//                case "32":
//                    intent.putExtra("url", Constant.Commom.newDetail+"type="+32);
//                    mContext.startActivity(intent);
//                    //startActivity(new Intent(MsgDetailActivity.this, H5CommomActivity.class).putExtra("url", Constant.Commom.newDetail));
//                    //认证详情
//                    break;
//                case "31":
//                    intent.putExtra("url", Constant.Commom.cash+ MyApplication.token);
//                    mContext.startActivity(intent);
//                    //startActivity(new Intent(MsgDetailActivity.this, H5CommomActivity.class).putExtra("url", Constant.Commom.cash+ MyApplication.token));
//                    //tv_title.setText("提现反馈");
//                    break;
//                case "21":
//                    intent.putExtra("url", Constant.Commom.reportDetail+MyApplication.token+"&id="+item.getBelongId());
//                    mContext.startActivity(intent);
//                    //startActivity(new Intent(MsgDetailActivity.this, H5CommomActivity.class).putExtra("url", Constant.Commom.reportDetail+MyApplication.token+"&id="+item.getBelongId()));
//                    //tv_title.setText("报告提醒");
//                    break;
//                case "11":
//                    intent.putExtra("url", Constant.Commom.disDetail+MyApplication.token+"&id="+item.getBelongId());
//                    mContext.startActivity(intent);
//                    //startActivity(new Intent(MsgDetailActivity.this, H5CommomActivity.class).putExtra("url", Constant.Commom.disDetail+MyApplication.token+"&id="+item.getBelongId()));
//                    //tv_title.setText("评论回复");//评论详情
//                    break;
//                case "12":
//                    intent.putExtra("url", Constant.Commom.invinteList+MyApplication.token);
//                    mContext.startActivity(intent);
//                    // tv_title.setText("好友提醒");//邀请详情
//                    //startActivity(new Intent(MsgDetailActivity.this, H5CommomActivity.class).
//                    break;
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

    }

}
