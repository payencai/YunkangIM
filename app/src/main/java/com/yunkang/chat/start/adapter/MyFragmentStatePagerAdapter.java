package com.yunkang.chat.start.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import java.util.List;

/**
 * 作者：凌涛 on 2019/3/31 16:29
 * 邮箱：771548229@qq..com
 */
public class MyFragmentStatePagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments = null;
    private List<String> titles = null;
    private Context context;

    public MyFragmentStatePagerAdapter(Context context, FragmentManager fm, List<Fragment> fragments,List<String> titles) {
        super(fm);
        this.context = context;
        this.fragments = fragments;
        this.titles=titles;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        return titles.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

}
