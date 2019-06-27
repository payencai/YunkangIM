package com.yunkang.chat.home.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunkang.chat.home.model.Msg;
import com.yunkang.chat.home.model.SearchResult;
import com.yunkang.chat.start.model.Medicine;

import java.util.List;

public class SearchAdapter extends BaseQuickAdapter<Medicine, BaseViewHolder> {
    public SearchAdapter(int layoutResId, @Nullable List<Medicine> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Medicine item) {
//        ImageView iv_logo = helper.getView(R.id.iv_logo);
//        Glide.with(helper.itemView.getContext()).load("http://img.1sucai.com/tuku/yulantu/110611/9120-110611114P085.jpg")
//                .apply(RequestOptions.bitmapTransform(new RoundedCorners(20)))
//                .into(iv_logo);
    }
}
