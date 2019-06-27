package com.yunkang.chat.home.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunkang.chat.home.model.OpenTickets;
import com.yunkang.chat.home.model.TicketsRecord;

import java.util.List;

public class TicketsRecordAdapter extends BaseQuickAdapter<TicketsRecord, BaseViewHolder> {
    public TicketsRecordAdapter(int layoutResId, @Nullable List<TicketsRecord> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TicketsRecord item) {

    }
}
