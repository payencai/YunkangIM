package com.yunkang.chat.mine.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.yunkang.chat.MyApplication;
import com.yunkang.chat.R;
import com.yunkang.chat.api.Api;
import com.yunkang.chat.h5.H5CommomActivity;
import com.yunkang.chat.home.adapter.LogisticsAdapter;
import com.yunkang.chat.home.adapter.RecordAdapter;
import com.yunkang.chat.home.model.Logistics;
import com.yunkang.chat.home.model.Record;
import com.yunkang.chat.http.HttpProxy;
import com.yunkang.chat.http.ICallBack;
import com.yunkang.chat.mine.model.Order;
import com.yunkang.chat.net.NetUtils;
import com.yunkang.chat.net.OnMessageReceived;
import com.yunkang.chat.start.model.Medicine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CallLogisticsActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_call)
    TextView tv_call;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.rv_logistics)
    RecyclerView rv_logistics;
    LogisticsAdapter mWaitodoAdapter;
    List<Order> mWaitToDos;
    int page = 1;
    String paySate = "";
    boolean isLoadMore = false;
    View view;
    String id;
    List<Order.OrderProductsBean> waitChildren;
    int pos=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_logistics);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tv_title.setText("呼叫物流");
        iv_back.setOnClickListener(this);
        tv_call.setOnClickListener(this);
        mWaitToDos = new ArrayList<>();
        mWaitodoAdapter = new LogisticsAdapter(R.layout.item_mine_wuliu, mWaitToDos);
        mWaitodoAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        rv_logistics.setLayoutManager(new LinearLayoutManager(this));
        rv_logistics.setAdapter(mWaitodoAdapter);
        view= LayoutInflater.from(this).inflate(R.layout.home_order_error,null);
        mWaitodoAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                isLoadMore = true;
                getListData();
            }
        }, rv_logistics);
        mWaitodoAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(view.getId()==R.id.iv_choose){
                    pos=position;
                    id=mWaitodoAdapter.getItem(pos).getId();
                    mWaitodoAdapter.setPos(position);
                    mWaitodoAdapter.notifyDataSetChanged();
                }
            }
        });
        getListData();
    }

    private void getListData() {
//        Map<String, Object> params = new HashMap<>();
//        params.put("page", page);
//        params.put("payState", "4");
//        HttpProxy.obtain().get(Api.Order.getOrderByUser, params, MyApplication.token, new ICallBack() {
//            @Override
//            public void OnSuccess(String result) {
//                Log.e("getOrderByUser", result);
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    JSONObject dataobject = jsonObject.getJSONObject("data");
//                    dataobject = dataobject.getJSONObject("data");
//                    int code = jsonObject.getInt("code");
//                    if (code == 0) {
//                        JSONArray data = dataobject.getJSONArray("beanList");
//                        if (page == 1 && data.length() == 0) {
//                            mWaitodoAdapter.setEmptyView(view);
//                        } else {
//                            List<Order> orders=new ArrayList<>();
//                            for (int i = 0; i < data.length(); i++) {
//                                JSONObject item = data.getJSONObject(i);
//                                Order homeCategory = new Gson().fromJson(item.toString(), Order.class);
//                                orders.add(homeCategory);
//                            }
//                            if (isLoadMore) {
//                                isLoadMore = false;
//                                mWaitodoAdapter.addData(orders);
//                                if (data.length() > 0)
//                                    mWaitodoAdapter.loadMoreComplete();
//                                else {
//                                    mWaitodoAdapter.loadMoreEnd(true);
//                                }
//                            } else {
//                                mWaitodoAdapter.setNewData(orders);
//
//                                // orderAdapter.loadMoreEnd(true);
//                            }
//                        }
//                    } else {
//                        mWaitodoAdapter.setEmptyView(view);
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
        params.put("page", page + "");
        params.put("payState", "4");
        NetUtils.getInstance().get(MyApplication.token, Api.Order.getOrderByUser, params, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                Log.e("getOrderByUser", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject dataobject = jsonObject.getJSONObject("data");
                    dataobject = dataobject.getJSONObject("data");
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        JSONArray data = dataobject.getJSONArray("beanList");
                        if (page == 1 && data.length() == 0) {
                            mWaitodoAdapter.setEmptyView(view);
                        } else {
                            List<Order> orders=new ArrayList<>();
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject item = data.getJSONObject(i);
                                Order homeCategory = new Gson().fromJson(item.toString(), Order.class);
                                orders.add(homeCategory);
                            }
                            if (isLoadMore) {
                                isLoadMore = false;
                                mWaitodoAdapter.addData(orders);
                                if (data.length() > 0)
                                    mWaitodoAdapter.loadMoreComplete();
                                else {
                                    mWaitodoAdapter.loadMoreEnd(true);
                                }
                            } else {
                                mWaitodoAdapter.setNewData(orders);

                                // orderAdapter.loadMoreEnd(true);
                            }
                        }
                    } else {
                        mWaitodoAdapter.setEmptyView(view);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_call:
                Intent intent1=new Intent(CallLogisticsActivity.this, H5CommomActivity.class);
                intent1.putExtra("url","http://47.106.233.240:8090/View/callLogister/callLogister.html?token="
                        +MyApplication.token+"&id="+id);
                startActivity(intent1);
                break;
        }
    }
}