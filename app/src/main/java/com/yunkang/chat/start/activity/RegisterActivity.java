package com.yunkang.chat.start.activity;

import android.app.Dialog;
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
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;

import com.xiasuhuei321.loadingdialog.view.LoadingDialog;
import com.yunkang.chat.MyApplication;
import com.yunkang.chat.R;
import com.yunkang.chat.api.Api;
import com.yunkang.chat.home.activity.MsgActivity;
import com.yunkang.chat.http.HttpProxy;
import com.yunkang.chat.http.ICallBack;
import com.yunkang.chat.net.NetUtils;
import com.yunkang.chat.net.OnMessageReceived;
import com.yunkang.chat.start.model.Protocol;
import com.yunkang.chat.tools.CheckDoubleClick;
import com.yunkang.chat.tools.ToastUtil;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.leefeng.promptlibrary.PromptDialog;

import static android.icu.lang.UScript.TRADITIONAL_HAN;
import static android.icu.lang.UScript.getCode;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.et_pwd)
    EditText et_pwd;
    @BindView(R.id.et_invate)
    EditText et_invate;
    @BindView(R.id.et_code)
    EditText et_code;
    @BindView(R.id.tv_send)
    TextView tv_send;
    @BindView(R.id.iv_proc)
    ImageView iv_proc;
    @BindView(R.id.iv_eye)
    ImageView iv_eye;
    @BindView(R.id.tv_reg)
    TextView tv_reg;
    @BindView(R.id.tv_login)
    TextView tv_login;
    @BindView(R.id.tv_protocol)
    TextView tv_protocol;
    int count = 60;
    int selectType = 2;
    String uuid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        //getJifen("13202908144");
        initView();
    }

    private void isExists(String phone) {
        tv_send.setEnabled(false);
        Map<String, String> params = new HashMap<>();
        params.put("telephone", phone);

        NetUtils.getInstance().get(Api.User.isHaveUser, params, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                Log.e("result", response);
                if (response.contains("data")) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        int code = jsonObject.getInt("code");
                        if (code == 0) {
                            getPhoneCode(phone);
                        } else {

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    tv_send.setEnabled(true);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String msg = jsonObject.getString("message");
                        ToastUtil.showToast(RegisterActivity.this, msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onError(String error) {
                tv_send.setEnabled(false);
                ToastUtil.showToast(RegisterActivity.this, "请求超时");
            }
        });
    }

    private void hide(EditText editText) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null)
            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);

    }

    private void initView() {

        mTimeCount = new TimeCount(60000, 1000);
        tv_reg.setEnabled(false);
        tv_login.setOnClickListener(this);
        tv_send.setOnClickListener(this);
        iv_proc.setOnClickListener(this);
        iv_eye.setOnClickListener(this);
        tv_reg.setOnClickListener(this);
        tv_protocol.setOnClickListener(this);
        iv_eye.setTag(1);
        et_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String phone = editable.toString();
                String pwd = et_pwd.getEditableText().toString();
                String code = et_code.getEditableText().toString();
                if (!TextUtils.isEmpty(code) && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(pwd) && selectType == 1) {
                    tv_reg.setBackgroundResource(R.drawable.bg_blue_login);
                    tv_reg.setEnabled(true);
                } else {
                    tv_reg.setBackgroundResource(R.drawable.bg_gray_login);
                    tv_reg.setEnabled(false);
                }
            }
        });
        et_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String pwd = editable.toString();
                String code = et_code.getEditableText().toString();
                String phone = et_phone.getEditableText().toString();
                if (!TextUtils.isEmpty(code) && !TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(phone) && selectType == 1) {
                    tv_reg.setBackgroundResource(R.drawable.bg_blue_login);
                    tv_reg.setEnabled(true);
                } else {
                    tv_reg.setBackgroundResource(R.drawable.bg_gray_login);
                    tv_reg.setEnabled(false);
                }
            }
        });
        et_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String code = editable.toString();
                String pwd = editable.toString();
                String phone = et_phone.getEditableText().toString();
                if (!TextUtils.isEmpty(code) && !TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(phone) && selectType == 1) {
                    tv_reg.setBackgroundResource(R.drawable.bg_blue_login);
                    tv_reg.setEnabled(true);
                } else {
                    tv_reg.setBackgroundResource(R.drawable.bg_gray_login);
                    tv_reg.setEnabled(false);
                }
            }
        });
    }

    private void showDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_reg_protocol, null);
        TextView tv_confirm = view.findViewById(R.id.tv_confirm);
        WebView tv_content = view.findViewById(R.id.webview);
        Dialog dialog = new Dialog(this, R.style.common_dialog_style);
        dialog.setCancelable(false);
        dialog.setContentView(view);
        dialog.show();
        WindowManager windowManager = getWindowManager();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = (int) (display.getWidth() * 0.8);
        layoutParams.height = (int) (display.getHeight() * 0.7);
        window.setAttributes(layoutParams);
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        getProtocol(tv_content);
        //getJifen("13202908144");
    }

    private void getProtocol(WebView webView) {
        Map<String, String> params = new HashMap<>();
        params.put("type", "1");

        NetUtils.getInstance().get(Api.Protocol.getProtocolListByType, params, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                Log.e("reslut", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    jsonObject = jsonObject.getJSONObject("data");
                    JSONObject data = jsonObject.getJSONObject("data");
                    Protocol protocol = new Gson().fromJson(data.toString(), Protocol.class);
                    webView.loadDataWithBaseURL(null, protocol.getProtocol(), "text/html", "UTF-8", null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    public static boolean isMobileNO(String phone) {
        if (phone.length() < 11) {
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View view) {
        if (CheckDoubleClick.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.tv_protocol:
                showDialog();
                break;
            case R.id.tv_send:
                hide(et_phone);
                String phone1 = et_phone.getEditableText().toString();
                if (TextUtils.isEmpty(phone1)) {
                    ToastUtil.showToast(this, "请输入手机号！");
                    return;
                }
                if (!isMobileNO(phone1)) {
                    ToastUtil.showToast(this, "手机号长度不正确！");
                    return;
                }

                isExists(phone1);
                break;
            case R.id.tv_login:
                finish();
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
            case R.id.iv_proc:
                if (selectType == 2) {
                    selectType = 1;
                    iv_proc.setImageResource(R.mipmap.ic_select);
                    if (!TextUtils.isEmpty(et_code.getEditableText().toString()) && !TextUtils.isEmpty(et_phone.getEditableText().toString()) && !TextUtils.isEmpty(et_pwd.getEditableText().toString())) {
                        tv_reg.setBackgroundResource(R.drawable.bg_blue_login);
                        tv_reg.setEnabled(true);
                    }
                } else {
                    selectType = 2;
                    iv_proc.setImageResource(R.mipmap.ic_unselect);
                    tv_reg.setBackgroundResource(R.drawable.bg_gray_login);
                    tv_reg.setEnabled(false);
                }

                break;
            case R.id.tv_reg:
                hide(et_phone);
                hide(et_code);
                String phone = et_phone.getEditableText().toString();
                String password = et_pwd.getEditableText().toString();
                String code = et_code.getEditableText().toString();
                String invatecode = et_invate.getEditableText().toString();
                if (TextUtils.isEmpty(phone)) {
                    ToastUtil.showToast(this, "请输入手机号！");
                    return;
                }
                if (!isMobileNO(phone)) {
                    ToastUtil.showToast(this, "手机号长度不正确!");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    ToastUtil.showToast(this, "请输入密码！");
                    return;
                }
                if (TextUtils.isEmpty(code)) {
                    ToastUtil.showToast(this, "请输入验证码！");
                    return;
                }
                if (TextUtils.isEmpty(uuid)) {
                    ToastUtil.showToast(this, "请先获取验证码！");
                    return;
                }
                if (password.length() < 6) {
                    ToastUtil.showToast(this, "密码至少是6位！");
                    return;
                }
                register(phone, code, password, invatecode);
                break;
        }
    }

//    private void getJifen(String phone) {
//        Map<String, Object> params = new HashMap<>();
//        params.put("telephone", phone);
//        HttpProxy.obtain().post(Api.Integral.registeredGetIntegral, "", params, new ICallBack() {
//            @Override
//            public void OnSuccess(String result) {
//                Log.e("zhuce", result);
//            }
//
//            @Override
//            public void onFailure(String error) {
//                Log.e("zhuce", error);
//            }
//        });
//    }

    private void register(String phone, String code, String password, String invateCode) {
        tv_reg.setEnabled(false);
        showLoading("提交中");
        Map<String, Object> params = new HashMap<>();
        if (!TextUtils.isEmpty(invateCode))
            params.put("beInvitationCode", invateCode);
        params.put("code", code);
        params.put("telephone", phone);
        params.put("password", password);
        params.put("uuid", uuid);
        String json = new Gson().toJson(params);
        Log.d("json", json);
//        HttpProxy.obtain().post(Api.User.userRegister, "", json, new ICallBack() {
//            @Override
//            public void OnSuccess(String result) {
//                tv_reg.setEnabled(true);
//                if (dialog != null) {
//                    if (dialog.isShowing())
//                        dialog.dismiss();
//                }
//                Log.e("result", result);
//                if (result.contains("data")) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(result);
//                        jsonObject = jsonObject.getJSONObject("data");
//                        int code = jsonObject.getInt("resultCode");
//                        if (code == 0) {
//                            ToastUtil.showToast(RegisterActivity.this, "注册成功");
//                            //getJifen(phone);
//                            finish();
//                        } else {
//                            String msg = jsonObject.getString("msg");
//                            ToastUtil.showToast(RegisterActivity.this, msg);
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    try {
//                        JSONObject jsonObject = new JSONObject(result);
//                        String msg = jsonObject.getString("message");
//                        ToastUtil.showToast(RegisterActivity.this, msg);
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
//                tv_reg.setEnabled(true);
//                if(dialog!=null)
//                    if (dialog.isShowing())
//                        dialog.dismiss();
//                ToastUtil.showToast(RegisterActivity.this, "请求超时");
//            }
//        });
        NetUtils.getInstance().post(Api.User.userRegister, json, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                tv_reg.setEnabled(true);
                if (dialog != null) {
                    if (dialog.isShowing())
                        dialog.dismiss();
                }
                Log.e("result", response);
                if (response.contains("data")) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        jsonObject = jsonObject.getJSONObject("data");
                        int code = jsonObject.getInt("resultCode");
                        if (code == 0) {
                            ToastUtil.showToast(RegisterActivity.this, "注册成功");
                            //getJifen(phone);
                            finish();
                        } else {
                            String msg = jsonObject.getString("msg");
                            ToastUtil.showToast(RegisterActivity.this, msg);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String msg = jsonObject.getString("message");
                        ToastUtil.showToast(RegisterActivity.this, msg);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(String error) {
                tv_reg.setEnabled(true);
                if(dialog!=null)
                    if (dialog.isShowing())
                        dialog.dismiss();
                ToastUtil.showToast(RegisterActivity.this, "请求超时");
            }
        });
    }

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

    KProgressHUD dialog;

    private void showLoading(String data) {
        dialog = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(data)
                .setCancellable(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface
                                                 dialogInterface) {

                    }
                });
        dialog.show();
        //scheduleDismiss();

    }

    private TimeCount mTimeCount;

    private void getPhoneCode(String phone1) {
        tv_send.setEnabled(false);
        showLoading("发送中");
        Map<String, Object> params = new HashMap<>();
        // params.put("application", "pmplatform");
        params.put("code", "PMPLATRORM_REGISTRATION_CODE");
        params.put("phone", phone1);
        String json = new Gson().toJson(params);
        Log.e("getPhoneCode", json);
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
//                    data = data.getJSONObject("data");
//                    uuid = data.getString("uuid");
//                    ToastUtil.showToast(RegisterActivity.this, "验证码已发送，请注意查收");
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(String error) {
//                tv_send.setEnabled(true);
//                if (dialog.isShowing())
//                    dialog.dismiss();
//                ToastUtil.showToast(RegisterActivity.this, "超时！");
//            }
//        });
        NetUtils.getInstance().post(Api.User.sendCode, json, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                if (dialog.isShowing())
                    dialog.dismiss();
                mTimeCount.start();
                Log.e("result", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.getJSONObject("data");
                    data = data.getJSONObject("data");
                    uuid = data.getString("uuid");
                    ToastUtil.showToast(RegisterActivity.this, "验证码已发送，请注意查收");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                tv_send.setEnabled(true);
                if (dialog.isShowing())
                    dialog.dismiss();
                ToastUtil.showToast(RegisterActivity.this, "超时！");
            }
        });
    }

}
