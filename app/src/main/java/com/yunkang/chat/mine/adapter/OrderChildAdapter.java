package com.yunkang.chat.mine.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunkang.chat.R;
import com.yunkang.chat.home.model.Record;
import com.yunkang.chat.mine.model.Order;

import java.util.List;

public class OrderChildAdapter extends BaseQuickAdapter<Order.OrderProductsBean, BaseViewHolder> {
    public OrderChildAdapter(int layoutResId, @Nullable List<Order.OrderProductsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Order.OrderProductsBean item) {

        ImageView iv_logo = helper.getView(R.id.iv_logo);
        TextView tv_name = helper.getView(R.id.tv_name);
        TextView tv_shop = helper.getView(R.id.tv_shop);
        TextView tv_count = helper.getView(R.id.tv_count);
        TextView tv_price = helper.getView(R.id.tv_price);

        tv_shop.setText(item.getSupplierName());
//        if(TextUtils.isEmpty(item.getLaboratoryAddress())||"null".equals(item.getLaboratoryAddress())){
//            tv_shop.setText(item.getSupplierName());
//        }
        tv_name.setText(item.getProductName());
        tv_price.setText("￥"+item.getProductPrice());
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
        if(item.getNum()>1){
            tv_count.setText("购买数量："+item.getNum());
        }

    }
}
