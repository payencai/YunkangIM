package com.yunkang.chat.home.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunkang.chat.R;
import com.yunkang.chat.home.model.Collect;
import com.yunkang.chat.home.model.WaitToDo;
import com.yunkang.chat.mine.model.Order;

import java.util.ArrayList;
import java.util.List;

public class WaitodoAdapter extends BaseQuickAdapter<Order, BaseViewHolder> {
    WaitChildAdapter waitChildAdapter;

    public WaitodoAdapter(int layoutResId, @Nullable List<Order> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Order item) {
        helper.setIsRecyclable(false);
        RecyclerView rv_child = helper.getView(R.id.rv_child);
        TextView tv_time = helper.getView(R.id.tv_time);
        TextView tv_status = helper.getView(R.id.tv_status);
        TextView tv_kaidan = helper.getView(R.id.tv_kaidan);
        TextView tv_delete = helper.getView(R.id.tv_delete);
        helper.addOnClickListener(R.id.tv_kaidan);
        tv_time.setText(item.getCreateDate());
        List<Order.OrderProductsBean> orderProductsBeans=new ArrayList<>();
        String oldProductId=item.getOrderProducts().get(0).getProductId();
        String oldLabs=item.getOrderProducts().get(0).getLaboratory();
        Order.OrderProductsBean oldProduct=item.getOrderProducts().get(0);
        int count=0;
        for (int i = 0; i <item.getOrderProducts().size() ; i++) {
            Order.OrderProductsBean orderProductsBean=item.getOrderProducts().get(i);
            String currentProductId=orderProductsBean.getProductId();
            String currentLabs=orderProductsBean.getLaboratory();
            if(!currentProductId.equals(oldProductId)){
                oldProductId=currentProductId;
                oldLabs=currentLabs;
                oldProduct.setNum(count);
                orderProductsBeans.add(oldProduct);
                oldProduct=orderProductsBean;
                count=1;
            }else{
                if(oldLabs.equals(currentLabs)){
                    count++;
                    int length=item.getOrderProducts().size()-1;
                    if(i==length){
                        oldProduct.setNum(count);
                        orderProductsBeans.add(oldProduct);
                    }
                }else{
                    oldProductId=currentProductId;
                    oldLabs=currentLabs;
                    oldProduct.setNum(count);
                    orderProductsBeans.add(oldProduct);
                    oldProduct=orderProductsBean;
                    count=1;
                }
            }
        }
        waitChildAdapter = new WaitChildAdapter(R.layout.item_undo_child, orderProductsBeans);
        rv_child.setLayoutManager(new LinearLayoutManager(helper.itemView.getContext()));
        rv_child.setAdapter(waitChildAdapter);
        String state = item.getPayState();
        switch (state) {
            case "1":
                tv_status.setText("等待付款");
                tv_kaidan.setText("付款");
                tv_kaidan.setVisibility(View.VISIBLE);
                tv_delete.setVisibility(View.GONE);
                break;
            case "2":
            case "3":
            case "4":
            case "5":
                tv_status.setText("等待录入");
                tv_kaidan.setText("信息录入");
                tv_delete.setVisibility(View.GONE);
                break;
            case "8":
                tv_status.setText("继续开单");
                tv_kaidan.setVisibility(View.VISIBLE);
                tv_delete.setVisibility(View.VISIBLE);
                break;
            case "7":
                tv_status.setText("等待评价");
                tv_kaidan.setText("立即评价");
                tv_kaidan.setVisibility(View.VISIBLE);
                tv_delete.setVisibility(View.GONE);
                break;
        }
    }
}
