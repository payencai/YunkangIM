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
import com.yunkang.chat.mine.model.Order;

import java.util.List;

public class SaleChildAdapter extends BaseQuickAdapter<Order.OrderProductsBean, BaseViewHolder> {
    public SaleChildAdapter(int layoutResId, @Nullable List<Order.OrderProductsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Order.OrderProductsBean item) {
        ImageView iv_logo = helper.getView(R.id.iv_logo);
        TextView tv_name=helper.getView(R.id.tv_name);
        TextView tv_price=helper.getView(R.id.tv_price);
        TextView tv_childstatus=helper.getView(R.id.tv_childstatus);
        TextView tv_labs=helper.getView(R.id.tv_labs);
        tv_name.setText(item.getProductName());
        tv_price.setText("￥"+item.getProductPrice());
        tv_labs.setText(item.getSupplierName());
        String images=item.getImage();
        tv_childstatus.setVisibility(View.VISIBLE);
        if("3".equals(item.getRefundType())){
            tv_childstatus.setText("已退款");
        }else if("4".equals(item.getRefundType())){
            tv_childstatus.setText("已驳回");
        }else if("2".equals(item.getRefundType())){
            tv_childstatus.setText("退款中");
        }
        if(!TextUtils.isEmpty(images)){
            if(images.contains(",")){
                String imgs[]=images.split(",");
                Glide.with(helper.itemView.getContext()).load(imgs[0])
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(20)))
                        .into(iv_logo);
            }else{
                Glide.with(helper.itemView.getContext()).load(images)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(20)))
                        .into(iv_logo);
            }
        }


    }
}
