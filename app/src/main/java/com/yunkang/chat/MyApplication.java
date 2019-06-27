package com.yunkang.chat;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.ToastUtils;
import com.easefun.polyv.businesssdk.vodplayer.PolyvVodSDKClient;
import com.easefun.polyv.cloudclass.config.PolyvLiveSDKClient;
import com.easefun.polyv.foundationsdk.log.PolyvCommonLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.lzy.okgo.OkGo;
import com.qiyukf.unicorn.api.StatusBarNotificationConfig;
import com.qiyukf.unicorn.api.UICustomization;
import com.qiyukf.unicorn.api.Unicorn;
import com.qiyukf.unicorn.api.YSFOptions;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.xiasuhuei321.loadingdialog.manager.StyleManager;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;
import com.yunkang.chat.http.HttpProxy;
import com.yunkang.chat.http.processor.OkHttpProcessor;
import com.yunkang.chat.net.NetUtils;
import com.yunkang.chat.start.model.UserInfo;
import com.yunkang.chat.tools.CrashHandler;
import com.yunkang.chat.tools.FrescoImageLoader;
import com.yunkang.chat.tools.RCrashHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import okhttp3.OkHttpClient;

/**
 * 作者：凌涛 on 2019/1/18 10:36
 * 邮箱：771548229@qq..com
 */
public class MyApplication extends MultiDexApplication {
    public  static Context mContext;
    public  static UserInfo sUserInfo;
    public static boolean isDebug = false;//改为false
    public static int isPt=0;
    public static String token;
    public static String splash;
    public static int  count;
    public static String loginResult;
    public static IWXAPI mWxApi;
    public static final String WX_APP_ID="wxb8e6775a201f6a1f";//微信appid
    public static final String WX_APP_SECRET="5a622dee340b60f8d9bb93ba9f39c3f7";//微信appsecret
    private static final String TAG = "PolyvCloudClassApp";
//加密秘钥和加密向量，在点播后台->设置->API接口中获取，用于解密SDK加密串
    //值修改请参考https://github.com/easefun/polyv-android-sdk-demo/wiki/10.%E5%85%B3%E4%BA%8E-SDK%E5%8A%A0%E5%AF%86%E4%B8%B2-%E4%B8%8E-%E7%94%A8%E6%88%B7%E9%85%8D%E7%BD%AE%E4%BF%A1%E6%81%AF%E5%8A%A0%E5%AF%86%E4%BC%A0%E8%BE%93
    /** 加密秘钥 */
    private String aeskey = "VXtlHmwfS2oYm0CZ";
    /** 加密向量 */
    private String iv = "2u9gDPKdX6GyQJKU";
    /** SDK加密串，可以在点播后台获取 */
    private String config = "";

    public static UserInfo getUserInfo() {
        return sUserInfo;
    }

    public static void setUserInfo(UserInfo userInfo) {
        sUserInfo = userInfo;
    }
    private void registerToWX() {
        //第二个参数是指你应用在微信开放平台上的AppID
        mWxApi = WXAPIFactory.createWXAPI(this, WX_APP_ID, false);
        // 将该app注册到微信
        mWxApi.registerApp(WX_APP_ID);

    }
    /**
     * 判断是够有SD卡
     *
     * @return
     */
    public static boolean hasSDCard() {
        String status = Environment.getExternalStorageState();
        if (!status.equals(Environment.MEDIA_MOUNTED)) {
            return false;
        }
        return true;
    }
    private void initLiveVideo(){
        PolyvCommonLog.setDebug(true);
        PolyvLiveSDKClient liveSDKClient = PolyvLiveSDKClient.getInstance();
        liveSDKClient.initContext(this);

        PolyvVodSDKClient client = PolyvVodSDKClient.getInstance();
        //使用SDK加密串来配置
        client.setConfig(config, aeskey, iv);
    }
    /**
     * 获取根目录
     *
     * @return
     */
    public static String getRootFilePath() {
        if (hasSDCard()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath() + "/";// filePath:/sdcard/
        } else {
            return Environment.getDataDirectory().getAbsolutePath() + "/data/"; // filePath: /data/data/
        }
    }
    public interface DIR {

