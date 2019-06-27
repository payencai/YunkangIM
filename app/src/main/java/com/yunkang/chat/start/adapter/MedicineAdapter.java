package com.yunkang.chat.start.adapter;

import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunkang.chat.MyApplication;
import com.yunkang.chat.R;
import com.yunkang.chat.start.model.Medicine;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：凌涛 on 2019/1/19 19:39
 * 邮箱：771548229@qq..com
 */
public class MedicineAdapter extends BaseQuickAdapter<Medicine, BaseViewHolder> {
    HomeTagAdapter mHomeTagAdapter;
    List<String> taglist;
    public MedicineAdapter(int layoutResId, @Nullable List<Medicine> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Medicine item) {

        helper.addOnClickListener(R.id.iv_add);
        taglist=new ArrayList<>();
        TagFlowLayout id_flowlayout=helper.getView(R.id.id_flowlayout);
        if(!TextUtils.isEmpty(item.getpLableName1())){
            taglist.add(item.getpLableName1());
        }
        if(!TextUtils.isEmpty(item.getpLableName2())){
            taglist.add(item.getpLableName2());
        }
        if(!TextUtils.isEmpty(item.getpLableName3())){
            taglist.add(item.getpLableName3());
        }
        mHomeTagAdapter=new HomeTagAdapter(taglist);
        id_flowlayout.setAdapter(mHomeTagAdapter);
        ImageView iv_logo = helper.getView(R.id.iv_logo);
        TextView tv_pricePt=helper.getView(R.id.tv_price1);
        TextView tv_price=helper.getView(R.id.tv_price2);
        TextView tv_name=helper.getView(R.id.tv_name);
        ImageView iv_add=helper.getView(R.id.iv_add);
        LinearLayout ll_price=helper.getView(R.id.ll_price);
        TextView tv_realprice=helper.getView(R.id.tv_realprice);
        tv_name.setText(item.getName());

        if(MyApplication.isPt==1){
            ll_price.setVisibility(View.VISIBLE);
            tv_pricePt.setText("￥"+doubleTranString(item.getPtPrice()));
            tv_price.setText("￥"+doubleTranString(item.getRetailPrice()));
            tv_realprice.setVisibility(View.GONE);
            tv_price.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG ); //中间横线
        }else{
            tv_realprice.setText("￥"+doubleTranString(item.getRetailPrice()));
            tv_realprice.setVisibility(View.VISIBLE);
            ll_price.setVisibility(View.GONE);
        }

        if(item.isHide()){
            iv_add.setVisibility(View.GONE);
        }
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.mipmap.bg_test)
                .error(R.mipmap.bg_test).apply(RequestOptions.bitmapTransform(new RoundedCorners(20)));
        if(!TextUtils.isEmpty(item.getCoverImages())){
            if(item.getCoverImages().contains(",")){
                String[] images=item.getCoverImages().split(",");
                Glide.with(mContext).load(images[0])
                        .apply(requestOptions)
                        .into(iv_logo);
            }else{
                Glide.with(mContext).load(item.getCoverImages())
                        .apply(requestOptions)
                        .into(iv_logo);
            }

        }
//        if(item.getPtPrice()==0){
//            tv_price1.setText("￥"+doubleTranString(item.getRetailPrice()));
//            tv_price1.setVisibility(View.VISIBLE);
//            tv_price2.setVisibility(View.GONE);
//        }


    }
    public static String doubleTranString(double num)
    {
        if(num % 1.0 == 0)
        {
            return String.valueOf((long)num);
        }

        return String.valueOf(num);
    }
}
