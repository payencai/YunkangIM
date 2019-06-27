package com.yunkang.chat.home.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yunkang.chat.R;
import com.yunkang.chat.home.model.HomeCategory;

import java.util.List;

/**
 * 作者：凌涛 on 2019/3/29 22:56
 * 邮箱：771548229@qq..com
 */
public class SearchTypeAdapter extends BaseAdapter {
    Context mContext;
    List<HomeCategory> mHomeCategories;

    public SearchTypeAdapter(Context context, List<HomeCategory> homeCategories) {
        mContext = context;
        mHomeCategories = homeCategories;
    }

    @Override
    public int getCount() {
        return mHomeCategories.size();
    }

    @Override
    public Object getItem(int position) {
        return mHomeCategories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HomeCategory homeCategory=mHomeCategories.get(position);
        convertView= LayoutInflater.from(mContext).inflate(R.layout.item_search_type,null);
        TextView tv_name=convertView.findViewById(R.id.tv_name);
        tv_name.setText(homeCategory.getName());
        return convertView;
    }
}
