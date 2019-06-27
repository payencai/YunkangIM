package com.yunkang.chat.home.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunkang.chat.R;
import com.yunkang.chat.home.model.Collect;
import com.yunkang.chat.home.model.OpenTickets;

import java.util.List;

public class OpenTicketsAdapter extends BaseQuickAdapter<OpenTickets, BaseViewHolder> {
    public OpenTicketsAdapter(int layoutResId, @Nullable List<OpenTickets> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OpenTickets item) {

    }
}
