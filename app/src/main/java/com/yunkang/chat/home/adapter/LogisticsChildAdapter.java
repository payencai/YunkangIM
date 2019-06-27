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
import com.yunkang.chat.home.model.Logistics;
import com.yunkang.chat.home.model.Record;
import com.yunkang.chat.mine.model.Order;

import java.util.List;

public class LogisticsChildAdapter extends BaseQuickAdapter<Order.OrderProductsBean, BaseViewHolder> {
    public LogisticsChildAdapter(int layoutResId, @Nullable List<Order.OrderProductsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper,Order.OrderProductsBean item) {
        ImageView iv_logo = helper.getView(R.id.iv_logo);
        TextView tv_name=helper.getView(R.id.tv_name);
        TextView tv_price=helper.getView(R.id.tv_price);
        TextView tv_labs=helper.getView(R.id.tv_labs);
        tv_name.setText(item.getProductName());
        tv_price.setText("￥"+item.getRealPrice());
        tv_labs.setText(item.getSupplierName());
        if (!TextUtils.isEmpty(item.getImage())) {
            if(item.getImage().contains(",")){
                String imgs[] = item.getImage().split(",");
                Glide.with(helper.itemView.getContext()).load(imgs[0])
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(20)))
                        .into(iv_logo);
            }else{
                Glide.with(helper.itemView.getContext()).load(item.getImage())
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(20)))
                        .into(iv_logo);
            }

        }
    }
}
