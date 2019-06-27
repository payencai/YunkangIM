package com.yunkang.chat.category.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.yunkang.chat.R;
import com.yunkang.chat.category.model.CategoryTag;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;

import java.util.List;

/**
 * 作者：凌涛 on 2019/1/22 18:40
 * 邮箱：771548229@qq..com
 */
public class CategoryTagAdapter extends TagAdapter<CategoryTag> {

    public CategoryTagAdapter(List<CategoryTag> datas) {
        super(datas);
    }

    @Override
    public void onSelected(int position, View view) {
        TextView tv_tag= (TextView) view;
        tv_tag.setTextColor(view.getContext().getResources().getColor(R.color.white));
        tv_tag.setBackgroundResource(R.drawable.bg_search);
        super.onSelected(position, tv_tag);
    }

    @Override
    public void unSelected(int position, View view) {
        TextView tv_tag= (TextView) view;
        tv_tag.setTextColor(view.getContext().getResources().getColor(R.color.color_666));
        tv_tag.setBackgroundResource(R.color.white);
        super.unSelected(position, tv_tag);

    }

    @Override
    public View getView(FlowLayout parent, int position, CategoryTag categoryTag) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag,null);
        TextView tv = view.findViewById(R.id.tv_tag);
        tv.setText(categoryTag.getName());
        return tv;
    }
}
