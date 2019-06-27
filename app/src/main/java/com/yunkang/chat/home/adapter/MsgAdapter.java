package com.yunkang.chat.home.adapter;

import android.support.annotation.Nullable;
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
import com.yunkang.chat.home.model.Msg;

import java.util.List;

public class MsgAdapter extends BaseQuickAdapter<Msg, BaseViewHolder> {
    public MsgAdapter(int layoutResId, @Nullable List<Msg> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Msg item) {
        helper.addOnClickListener(R.id.btnDelete);
        helper.addOnClickListener(R.id.ll_content);
        TextView tv_type=helper.getView(R.id.tv_type);
        TextView tv_time=helper.getView(R.id.tv_time);
        TextView tv_content=helper.getView(R.id.tv_content);
        ImageView iv_logo = helper.getView(R.id.iv_logo);
        View view=helper.getView(R.id.view);
        if(item.getReadStatus().equals("1")){
            view.setVisibility(View.VISIBLE);
        }
        tv_time.setText(item.getCreateDate().substring(0,10));
        tv_content.setText(item.getMassageContent());
        switch (item.getMassageType()){
            case "1":
                iv_logo.setImageResource(R.mipmap.ic_msg_hudong);
                tv_type.setText("互动信息");
                break;
            case "2":
                tv_type.setText("服务通知");
                iv_logo.setImageResource(R.mipmap.ic_msg_service);
                break;
            case "3":
                tv_type.setText("账号通知");
                iv_logo.setImageResource(R.mipmap.ic_msg_account);
                break;
            case "4":
                tv_type.setText("交易信息");
                iv_logo.setImageResource(R.mipmap.ic_msg_jiaoyi);
                break;
            case "5":
                tv_type.setText("物流信息");
                iv_logo.setImageResource(R.mipmap.ic_msg_wuliu);
                break;

        }
    }
}
