package com.yunkang.chat.study.adapter;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunkang.chat.R;
import com.yunkang.chat.study.model.CallbackVideo;
import com.yunkang.chat.study.model.RecordVideo;

import java.util.List;

/**
 * 作者：凌涛 on 2019/4/20 15:52
 * 邮箱：771548229@qq..com
 */
public class RecordVideoAdapter extends BaseQuickAdapter<RecordVideo, BaseViewHolder> {
    public RecordVideoAdapter(int layoutResId, @Nullable List<RecordVideo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RecordVideo item) {
          ImageView iv_logo=helper.getView(R.id.iv_logo);
          Glide.with(helper.itemView.getContext()).load(item.getPhoto()).into(iv_logo);

    }
}
