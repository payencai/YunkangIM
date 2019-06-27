package com.yunkang.chat.start.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;

import com.xiasuhuei321.loadingdialog.view.LoadingDialog;
import com.yunkang.chat.R;
import com.yunkang.chat.api.Api;
import com.yunkang.chat.http.HttpProxy;
import com.yunkang.chat.http.ICallBack;
import com.yunkang.chat.net.NetUtils;
import com.yunkang.chat.net.OnMessageReceived;
import com.yunkang.chat.tools.CheckDoubleClick;
import com.yunkang.chat.tools.ToastUtil;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.tv_confirm)
    TextView tv_confirm;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_send)
    TextView tv_send;
    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.et_pwd2)
    EditText et_pwd2;
    @BindView(R.id.et_pwd)
    EditText et_pwd;
    @BindView(R.id.et_code)
    EditText et_code;
    int count = 60;
    String uuid = "1";
    String userId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ButterKnife.bind(this);
        initView();
    }
    KProgressHUD dialog;
    private void showLoading(String data){
        dialog= KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(data)
                .setCancellable(new DialogInterface.OnCancelListener()
                {
                    @Override public void onCancel(DialogInterface
                                                           dialogInterface)
                    {

                    }
                });
        dialog.show();
        //scheduleDismiss();

    }
    TimeCount mTimeCount;

    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tv_send.setBackgroundResource(R.drawable.bg_unselect_sendcode);
            tv_send.setTextColor(getResources().getColor(R.color.color_999));
            tv_send.setText(millisUntilFinished / 1000 + "");
        }

        @Override
        public void onFinish() {
            count = 60;
            tv_send.setText("获取验证码");
            tv_send.setEnabled(true);
            tv_send.setBackgroundResource(R.drawable.bg_shape_sendcode);
            tv_send.setTextColor(getResources().getColor(R.color.color_blue));

        }
    }


    private void initView() {

        mTimeCount = new TimeCount(60000, 1000);
        tv_confirm.setOnClickListener(this);
        tv_send.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        et_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        et_pwd2.setTransformationMethod(PasswordTransformationMethod.getInstance());

    }

    private void getUserId(String phone) {
//        tv_send.setEnabled(false);
//        Map<String, Object> params = new HashMap<>();
//        params.put("userName", phone);
//        HttpProxy.obtain().get(Api.User.getUserInfoByUsername, params, new ICallBack() {
//            @Override
//            public void OnSuccess(String result) {
//                Log.e("result", result);
//                if (result.contains("data")) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(result);
//                        int code = jsonObject.getInt("code");
//                        String msg = jsonObject.getString("msg");
//                        jsonObject = jsonObject.getJSONObject("data");
//                        JSONObject data = jsonObject.getJSONObject("data");
//                        if (code == 0) {
//                            userId = data.getString("userId");
//                            getPhoneCode(phone);
//                        } else {
//                            ToastUtil.showToast(ForgetPasswordActivity.this, msg);
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    tv_send.setEnabled(true);
//                    try {
//                        JSONObject jsonObject = new JSONObject(result);
//                        String msg = jsonObject.getString("message");
//                        ToastUtil.showToast(ForgetPasswordActivity.this, msg);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(String error) {
//                tv_send.setEnabled(true);
//                ToastUtil.showToast(ForgetPasswordActivity.this, "请求超时");
//            }
//        });

        tv_send.setEnabled(false);
        Map<String, String> params = new HashMap<>();
        params.put("userName", phone);
        NetUtils.getInstance().get(Api.User.getUserInfoByUsername, params, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                Log.e("result", response);
                if (response.contains("data")) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        int code = jsonObject.getInt("code");
                        String msg = jsonObject.getString("msg");
                        jsonObject = jsonObject.getJSONObject("data");
                        JSONObject data = jsonObject.getJSONObject("data");
                        if (code == 0) {
                            userId = data.getString("userId");
                            getPhoneCode(phone);
                        } else {
                            ToastUtil.showToast(ForgetPasswordActivity.this, msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    tv_send.setEnabled(true);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String msg = jsonObject.getString("message");
                        ToastUtil.showToast(ForgetPasswordActivity.this, msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(String error) {
                tv_send.setEnabled(true);
                ToastUtil.showToast(ForgetPasswordActivity.this, "请求超时");
            }
        });

    }
    private void hide(EditText editText) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);

    }
    private void reSetPassword(String phone, String code, String password) {
//        showLoading("提交中");
//        tv_confirm.setEnabled(false);
//        Map<String, Object> params = new HashMap<>();
//        params.put("confirmPassword", password);
//        params.put("currentPassword", password);
//        params.put("verifyCode", code);
//        params.put("phone", phone);
//        params.put("password", password);
//        params.put("uuid", uuid);
//        params.put("userId", userId);
//        String json = new Gson().toJson(params);
//        Log.e("data", json);
//        HttpProxy.obtain().post(Api.User.resetPassword, "", json, new ICallBack() {
//            @Override
//            public void OnSuccess(String result) {
//                tv_confirm.setEnabled(true);
//                if (dialog.isShowing())
//                    dialog.dismiss();
//                if (result.contains("data")) {
//                    try {
//                        JSONObject data = new JSONObject(result);
//                        String msg = data.getString("msg");
//                        int code = data.getInt("code");
//                        if (code == 0) {
//                            ToastUtil.showToast(ForgetPasswordActivity.this, "重置成功");
//                            finish();
//                        } else {
//                            ToastUtil.showToast(ForgetPasswordActivity.this, msg);
//                        }
//
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    try {
//                        JSONObject jsonObject = new JSONObject(result);
//                        String msg = jsonObject.getString("message");
//                        ToastUtil.showToast(ForgetPasswordActivity.this, msg);
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//
//            }
//
//            @Override
//            public void onFailure(String error) {
//                tv_confirm.setEnabled(true);
//                if (dialog.isShowing())
//                    dialog.dismiss();
//                ToastUtil.showToast(ForgetPasswordActivity.this, error + "");
//            }
//        });

        showLoading("提交中");
        tv_confirm.setEnabled(false);
        Map<String, String> params = new HashMap<>();
        params.put("confirmPassword", password);
        params.put("currentPassword", password);
        params.put("verifyCode", code);
        params.put("phone", phone);
        params.put("password", password);
        params.put("uuid", uuid);
        params.put("userId", userId);
        NetUtils.getInstance().post(Api.User.resetPassword, params, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                tv_confirm.setEnabled(true);
                if (dialog.isShowing())
                    dialog.dismiss();
                if (response.contains("data")) {
                    try {
                        JSONObject data = new JSONObject(response);
                        String msg = data.getString("msg");
                        int code = data.getInt("code");
                        if (code == 0) {
                            ToastUtil.showToast(ForgetPasswordActivity.this, "重置成功");
                            finish();
                        } else {
                            ToastUtil.showToast(ForgetPasswordActivity.this, msg);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String msg = jsonObject.getString("message");
                        ToastUtil.showToast(ForgetPasswordActivity.this, msg);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(String error) {
                tv_confirm.setEnabled(true);
                if (dialog.isShowing())
                    dialog.dismiss();
                ToastUtil.showToast(ForgetPasswordActivity.this, error + "");
            }
        });

    }





    private void getPhoneCode(String phone1) {
//        tv_send.setEnabled(false);
//        showLoading("发送中");
//        Map<String, Object> params = new HashMap<>();
//        //params.put("application", "pmplatform");
//        params.put("code", "PMPLATRORM_RESET_PASSWORD");
//        params.put("phone", phone1);
//        String json = new Gson().toJson(params);
//        HttpProxy.obtain().post(Api.User.sendCode, "", json, new ICallBack() {
//            @Override
//            public void OnSuccess(String result) {
//                if (dialog.isShowing())
//                    dialog.dismiss();
//                mTimeCount.start();
//                Log.e("result", result);
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    JSONObject data = jsonObject.getJSONObject("data");
//                    String msg = jsonObject.getString("msg");
//                    int code = jsonObject.getInt("code");
//                    if (code == 0) {
//                        ToastUtil.showToast(ForgetPasswordActivity.this, "验证码已发送，请注意查收");
//                        data = data.getJSONObject("data");
//                        uuid = data.getString("uuid");
//
//                    } else {
//                        ToastUtil.showToast(ForgetPasswordActivity.this, msg);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(String error) {
//                if (dialog.isShowing())
//                    dialog.dismiss();
//            }
//        });

        tv_send.setEnabled(false);
        showLoading("发送中");
        Map<String, String> params = new HashMap<>();
        //params.put("application", "pmplatform");
        params.put("code", "PMPLATRORM_RESET_PASSWORD");
        params.put("phone", phone1);
        NetUtils.getInstance().post(Api.User.sendCode, params, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                if (dialog.isShowing())
                    dialog.dismiss();
                mTimeCount.start();
                Log.e("result", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.getJSONObject("data");
                    String msg = jsonObject.getString("msg");
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        ToastUtil.showToast(ForgetPasswordActivity.this, "验证码已发送，请注意查收");
                        data = data.getJSONObject("data");
                        uuid = data.getString("uuid");

                    } else {
                        ToastUtil.showToast(ForgetPasswordActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                if (dialog.isShowing())
                    dialog.dismiss();
            }
        });

    }

    @Override
    public void onClick(View view) {
        if (CheckDoubleClick.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.tv_confirm:
                String pwd = et_pwd.getEditableText().toString();
                String pwd2 = et_pwd2.getEditableText().toString();
                hide(et_phone);
                hide(et_code);

                if (TextUtils.isEmpty(et_phone.getEditableText().toString())) {
                    ToastUtil.showToast(this, "请输入手机号！");
                    return;
                }
                if (!isMobileNO(et_phone.getEditableText().toString())) {
                    ToastUtil.showToast(this, "手机号长度不正确！");
                    return;
                }
                if (TextUtils.isEmpty(et_code.getEditableText().toString())) {
                    ToastUtil.showToast(this, "请输入验证码！");
                    return;
                }
                if (TextUtils.isEmpty(et_pwd.getEditableText().toString())) {
                    ToastUtil.showToast(this, "请输入密码！");
                    return;
                }
                if (TextUtils.isEmpty(et_pwd2.getEditableText().toString())) {
                    ToastUtil.showToast(this, "请输入确认密码！");
                    return;
                }
                if (!TextUtils.equals(pwd, pwd2)) {
                    ToastUtil.showToast(this, "2次密码输入不一致！");
                    return;
                }

                if (TextUtils.isEmpty(uuid)) {
                    ToastUtil.showToast(this, "请先获取验证码！");
                    return;
                }
                reSetPassword(et_phone.getEditableText().toString(), et_code.getEditableText().toString(), et_pwd.getEditableText().toString());
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_send:
                hide(et_phone);
                hide(et_code);
                String phone1 = et_phone.getEditableText().toString();
                if (TextUtils.isEmpty(et_phone.getEditableText().toString())) {
                    ToastUtil.showToast(this, "请输入手机号！");
                    return;
                }
                if (!isMobileNO(et_phone.getEditableText().toString())) {
                    ToastUtil.showToast(this, "手机号长度不正确！");
                }
                getUserId(phone1);

                break;
        }
    }

    public static boolean isMobileNO(String phone) {
        if (phone.length() < 11) {
            return false;
        }

        return true;
    }

}
