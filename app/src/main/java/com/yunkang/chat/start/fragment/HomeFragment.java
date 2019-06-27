package com.yunkang.chat.start.fragment;


import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.qiyukf.unicorn.api.ConsultSource;
import com.qiyukf.unicorn.api.Unicorn;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.yunkang.chat.MyApplication;
import com.yunkang.chat.R;
import com.yunkang.chat.api.Api;
import com.yunkang.chat.constant.Constant;
import com.yunkang.chat.h5.H5CommomActivity;
import com.yunkang.chat.home.OnHomeCheckItemEvent;
import com.yunkang.chat.home.activity.CollectActivity;
import com.yunkang.chat.home.activity.MsgActivity;
import com.yunkang.chat.home.activity.NewReportActivity;
import com.yunkang.chat.home.activity.RecordActivity;
import com.yunkang.chat.home.activity.SearchActivity;
import com.yunkang.chat.home.activity.WaittodoActivity;
import com.yunkang.chat.home.adapter.FragmentHomeAdapter;
import com.yunkang.chat.home.fragment.CheckItemFragment;
import com.yunkang.chat.home.model.HomeCategory;
import com.yunkang.chat.http.HttpProxy;
import com.yunkang.chat.http.ICallBack;
import com.yunkang.chat.net.NetUtils;
import com.yunkang.chat.net.OnMessageReceived;
import com.yunkang.chat.start.adapter.GlideImageLoader;
import com.yunkang.chat.start.adapter.MyFragmentStatePagerAdapter;
import com.yunkang.chat.start.model.ContentImage;
import com.yunkang.chat.tools.DensityUtil;
import com.yunkang.chat.tools.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    @BindView(R.id.banner)
    Banner mBanner;
    @BindView(R.id.rl_title)
    RelativeLayout rl_title;
    @BindView(R.id.tab_layout)
    SlidingTabLayout tab_layout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.refresh)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.app_barlayout)
    AppBarLayout appBarlayout;
    @BindView(R.id.iv_cart)
    ImageView iv_cart;
    @BindView(R.id.iv_change)
    ImageView iv_change;
    @BindView(R.id.rl_empty)
    RelativeLayout rl_empty;
    List<HomeCategory> mHomeCategories;
    private ArrayList<Fragment> fragments;
    private List<String> mtitles ;
    private ArrayList<String> images;
    List<ContentImage> mContentImages ;
    float startX = 0;
    FragmentHomeAdapter mFragmentHomeAdapter;
    Timer timer;
    int type=1;
