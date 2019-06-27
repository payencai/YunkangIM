package com.yunkang.chat.start.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.umeng.commonsdk.debug.UInterface;
import com.yunkang.chat.R;
import com.yunkang.chat.start.model.Coupon;
import com.yunkang.chat.start.model.UserInfo;

import java.util.List;

/**
 * 作者：凌涛 on 2019/3/14 14:43
 * 邮箱：771548229@qq..com
 */
public class YouhuiAdapter extends BaseAdapter {
    Context mContext;
    List<Coupon> mCouponDetails;

    public YouhuiAdapter(Context context, List<Coupon> couponDetails) {
        mContext = context;
        mCouponDetails = couponDetails;
    }

    @Override
    public int getCount() {
        return mCouponDetails.size();
    }

    @Override
    public Object getItem(int position) {
        return mCouponDetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Coupon item=mCouponDetails.get(position);
        convertView= LayoutInflater.from(mContext).inflate(R.layout.item_youhui,null);
        TextView tv_name=convertView.findViewById(R.id.tv_name);
        TextView tv_price=convertView.findViewById(R.id.tv_price);
        TextView tv_remark=convertView.findViewById(R.id.tv_remark);
        TextView tv_time=convertView.findViewById(R.id.tv_time);
        tv_name.setText(item.getCouponName());
        tv_price.setText(item.getCouponPrice()+"");
        if(item.getSatisfy()==0)
           tv_remark.setText("无门槛");
        else{
            tv_remark.setText("满"+item.getSatisfy()+"减"+item.getCouponPrice());
        }
        if("1".equals(item.getTermValidityStatus()))
            tv_time.setText(item.getStartTermValidity().substring(0,10)+"-"+item.getEndTermValidity().substring(0,10));
        else{
            tv_time.setText(item.getRegularTime()+"天内可用");
        }
        return convertView;
    }
}
