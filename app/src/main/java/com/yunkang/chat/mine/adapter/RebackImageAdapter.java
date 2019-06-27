package com.yunkang.chat.mine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yunkang.chat.R;

import java.util.List;

/**
 * 作者：凌涛 on 2019/1/21 15:38
 * 邮箱：771548229@qq..com
 */
public class RebackImageAdapter  extends BaseAdapter{
    List<String> imagesList;
    Context mContext;

    public RebackImageAdapter(List<String> imagesList, Context context) {
        this.imagesList = imagesList;
        mContext = context;
    }

    @Override
    public int getCount() {
        return imagesList.size();
    }

    @Override
    public Object getItem(int position) {
        return imagesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView= LayoutInflater.from(mContext).inflate(R.layout.item_gv_reback,null);
        return convertView;
    }
}
