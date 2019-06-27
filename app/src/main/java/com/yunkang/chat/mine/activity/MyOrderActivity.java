package com.yunkang.chat.mine.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.iapppay.sdk.main.IAppPay;
import com.jaeger.library.StatusBarUtil;
import com.yunkang.chat.R;
import com.yunkang.chat.mine.fragment.AllOrderFragment;
import com.yunkang.chat.mine.fragment.FinishFragment;
import com.yunkang.chat.mine.fragment.UnpayFragment;
import com.yunkang.chat.mine.fragment.WaitResultFragment;
import com.yunkang.chat.start.adapter.MyPagerAdapter;
import com.yunkang.chat.tools.TabUtils;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyOrderActivity extends AppCompatActivity implements View.OnClickListener{

    ArrayList<Fragment> mFragments;
    @BindView(R.id.vp_order)
    ViewPager vp_order;
    @BindView(R.id.tab_order)
    SlidingTabLayout tab_order;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    int type=0;
    String []mTitles={"全部","待付款","待出报告","已完成"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_order);
        ButterKnife.bind(this);
        type=getIntent().getIntExtra("type",0);
        StatusBarUtil.setColor(this,getResources().getColor(R.color.color_blue));
        initView();
    }
    public  void refresh(){

         mAllOrderFragment1.refresh();
        mAllOrderFragment2.refresh();
        mAllOrderFragment3.refresh();
        mAllOrderFragment4.refresh();


    }

    AllOrderFragment mAllOrderFragment1;
    AllOrderFragment mAllOrderFragment2;
    AllOrderFragment mAllOrderFragment3;
    AllOrderFragment mAllOrderFragment4;

    boolean isSee=false;

    private void initView() {

        tv_title.setText("检测订单");
        iv_back.setOnClickListener(this);
        mFragments=new ArrayList<>();
        mAllOrderFragment1=AllOrderFragment.newInstance(0+"");
        mAllOrderFragment2=AllOrderFragment.newInstance(1+"");
        mAllOrderFragment3=AllOrderFragment.newInstance(2+"");
        mAllOrderFragment4=AllOrderFragment.newInstance(3+"");
        mFragments.add(mAllOrderFragment1);
        mFragments.add(mAllOrderFragment2);
        mFragments.add(mAllOrderFragment3);
        mFragments.add(mAllOrderFragment4);
        tab_order.setViewPager(vp_order,  mTitles,this,mFragments);
        vp_order.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i){
                    case 0:
                        if(!mAllOrderFragment1.isAsk()&&mAllOrderFragment1.getOrderAdapter()!=null)
                            mAllOrderFragment1.refresh();
                        break;
                    case 1:
                        if(!mAllOrderFragment2.isAsk()&&mAllOrderFragment2.getOrderAdapter()!=null)
                            mAllOrderFragment2.refresh();
                        break;
                    case 2:
                        if(!mAllOrderFragment3.isAsk()&&mAllOrderFragment3.getOrderAdapter()!=null)
                            mAllOrderFragment3.refresh();
                        break;
                    case 3:
                        if(!mAllOrderFragment4.isAsk()&&mAllOrderFragment4.getOrderAdapter()!=null)
                            mAllOrderFragment4.refresh();
                        break;

                }
            }


            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        tab_order.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                switch (position){
                    case 0:
                        if(!mAllOrderFragment1.isAsk()&&mAllOrderFragment1.getOrderAdapter()!=null)
                            mAllOrderFragment1.refresh();

                        break;
                    case 1:
                        if(!mAllOrderFragment2.isAsk()&&mAllOrderFragment2.getOrderAdapter()!=null)
                            mAllOrderFragment2.refresh();
                        break;
                    case 2:
                        if(!mAllOrderFragment3.isAsk()&&mAllOrderFragment3.getOrderAdapter()!=null)
                            mAllOrderFragment3.refresh();
                        break;
                    case 3:
                        if(!mAllOrderFragment4.isAsk()&&mAllOrderFragment4.getOrderAdapter()!=null)
                            mAllOrderFragment4.refresh();
                        break;

                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        switch (type){
            case 1:
                vp_order.setCurrentItem(1);
                break;
            case 2:
                vp_order.setCurrentItem(2);
                break;
            case 3:
                vp_order.setCurrentItem(3);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
