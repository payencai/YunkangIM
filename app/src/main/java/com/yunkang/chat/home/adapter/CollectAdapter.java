package com.yunkang.chat.home.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunkang.chat.R;
import com.yunkang.chat.home.model.Collect;


import java.util.List;

public class CollectAdapter extends BaseQuickAdapter<Collect, BaseViewHolder> {
    public CollectAdapter(int layoutResId, @Nullable List<Collect> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Collect item) {
        ImageView iv_logo = helper.getView(R.id.iv_logo);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.mipmap.bg_test)
                .error(R.mipmap.bg_test).apply(RequestOptions.bitmapTransform(new RoundedCorners(20)));
        if(!TextUtils.isEmpty(item.getCoverImages())){
            if(item.getCoverImages().contains(",")){
                String[] images=item.getCoverImages().split(",");
                Glide.with(mContext).load(images[0])
                        .apply(requestOptions)
                        .into(iv_logo);
            }else{
                Glide.with(mContext).load(item.getCoverImages())
                        .apply(requestOptions)
                        .into(iv_logo);
            }

        }
        TextView tv_name=helper.getView(R.id.tv_name);
        TextView tv_price=helper.getView(R.id.tv_price);
        tv_name.setText(item.getName());
        tv_price.setText("￥"+item.getRetailPrice());
        helper.addOnClickListener(R.id.tv_cancel);
    }
}
