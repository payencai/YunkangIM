package com.yunkang.chat.start.activity;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.yunkang.chat.R;
import com.yunkang.chat.tools.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BindPhoneActivity extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_confirm)
    TextView tv_confirm;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_send)
    TextView tv_send;
    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.et_invate)
    EditText et_invate;
    @BindView(R.id.et_code)
    EditText et_code;
    int count = 60;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tv_title.setText("绑定手机");
        iv_back.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);
        tv_send.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_confirm:
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_send:
                String phone1 = et_phone.getEditableText().toString();
                if (TextUtils.isEmpty(phone1)) {
                    ToastUtil.showToast(this, "请输入手机号！");
                    return;
                }
                tv_send.setBackgroundResource(R.drawable.bg_unselect_sendcode);
                tv_send.setTextColor(getResources().getColor(R.color.color_999));
                tv_send.setEnabled(false);
                ToastUtil.showToast(this, "验证码已发送，请注意查收");
                new CountDownTimer(60 * 1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        count--;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //倒计时的过程中回调该函数
                                tv_send.setText(count + "s");
                                if (count == 0) {
                                    count=60;
                                    tv_send.setText("获取验证码");
                                    tv_send.setEnabled(true);
                                    tv_send.setBackgroundResource(R.drawable.bg_shape_sendcode);
                                    tv_send.setTextColor(getResources().getColor(R.color.color_blue));
                                }
                            }
                        });


                    }

                    @Override
                    public void onFinish() {
                        //倒计时结束时回调该函数
                    }
                }.start();
                getPhoneCode(phone1);
                break;
        }
    }

    private void getPhoneCode(String phone1) {
    }
}
