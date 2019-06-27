package com.yunkang.chat.start.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kaopiz.kprogresshud.KProgressHUD;

import com.xiasuhuei321.loadingdialog.view.LoadingDialog;
import com.yunkang.chat.MyApplication;
import com.yunkang.chat.R;
import com.yunkang.chat.api.Api;
import com.yunkang.chat.http.HttpProxy;
import com.yunkang.chat.http.ICallBack;
import com.yunkang.chat.net.NetUtils;
import com.yunkang.chat.net.OnMessageReceived;
import com.yunkang.chat.start.model.Account;
import com.yunkang.chat.start.model.UserInfo;
import com.yunkang.chat.tools.CheckDoubleClick;
import com.yunkang.chat.tools.SharePreferenceUtils;
import com.yunkang.chat.tools.ToastUtil;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.leefeng.promptlibrary.PromptDialog;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.ll_pwd)
    LinearLayout ll_pwd;
    @BindView(R.id.ll_phone)
    LinearLayout ll_phone;
    @BindView(R.id.view_pwd)
    View view_pwd;
    @BindView(R.id.view_phone)
    View view_phone;
    @BindView(R.id.tv_typePwd)
    TextView tv_typePwd;
    @BindView(R.id.tv_typePhone)
    TextView tv_typePhone;
    @BindView(R.id.tv_forget)
    TextView tv_forget;
    @BindView(R.id.rl_pwd)
    RelativeLayout rl_pwd;
    @BindView(R.id.rl_phone)
    RelativeLayout rl_phone;
    @BindView(R.id.rl_forget)
    RelativeLayout rl_forget;
    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.et_pwd)
    EditText et_pwd;
    @BindView(R.id.et_code)
    EditText et_code;
    @BindView(R.id.tv_send)
    TextView tv_send;
    @BindView(R.id.iv_eye)
    ImageView iv_eye;
    @BindView(R.id.tv_login)
    TextView tv_login;
    @BindView(R.id.tv_register)
    TextView tv_register;
    int count = 60;
    int loginType = 1;
    String uuid;


    String logoutPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        logoutPhone=getIntent().getStringExtra("phone");
        initView();
    }

    PromptDialog mPromptDialog;
    private void initView() {
        if(!TextUtils.isEmpty(logoutPhone)){
            et_phone.setText(logoutPhone);
        }
        mPromptDialog=new PromptDialog(this);

        mTimeCount=new TimeCount(60000,1000);
        ll_pwd.setOnClickListener(this);
        ll_phone.setOnClickListener(this);
        iv_eye.setOnClickListener(this);
        tv_send.setOnClickListener(this);
        tv_login.setOnClickListener(this);
        tv_forget.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        et_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        //et_pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        iv_eye.setTag(1);
    }
    private void isExists(String phone) {
//        tv_send.setEnabled(false);
//        Map<String, Object> params = new HashMap<>();
//        params.put("telephone", phone);
//        HttpProxy.obtain().get(Api.User.isHaveUser, params, new ICallBack() {
//            @Override
//            public void OnSuccess(String result) {
//                tv_send.setEnabled(true);
//                Log.e("result", result);
//                if (result.contains("data")) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(result);
//                        jsonObject= jsonObject.getJSONObject("data");
//                        int code = jsonObject.getInt("resultCode");
//                        String msg=jsonObject.getString("message");
//                        if (code == 0) {
//                            ToastUtil.showToast(LoginActivity.this, msg);
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//
//                    try {
//                        JSONObject jsonObject = new JSONObject(result);
//                        String msg=jsonObject.getString("message");
//                        //ToastUtil.showToast(LoginActivity.this, msg);
//                        getPhoneCode(phone);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }
//
//            @Override
//            public void onFailure(String error) {
//                tv_send.setEnabled(true);
//                ToastUtil.showToast(LoginActivity.this, "请求超时");
//            }
//        });

        tv_send.setEnabled(false);
        Map<String, String> params = new HashMap<>();
        params.put("telephone", phone);
        NetUtils.getInstance().get(Api.User.isHaveUser, params, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                tv_send.setEnabled(true);
                Log.e("result", response);
                if (response.contains("data")) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        jsonObject= jsonObject.getJSONObject("data");
                        int code = jsonObject.getInt("resultCode");
                        String msg=jsonObject.getString("message");
                        if (code == 0) {
                            ToastUtil.showToast(LoginActivity.this, msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String msg=jsonObject.getString("message");
                        //ToastUtil.showToast(LoginActivity.this, msg);
                        getPhoneCode(phone);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onError(String error) {
                tv_send.setEnabled(true);
                ToastUtil.showToast(LoginActivity.this, "请求超时");
            }
        });

    }
    @Override
    public void onClick(View v) {
        if(CheckDoubleClick.isFastDoubleClick()){
            return;
        }
        switch (v.getId()) {
            case R.id.tv_register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.tv_forget:
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
                break;
            case R.id.ll_pwd:
                loginType = 1;
                tv_typePwd.setTextColor(getResources().getColor(R.color.color_blue));
                tv_typePhone.setTextColor(getResources().getColor(R.color.color_999));
                view_pwd.setVisibility(View.VISIBLE);
                view_phone.setVisibility(View.GONE);
                rl_pwd.setVisibility(View.VISIBLE);
                rl_phone.setVisibility(View.GONE);
                rl_forget.setVisibility(View.VISIBLE);
                break;
            case R.id.ll_phone:
                loginType = 2;
                tv_typePhone.setTextColor(getResources().getColor(R.color.color_blue));
                tv_typePwd.setTextColor(getResources().getColor(R.color.color_999));
                view_phone.setVisibility(View.VISIBLE);
                view_pwd.setVisibility(View.GONE);
                rl_forget.setVisibility(View.GONE);
                rl_phone.setVisibility(View.VISIBLE);
                rl_pwd.setVisibility(View.GONE);
                break;
            case R.id.tv_login:
                if (MyApplication.isDebug) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                    return;
                }

                String phone = et_phone.getEditableText().toString();
                String password = et_pwd.getEditableText().toString();
                String code = et_code.getEditableText().toString();
                if (loginType == 1) {
                    if (TextUtils.isEmpty(phone)) {
                        ToastUtil.showToast(this, "请输入手机号！");
                        return;
                    }
                    if(!isMobileNO(phone)){
                        ToastUtil.showToast(this, "手机号长度不正确！");
                        return;
                    }
                    if (TextUtils.isEmpty(password)) {
                        ToastUtil.showToast(this, "请输入密码！");
                        return;
                    }
                    hide(et_pwd);
                    loginByPassword(phone, password);
                } else {
                    hide(et_code);
                    if (TextUtils.isEmpty(phone)) {
                        ToastUtil.showToast(this, "请输入手机号！");
                        return;
                    }
                    if(!isMobileNO(phone)){
                        ToastUtil.showToast(this, "手机号长度不正确！");
                        return;
                    }
                    if (TextUtils.isEmpty(code)) {
                        ToastUtil.showToast(this, "请输入验证码！");
                        return;
                    }
                    if(TextUtils.isEmpty(uuid)){
                        ToastUtil.showToast(this, "请先获取验证码");
                        return;
                    }

                    loginByPhone(phone, code);
                }
                break;
            case R.id.iv_eye:
                int tag = (int) iv_eye.getTag();
                if (tag == 1) {
                    iv_eye.setImageResource(R.mipmap.ic_eye);
                    iv_eye.setTag(2);
                    et_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    iv_eye.setImageResource(R.mipmap.ic_eyeoff);
                    iv_eye.setTag(1);
                    et_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                et_pwd.setSelection(et_pwd.getText().toString().length());
                break;
            case R.id.tv_send:
                String phone1 = et_phone.getEditableText().toString();
                if (TextUtils.isEmpty(phone1)) {
                    ToastUtil.showToast(this, "请输入手机号！");
                    return;
                }
                if(!isMobileNO(phone1)){
                    ToastUtil.showToast(this, "手机号长度不正确！");
                    return;
                }
                hide(et_phone);
                isExists(phone1);
                break;

        }
    }
    private void hide(EditText editText) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);

    }
    private void getPhoneCode(String phone1) {
//        tv_send.setEnabled(false);
//        showLoading("发送中。。");
//        Map<String, Object> params = new HashMap<>();
//        //params.put("application", "pmplatform");
//        params.put("code", "COMMUNITY_ORG_APPLY");
//        params.put("phone", phone1);
//        String json = new Gson().toJson(params);
//        HttpProxy.obtain().post(Api.User.sendCode, "", json, new ICallBack() {
//            @Override
//            public void OnSuccess(String result) {
//                if(dialog.isShowing())
//                    dialog.dismiss();
//                Log.e("result", result);
//                if (result.contains("data")) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(result);
//                        int code=jsonObject.getInt("code");
//                        String msg=jsonObject.getString("msg");
//                        if(code==0){
//                            mTimeCount.start();
//                            JSONObject data = jsonObject.getJSONObject("data");
//                            data=data.getJSONObject("data");
//                            uuid = data.getString("uuid");
//                            ToastUtil.showToast(LoginActivity.this, "验证码已发送，请注意查收");
//                        }else{
//                            ToastUtil.showToast(LoginActivity.this, msg);
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    tv_send.setEnabled(true);
//                    try {
//                        JSONObject jsonObject = new JSONObject(result);
//                        String msg = jsonObject.getString("message");
//                        ToastUtil.showToast(LoginActivity.this, msg);
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//            }
//
//            @Override
//            public void onFailure(String error) {
//                tv_send.setEnabled(true);
//                if(dialog.isShowing())
//                    dialog.dismiss();
//                ToastUtil.showToast(LoginActivity.this, "请求超时");
//            }
//        });

        tv_send.setEnabled(false);
        showLoading("发送中。。");
        Map<String, String> params = new HashMap<>();
        //params.put("application", "pmplatform");
        params.put("code", "COMMUNITY_ORG_APPLY");
        params.put("phone", phone1);
        NetUtils.getInstance().post(Api.User.sendCode, params, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                if(dialog.isShowing())
                    dialog.dismiss();
                Log.e("result", response);
                if (response.contains("data")) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        int code=jsonObject.getInt("code");
                        String msg=jsonObject.getString("msg");
                        if(code==0){
                            mTimeCount.start();
                            JSONObject data = jsonObject.getJSONObject("data");
                            data=data.getJSONObject("data");
                            uuid = data.getString("uuid");
                            ToastUtil.showToast(LoginActivity.this, "验证码已发送，请注意查收");
                        }else{
                            ToastUtil.showToast(LoginActivity.this, msg);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    tv_send.setEnabled(true);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String msg = jsonObject.getString("message");
                        ToastUtil.showToast(LoginActivity.this, msg);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(String error) {
                tv_send.setEnabled(true);
                if(dialog.isShowing())
                    dialog.dismiss();
                ToastUtil.showToast(LoginActivity.this, "请求超时");
            }
        });

    }
     //获取登录积分
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
    //获取业务员状态
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
//                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                        finish();
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
//                ToastUtil.showToast(LoginActivity.this, "请求超时");
//            }
//        });

        NetUtils.getInstance().get(MyApplication.getUserInfo().getToken(), Api.Product.getStatus, new HashMap<>(), new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                Log.e("getStatus", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject dataobject = jsonObject.getJSONObject("data");
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        MyApplication.isPt = dataobject.getInt("data");
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                ToastUtil.showToast(LoginActivity.this, "请求超时");
            }
        });

    }

    private void loginByPassword(String phone, String password) {
        Log.e("userLoginByApp", "start");
        tv_login.setEnabled(false);
        showLoading("登录中");
        Map<String, String> params = new HashMap<>();
        params.put("loginName", phone);
        params.put("password", password);
        String json = new Gson().toJson(params);
        NetUtils.getInstance().post(Api.User.userLoginByApp, params, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                tv_login.setEnabled(true);
                if(dialog.isShowing())
                    dialog.dismiss();
                Log.d("myflogin", response);
                MyApplication.loginResult=response;
                if (response.contains("data")) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        int code = jsonObject.getInt("code");
                        if (code == 0) {
                            JSONObject data = jsonObject.getJSONObject("data");
                            data = data.getJSONObject("data");
                            UserInfo userInfo = new Gson().fromJson(data.toString(), UserInfo.class);
                            MyApplication.token = userInfo.getToken();
                            SharePreferenceUtils.putString(LoginActivity.this,"phone",phone);
                            SharePreferenceUtils.putString(LoginActivity.this,"pwd",password);
                            MyApplication.setUserInfo(userInfo);
                            if("2".equals(userInfo.getType())){
                                getStatus();
                            } else{
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }

                        }
                    } catch (JSONException e) {
                        ToastUtil.showToast(LoginActivity.this, e.getMessage());
                    }
                } else {
                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        String msg = jsonObject.getString("message");
                        ToastUtil.showToast(LoginActivity.this, msg);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(String error) {
                tv_login.setEnabled(true);
                if(dialog.isShowing())
                    dialog.dismiss();
                ToastUtil.showToast(LoginActivity.this, error);
            }
        });

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
            tv_send.setText(millisUntilFinished / 1000 +"");
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
    private void loginByPhone(String phone, String code) {


        tv_login.setEnabled(false);
        showLoading("登录中");
        Map<String, String> params = new HashMap<>();
        params.put("loginName", phone);
        params.put("uuid", uuid);
        params.put("verifyCode", code);
        NetUtils.getInstance().post(Api.User.userLoginByCode, params, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                if(dialog.isShowing())
                    dialog.dismiss();
                tv_login.setEnabled(true);
                Log.e("result", response);
                MyApplication.loginResult=response;
                if (response.contains("data")) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        int code = jsonObject.getInt("code");
                        if (code == 0) {
                            JSONObject data = jsonObject.getJSONObject("data");
                            data=data.getJSONObject("data");
                            UserInfo userInfo = new Gson().fromJson(data.toString(), UserInfo.class);
                            MyApplication.token = userInfo.getToken();
                            MyApplication.setUserInfo(userInfo);
                            getJifen();
                            if("2".equals(userInfo.getType())){
                                getStatus();
                            } else{
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }

                        } else {
                            String msg = jsonObject.getString("message");
                            ToastUtil.showToast(LoginActivity.this, msg);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String msg = jsonObject.getString("message");
                        ToastUtil.showToast(LoginActivity.this, msg);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(String error) {
                tv_login.setEnabled(true);
                if(dialog.isShowing())
                    dialog.dismiss();
                ToastUtil.showToast(LoginActivity.this, "请求失败");
            }
        });

    }

    public static boolean isMobileNO(String phone) {
        if(phone.length()<11){
            return false;
        }

        return true;
    }
}
