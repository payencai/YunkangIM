package com.yunkang.chat.start.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;


import com.yunkang.chat.MyApplication;
import com.yunkang.chat.R;
import com.yunkang.chat.api.Api;
import com.yunkang.chat.http.HttpProxy;
import com.yunkang.chat.http.ICallBack;
import com.yunkang.chat.net.NetUtils;
import com.yunkang.chat.net.OnMessageReceived;
import com.yunkang.chat.start.model.ContentImage;
import com.yunkang.chat.start.model.UserInfo;
import com.yunkang.chat.tools.CheckDoubleClick;
import com.yunkang.chat.tools.SharePreferenceUtils;
import com.yunkang.chat.tools.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;

public class WecomeActivity extends AppCompatActivity {
    //    @BindView(R.id.iv_wel)
//    ImageView iv_wel;
    int count = 3;
    CountDownTimer mCountDownTimer;
    List<ContentImage> mContentImages = new ArrayList<>();
    ;

    TextView tv_time;
    ImageView iv_logo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("FirstRun", 0);
        Boolean first_run = sharedPreferences.getBoolean("First", true);
        if (first_run) {
            sharedPreferences.edit().putBoolean("First", false).commit();
            try {
                Thread.sleep(2000);
                startActivity(new Intent(WecomeActivity.this, GuideActivity.class));
                finish();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {

            setContentView(R.layout.activity_splash);
            iv_logo = findViewById(R.id.iv_logo);
            tv_time = findViewById(R.id.tv_time);
            tv_time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (CheckDoubleClick.isFastDoubleClick()) {
                        return;
                    }
                    if(mCountDownTimer!=null){
                        mCountDownTimer.cancel();
                    }
                    tv_time.setEnabled(false);
                    startSwitch();
                }
            });
            getImage();
        }


    }

    private void startJump() {
        Timer mTimer = new Timer();
        TimerTask mTimerTask = new TimerTask() {//创建一个线程来执行run方法中的代码
            @Override
            public void run() {

                //要执行的代码
                if (!isSuccess) {
                    isJump = false;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showDefaultPic();
                        }
                    });
                }

            }
        };
        mTimer.schedule(mTimerTask, 3000);//延迟3秒执行
    }

    private void timer(int time) {
        mCountDownTimer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tv_time.setText("跳过(" + count + "s)");
                count--;
            }

            @Override
            public void onFinish() {
                tv_time.setText("跳过(" + count + "s)");
                count = 3;
                startSwitch();
                //倒计时结束时回调该函数
            }
        }.start();
    }

    boolean isSuccess = false;
    boolean isJump = true;

    private void startSwitch() {


        String phone = SharePreferenceUtils.getString(this, "phone");
        String password =  SharePreferenceUtils.getString(this, "pwd");
        if (!TextUtils.isEmpty(phone)) {
            loginByPassword(phone, password);
        } else {
            startActivity(new Intent(WecomeActivity.this, LoginActivity.class));
            finish();
        }


    }

    private void getStatus() {

        NetUtils.getInstance().get(MyApplication.getUserInfo().getToken(), Api.Product.getStatus, new HashMap<>(), new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                Log.e("result", response);
                if (response.contains("data")) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject dataobject = jsonObject.getJSONObject("data");
                        int code = jsonObject.getInt("code");
                        if (code == 0) {
                            MyApplication.isPt = dataobject.getInt("data");
                            startActivity(new Intent(WecomeActivity.this, MainActivity.class));
                            finish();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    startActivity(new Intent(WecomeActivity.this, MainActivity.class));
                    finish();
                }
            }

            @Override
            public void onError(String error) {
                ToastUtil.showToast(WecomeActivity.this, "网络不好");

            }
        });

    }

    private void getJifen() {
//        HttpProxy.obtain().post(Api.Integral.loginGetIntegral, MyApplication.token, "", new ICallBack() {
//            @Override
//            public void OnSuccess(String result) {
//                Log.e("jifen", result);
//            }
//
//            @Override
//            public void onFailure(String error) {
//
//            }
//        });

        NetUtils.getInstance().post(MyApplication.token, Api.Integral.loginGetIntegral, new HashMap<>(), new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                Log.e("jifen", response);
            }

            @Override
            public void onError(String error) {

            }
        });

    }

    private void loginByPassword(String phone, String password) {

        Log.e("userLoginByApp", "start");
        Map<String, String> params = new HashMap<>();
        params.put("loginName", phone);
        params.put("password", password);
        NetUtils.getInstance().post(Api.User.userLoginByApp, params, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                MyApplication.loginResult = response;
                Log.e("autologin", response);
                if (response.contains("data")) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        int code = jsonObject.getInt("code");
                        if (code == 0) {
                            JSONObject data = jsonObject.getJSONObject("data");
                            data = data.getJSONObject("data");
                            UserInfo userInfo = new Gson().fromJson(data.toString(), UserInfo.class);
                            MyApplication.token = userInfo.getToken();
                            MyApplication.setUserInfo(userInfo);
                            SharePreferenceUtils.putString(WecomeActivity.this,"phone",phone);
                            SharePreferenceUtils.putString(WecomeActivity.this,"pwd",password);
                            getJifen();
                            if ("2".equals(userInfo.getType())) {
                                getStatus();
                            } else {
                                startActivity(new Intent(WecomeActivity.this, MainActivity.class));
                                finish();
                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String msg = jsonObject.getString("message");
                        ToastUtil.showToast(WecomeActivity.this, msg);
                        startActivity(new Intent(WecomeActivity.this, LoginActivity.class));
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(String error) {
                ToastUtil.showToast(WecomeActivity.this, "网络异常");
                startActivity(new Intent(WecomeActivity.this, LoginActivity.class));
                finish();
            }
        });

    }

    long start;
    long end;

    private void getImage() {
        startJump();

//        OkGo.<String>get(Api.ContentManage.getContentManageByApp).params("type",1).execute(new Callback<String>() {
//            @Override
//            public void onStart(Request<String, ? extends Request> request) {
//                start=System.currentTimeMillis();
//            }
//
//            @Override
//            public void onSuccess(Response<String> response) {
//                isSuccess=true;
//                if(!isJump){
//                    return;
//                }
//                String result=response.body();
//                Log.e("getContentManageByApp", result);
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    JSONObject dataobject = jsonObject.getJSONObject("data");
//                    int code = jsonObject.getInt("code");
//                    if (code == 0) {
//                        JSONArray data = dataobject.getJSONArray("data");
//                        for (int i = 0; i < data.length(); i++) {
//                            JSONObject item = data.getJSONObject(i);
//                            ContentImage contentImage = new Gson().fromJson(item.toString(), ContentImage.class);
//                            mContentImages.add(contentImage);
//                        }
//                        if (data.length() > 0) {
//                            String url = mContentImages.get(0).getImage();
//                            MyApplication.splash = url;
//                            count = mContentImages.get(0).getFlashFrequencyTime();
//                            Glide.with(WecomeActivity.this).load(url).listener(new RequestListener<Drawable>() {
//                                @Override
//                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                                    return false;
//                                }
//                                @Override
//                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                                    tv_time.setVisibility(View.VISIBLE);
//                                    timer(count * 1000);
//                                    return false;
//                                }
//                            }).into(iv_logo);
//                        } else {
//                            showDefaultPic();
//                        }
//                    } else {
//                        showDefaultPic();
//                    }
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onCacheSuccess(Response<String> response) {
//
//            }
//
//            @Override
//            public void onError(Response<String> response) {
//                showDefaultPic();
//                ToastUtil.showToast(WecomeActivity.this, "请求超时");
//            }
//
//            @Override
//            public void onFinish() {
//                end=System.currentTimeMillis();
//                Log.e("time",(end-start)+"");
//            }
//
//            @Override
//            public void uploadProgress(Progress progress) {
//
//            }
//
//            @Override
//            public void downloadProgress(Progress progress) {
//
//            }
//
//            @Override
//            public String convertResponse(okhttp3.Response response) throws Throwable {
//                return response.body().string();
//            }
//        });

        Map<String, String> params = new HashMap<>();
        params.put("type", "1");
        NetUtils.getInstance().get(Api.ContentManage.getContentManageByApp, params, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                isSuccess = true;
                if (!isJump) {
                    return;
                }
                Log.e("getContentManageByApp", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject dataobject = jsonObject.getJSONObject("data");
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        JSONArray data = dataobject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            ContentImage contentImage = new Gson().fromJson(item.toString(), ContentImage.class);
                            mContentImages.add(contentImage);
                        }
                        if (data.length() > 0) {
                            String url = mContentImages.get(0).getImage();
                            MyApplication.splash = url;
                            count = mContentImages.get(0).getFlashFrequencyTime();
                            Glide.with(WecomeActivity.this).load(url).listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    tv_time.setVisibility(View.VISIBLE);
                                    timer(count * 1000);
                                    return false;
                                }
                            }).into(iv_logo);
                        } else {
                            showDefaultPic();
                        }
                    } else {
                        showDefaultPic();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                showDefaultPic();
                ToastUtil.showToast(WecomeActivity.this, "请求超时");
            }
        });

    }


    private void showDefaultPic() {
        Glide.with(WecomeActivity.this).load(R.mipmap.icon_splash)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        tv_time.setVisibility(View.VISIBLE);
                        timer(3 * 1000);
                        return false;
                    }
                })
                .into(iv_logo);
//        tv_time.setVisibility(View.VISIBLE);
//        timer(3 * 1000);
    }
}
