package com.yunkang.chat.study.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunkang.chat.R;
import com.yunkang.chat.study.model.CallbackVideo;
import com.yunkang.chat.study.model.LiveVideo;

import java.util.List;

/**
 * 作者：凌涛 on 2019/4/20 15:52
 * 邮箱：771548229@qq..com
 */
public class CallbackVideoAdapter extends BaseQuickAdapter<CallbackVideo, BaseViewHolder> {
    public CallbackVideoAdapter(int layoutResId, @Nullable List<CallbackVideo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CallbackVideo item) {
          ImageView iv_logo=helper.getView(R.id.iv_logo);
          TextView tv_name=helper.getView(R.id.tv_name);
          TextView tv_num=helper.getView(R.id.tv_num);
          tv_num.setText(""+item.getPlayNum());
          Glide.with(helper.itemView.getContext()).load(item.getVideoPhoto()).into(iv_logo);
          tv_name.setText(item.getVideoTittle());
    }
}