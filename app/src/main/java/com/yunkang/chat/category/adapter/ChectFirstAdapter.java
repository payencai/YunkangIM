package com.yunkang.chat.category.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.yunkang.chat.R;
import com.yunkang.chat.category.model.CheckFirst;
import com.yunkang.chat.home.model.HomeCategory;

import java.util.List;

/**
 * 作者：凌涛 on 2019/1/3 20:04
 * 邮箱：771548229@qq..com
 */
public class ChectFirstAdapter extends BaseAdapter {
    private List<HomeCategory> mClassItems;
    private Context mContext;
    private int selectedPosition = 0;// 选中的位置

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    public ChectFirstAdapter(Context context, List<HomeCategory> classItems) {
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
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_category_first, null);
        View view_index = convertView.findViewById(R.id.view_index);
        TextView tv_name = convertView.findViewById(R.id.tv_name);
        tv_name.setText(mClassItems.get(position).getName());
        if (position == selectedPosition) {
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            view_index.setVisibility(View.VISIBLE);
        } else {
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.color_f7));
            view_index.setVisibility(View.GONE);
        }

        return convertView;
    }
}
