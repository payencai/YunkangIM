package com.yunkang.chat.mine.activity;

import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;

import com.yunkang.chat.MyApplication;
import com.yunkang.chat.R;
import com.yunkang.chat.api.Api;
import com.yunkang.chat.http.HttpProxy;
import com.yunkang.chat.http.ICallBack;
import com.yunkang.chat.net.NetUtils;
import com.yunkang.chat.net.OnMessageReceived;
import com.yunkang.chat.start.activity.ForgetPasswordActivity;
import com.yunkang.chat.tools.CheckDoubleClick;
import com.yunkang.chat.tools.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.leefeng.promptlibrary.PromptDialog;

public class UpdatePasswordActivity extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_send)
    TextView tv_send;
    @BindView(R.id.tv_confirm)
    TextView tv_confirm;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.et_pwd2)
    EditText et_pwd2;
    @BindView(R.id.et_pwd)
    EditText et_pwd;
    @BindView(R.id.et_code)
    EditText et_code;
    int count = 60;
    String uuid = "1";
    String userId = "1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        ButterKnife.bind(this);
        initView();
    }
    TimeCount mTimeCount;
    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tv_send.setEnabled(false);
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



    private void initView() {

        mTimeCount=new TimeCount(60000,1000);
        tv_title.setText("修改密码");
        iv_back.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);
        tv_send.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        if(CheckDoubleClick.isFastDoubleClick()){
            return;
        }
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_send:
                getUserId(MyApplication.getUserInfo().getTelephone());
                break;
            case R.id.tv_confirm:
                submit();
                break;

        }
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
    private void submit(){
        String pwd = et_pwd.getEditableText().toString();
        String pwd2 = et_pwd2.getEditableText().toString();
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
            ToastUtil.showToast(this, "请先去发送验证码");
            return;
        }
        reSetPassword(MyApplication.getUserInfo().getTelephone(), et_code.getEditableText().toString(),pwd);
    }
    private void getPhoneCode(String phone1) {
//        showLoading("发送中");
//        tv_send.setEnabled(false);
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
//                if(result.contains("data")) {
//                    Log.e("result", result);
//                    try {
//                        JSONObject jsonObject = new JSONObject(result);
//                        JSONObject data = jsonObject.getJSONObject("data");
//                        String msg = jsonObject.getString("msg");
//                        int code = jsonObject.getInt("code");
//                        if (code == 0) {
//                            mTimeCount.start();
//                            ToastUtil.showToast(UpdatePasswordActivity.this, "验证码已发送，请注意查收");
//                            data=data.getJSONObject("data");
//                            uuid = data.getString("uuid");
//
//                        } else {
//                            ToastUtil.showToast(UpdatePasswordActivity.this, msg);
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }else{
//                    try {
//                        JSONObject jsonObject = new JSONObject(result);
//                        String msg=jsonObject.getString("message");
//                        ToastUtil.showToast(UpdatePasswordActivity.this, msg);
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
//                if (dialog.isShowing())
//                    dialog.dismiss();
//            }
//        });

        showLoading("发送中");
        tv_send.setEnabled(false);
        Map<String, String> params = new HashMap<>();
        //params.put("application", "pmplatform");
        params.put("code", "PMPLATRORM_RESET_PASSWORD");
        params.put("phone", phone1);
        NetUtils.getInstance().post(Api.User.sendCode, params, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                if (dialog.isShowing())
                    dialog.dismiss();
                if(response.contains("data")) {
                    Log.e("result", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject data = jsonObject.getJSONObject("data");
                        String msg = jsonObject.getString("msg");
                        int code = jsonObject.getInt("code");
                        if (code == 0) {
                            mTimeCount.start();
                            ToastUtil.showToast(UpdatePasswordActivity.this, "验证码已发送，请注意查收");
                            data=data.getJSONObject("data");
                            uuid = data.getString("uuid");

                        } else {
                            ToastUtil.showToast(UpdatePasswordActivity.this, msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String msg=jsonObject.getString("message");
                        ToastUtil.showToast(UpdatePasswordActivity.this, msg);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(String error) {
                if (dialog.isShowing())
                    dialog.dismiss();
            }
        });

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
//                            ToastUtil.showToast(UpdatePasswordActivity.this, msg);
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                   ToastUtil.showToast(UpdatePasswordActivity.this, "用户不存在");
//                }
//            }
//
//            @Override
//            public void onFailure(String error) {
//                tv_send.setEnabled(true);
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
                            ToastUtil.showToast(UpdatePasswordActivity.this, msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    ToastUtil.showToast(UpdatePasswordActivity.this, "用户不存在");
                }
            }

            @Override
            public void onError(String error) {
                tv_send.setEnabled(true);
            }
        });

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
//                if (dialog.isShowing())
//                    dialog.dismiss();
//                tv_confirm.setEnabled(true);
//
//                if (result.contains("data")) {
//                    try {
//                        JSONObject data = new JSONObject(result);
//                        String msg = data.getString("msg");
//                        int code = data.getInt("code");
//                        if (code == 0) {
//                            ToastUtil.showToast(UpdatePasswordActivity.this, "重置成功");
//                            finish();
//                        } else {
//                            ToastUtil.showToast(UpdatePasswordActivity.this, msg);
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
//                        ToastUtil.showToast(UpdatePasswordActivity.this, msg);
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
//                ToastUtil.showToast(UpdatePasswordActivity.this, error + "");
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
                if (dialog.isShowing())
                    dialog.dismiss();
                tv_confirm.setEnabled(true);

                if (response.contains("data")) {
                    try {
                        JSONObject data = new JSONObject(response);
                        String msg = data.getString("msg");
                        int code = data.getInt("code");
                        if (code == 0) {
                            ToastUtil.showToast(UpdatePasswordActivity.this, "重置成功");
                            finish();
                        } else {
                            ToastUtil.showToast(UpdatePasswordActivity.this, msg);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String msg = jsonObject.getString("message");
                        ToastUtil.showToast(UpdatePasswordActivity.this, msg);

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
                ToastUtil.showToast(UpdatePasswordActivity.this, error + "");
            }
        });

    }
}
