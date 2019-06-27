package com.yunkang.chat.study;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yunkang.chat.R;
import com.yunkang.chat.study.adapter.BasePagerAdapter;
import com.yunkang.chat.study.fragment.CallbackVideoFragment;
import com.yunkang.chat.study.fragment.CommentFragment;
import com.yunkang.chat.study.fragment.X5DetailFragment;

public class VideoContentActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_content);
        initView();
        setViewpagerAdapter();
    }
    private void initView() {
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.id_stickynavlayout_viewpager);
    }

    private void setViewpagerAdapter() {
        BasePagerAdapter mAdapter = new BasePagerAdapter(getSupportFragmentManager());
        CommentFragment commentFragment =new CommentFragment();
        X5DetailFragment x5DetailFragment = new X5DetailFragment();
        mAdapter.addFragment(commentFragment, "评论");
        mAdapter.addFragment(x5DetailFragment, "详情");
        viewPager.setAdapter(mAdapter);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
    }
}
