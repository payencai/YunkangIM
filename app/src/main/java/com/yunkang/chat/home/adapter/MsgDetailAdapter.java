package com.yunkang.chat.home.adapter;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunkang.chat.R;
import com.yunkang.chat.home.model.Msg;
import com.yunkang.chat.home.model.MsgDetail;

import java.util.List;

/**
 * 作者：凌涛 on 2019/3/7 10:50
 * 邮箱：771548229@qq..com
 */
public class MsgDetailAdapter extends BaseQuickAdapter<Msg,BaseViewHolder> {
    public MsgDetailAdapter(int layoutResId, @Nullable List<Msg> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Msg item) {

        TextView tv_time=helper.getView(R.id.tv_time);
        TextView tv_title=helper.getView(R.id.tv_title);
        TextView tv_content=helper.getView(R.id.tv_content);
        tv_content.setText(item.getMassageContent());
        tv_time.setText(item.getCreateDate());
        switch (item.getPushNode()){
            case "51":
                tv_title.setText("物流反馈");
                break;
            case "41":
                tv_title.setText("订单已付款");
                break;
            case "33":
                tv_title.setText("报备反馈");
                break;
            case "32":
                tv_title.setText("认证反馈");
                break;
            case "31":
                tv_title.setText("提现反馈");
                break;
            case "21":
                tv_title.setText("报告提醒");
                break;
            case "11":
                tv_title.setText("评论回复");
                break;
            case "12":
                tv_title.setText("好友提醒");
                break;
            default:
                tv_title.setText("自定义消息");
                break;
        }
    }
}
