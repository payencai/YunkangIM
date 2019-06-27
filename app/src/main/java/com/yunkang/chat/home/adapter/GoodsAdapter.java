package com.yunkang.chat.home.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunkang.chat.R;
import com.yunkang.chat.home.model.Goods;
import com.yunkang.chat.home.model.Record;

import java.util.List;

public class GoodsAdapter extends BaseQuickAdapter<Goods, BaseViewHolder> {
    GoodsChildAdapter recordChildAdapter;
    public GoodsAdapter(int layoutResId, @Nullable List<Goods> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Goods item) {
        RecyclerView rv_child=helper.getView(R.id.rv_child);
        recordChildAdapter=new GoodsChildAdapter(R.layout.item_shopcar_child,item.getChildList());
        rv_child.setLayoutManager(new LinearLayoutManager(helper.itemView.getContext()));
        rv_child.setAdapter(recordChildAdapter);
    }
}
