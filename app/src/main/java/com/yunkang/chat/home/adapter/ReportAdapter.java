package com.yunkang.chat.home.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunkang.chat.R;
import com.yunkang.chat.home.model.Collect;
import com.yunkang.chat.home.model.Report;

import java.util.List;

public class ReportAdapter extends BaseQuickAdapter<Report, BaseViewHolder> {
    public ReportAdapter(int layoutResId, @Nullable List<Report> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Report item) {
        helper.setIsRecyclable(false);
        TextView tv_name=helper.getView(R.id.tv_name);
        TextView tv_time=helper.getView(R.id.tv_time);
        tv_name.setText(item.getPatientName());
        tv_time.setText(item.getCreateDate().substring(0,10));
        TextView tv_content=helper.getView(R.id.tv_content);
        String content="";
        for (int i = 0; i <item.getProducts().size() ; i++) {
            content=content+","+item.getProducts().get(i).getProductName();
        }
        if(!TextUtils.isEmpty(content)&&content.length()>0)
             tv_content.setText("检测项目："+content.substring(1));
        else{
            tv_content.setText("检测项目：");
        }
    }
}
