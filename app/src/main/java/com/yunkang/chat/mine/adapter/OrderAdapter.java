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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends BaseQuickAdapter<Order, BaseViewHolder> {
    OrderChildAdapter recordChildAdapter;
    public OrderAdapter(int layoutResId, @Nullable List<Order> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Order item) {
        helper.setIsRecyclable(false);
        RecyclerView rv_child=helper.getView(R.id.rv_child);
        TextView tv_status=helper.getView(R.id.tv_status);
        LinearLayout ll_item=helper.getView(R.id.ll_item);
        TextView tv_wuliu=helper.getView(R.id.tv_wuliu);
        TextView tv_delete=helper.getView(R.id.tv_delete);
        TextView tv_name=helper.getView(R.id.tv_name);
        TextView tv_date=helper.getView(R.id.tv_date);
        TextView tv_pay=helper.getView(R.id.tv_pay);
        tv_pay.setVisibility(View.GONE);
        helper.addOnClickListener(R.id.tv_pay).addOnClickListener(R.id.tv_delete);
        TextView tv_orderNumber=helper.getView(R.id.tv_orderNumber);
        tv_orderNumber.setText("订单号："+item.getId());
        String state=item.getPayState();
        int paystate=0;
        if(!TextUtils.isEmpty(state)){
            paystate= Integer.parseInt(state);
        }
        if(item.getOrderLogisticsApply()!=null){
            tv_wuliu.setVisibility(View.VISIBLE);
        }else{
            tv_wuliu.setVisibility(View.GONE);
        }
        tv_name.setText("受检信息："+item.getPatientName());
        if(!TextUtils.isEmpty(item.getCreateDate()))
            tv_date.setText(item.getCreateDate().substring(5,7)+"/"+item.getCreateDate().substring(8,10));

        switch (paystate){
            case 1:
                tv_status.setText("等待付款");
                tv_pay.setVisibility(View.VISIBLE);
                tv_delete.setVisibility(View.VISIBLE);
                break;
            case 2:
                tv_status.setText("已支付");
                tv_pay.setVisibility(View.GONE);
                tv_delete.setVisibility(View.GONE);
                break;
            case 3:
                tv_status.setText("标本已寄送");
                tv_pay.setVisibility(View.GONE);
                tv_delete.setVisibility(View.GONE);
                break;
            case 4:
                tv_status.setText("已收标本");
                tv_pay.setVisibility(View.GONE);
                tv_delete.setVisibility(View.GONE);
                break;
            case 5:
                tv_status.setText("检测中");
                tv_pay.setVisibility(View.GONE);
                tv_delete.setVisibility(View.GONE);
                break;
            case 6:
                tv_delete.setVisibility(View.GONE);
                tv_pay.setText("确认收货");
                tv_pay.setVisibility(View.VISIBLE);
                tv_status.setText("已寄送");
                break;
            case 7:
                tv_pay.setVisibility(View.VISIBLE);
                tv_pay.setText("立即评价");
                tv_delete.setVisibility(View.VISIBLE);
                tv_status.setText("等待评价");
                break;
            case 8:
            case 9:
                tv_pay.setVisibility(View.GONE);
                tv_delete.setVisibility(View.VISIBLE);
                tv_status.setText("交易完成");
                break;
            case 10:

                tv_pay.setText("确认收货");
                tv_pay.setVisibility(View.VISIBLE);
                tv_delete.setVisibility(View.GONE);
                tv_status.setText("已出报告");
                break;
            case 99:
                tv_delete.setVisibility(View.VISIBLE);
                tv_pay.setVisibility(View.GONE);
                tv_status.setText("订单失效");

                break;
        }

        List<Order.OrderProductsBean> orderProductsBeans=new ArrayList<>();
        String oldProductId=item.getOrderProducts().get(0).getProductId();
        String oldLabs=item.getOrderProducts().get(0).getLaboratory();
        String remarks="3-5";
        Order.OrderProductsBean oldProduct=item.getOrderProducts().get(0);
        oldProduct.setPayState(paystate);
        int count=0;
        for (int i = 0; i <item.getOrderProducts().size() ; i++) {
            Order.OrderProductsBean orderProductsBean=item.getOrderProducts().get(i);
            if(i==0){
                remarks=orderProductsBean.getRemarks();
            }
            orderProductsBean.setPayState(paystate);
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
        TextView tv_time = helper.getView(R.id.tv_time);
        LinearLayout ll_time=helper.getView(R.id.ll_time);

        if(item.getPayState().equals("7")||item.getPayState().equals("8")||item.getPayState().equals("9")||item.getPayState().equals("10")) {
            ll_time.setVisibility(View.GONE);
        }else{
            ll_time.setVisibility(View.VISIBLE);
            tv_time.setText("预计出报告时间："+remarks+"个工作日");
        }

        if(paystate==99||paystate==10){
            ll_time.setVisibility(View.GONE);
        }
        recordChildAdapter=new OrderChildAdapter(R.layout.item_order_child,orderProductsBeans);
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
