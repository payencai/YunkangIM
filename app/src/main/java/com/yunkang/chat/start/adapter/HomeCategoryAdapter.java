package com.yunkang.chat.start.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yunkang.chat.R;
import com.yunkang.chat.home.model.HomeCategory;

import java.util.List;

/**
 * 作者：凌涛 on 2019/1/22 15:02
 * 邮箱：771548229@qq..com
 */
public class HomeCategoryAdapter extends BaseAdapter{
    Context mContext;
    List<HomeCategory> mHomeCategories;

    public HomeCategoryAdapter(Context context, List<HomeCategory> homeCategories) {
        mContext = context;
        mHomeCategories = homeCategories;
    }

    int pos=0;

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
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
        convertView= LayoutInflater.from(mContext).inflate(R.layout.item_home_category,null);
        TextView tv_category=convertView.findViewById(R.id.tv_category);
        View v_index=convertView.findViewById(R.id.view_index);
        tv_category.setText(mHomeCategories.get(position).getName());
        if(pos==position){
            tv_category.setTextColor(mContext.getResources().getColor(R.color.color_blue));
            v_index.setBackgroundColor(mContext.getResources().getColor(R.color.color_blue));
        }else{
            tv_category.setTextColor(mContext.getResources().getColor(R.color.color_999));
            v_index.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }
        return convertView;
    }
}
