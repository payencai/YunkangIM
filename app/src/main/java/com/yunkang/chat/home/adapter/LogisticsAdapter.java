package com.yunkang.chat.home.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunkang.chat.R;
import com.yunkang.chat.api.Api;
import com.yunkang.chat.home.model.Logistics;
import com.yunkang.chat.home.model.Record;
import com.yunkang.chat.mine.model.Order;

import java.util.List;

public class LogisticsAdapter extends BaseQuickAdapter<Order, BaseViewHolder> {
    LogisticsChildAdapter recordChildAdapter;
    int pos=0;
    public LogisticsAdapter(int layoutResId, @Nullable List<Order> data) {
        super(layoutResId, data);
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    @Override
    protected void convert(BaseViewHolder helper, Order item) {
        helper.addOnClickListener(R.id.iv_choose);
        ImageView iv_choose=helper.getView(R.id.iv_choose);
        RecyclerView rv_child=helper.getView(R.id.rv_child);
        recordChildAdapter=new LogisticsChildAdapter(R.layout.item_wuliu_child,item.getOrderProducts());
        rv_child.setLayoutManager(new LinearLayoutManager(helper.itemView.getContext()));
        rv_child.setAdapter(recordChildAdapter);
        if(pos==helper.getAdapterPosition()){
            iv_choose.setImageResource(R.mipmap.ic_choose);
        }else{
            iv_choose.setImageResource(R.mipmap.ic_unchoose);
        }
    }
}
