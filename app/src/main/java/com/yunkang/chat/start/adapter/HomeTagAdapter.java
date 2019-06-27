package com.yunkang.chat.start.adapter;

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
public class HomeTagAdapter extends TagAdapter<String> {

    public HomeTagAdapter(List<String> datas) {
        super(datas);
    }


    @Override
    public View getView(FlowLayout parent, int position, String categoryTag) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_tag,null);
        TextView tv = view.findViewById(R.id.tv_tag);
        tv.setText(categoryTag);
        return tv;
    }
}
