package com.yunkang.chat.start.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 作者：凌涛 on 2019/1/18 15:58
 * 邮箱：771548229@qq..com
 */
public class GuidePageAdapter extends PagerAdapter {
    List<View> mViews;
    Context mContext;

    public GuidePageAdapter(List<View> views, Context context) {
        mViews = views;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViews.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mViews.get(position));
        return mViews.get(position);
    }

}
