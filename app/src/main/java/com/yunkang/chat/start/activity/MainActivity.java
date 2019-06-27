package com.yunkang.chat.start.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.azhon.appupdate.config.UpdateConfiguration;
import com.azhon.appupdate.listener.OnButtonClickListener;
import com.azhon.appupdate.listener.OnDownloadListener;
import com.azhon.appupdate.manager.DownloadManager;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.umeng.commonsdk.debug.I;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;
import com.yunkang.chat.JPush.JpushConfig;
import com.yunkang.chat.MyApplication;
import com.yunkang.chat.R;
import com.yunkang.chat.api.Api;
import com.yunkang.chat.constant.Constant;
import com.yunkang.chat.h5.H5CommomActivity;
import com.yunkang.chat.home.activity.AlwaysOpenActivity;
import com.yunkang.chat.http.HttpProxy;
import com.yunkang.chat.http.ICallBack;
import com.yunkang.chat.mine.activity.MyOrderActivity;
import com.yunkang.chat.mine.activity.UserInfoActivity;
import com.yunkang.chat.net.NetUtils;
import com.yunkang.chat.net.OnMessageReceived;
import com.yunkang.chat.start.adapter.YouhuiAdapter;
import com.yunkang.chat.start.fragment.CategoryFragment;
import com.yunkang.chat.start.fragment.HomeFragment;
import com.yunkang.chat.start.fragment.MineFragment;
import com.yunkang.chat.start.fragment.NewHomeFragment;
import com.yunkang.chat.start.fragment.StudyFragment;
import com.yunkang.chat.start.model.Coupon;
import com.yunkang.chat.start.model.UserInfo;
import com.yunkang.chat.start.model.Version;
import com.yunkang.chat.tools.AppManager;
import com.yunkang.chat.tools.MyGlideEngine;
import com.yunkang.chat.tools.ShareUtils;
import com.yunkang.chat.tools.ToastUtil;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.iv_tab_home)
    ImageView iv_tab_home;
    @BindView(R.id.fr_container)
    FrameLayout fr_container;
    @BindView(R.id.iv_tab_category)
    ImageView iv_tab_category;
    @BindView(R.id.iv_tab_study)
    ImageView iv_tab_study;
    @BindView(R.id.iv_tab_mine)
    ImageView iv_tab_mine;
    @BindView(R.id.tv_tab_home)
    TextView tv_tab_home;
    @BindView(R.id.tv_tab_category)
    TextView tv_tab_category;
    @BindView(R.id.tv_tab_study)
    TextView tv_tab_study;
    @BindView(R.id.tv_tab_mine)
    TextView tv_tab_mine;
    @BindView(R.id.ll_tab_home)
    LinearLayout ll_tab_home;
    @BindView(R.id.ll_tab_category)
    LinearLayout ll_tab_category;
    @BindView(R.id.ll_tab_study)
    LinearLayout ll_tab_study;
    @BindView(R.id.ll_tab_mine)
    LinearLayout ll_tab_mine;
    @BindView(R.id.iv_add)
    ImageView iv_add;
    DownloadManager manager;
    FragmentManager fm;
    List<Fragment> fragments;
    HomeFragment fragment1;
    CategoryFragment fragment2;
    StudyFragment fragment3;
    MineFragment fragment4;
    int type = 0;
    private static boolean isExit = false;  // 标识是否退出
    Version version=new Version();
    private static Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };
    @Override
    public void onBackPressed() {
        exit();
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(this, "再按一次后退键退出程序", Toast.LENGTH_SHORT).show();
            mHandler.sendEmptyMessageDelayed(0, 2000);  // 利用handler延迟发送更改状态信息
        } else {
            this.finish();
        }
    }

    private void startUpdate3(Version version) {
        /*
         * 整个库允许配置的内容
         * 非必选
         */
        if(TextUtils.isEmpty(version.getRemarks())||!version.getRemarks().contains("apk")){
            return;
            //version.setRemarks("https://raw.githubusercontent.com/azhon/AppUpdate/master/apk/appupdate.apk");
        }
        UpdateConfiguration configuration = new UpdateConfiguration()
                //输出错误日志
                .setEnableLog(true)
                //设置自定义的下载
                //.setHttpManager()
                //下载完成自动跳动安装页面
                .setJumpInstallPage(true)
                //设置对话框背景图片 (图片规范参照demo中的示例图)
                //.setDialogImage(R.drawable.ic_dialog)
                //设置按钮的颜色
                //.setDialogButtonColor(Color.parseColor("#E743DA"))
                //设置按钮的文字颜色
                .setDialogButtonTextColor(Color.WHITE)
                //支持断点下载
                .setBreakpointDownload(true)
                //设置是否显示通知栏进度
                .setShowNotification(true)
                //设置是否提示后台下载toast
                .setShowBgdToast(false)
                //设置强制更新
                .setForcedUpgrade(false)
                //设置对话框按钮的点击监听
                .setButtonClickListener(new OnButtonClickListener() {
                    @Override
                    public void onButtonClick(int id) {
                        Log.e("TAG", String.valueOf(id));
                    }
                })
                //设置下载过程的监听
                .setOnDownloadListener(new OnDownloadListener() {
                    @Override
                    public void start() {

                    }

                    @Override
                    public void downloading(int max, int progress) {

                    }

                    @Override
                    public void done(File apk) {

                    }

                    @Override
                    public void cancel() {

                    }

                    @Override
                    public void error(Exception e) {

                    }
                });

        manager = DownloadManager.getInstance(this);
        manager.setApkName("miaoyizhai.apk")
                .setApkUrl(version.getRemarks())
                .setSmallIcon(R.mipmap.ic_logo)
                .setShowNewerToast(true)
                .setAuthorities("com.yunkang.miaoyizhai")
                .setConfiguration(configuration)
//                .setDownloadPath(Environment.getExternalStorageDirectory() + "/AppUpdate")
                .setApkVersionCode(2)
                .setApkVersionName(version.getParkey())
//                .setApkSize("11.2")
                .setApkDescription(version.getParvalue())
                .download();
    }
    public static int compareAppVersion(String newVersion, String oldVersion) {
        if (newVersion == null || oldVersion == null) {
            throw new RuntimeException("版本号不能为空");
        }
        // 注意此处为正则匹配，不能用.
        String[] versionArray1 = newVersion.split("\\.");
        String[] versionArray2 = oldVersion.split("\\.");
        int idx = 0;
        // 取数组最小长度值
        int minLength = Math.min(versionArray1.length, versionArray2.length);
        int diff = 0;
        // 先比较长度，再比较字符
        while (idx < minLength
                && (diff = versionArray1[idx].length() - versionArray2[idx].length()) == 0
                && (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0) {
            ++idx;
        }
        // 如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大
        diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;
        return diff;
    }
    private  void getNewVersion(){
//        Map<String,Object> params=new HashMap<>();
//        params.put("type","2");
//        HttpProxy.obtain().get(Api.Veision.getVersion, params, new ICallBack() {
//            @Override
//            public void OnSuccess(String result) {
//                Log.e("version",result);
//                try {
//                    JSONObject jsonObject=new JSONObject(result);
//                    jsonObject=jsonObject.getJSONObject("data");
//                    JSONArray data=jsonObject.getJSONArray("data");
//
//                    version.setParkey("1.0");
//                    for (int i = 0; i <data.length() ; i++) {
//                        if(i==0){
//                            JSONObject item=data.getJSONObject(i);
//                            version =new Gson().fromJson(item.toString(),Version.class);
//                            break;
//                        }
//                    }
//                    if(compareAppVersion(version.getParkey(), AppUtils.getAppVersionName())>0){
//                        startUpdate3(version);
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//            @Override
//            public void onFailure(String error) {
//
//            }
//        });

        Map<String,String> params=new HashMap<>();
        params.put("type","2");
        NetUtils.getInstance().get(MyApplication.token, Api.Veision.getVersion, params, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                Log.e("version",response);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    jsonObject=jsonObject.getJSONObject("data");
                    JSONArray data=jsonObject.getJSONArray("data");

                    version.setParkey("1.0");
                    for (int i = 0; i <data.length() ; i++) {
                        if(i==0){
                            JSONObject item=data.getJSONObject(i);
                            version =new Gson().fromJson(item.toString(),Version.class);
                            break;
                        }
                    }
                    if(compareAppVersion(version.getParkey(), AppUtils.getAppVersionName())>0){
                        startUpdate3(version);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if(MyApplication.getUserInfo()!=null){
            JpushConfig.getInstance().setAlias(MyApplication.getUserInfo().getUsername());
            JpushConfig.getInstance().setTag(MyApplication.getUserInfo().getUsername());
        }
        AppManager.getInstance().addActivity(this);
        // StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary));
        // StatusBarUtil.setTransparent(this);
        // StatusBarUtil.setTransparentForImageView(this,fr_container);
        RxPermissions rxPermissions = new RxPermissions(MainActivity.this);
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_SETTINGS, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        initView();

        showYouhui();

        getNewVersion();
        //getJifen();
    }


    private void showYouhui() {
        if(TextUtils.isEmpty(MyApplication.loginResult)){
            return;
        }
        List<Coupon> coupons=new ArrayList<>();
        try {
            JSONObject jsonObject=new JSONObject(MyApplication.loginResult);
            jsonObject=jsonObject.getJSONObject("data");
            JSONObject data=jsonObject.getJSONObject("data");
            JSONArray jsonArray=data.getJSONArray("couponDetails");
            if(jsonArray!=null){
                for (int i = 0; i <jsonArray.length() ; i++) {
                    JSONObject item=jsonArray.getJSONObject(i);
                    Coupon coupon=new Gson().fromJson(item.toString(),Coupon.class);
                    coupons.add(coupon);
                }
                Dialog dialog = new Dialog(this, R.style.common_dialog_style);
                View view = LayoutInflater.from(this).inflate(R.layout.dialog_youhui, null);
                TextView tv_confirm = view.findViewById(R.id.tv_confirm);
                tv_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                ListView listView = view.findViewById(R.id.lv_youhui);
                YouhuiAdapter youhuiAdapter = new YouhuiAdapter(this, coupons);
                listView.setAdapter(youhuiAdapter);
                dialog.setContentView(view);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                Window window = dialog.getWindow();
                WindowManager windowManager = getWindowManager();
                Display display = windowManager.getDefaultDisplay();
                window.setGravity(Gravity.CENTER);
                WindowManager.LayoutParams layoutParams = window.getAttributes();
                layoutParams.width = (int) (display.getWidth() * 0.8);
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                window.setAttributes(layoutParams);
            }else{
                return;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    private static final String TAG_FAKE_STATUS_BAR_VIEW = "statusBarView";
    private static final String TAG_MARGIN_ADDED = "marginAdded";


    private void initView() {
        ll_tab_home.setOnClickListener(this);
        ll_tab_category.setOnClickListener(this);
        ll_tab_study.setOnClickListener(this);
        ll_tab_mine.setOnClickListener(this);
        iv_add.setOnClickListener(this);
        fm = getSupportFragmentManager();
        fragment1 = new HomeFragment();
        fragment2 = new CategoryFragment();
        fragment3 = new StudyFragment();
        fragment4 = new MineFragment();
        fragments = new ArrayList<>();
        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);
        fragments.add(fragment4);
        for (Fragment fragment : fragments) {
            fm.beginTransaction().add(R.id.fr_container, fragment).commit();
        }
        hideAllFragment();
        showFragment(0);
    }

    private void turnoff() {
//        HttpProxy.obtain().post(Api.Product.turnOff, MyApplication.token, "", new ICallBack() {
//            @Override
//            public void OnSuccess(String result) {
//                Log.e("turnOff", result);
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    JSONObject dataobject = jsonObject.getJSONObject("data");
//                    int code = dataobject.getInt("resultCode");
//                    if (code == 0) {
//                        iv_switch.setImageResource(R.mipmap.ic_close);
//                        MyApplication.isPt = 0;
//                        type = 0;
//                        ToastUtil.showToast(MainActivity.this, "已关闭");
//                        if(fragment1!=null)
//                           fragment1.refresh();
//                    } else {
//                        String message = jsonObject.getString("message");
//                        ToastUtil.showToast(MainActivity.this, message);
//                    }
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(String error) {
//
//            }
//        });

        NetUtils.getInstance().post(MyApplication.token, Api.Product.turnOff, new HashMap<>(), new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                Log.e("turnOff", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject dataobject = jsonObject.getJSONObject("data");
                    int code = dataobject.getInt("resultCode");
                    if (code == 0) {
                        iv_switch.setImageResource(R.mipmap.ic_close);
                        MyApplication.isPt = 0;
                        type = 0;
                        ToastUtil.showToast(MainActivity.this, "已关闭");
                        if(fragment1!=null)
                            fragment1.refreshAll();
                    } else {
                        String message = jsonObject.getString("message");
                        ToastUtil.showToast(MainActivity.this, message);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {

            }
        });

    }

    private void turnon() {


        NetUtils.getInstance().post(MyApplication.token, Api.Product.turnON, new HashMap<>(), new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                Log.e("turnON", response);
                if(response.contains("data")){
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject dataobject = jsonObject.getJSONObject("data");
                        int code = dataobject.getInt("resultCode");
                        if (code == 0) {
                            iv_switch.setImageResource(R.mipmap.ic_open);
                            MyApplication.isPt = 1;
                            type = 1;
                            ToastUtil.showToast(MainActivity.this, "已打开");
                            if(fragment1!=null)
                                fragment1.refreshAll();
                        } else {
                            String message = jsonObject.getString("message");
                            ToastUtil.showToast(MainActivity.this, message);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String msg = jsonObject.getString("message");
                        ToastUtil.showToast(MainActivity.this, msg);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(String error) {

            }
        });

    }


    private void getStatus(ImageView iv_switch) {
//        HttpProxy.obtain().get(Api.Product.getStatus, MyApplication.token, new ICallBack() {
//            @Override
//            public void OnSuccess(String result) {
//                Log.e("getStatus", result);
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    JSONObject dataobject = jsonObject.getJSONObject("data");
//                    int code = jsonObject.getInt("code");
//                    if (code == 0) {
//                        type = dataobject.getInt("data");
//                        if (type == 1) {
//                            iv_switch.setImageResource(R.mipmap.ic_open);
//                        } else {
//                            iv_switch.setImageResource(R.mipmap.ic_close);
//                        }
//                    } else {
//
//                    }
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(String error) {
//
//            }
//        });

        NetUtils.getInstance().get(MyApplication.token, Api.Product.getStatus, new HashMap<>(), new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                Log.e("getStatus", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject dataobject = jsonObject.getJSONObject("data");
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        type = dataobject.getInt("data");
                        if (type == 1) {
                            iv_switch.setImageResource(R.mipmap.ic_open);
                        } else {
                            iv_switch.setImageResource(R.mipmap.ic_close);
                        }
                    } else {

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {

            }
        });

    }

    ImageView iv_switch;

    private void showBottomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_home,
                null);
        iv_switch = view.findViewById(R.id.iv_switch);

        RelativeLayout rl_always = view.findViewById(R.id.rl_always);
        RelativeLayout rl_opentickets = view.findViewById(R.id.rl_opentickets);

        RelativeLayout rl_switch = view.findViewById(R.id.rl_switch);
        RelativeLayout rl_order = view.findViewById(R.id.rl_order);

        rl_always.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AlwaysOpenActivity.class));
            }
        });
        rl_opentickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this, H5CommomActivity.class);
                intent2.putExtra("url", Constant.Commom.invoince + MyApplication.token);
                startActivity(intent2);
            }
        });

        rl_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == 1) {
                    turnoff();
                } else {
                    turnon();
                }
                // startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });
        rl_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MyOrderActivity.class));
            }
        });

        builder.setView(view);
        AlertDialog titleDialog = builder.create();
        Window window = titleDialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.y = 150;
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        titleDialog.show();
        window.setGravity(Gravity.BOTTOM);
        getStatus(iv_switch);
    }
    public void refresh(){
        if(fragment1!=null)
          fragment1.refreshAll();
    }

    private void clearTagbarState() {
        tv_tab_home.setTextColor(ContextCompat.getColor(this, R.color.tab_text_unselect));
        iv_tab_home.setImageResource(R.mipmap.tab_home_unselect);
        tv_tab_category.setTextColor(ContextCompat.getColor(this, R.color.tab_text_unselect));
        iv_tab_category.setImageResource(R.mipmap.tab_category_unselect);
        tv_tab_study.setTextColor(ContextCompat.getColor(this, R.color.tab_text_unselect));
        iv_tab_study.setImageResource(R.mipmap.tab_study_unselect);
        tv_tab_mine.setTextColor(ContextCompat.getColor(this, R.color.tab_text_unselect));
        iv_tab_mine.setImageResource(R.mipmap.tab_mime_unselect);
    }

    private void hideAllFragment() {
        for (Fragment fragment : fragments) {
            fm.beginTransaction().hide(fragment).commit();
        }
    }

    private void showFragment(int position) {
        fm.beginTransaction().show(fragments.get(position)).commit();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_tab_home:
                clearTagbarState();
                tv_tab_home.setTextColor(ContextCompat.getColor(this, R.color.tab_text_select));
                iv_tab_home.setImageResource(R.mipmap.tab_home_select);
                hideAllFragment();
                showFragment(0);
                break;
            case R.id.ll_tab_category:
                clearTagbarState();
                tv_tab_category.setTextColor(ContextCompat.getColor(this, R.color.tab_text_select));
                iv_tab_category.setImageResource(R.mipmap.tab_category_select);
                hideAllFragment();
                showFragment(1);
                break;
            case R.id.ll_tab_study:
                clearTagbarState();
                tv_tab_study.setTextColor(ContextCompat.getColor(this, R.color.tab_text_select));
                iv_tab_study.setImageResource(R.mipmap.tab_study_select);
                hideAllFragment();
                showFragment(2);
                break;
            case R.id.ll_tab_mine:
                clearTagbarState();
                tv_tab_mine.setTextColor(ContextCompat.getColor(this, R.color.tab_text_select));
                iv_tab_mine.setImageResource(R.mipmap.tab_mime_select);
                hideAllFragment();
                showFragment(3);
                break;
            case R.id.iv_add:
                showBottomDialog();
                //openShare("测试分享","http://www.baidu.com","哈哈哈哈");
                break;
        }
    }

    //为fragment添加事件分发


    private ArrayList<FragmentTouchListener> mFragmentTouchListeners = new ArrayList<>();


    public void registerFragmentTouchListener(FragmentTouchListener listener) {
        mFragmentTouchListeners.add(listener);
    }


    public void unRegisterFragmentTouchListener(FragmentTouchListener listener) {
        mFragmentTouchListeners.remove(listener);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        for (FragmentTouchListener listener : mFragmentTouchListeners) {
            listener.onTouchEvent(event);
        }

        return super.dispatchTouchEvent(event);
    }

    public interface FragmentTouchListener {

        boolean onTouchEvent(MotionEvent event);
    }

}
