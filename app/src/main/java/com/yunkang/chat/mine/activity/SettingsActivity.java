package com.yunkang.chat.mine.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import com.yunkang.chat.MyApplication;
import com.yunkang.chat.R;
import com.yunkang.chat.api.Api;
import com.yunkang.chat.constant.Constant;
import com.yunkang.chat.h5.H5CommomActivity;
import com.yunkang.chat.home.adapter.LabsAdapter;
import com.yunkang.chat.http.HttpProxy;
import com.yunkang.chat.http.ICallBack;
import com.yunkang.chat.mine.model.Order;
import com.yunkang.chat.net.NetUtils;
import com.yunkang.chat.net.OnMessageReceived;
import com.yunkang.chat.start.activity.LoginActivity;
import com.yunkang.chat.start.activity.MainActivity;
import com.yunkang.chat.start.model.UserInfo;
import com.yunkang.chat.tools.AppManager;
import com.yunkang.chat.tools.SharePreferenceUtils;
import com.yunkang.chat.tools.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.iv_switch)
    ImageView iv_switch;
    @BindView(R.id.rl_update)
    RelativeLayout rl_update;
    @BindView(R.id.rl_reback)
    RelativeLayout rl_reback;
    @BindView(R.id.tv_exit)
    TextView tv_exit;
    @BindView(R.id.rl_about)
    RelativeLayout rl_about;
    @BindView(R.id.rl_version)
    RelativeLayout rl_version;
    @BindView(R.id.tv_phone)
    TextView tv_phone;
    @BindView(R.id.rl_switch)
    RelativeLayout rl_switch;
    int type = 0;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        AppManager.getInstance().addActivity(this);
        ButterKnife.bind(this);
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
    }

    private void getUserInfo() {

        NetUtils.getInstance().get(MyApplication.token, Api.User.getUserDtoByToken, new HashMap<>(), new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                Log.e("getUserDtoByToken", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        data = data.getJSONObject("data");
                        UserInfo mUserInfo = new Gson().fromJson(data.toString(), UserInfo.class);
                        phone = mUserInfo.getTelephone();
                        if (!TextUtils.isEmpty(phone))
                            tv_phone.setText(phone.substring(0, 3) + "****" + phone.substring(7, 11));
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

    private void initView() {

        if (MyApplication.getUserInfo() != null) {
            String phone = MyApplication.getUserInfo().getTelephone();
            if (!TextUtils.isEmpty(phone))
                tv_phone.setText(phone.substring(0, 3) + "****" + phone.substring(7, 11));
        }
        rl_about.setOnClickListener(this);
        rl_version.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        rl_update.setOnClickListener(this);
        rl_reback.setOnClickListener(this);
        tv_exit.setOnClickListener(this);
        iv_switch.setOnClickListener(this);
        if (MyApplication.getUserInfo().getType().equals("2")) {
            getStatus();
            rl_switch.setVisibility(View.VISIBLE);
        } else {
            iv_switch.setVisibility(View.GONE);
        }

    }

    private void showLogoutDialog() {

        Dialog dialog = new Dialog(this, R.style.common_dialog_style);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_logout, null);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        TextView tv_confirm = view.findViewById(R.id.tv_confirm);
        TextView tv_cancel = view.findViewById(R.id.tv_cancel);
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                SharePreferenceUtils.putString(SettingsActivity.this, "phone", "");
                AppManager.getInstance().killAllActivity();
                Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                intent.putExtra("phone", phone);
                startActivity(intent);
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        Window window = dialog.getWindow();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();

        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.width = (int) (display.getWidth() * 0.7);
        window.setAttributes(layoutParams);

    }

    boolean isChange = false;

    private void turnoff() {


        NetUtils.getInstance().post(MyApplication.token, Api.Product.turnOff, new HashMap<>(), new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                Log.e("turn",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        MyApplication.isPt = 0;
                        isChange = true;
                        type = 0;
                        iv_switch.setImageResource(R.mipmap.ic_off);
                        ToastUtil.showToast(SettingsActivity.this, "已关闭");
                    } else {
                        String message = jsonObject.getString("msg");
                        ToastUtil.showToast(SettingsActivity.this, message);
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
                Log.e("turn",response);
                if (response.contains("data")) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        int code = jsonObject.getInt("code");
                        if (code == 0) {
                            type = 1;
                            iv_switch.setImageResource(R.mipmap.ic_on);
                            MyApplication.isPt = 1;
                            isChange = true;
                            ToastUtil.showToast(SettingsActivity.this, "已打开");
                        } else {
                            String message = jsonObject.getString("msg");
                            ToastUtil.showToast(SettingsActivity.this, message);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        int code = jsonObject.getInt("resultCode");
                        if (code == 0) {

                        } else {
                            String message = jsonObject.getString("message");
                            ToastUtil.showToast(SettingsActivity.this, message);
                        }


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

    private void getStatus() {

        NetUtils.getInstance().get(MyApplication.token, Api.Product.getStatus, new HashMap<>(), new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                Log.e("result", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject dataobject = jsonObject.getJSONObject("data");
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        type = dataobject.getInt("data");
                        if (type == 1) {
                            iv_switch.setImageResource(R.mipmap.ic_on);
                        } else {
                            iv_switch.setImageResource(R.mipmap.ic_off);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_about:
                startActivity(new Intent(SettingsActivity.this, H5CommomActivity.class).putExtra("url", Constant.Commom.about + MyApplication.token));
                break;
            case R.id.rl_version:
                startActivity(new Intent(SettingsActivity.this, VersioninfoActivity.class));
                break;

            case R.id.iv_switch:
                if (type == 1) {
                    turnoff();
                } else {
                    turnon();
                }
                break;
            case R.id.iv_back:
                Intent intent = new Intent();
                intent.putExtra("isChange", isChange);
                setResult(0, intent);
                finish();
                break;
            case R.id.rl_update:
                startActivity(new Intent(SettingsActivity.this, UpdatePasswordActivity.class));
                break;
            case R.id.rl_reback:
                startActivity(new Intent(SettingsActivity.this, H5CommomActivity.class).putExtra("url", Constant.Commom.advince + MyApplication.token));
                //startActivity(new Intent(SettingsActivity.this,RebackActivity.class));
                break;
            case R.id.tv_exit:
                showLogoutDialog();

                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("isChange", isChange);
        setResult(0, intent);
        finish();
    }
}