//    KProgressHUD dialog;
//    private void showLoading(String data){
//        dialog= KProgressHUD.create(getContext())
//                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
//
////                .setLabel(data)
//                .setCancellable(new DialogInterface.OnCancelListener()
//                {
//                    @Override public void onCancel(DialogInterface
//                                                           dialogInterface)
//                    {
//
//                    }
//                });
//        dialog.show();
//
//    }
    public HomeFragment() {
        // Required empty public constructor
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(OnHomeCheckItemEvent event){
        refreshLayout.finishRefresh();
    }

    private String[] getTitles(List<String> mtitles) {
        String[] stockArr = new String[mtitles.size()];
        stockArr = mtitles.toArray(stockArr);
        return stockArr;
    }
    private void getStatus() {

        NetUtils.getInstance().get(MyApplication.token, Api.Product.getStatus, new HashMap<>(), new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                Log.e("getStatus", response);
                if(response.contains("data")){
                    try {
                        int type;
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject dataobject = jsonObject.getJSONObject("data");
                        int code = jsonObject.getInt("code");
                        if (code == 0) {
                            type = dataobject.getInt("data");
                            MyApplication.isPt=type;
                            refreshAll();
                            refreshLayout.finishRefresh(2000);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    refreshAll();
                    refreshLayout.finishRefresh(2000);
                }
            }

            @Override
            public void onError(String error) {

            }
        });

    }
    private void getCategory() {

//        showLoading("loading...");

        Map<String, String> params = new HashMap<>();
        params.put("rank", "1");
        NetUtils.getInstance().get(Api.Product.getHomeCategoryList, params, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
//                if(dialog.isShowing()){
//                    dialog.dismiss();
//                }
                Log.e("result", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject dataobject = jsonObject.getJSONObject("data");
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        JSONArray data = dataobject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            HomeCategory homeCategory = new Gson().fromJson(item.toString(), HomeCategory.class);
                            mHomeCategories.add(homeCategory);
                            mtitles.add(homeCategory.getName());
                        }
                        initData();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
//                if(dialog.isShowing()){
//                    dialog.dismiss();
//                }
            }
        });

    }
    private void getNewCategory(int type) {

//        showLoading("loading...");

        Map<String, String> params = new HashMap<>();
        params.put("rank", "1");
        NetUtils.getInstance().get(Api.Product.getHomeCategoryList, params, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
//                if(dialog.isShowing()){
//                    dialog.dismiss();
//                }
                Log.e("result", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject dataobject = jsonObject.getJSONObject("data");
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        JSONArray data = dataobject.getJSONArray("data");
                        List<String> homeTitles=new ArrayList<>();
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            HomeCategory homeCategory = new Gson().fromJson(item.toString(), HomeCategory.class);
                            mHomeCategories.add(homeCategory);
                            homeTitles.add(homeCategory.getName());
                            fragments.add(CheckItemFragment.newInstance(type,homeCategory.getId(),i));
                        }
                        mFragmentHomeAdapter.setNewTitleFragment(fragments, homeTitles);
                        tab_layout.notifyDataSetChanged();
                        if (fragments.size() > 0) {
                            viewpager.setCurrentItem(0);
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
//                if(dialog.isShowing()){
//                    dialog.dismiss();
//                }
            }
        });

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        Utils.setStatusBarTransparent(getActivity());
        EventBus.getDefault().register(this);
        initView(view);
        return view;
    }

    @OnClick({R.id.iv_change,R.id.ll_baogao, R.id.iv_service, R.id.ll_bulu, R.id.ll_undo, R.id.ll_collect, R.id.ll_search, R.id.iv_cart, R.id.iv_msg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_change:
                if(type==1){
                    type=2;
                    iv_change.setImageResource(R.mipmap.ic_change_one);
                }else{
                    type=1;
                    iv_change.setImageResource(R.mipmap.ic_change_two);
                }
                refreshAll();
                break;
            case R.id.iv_service:
                String title = "客服";
                ConsultSource source = new ConsultSource("msg", "客户", "custom information string");
                Unicorn.openServiceActivity(getActivity(), title, source);
                break;
            case R.id.ll_baogao:
                startActivity(new Intent(getContext(), NewReportActivity.class));
                break;
            case R.id.ll_bulu:
                startActivity(new Intent(getContext(), RecordActivity.class));
                break;
            case R.id.ll_undo:
                startActivity(new Intent(getContext(), WaittodoActivity.class));
                break;
            case R.id.ll_collect:
                startActivity(new Intent(getContext(), CollectActivity.class));
                break;
            case R.id.ll_search:
                startActivity(new Intent(getContext(), SearchActivity.class));
                break;
            case R.id.iv_msg:
                startActivity(new Intent(getContext(), MsgActivity.class));
                break;
            case R.id.iv_cart:
                startX = iv_cart.getTranslationX();
                if (startX == 0) {
                    Intent intent = new Intent(getContext(), H5CommomActivity.class);
                    intent.putExtra("url", Constant.Commom.shopCar + MyApplication.token);
                    startActivity(intent);
                } else {
                    showShopCar(iv_cart);
                }
                break;
            default:
                break;
        }
    }



    @Override
    public void onResume() {
        super.onResume();

    }

    public void refreshAll(){
        //mHomeCategories.clear();
        images.clear();
        mContentImages.clear();
        mtitles.clear();
        fragments.clear();
        mHomeCategories.clear();
        getBanner();
        getNewCategory(type);

    }

    private void initView(View view) {

        fragments = new ArrayList<>();
        mContentImages=new ArrayList<>();
        images=new ArrayList<>();
        mtitles=new ArrayList<>();
        mHomeCategories = new ArrayList<>();
        appBarlayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (verticalOffset == 0) {
                    rl_title.setBackgroundColor(Color.argb(0, 0, 0, 0));
                    // mBanner.setVisibility(View.GONE);
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    rl_title.setBackgroundColor(Color.argb(255, 73, 159, 229));
                } else {
                    int alpha = (int) (255 - verticalOffset / (float) appBarLayout.getTotalScrollRange() * 255);
                    rl_title.setBackgroundColor(Color.argb(alpha, 73, 159, 229));

                    if (startX == 0) {
                        hideShopCar(iv_cart);
                    }
                }
            }
        });
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                Log.e("refresh","刷新");
                getStatus();
            }
        });
        getCategory();
        getBanner();
    }
    MyFragmentStatePagerAdapter mMyFragmentStatePagerAdapter;
    private void initData() {
        for (int i = 0; i < mHomeCategories.size(); i++) {
            CheckItemFragment checkItemFragment = CheckItemFragment.newInstance(1,mHomeCategories.get(i).getId(),i);
            fragments.add(checkItemFragment);
        }
        if (mtitles.size() > 0){
            mFragmentHomeAdapter = new FragmentHomeAdapter(getContext(), getChildFragmentManager(), fragments, mtitles);
            viewpager.setAdapter(mFragmentHomeAdapter);
            tab_layout.setViewPager(viewpager);
        }
        else {
            viewpager.setVisibility(View.GONE);
            rl_empty.setVisibility(View.VISIBLE);
        }

    }


    private void initBanner() {
        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Intent intent5 = new Intent(getContext(), H5CommomActivity.class);
                 Log.e("jump", mContentImages.get(position).getJumpLink()+"");
                String url=mContentImages.get(position).getJumpLink();
                if(!TextUtils.isEmpty(url)){
                    if(!url.contains("http://")&&!url.contains("https://")){
                        if(!url.contains("www")){
                            url="http://www."+url;
                        }else{
                            url="http://"+url;
                        }
                    }
                    intent5.putExtra("url", url);
                    startActivity(intent5);
                }

            }
        });
        mBanner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        mBanner.setImages(images);

        //设置自动轮播，默认为true
        mBanner.isAutoPlay(true);
        //设置轮播时间
        mBanner.setDelayTime(2000);
        //设置指示器位置（当banner模式中有指示器时）
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        mBanner.start();
    }

    private void getBanner() {


        Map<String, String> params = new HashMap<>();
        params.put("type", "3");
        NetUtils.getInstance().get(Api.ContentManage.getContentManageByApp, params, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                Log.e("result", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject dataobject = jsonObject.getJSONObject("data");
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        images = new ArrayList<>();
                        JSONArray data = dataobject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            ContentImage homeCategory = new Gson().fromJson(item.toString(), ContentImage.class);
                            images.add(homeCategory.getImage());
                            mContentImages.add(homeCategory);
                        }
                        initBanner();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {

            }
        });

    }



    public void showShopCar(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", 0);
        animator.setDuration(500).start();
        startX = 0;
    }

    public void hideShopCar(View view) {
        int value= DensityUtil.dip2px(getContext(),45);
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", value);
        animator.setDuration(500).start();
        startX = view.getTranslationX();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
