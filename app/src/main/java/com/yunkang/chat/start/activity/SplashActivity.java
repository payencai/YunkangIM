package com.yunkang.chat.start.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import com.xiasuhuei321.loadingdialog.view.LoadingDialog;
import com.yunkang.chat.MyApplication;
import com.yunkang.chat.R;
import com.yunkang.chat.api.Api;
import com.yunkang.chat.http.HttpProxy;
import com.yunkang.chat.http.ICallBack;
import com.yunkang.chat.net.NetUtils;
import com.yunkang.chat.net.OnMessageReceived;
import com.yunkang.chat.start.model.Account;
import com.yunkang.chat.start.model.ContentImage;
import com.yunkang.chat.start.model.UserInfo;
import com.yunkang.chat.tools.SharePreferenceUtils;
import com.yunkang.chat.tools.ToastUtil;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {


    int count = 3;
    CountDownTimer mCountDownTimer;
    List<ContentImage> mContentImages;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.iv_logo)
    ImageView iv_logo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        mContentImages = new ArrayList<>();
        tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_time.setEnabled(false);
                if (mCountDownTimer != null)
                    mCountDownTimer.cancel();
                startSwitch();
            }
        });
        if(TextUtils.isEmpty(MyApplication.splash)){
            iv_logo.setImageResource(R.mipmap.icon_splash);
            timer(3*1000);
        }else{
            Glide.with(this).load(MyApplication.splash).into(iv_logo);
            timer(MyApplication.count*1000);
        }
        //getStartImage();
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
                startSwitch();
                //倒计时结束时回调该函数
            }
        }.start();
    }




    private void startSwitch() {
        String isFirstEnter = SharePreferenceUtils.getString(SplashActivity.this, "enter");
        if (TextUtils.isEmpty(isFirstEnter)) {
            startActivity(new Intent(SplashActivity.this, GuideActivity.class));
            finish();
        } else {
            if (TextUtils.equals("notfirst", isFirstEnter)) {
                String phone = SharePreferenceUtils.getString(SplashActivity.this, "phone");
                String password = SharePreferenceUtils.getString(SplashActivity.this, "password");
                if (!TextUtils.isEmpty(phone)) {
                    loginByPassword(phone, password);
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            } else {
                startActivity(new Intent(SplashActivity.this, GuideActivity.class));
                finish();
            }

        }
    }

    private void getStatus() {
//        HttpProxy.obtain().get(Api.Product.getStatus, MyApplication.getUserInfo().getToken(), new ICallBack() {
//            @Override
//            public void OnSuccess(String result) {
//                Log.e("result", result);
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    JSONObject dataobject = jsonObject.getJSONObject("data");
//                    int code = jsonObject.getInt("code");
//                    if (code == 0) {
//                        MyApplication.isPt = dataobject.getInt("data");
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
//                ToastUtil.showToast(SplashActivity.this, "网络不好");
//            }
//        });

        NetUtils.getInstance().get(MyApplication.getUserInfo().getToken(), Api.Product.getStatus, new HashMap<>(), new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                Log.e("result", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject dataobject = jsonObject.getJSONObject("data");
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        MyApplication.isPt = dataobject.getInt("data");
                    } else {

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                ToastUtil.showToast(SplashActivity.this, "网络不好");
            }
        });

    }

    private void loginByPassword(String phone, String password) {

        Map<String, String> params = new HashMap<>();
        params.put("loginName", phone);
        params.put("password", password);
//        String json = new Gson().toJson(params);
//        Log.e("json", json);
//        HttpProxy.obtain().post(Api.User.userLoginByApp, "", json, new ICallBack() {
//            @Override
//            public void OnSuccess(String result) {
//                MyApplication.loginResult = result;
//                Log.e("userLoginByApp", result);
//                if (result.contains("data")) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(result);
//                        int code = jsonObject.getInt("code");
//                        if (code == 0) {
//                            JSONObject data = jsonObject.getJSONObject("data");
//                            data = data.getJSONObject("data");
//                            UserInfo userInfo = new Gson().fromJson(data.toString(), UserInfo.class);
//                            MyApplication.token = userInfo.getToken();
//                            MyApplication.setUserInfo(userInfo);
//                            SharePreferenceUtils.putString(SplashActivity.this, "phone", phone);
//                            SharePreferenceUtils.putString(SplashActivity.this, "password", password);
//                            getStatus();
//                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                            finish();
//
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    try {
//                        JSONObject jsonObject = new JSONObject(result);
//                        String msg = jsonObject.getString("message");
//                        ToastUtil.showToast(SplashActivity.this, msg);
//                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
//                        finish();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//            }
//
//            @Override
//            public void onFailure(String error) {
//
//                ToastUtil.showToast(SplashActivity.this, "网络异常");
//                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
//                finish();
//            }
//        });

        NetUtils.getInstance().post(Api.User.userLoginByApp, params, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                MyApplication.loginResult = response;
                Log.e("userLoginByApp", response);
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
                            SharePreferenceUtils.putString(SplashActivity.this, "phone", phone);
                            SharePreferenceUtils.putString(SplashActivity.this, "password", password);
                            getStatus();
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            finish();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String msg = jsonObject.getString("message");
                        ToastUtil.showToast(SplashActivity.this, msg);
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(String error) {
                ToastUtil.showToast(SplashActivity.this, "网络异常");
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        });

    }
}
