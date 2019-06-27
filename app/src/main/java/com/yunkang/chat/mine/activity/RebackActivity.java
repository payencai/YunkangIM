package com.yunkang.chat.mine.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunkang.chat.MyApplication;
import com.yunkang.chat.R;
import com.yunkang.chat.h5.H5CommomActivity;
import com.yunkang.chat.mine.adapter.RebackImageAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RebackActivity extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.gv_reback)
    GridView gv_reback;
    List<String> images;
    RebackImageAdapter mRebackImageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reback);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tv_title.setText("意见反馈");
        iv_back.setOnClickListener(this);
        images=new ArrayList<>();
        images.add("hhhh");
        mRebackImageAdapter=new RebackImageAdapter(images,RebackActivity.this);
        gv_reback.setAdapter(mRebackImageAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;

        }
    }
}
