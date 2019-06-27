package com.yunkang.chat.category.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.yunkang.chat.R;
import com.yunkang.chat.category.model.CheckSecond;
import com.yunkang.chat.home.model.HomeCategory;

import java.util.List;

/**
 * 作者：凌涛 on 2019/1/3 20:04
 * 邮箱：771548229@qq..com
 */
public class CheckSecondAdapter extends BaseAdapter {
    private List<HomeCategory> mClassItems;
    private Context mContext;
    private int selectedPosition = 0;// 选中的位置

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    public CheckSecondAdapter(Context context, List<HomeCategory> classItems) {
        mClassItems = classItems;
        mContext = context;
    }


    @Override
    public int getCount() {
        //return mClassItems.size();
        return mClassItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mClassItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_category_second, null);
        TextView tv_name = convertView.findViewById(R.id.tv_name);
        ImageView iv_logo = convertView.findViewById(R.id.iv_logo);
        tv_name.setText(mClassItems.get(position).getName());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.mipmap.bg_test)
                .error(R.mipmap.bg_test).apply(RequestOptions.bitmapTransform(new RoundedCorners(20)));
        Glide.with(mContext).load(mClassItems.get(position).getIcon())
                .apply(requestOptions)
                .into(iv_logo);
        return convertView;
    }
}
