package com.yunkang.chat.mine.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunkang.chat.R;
import com.yunkang.chat.mine.model.Order;

import java.util.ArrayList;
import java.util.List;

public class AfterSaleAdapter extends BaseQuickAdapter<Order, BaseViewHolder> {
    SaleChildAdapter recordChildAdapter;
    public AfterSaleAdapter(int layoutResId, @Nullable List<Order> data) {
        super(layoutResId, data);
    }
    List<Order.OrderProductsBean> orderProductsBeans;
    @Override
    protected void convert(BaseViewHolder helper, Order item) {
        RecyclerView rv_child=helper.getView(R.id.rv_child);
        TextView tv_orderNumber=helper.getView(R.id.tv_orderNumber);
        LinearLayout ll_item=helper.getView(R.id.ll_item);
        tv_orderNumber.setText("订单号："+item.getId());
        orderProductsBeans=new ArrayList<>();
        for (int i = 0; i <item.getOrderProducts().size() ; i++) {
            Order.OrderProductsBean orderProductsBean=item.getOrderProducts().get(i);
            orderProductsBeans.add(orderProductsBean);
        }
        recordChildAdapter=new SaleChildAdapter(R.layout.item_wuliu_child,orderProductsBeans);
        rv_child.setLayoutManager(new LinearLayoutManager(helper.itemView.getContext()));
        rv_child.setAdapter(recordChildAdapter);
        rv_child.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return ll_item.onTouchEvent(event);
            }
        });
    }
}
