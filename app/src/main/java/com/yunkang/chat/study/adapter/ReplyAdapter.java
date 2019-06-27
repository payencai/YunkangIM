package com.yunkang.chat.study.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunkang.chat.R;
import com.yunkang.chat.study.model.Reply;
import com.yunkang.chat.study.model.VideoComment;
import com.yunkang.chat.view.CircleImageView;

import java.util.List;

/**
 * 作者：凌涛 on 2019/5/6 20:47
 * 邮箱：771548229@qq..com
 */
public class ReplyAdapter extends BaseQuickAdapter<VideoComment, BaseViewHolder> {
    public ReplyAdapter(int layoutResId, @Nullable List<VideoComment> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoComment item) {
        CircleImageView iv_head = helper.getView(R.id.iv_head);
        TextView tv_name = helper.getView(R.id.tv_name);
        LinearLayout ll_num=helper.getView(R.id.ll_num);
        TextView tv_time = helper.getView(R.id.tv_time);
        TextView tv_content = helper.getView(R.id.tv_content);
        TextView tv_num = helper.getView(R.id.tv_num);
        ll_num.setVisibility(View.GONE);
        Glide.with(helper.itemView.getContext()).load(item.getHeading()).into(iv_head);
        tv_name.setText(item.getNickName());
        tv_time.setText(item.getCreateDate().substring(0, 10));
        tv_content.setText(item.getComment());
        tv_num.setText("" + item.getComments());
    }
}
