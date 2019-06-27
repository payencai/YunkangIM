package com.yunkang.chat.mine.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.NetworkUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.lzy.okgo.model.HttpParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yunkang.chat.MyApplication;
import com.yunkang.chat.R;
import com.yunkang.chat.api.Api;
import com.yunkang.chat.constant.Constant;
import com.yunkang.chat.h5.H5CommomActivity;
import com.yunkang.chat.home.adapter.LogisticsAdapter;
import com.yunkang.chat.http.HttpProxy;
import com.yunkang.chat.http.ICallBack;
import com.yunkang.chat.mine.adapter.AfterSaleAdapter;
import com.yunkang.chat.mine.model.Order;
import com.yunkang.chat.net.NetUtils;
import com.yunkang.chat.net.OnMessageReceived;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.leefeng.promptlibrary.PromptDialog;

public class AfterSaleActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.tv_sale)
    TextView tv_sale;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.rv_sale)
    RecyclerView rv_logistics;
    @BindView(R.id.refresh)
    SmartRefreshLayout refreshLayout;
    AfterSaleAdapter mWaitodoAdapter;
    List<Order> mWaitToDos;
    int page = 1;
    String paySate = "";
    boolean isLoadMore = false;
    View view;
    List<Order.OrderProductsBean> waitChildren;
    int pos=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_sale);
        ButterKnife.bind(this);
        initView();
    }
    public void refresh() {
        page = 1;
        mWaitToDos.clear();
        View empty=mWaitodoAdapter.getEmptyView();
        if(empty!=null){
            empty.setVisibility(View.GONE);
        }
        mWaitodoAdapter.setNewData(mWaitToDos);
        getListData();
    }
    private boolean isAdd(Order order){
        for (int i = 0; i <order.getOrderProducts().size() ; i++) {
            Order.OrderProductsBean orderProductsBean=order.getOrderProducts().get(i);
            if(!"0".equals(orderProductsBean.getRefundType())){
                return true;
            }
        }
        return false;
    }
    KProgressHUD dialog;
    View noWeb;
    private void showLoading(String data){
        dialog= KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)

                .setCancellable(new DialogInterface.OnCancelListener()
                {
                    @Override public void onCancel(DialogInterface
                                                           dialogInterface)
                    {

                    }
                });
        dialog.show();

    }
    private void initView() {


        tv_title.setText("申请售后");
        iv_back.setOnClickListener(this);
        tv_sale.setOnClickListener(this);
        mWaitToDos = new ArrayList<>();
        mWaitodoAdapter = new AfterSaleAdapter(R.layout.item_mine_shouhou, mWaitToDos);
        rv_logistics.setLayoutManager(new LinearLayoutManager(this));
        rv_logistics.setAdapter(mWaitodoAdapter);
        view= LayoutInflater.from(this).inflate(R.layout.home_order_error,null);
        noWeb= LayoutInflater.from(this).inflate(R.layout.no_web_error,null);
        mWaitodoAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                isLoadMore = true;
                getListData();
            }
        }, rv_logistics);
        mWaitodoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Order order = (Order) adapter.getItem(position);
                Intent intent = new Intent(AfterSaleActivity.this, H5CommomActivity.class);
                intent.putExtra("url", Constant.Commom.backDetail + MyApplication.token
                        + "&id=" + order.getId());
                startActivityForResult(intent,2);
//                Order order= (Order) adapter.getItem(position);
//                Intent intent1=new Intent(AfterSaleActivity.this, H5CommomActivity.class);
//                intent1.putExtra("url","http://47.106.233.240:8090/View/orders/backDetail.html?token="
//                        +MyApplication.token+"&id="+order.getId());
//                startActivity(intent1);
            }
        });
        mWaitodoAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                Log.e("refresh","刷新");
                refresh();
                refreshLayout.finishRefresh(1000);
            }
        });
        getListData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refresh();
    }

    private void getListData() {


        HttpParams httpParams=new HttpParams();
        httpParams.put("page",page);
        if(page==1){
            showLoading("加载中");
        }
        NetUtils.getInstance().get(MyApplication.token, Api.Order.getOrderByUser, httpParams, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                if(dialog.isShowing())
                    dialog.dismiss();
                Log.e("getOrderByUser", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject dataobject = jsonObject.getJSONObject("data");
                    dataobject = dataobject.getJSONObject("data");
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        JSONArray data = dataobject.getJSONArray("beanList");

                        List<Order> orders=new ArrayList<>();
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            Order homeCategory = new Gson().fromJson(item.toString(), Order.class);
                            if(isAdd(homeCategory))
                                orders.add(homeCategory);
                        }
                        if(page==1&&orders.size()==0){
                            mWaitodoAdapter.setEmptyView(view);
                            mWaitodoAdapter.getEmptyView().setVisibility(View.VISIBLE);
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

                    } else {
                        mWaitodoAdapter.setEmptyView(view);
                        mWaitodoAdapter.getEmptyView().setVisibility(View.VISIBLE);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                if(dialog.isShowing())
                    dialog.dismiss();
                if(!NetworkUtils.isConnected()){
                    mWaitodoAdapter.setEmptyView(noWeb);
                    mWaitodoAdapter.getEmptyView().setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_sale:
                Intent intent1=new Intent(AfterSaleActivity.this, H5CommomActivity.class);
                intent1.putExtra("url",Constant.Commom.orderBack+MyApplication.token);
                startActivity(intent1);
                break;
        }
    }
}
