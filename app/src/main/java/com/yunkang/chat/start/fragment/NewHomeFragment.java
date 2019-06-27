package com.yunkang.chat.start.fragment;

import android.animation.ObjectAnimator;
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
import com.yunkang.chat.home.activity.CollectActivity;
import com.yunkang.chat.home.activity.MsgActivity;
import com.yunkang.chat.home.activity.NewReportActivity;
import com.yunkang.chat.home.activity.RecordActivity;
import com.yunkang.chat.home.activity.SearchActivity;
import com.yunkang.chat.home.activity.WaittodoActivity;
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


public class NewHomeFragment extends Fragment {

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
    @BindView(R.id.rl_empty)
    RelativeLayout rl_empty;
    List<HomeCategory> mHomeCategories;
    private ArrayList<Fragment> fragments;
    private ArrayList<String> mTitles ;
    private ArrayList<String> images;
    List<ContentImage> mContentImages ;
    float startX = 0;

    Timer timer;

    public NewHomeFragment() {
        // Required empty public constructor
    }

    private String[] getTitles(List<String> mtitles) {
        String[] stockArr = new String[mtitles.size()];
        stockArr = mtitles.toArray(stockArr);
        return stockArr;
    }
    private void getStatus() {
//        HttpProxy.obtain().get(Api.Product.getStatus, MyApplication.token, new ICallBack() {
//            @Override
//            public void OnSuccess(String result) {
//                Log.e("getStatus", result);
//                try {
//                    int type;
//                    JSONObject jsonObject = new JSONObject(result);
//                    JSONObject dataobject = jsonObject.getJSONObject("data");
//                    int code = jsonObject.getInt("code");
//                    if (code == 0) {
//                        type = dataobject.getInt("data");
//                        MyApplication.isPt=type;
//                        refreshAll();
//                        refreshLayout.finishRefresh(2000);
//                    }
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(String error) {
//
//            }
//        });

        NetUtils.getInstance().get(MyApplication.token, Api.Product.getStatus, new HashMap<>(), new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                Log.e("getStatus", response);
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
            }

            @Override
            public void onError(String error) {

            }
        });

    }
    private void getCategory() {
//        Map<String, Object> params = new HashMap<>();
//        params.put("rank", 1);
//        HttpProxy.obtain().get(Api.Product.getHomeCategoryList, params, new ICallBack() {
//            @Override
//            public void OnSuccess(String result) {
//                Log.e("result", result);
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    JSONObject dataobject = jsonObject.getJSONObject("data");
//                    int code = jsonObject.getInt("code");
//                    if (code == 0) {
//                        JSONArray data = dataobject.getJSONArray("data");
//                        for (int i = 0; i < data.length(); i++) {
//                            JSONObject item = data.getJSONObject(i);
//                            HomeCategory homeCategory = new Gson().fromJson(item.toString(), HomeCategory.class);
//                            mHomeCategories.add(homeCategory);
//                            mTitles.add(homeCategory.getName());
//                        }
//                        initData();
//                    }
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(String error) {
//
//            }
//        });

        Map<String, String> params = new HashMap<>();
        params.put("rank", "1");
        NetUtils.getInstance().get(MyApplication.token, Api.Product.getHomeCategoryList, params, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
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
                            mTitles.add(homeCategory.getName());
                        }
                        initData();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_home, container, false);
        ButterKnife.bind(this, view);
        Utils.setStatusBarTransparent(getActivity());
        initView(view);
        return view;
    }

    @OnClick({R.id.ll_baogao, R.id.iv_service, R.id.ll_bulu, R.id.ll_undo, R.id.ll_collect, R.id.ll_search, R.id.iv_cart, R.id.iv_msg})
    public void onClick(View view) {
        switch (view.getId()) {
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

    public void refresh() {
        for (int i = 0; i < fragments.size(); i++) {
            CheckItemFragment checkItemFragment = (CheckItemFragment) fragments.get(i);
            checkItemFragment.refresh();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }
    private void getNewCategory() {
//        Map<String, Object> params = new HashMap<>();
//        params.put("rank", 1);
//        HttpProxy.obtain().get(Api.Product.getHomeCategoryList, params, new ICallBack() {
//            @Override
//            public void OnSuccess(String result) {
//
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    JSONObject dataobject = jsonObject.getJSONObject("data");
//                    int code = jsonObject.getInt("code");
//                    if (code == 0) {
//                        JSONArray data = dataobject.getJSONArray("data");
//                        for (int i = 0; i < data.length(); i++) {
//                            Log.e("result", result);
//                            JSONObject item = data.getJSONObject(i);
//                            HomeCategory homeCategory = new Gson().fromJson(item.toString(), HomeCategory.class);
//                            mHomeCategories.add(homeCategory);
//                            mTitles.add("哈哈哈");
//                            CheckItemFragment checkItemFragment = CheckItemFragment.newInstance(mHomeCategories.get(i).getId(),i);
//                            fragments.add(checkItemFragment);
//                        }
//                        //tab_layout.setViewPager(viewpager,getTitles(mTitles),getActivity(),fragments);
//                    }
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(String error) {
//
//            }
//        });

        Map<String, String> params = new HashMap<>();
        params.put("rank", "1");
        NetUtils.getInstance().get(MyApplication.token, Api.Product.getHomeCategoryList, params, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject dataobject = jsonObject.getJSONObject("data");
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        JSONArray data = dataobject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            Log.e("result", response);
                            JSONObject item = data.getJSONObject(i);
                            HomeCategory homeCategory = new Gson().fromJson(item.toString(), HomeCategory.class);
                            mHomeCategories.add(homeCategory);
                            mTitles.add("哈哈哈");
                            CheckItemFragment checkItemFragment = CheckItemFragment.newInstance(1,mHomeCategories.get(i).getId(),i);
                            fragments.add(checkItemFragment);
                        }
                        //tab_layout.setViewPager(viewpager,getTitles(mTitles),getActivity(),fragments);
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
    private void refreshAll(){
        //mHomeCategories.clear();
        images.clear();
        mContentImages.clear();
        getBanner();
        //getNewCategory();
        refresh();
    }
    String []titles;
    private void initView(View view) {

        fragments = new ArrayList<>();
        mContentImages=new ArrayList<>();
        images=new ArrayList<>();
        mTitles=new ArrayList<>();
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
        if (mTitles.size() > 0){
            titles=getTitles(mTitles);
            tab_layout.setViewPager(viewpager,titles,getActivity(),fragments);
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
               /// Log.e("jump", mContentImages.get(position).getJumpLink()+"");
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
//        Map<String, Object> params = new HashMap<>();
//        params.put("type", 3);
//        HttpProxy.obtain().get(Api.ContentManage.getContentManageByApp, params, new ICallBack() {
//            @Override
//            public void OnSuccess(String result) {
//                Log.e("result", result);
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    JSONObject dataobject = jsonObject.getJSONObject("data");
//                    int code = jsonObject.getInt("code");
//                    if (code == 0) {
//                        images = new ArrayList<>();
//                        JSONArray data = dataobject.getJSONArray("data");
//                        for (int i = 0; i < data.length(); i++) {
//                            JSONObject item = data.getJSONObject(i);
//                            ContentImage homeCategory = new Gson().fromJson(item.toString(), ContentImage.class);
//                            images.add(homeCategory.getImage());
//                            mContentImages.add(homeCategory);
//                        }
//                        initBanner();
//                    }
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(String error) {
//
//            }
//        });

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
        int value=DensityUtil.dip2px(getContext(),45);
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", value);
        animator.setDuration(500).start();
        startX = view.getTranslationX();
    }
}
