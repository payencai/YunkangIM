package com.yunkang.chat.start.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


import com.yunkang.chat.MyApplication;
import com.yunkang.chat.R;
import com.yunkang.chat.start.adapter.GuidePageAdapter;
import com.yunkang.chat.tools.SharePreferenceUtils;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity {
    ViewPager vp_guide;
    List<View> viewList;
    View view1;
    View view2;
    View view3;
    View view4;
    TextView tv_submit;
    GuidePageAdapter mGuidePageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        SharePreferenceUtils.putString(GuideActivity.this,"enter","notfirst");
        vp_guide=findViewById(R.id.vp_guide);
        LayoutInflater lf = getLayoutInflater().from(this);
        view1 = lf.inflate(R.layout.guide_view1, null);
        view2 = lf.inflate(R.layout.guide_view2, null);
        view3 = lf.inflate(R.layout.guide_view3, null);
        view4 = lf.inflate(R.layout.guide_view4, null);
        viewList = new ArrayList<View>();
        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);
        viewList.add(view4);
        tv_submit=view4.findViewById(R.id.tv_submit);
        mGuidePageAdapter=new GuidePageAdapter(viewList,this);
        vp_guide.setAdapter(mGuidePageAdapter);

        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuideActivity.this,LoginActivity.class));
                finish();
            }
        });

    }
}
