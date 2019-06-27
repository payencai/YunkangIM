package com.yunkang.chat.start.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yunkang.chat.R;
import com.yunkang.chat.api.Api;
import com.yunkang.chat.category.fragment.CategoryMedicineFragment;
import com.yunkang.chat.home.adapter.FragmentHomeAdapter;
import com.yunkang.chat.home.model.HomeCategory;
import com.yunkang.chat.http.HttpProxy;
import com.yunkang.chat.http.ICallBack;
import com.yunkang.chat.net.NetUtils;
import com.yunkang.chat.net.OnMessageReceived;
import com.yunkang.chat.start.adapter.MyPagerAdapter;
import com.yunkang.chat.tools.TabUtils;
import com.yunkang.chat.tools.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {
    List<String> mTitle;
    List<Fragment> mFragments;
    MyPagerAdapter myPagerAdapter;
    @BindView(R.id.vp_category)
    ViewPager vp_category;
    @BindView(R.id.tab_category)
    SlidingTabLayout tab_category;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    FragmentHomeAdapter mFragmentHomeAdapter;
    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.bind(this, view);
        initView();
        getCategory();
        return view;
    }

    private void getCategory() {
        Map<String, String> params = new HashMap<>();
        params.put("rank", "1");
        NetUtils.getInstance().get(Api.Product.getHomeCategoryList, params, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                refresh.finishRefresh();
                Log.e("getHomeCategoryList", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject dataobject = jsonObject.getJSONObject("data");
                    int code = jsonObject.getInt("code");
                    String msg = jsonObject.getString("msg");
                    if (code == 0) {
                        JSONArray data = dataobject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            HomeCategory homeCategory = new Gson().fromJson(item.toString(), HomeCategory.class);
                            mTitle.add(homeCategory.getName());
                            mFragments.add(CategoryMedicineFragment.newInstance(homeCategory.getId(),i));
                        }
                        mFragmentHomeAdapter = new FragmentHomeAdapter(getContext(), getChildFragmentManager(), mFragments, mTitle);
                        vp_category.setAdapter(mFragmentHomeAdapter);
                        tab_category.setViewPager(vp_category);

                    } else {
                        ToastUtil.showToast(getContext(), msg);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                ToastUtil.showToast(getContext(), "网络不通" + error);
            }
        });

    }
    private void getNewCategory() {
        Map<String, String> params = new HashMap<>();
        params.put("rank", "1");
        NetUtils.getInstance().get(Api.Product.getHomeCategoryList, params, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                refresh.finishRefresh();
                Log.e("getHomeCategoryList", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject dataobject = jsonObject.getJSONObject("data");
                    int code = jsonObject.getInt("code");
                    String msg = jsonObject.getString("msg");
                    if (code == 0) {
                        JSONArray data = dataobject.getJSONArray("data");
                        List<String> homeTitles=new ArrayList<>();
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            HomeCategory homeCategory = new Gson().fromJson(item.toString(), HomeCategory.class);
                            homeTitles.add(homeCategory.getName());
                            mTitle.add(homeCategory.getName());
                            mFragments.add(CategoryMedicineFragment.newInstance(homeCategory.getId(),i));
                        }
                        mFragmentHomeAdapter.setNewTitleFragment(mFragments, homeTitles);
                        tab_category.notifyDataSetChanged();

                    } else {
                        ToastUtil.showToast(getContext(), msg);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                ToastUtil.showToast(getContext(), "网络不通" + error);
            }
        });

    }
    private void initView() {
        mTitle = new ArrayList<>();
        mFragments = new ArrayList<>();

        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mFragments.clear();
                mTitle.clear();
                getNewCategory();
            }
        });
    }


}