        String CRASH = getRootFilePath() + "EasySport/crashLog";

    }
//    private void initChat(Context context) {
//        EMOptions options = new EMOptions();
//// 默认添加好友时，是不需要验证的，改成需要验证
//        options.setAcceptInvitationAlways(false);
//
//        options.isAutoAcceptGroupInvitation();
//        options.setAppKey("1121190511097928#huafeng");
//// 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载，如果设为 false，需要开发者自己处理附件消息的上传和下载
//        options.setAutoTransferMessageAttachments(true);
//// 是否自动下载附件类消息的缩略图等，默认为 true 这里和上边这个参数相关联
//        options.setAutoDownloadThumbnail(true);
//        //options.setAutoLogin(false);
////初始化
//        EaseUI.getInstance().init(context,  options);
//        EMClient.getInstance().init(context,options);
////在做打包混淆时，关闭debug模式，避免消耗不必要的资源
//        EMClient.getInstance().setDebugMode(true);
//        //注册一个监听连接状态的listener
//
//        EMClient.getInstance().addConnectionListener(new EMConnectionListener() {
//            @Override
//            public void onConnected() {
//
//            }
//
//            @Override
//            public void onDisconnected(int error) {
//                if(error == EMError.USER_REMOVED){
//                    // 显示帐号已经被移除
//                    ToastUtils.showLong("你的账号被移除");
//                    onConnectionConflict();
//                }else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
//                    // 显示帐号在其他设备登陆
//                    ToastUtils.showLong("你的账号已经在别处登录");
//                    onConnectionConflict();
//                } else {
//
//                }
//
//            }
//        });
//    }
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        CrashReport.initCrashReport(getApplicationContext(), "f72d413b2b", true);
        JPushInterface.init(this);
        Fresco.initialize(this);
        registerToWX();

        //NetUtils.getInstance();
        NetUtils.getInstance().initNetWorkUtils(this);

        JPushInterface.setDebugMode(true);
        PlatformConfig.setWeixin(WX_APP_ID, WX_APP_SECRET);
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
        UMShareAPI.get(this);
        UMConfigure.init(this,"5c7c9739203657f795000989"
                ,"umeng",UMConfigure.DEVICE_TYPE_PHONE,"");
        HttpProxy.init(new OkHttpProcessor());
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        initLiveVideo();
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);

        boolean isSuccess=Unicorn.init(this, "432b3a81366f2191e520c4a87d1b78d0", options(), new FrescoImageLoader(this));
        if(isSuccess){
            Log.e("success","true");
        }else{
            Log.e("success","false");
        }
        initOkGo();
        initDialog();

    }
    private void initDialog(){
        StyleManager s = new StyleManager();


        s.Anim(false).repeatTime(0).contentSize(-1).intercept(true);

        LoadingDialog.initStyle(s);
    }
    private void initOkGo(){
        //必须调用初始化


        //以下设置的所有参数是全局参数,同样的参数可以在请求的时候再设置一遍,那么对于该请求来讲,请求中的参数会覆盖全局参数
        //好处是全局参数统一,特定请求可以特别定制参数
        try {
            OkGo.getInstance().init(this);
            OkHttpClient okHttpClient=new OkHttpClient();
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.readTimeout(3000, TimeUnit.MILLISECONDS);
//全局的写入超时时间
            builder.writeTimeout(3000, TimeUnit.MILLISECONDS);
//全局的连接超时时间
            builder.connectTimeout(3000, TimeUnit.MILLISECONDS);
            OkGo.getInstance().setOkHttpClient(okHttpClient).setRetryCount(1);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        MultiDex.install(base);
//    }
    private YSFOptions options() {
        UICustomization uiCustomization=new UICustomization();
        uiCustomization.titleCenter=true;

        YSFOptions options = new YSFOptions();
        options.uiCustomization=uiCustomization;
        options.statusBarNotificationConfig = new StatusBarNotificationConfig();
        return options;
    }
    public static Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        mContext = context;
    }
    public static boolean inMainProcess(Context context) {
        String mainProcessName = context.getApplicationInfo().processName;
        String processName = getProcessName();
        return TextUtils.equals(mainProcessName, processName);
    }
    /**
     * 设置点击Notification消息后进入的页面
     * @param activity
     */

    /**
     * 获取当前进程名
     */
    public static String getProcessName() {
        BufferedReader reader = null;
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            reader = new BufferedReader(new FileReader(file));
            return reader.readLine().trim();
        } catch (IOException e) {
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
