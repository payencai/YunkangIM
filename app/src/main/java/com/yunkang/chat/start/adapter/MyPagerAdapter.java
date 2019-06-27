package com.yunkang.chat.start.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;


public class MyPagerAdapter extends FragmentPagerAdapter {
    List<Fragment> fragmentList;
    List<String> mTitles;

    public MyPagerAdapter(FragmentManager fm,List<Fragment> fragments,List<String> mTitles) {
        super(fm);
        this.mTitles=mTitles;
        this.fragmentList=fragments;
    }


    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mTitles.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}
