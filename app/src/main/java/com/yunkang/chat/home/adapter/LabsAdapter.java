package com.yunkang.chat.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunkang.chat.R;
import com.yunkang.chat.home.model.Labs;

import java.util.List;



/**
 * 作者：凌涛 on 2019/1/23 17:50
 * 邮箱：771548229@qq..com
 */
public class LabsAdapter extends BaseAdapter {
    Context mContext;
    List<Labs> mLabs;

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    int pos=0;
    public LabsAdapter(Context context, List<Labs> labs) {
        mContext = context;
        mLabs = labs;
    }

    @Override
    public int getCount() {
        return mLabs.size();
    }

    @Override
    public Object getItem(int position) {
        return mLabs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView= LayoutInflater.from(mContext).inflate(R.layout.item_labs,null);
        TextView tv_name=convertView.findViewById(R.id.tv_name);
        TextView tv_province=convertView.findViewById(R.id.tv_province);
        ImageView iv_select=convertView.findViewById(R.id.iv_select);
        tv_name.setText(mLabs.get(position).getName());
        tv_province.setText(mLabs.get(position).getProvince());
        if(pos==position){
            iv_select.setImageResource(R.mipmap.ic_choose);
        }else{
            iv_select.setImageResource(R.mipmap.ic_unchoose);
        }
        return convertView;
    }
}
