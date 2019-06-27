package com.yunkang.chat.home.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunkang.chat.R;
import com.yunkang.chat.home.model.Goods;
import com.yunkang.chat.home.model.Record;

import java.util.List;

public class GoodsChildAdapter extends BaseQuickAdapter<Goods.Child, BaseViewHolder> {
    public GoodsChildAdapter(int layoutResId, @Nullable List<Goods.Child> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Goods.Child item) {
        ImageView iv_logo = helper.getView(R.id.iv_logo);
        Glide.with(helper.itemView.getContext()).load("http://img.1sucai.com/tuku/yulantu/110611/9120-110611114P085.jpg")
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(20)))
                .into(iv_logo);
    }
}
