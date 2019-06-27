package com.yunkang.chat.mine.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunkang.chat.MyApplication;
import com.yunkang.chat.R;
import com.yunkang.chat.h5.H5CommomActivity;
import com.yunkang.chat.http.HttpProxy;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BindAlipayActivity extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_alipay);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tv_title.setText("绑定支付宝");
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;

        }
    }
}
