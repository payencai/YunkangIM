package com.yunkang.chat.mine.activity;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kaopiz.kprogresshud.KProgressHUD;

import com.yunkang.chat.MyApplication;
import com.yunkang.chat.R;
import com.yunkang.chat.api.Api;
import com.yunkang.chat.http.HttpProxy;
import com.yunkang.chat.http.ICallBack;
import com.yunkang.chat.net.NetUtils;
import com.yunkang.chat.net.OnMessageReceived;
import com.yunkang.chat.tools.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.leefeng.promptlibrary.PromptDialog;

public class UpdateNicknameActivity extends AppCompatActivity {
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.tv_save)
    TextView tv_save;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_nickname);
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

    }
    private void initView() {


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if(MyApplication.getUserInfo()!=null){
            et_name.setText(MyApplication.getUserInfo().getNickName());
        }
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=et_name.getEditableText().toString();
                if(!TextUtils.isEmpty(name)){
                    updateNick(name);
                }else{
                    ToastUtil.showToast(UpdateNicknameActivity.this, "输入不能为空");
                }

            }
        });
    }

    private void updateNick(String name) {
        showLoading("提交中");
//        Map<String, Object> params = new HashMap<>();
//        params.put("nickName", name);
//        params.put("id", MyApplication.getUserInfo().getId());
//        // params.put("type", MyApplication.getUserInfo().getType());
//        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
//        String json = gson.toJson(params);
//        Log.e("json",json);
//        HttpProxy.obtain().post(Api.User.updateUserByUser, MyApplication.token, json, new ICallBack() {
//            @Override
//            public void OnSuccess(String result) {
//                if(dialog.isShowing())
//                    dialog.dismiss();
//                Log.e("upload", result);
//                try {
//                    JSONObject object = new JSONObject(result);
//                    int resultCode = object.getInt("code");
//                    if (resultCode == 0) {
//                        ToastUtil.showToast(UpdateNicknameActivity.this, "更新成功");
//                        MyApplication.getUserInfo().setNickName(name);
//                        finish();
//                    } else {
//                        String msg = object.getString("message");
//                        ToastUtil.showToast(UpdateNicknameActivity.this, msg);
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(String error) {
//                if(dialog.isShowing())
//                    dialog.dismiss();
//            }
//        });

        Map<String, String> params = new HashMap<>();
        params.put("nickName", name);
        params.put("id", MyApplication.getUserInfo().getId());
        NetUtils.getInstance().post( Api.User.updateUserByUser, params,MyApplication.token, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                if(dialog.isShowing())
                    dialog.dismiss();
                Log.e("upload", response);
                if(response.contains("data")){
                    try {
                        JSONObject object = new JSONObject(response);
                        int resultCode = object.getInt("code");
                        if (resultCode == 0) {
                            ToastUtil.showToast(UpdateNicknameActivity.this, "更新成功");
                            MyApplication.getUserInfo().setNickName(name);
                            finish();
                        } else {
                            String msg = object.getString("msg");
                            ToastUtil.showToast(UpdateNicknameActivity.this, msg);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        JSONObject object = new JSONObject(response);
                        int resultCode = object.getInt("resultCode");
                        if (resultCode == 0) {

                        } else {
                            String msg = object.getString("message");
                            ToastUtil.showToast(UpdateNicknameActivity.this, msg);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onError(String error) {
                if(dialog.isShowing())
                    dialog.dismiss();
            }
        });

    }

}
