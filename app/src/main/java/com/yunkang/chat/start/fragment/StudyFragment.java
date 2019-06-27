package com.yunkang.chat.start.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.iapppay.utils.v;
import com.yunkang.chat.R;
import com.yunkang.chat.start.View.NoScrollViewPager;
import com.yunkang.chat.start.adapter.MyPagerAdapter;
import com.yunkang.chat.study.fragment.CallbackVideoFragment;
import com.yunkang.chat.study.fragment.LiveVideoFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudyFragment extends Fragment {
    List<String> mTitle;
    ArrayList<Fragment> mFragments;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.tab_study)
    SlidingTabLayout tab_study;
    MyPagerAdapter myPagerAdapter;
    String []mTitles={"直播"};
    public StudyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_study, container, false);
        ButterKnife.bind(this,view);
        initView();
        return view;
    }

    private void initView() {
        mTitle=new ArrayList<>();
        mFragments=new ArrayList<>();
        mTitle.add("直播");
        mTitle.add("回播");
        mFragments.add(new LiveVideoFragment());
        mFragments.add(new CallbackVideoFragment());
        myPagerAdapter = new MyPagerAdapter(getChildFragmentManager(), mFragments, mTitle);
        viewpager.setAdapter(myPagerAdapter);
        tab_study.setViewPager(viewpager);
        tab_study.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {

            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

}
