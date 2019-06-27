package com.yunkang.chat.home.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunkang.chat.R;
import com.yunkang.chat.api.Api;
import com.yunkang.chat.home.model.Record;
import com.yunkang.chat.home.model.WaitToDo;
import com.yunkang.chat.mine.model.Order;

import java.util.ArrayList;
import java.util.List;

public class RecordAdapter extends BaseQuickAdapter<Order, BaseViewHolder> {
    RecordChildAdapter recordChildAdapter;
    public RecordAdapter(int layoutResId, @Nullable List<Order> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Order item) {
        RecyclerView rv_child=helper.getView(R.id.rv_child);
        TextView tv_time=helper.getView(R.id.tv_time);
        TextView tv_order=helper.getView(R.id.tv_orderNumber);
        tv_time.setText(item.getCreateDate().substring(0,10));
        tv_order.setText("订单号："+item.getId());
        helper.addOnClickListener(R.id.tv_luru);
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
        recordChildAdapter=new RecordChildAdapter(R.layout.item_record_child,orderProductsBeans);
        rv_child.setLayoutManager(new LinearLayoutManager(helper.itemView.getContext()));
        rv_child.setAdapter(recordChildAdapter);
    }
}
