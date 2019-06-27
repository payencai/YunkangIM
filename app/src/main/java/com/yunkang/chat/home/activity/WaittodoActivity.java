package com.yunkang.chat.home.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
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
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;
import com.yunkang.chat.MyApplication;
import com.yunkang.chat.R;
import com.yunkang.chat.api.Api;
import com.yunkang.chat.constant.Constant;
import com.yunkang.chat.h5.H5CommomActivity;
import com.yunkang.chat.home.adapter.CollectAdapter;
import com.yunkang.chat.home.adapter.WaitodoAdapter;
import com.yunkang.chat.home.model.Collect;
import com.yunkang.chat.home.model.WaitToDo;
import com.yunkang.chat.http.HttpProxy;
import com.yunkang.chat.http.ICallBack;
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

public class WaittodoActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.rv_waitotdo)
    RecyclerView rv_waitotdo;
    @BindView(R.id.refresh)
    SmartRefreshLayout refreshLayout;
    WaitodoAdapter mWaitodoAdapter;
    List<Order> mWaitToDos;
    int page=1;
    boolean isLoadMore=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waittodo);
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
        getListData(page);
    }
    KProgressHUD dialog;
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
    View view;
    View noWeb;
    private void initView() {


        tv_title.setText("待办事项");
        iv_back.setOnClickListener(this);
        mWaitToDos=new ArrayList<>();
        mWaitodoAdapter=new WaitodoAdapter(R.layout.item_home_undo,mWaitToDos);
        mWaitodoAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mWaitodoAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                isLoadMore = true;
                Log.e("load", "load");
                getListData(page);
            }
        }, rv_waitotdo);
        mWaitodoAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(WaittodoActivity.this, H5CommomActivity.class);
                if(view.getId()==R.id.tv_kaidan){
                    String url="";
                    Order order= (Order) adapter.getItem(position);
                    String state=order.getPayState();
                    switch (state){
                        case "1":
                            intent.putExtra("url", Constant.Commom.payOrder
                                    + MyApplication.token + "&id=" + order.getId());
                            startActivityForResult(intent,1);
                            break;
                        case "2":
                        case "3":
                        case "4":
                        case "5":
                            intent.putExtra("url", Constant.Commom.bulu
                                    + MyApplication.token + "&id=" + order.getId());
                            startActivityForResult(intent,2);
                            break;
                        case "8":
//                            intent.putExtra("url", Constant.Commom.goodsDetail
//                                    + MyApplication.token + "&id=" + order.getId());
//                            startActivity(intent);
                            break;
                        case "7":
                            url= Constant.Commom.orderDiscuss+MyApplication.token+"&id="+order.getId();
                            intent.putExtra("url",url);
                            startActivityForResult(intent,3);
                        break;
                    }

                }

            }
        });
        rv_waitotdo.setLayoutManager(new LinearLayoutManager(this));
        rv_waitotdo.setAdapter(mWaitodoAdapter);
        noWeb=getLayoutInflater().inflate(R.layout.no_web_error,null);
        view= LayoutInflater.from(this).inflate(R.layout.home_undo_error, null);
        getListData(page);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                Log.e("refresh","刷新");
                refresh();
                refreshLayout.finishRefresh(1000);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode>0){
            page=1;
            mWaitToDos.clear();
            mWaitodoAdapter.setNewData(mWaitToDos);
            getListData(page);
        }
    }

    private void getListData(int page) {
//        if(page==1){
//            showLoading("加载中");
//        }
//        Map<String, Object> params = new HashMap<>();
//        params.put("page", page);
//        params.put("payState", "5");
//        Log.e("token", MyApplication.token);
//        HttpProxy.obtain().get(Api.Order.getOrderByUser, params, MyApplication.token, new ICallBack() {
//            @Override
//            public void OnSuccess(String result) {
//                if(dialog.isShowing())
//                    dialog.dismiss();
//                Log.e("record", result);
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    JSONObject dataobject = jsonObject.getJSONObject("data");
//                    dataobject = dataobject.getJSONObject("data");
//                    int code = jsonObject.getInt("code");
//                    if (code == 0) {
//                        JSONArray data = dataobject.getJSONArray("beanList");
//                        if (page == 1 && data.length() == 0) {
//                            mWaitodoAdapter.setEmptyView(view);
//                            mWaitodoAdapter.getEmptyView().setVisibility(View.VISIBLE);
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
//                if(dialog.isShowing())
//                    dialog.dismiss();
//                if(!NetworkUtils.isConnected()){
//                    mWaitodoAdapter.setEmptyView(noWeb);
//                    mWaitodoAdapter.getEmptyView().setVisibility(View.VISIBLE);
//                }
//            }
//        });

        if(page==1){
            showLoading("加载中");
        }
        Map<String, String> params = new HashMap<>();
        params.put("page", page + "");
        params.put("payState", "5");
        Log.e("token", MyApplication.token);
        NetUtils.getInstance().get(MyApplication.token, Api.Order.getOrderByUser, params, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                if(dialog.isShowing())
                    dialog.dismiss();
                Log.e("record", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject dataobject = jsonObject.getJSONObject("data");
                    dataobject = dataobject.getJSONObject("data");
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        JSONArray data = dataobject.getJSONArray("beanList");
                        if (page == 1 && data.length() == 0) {
                            mWaitodoAdapter.setEmptyView(view);
                            mWaitodoAdapter.getEmptyView().setVisibility(View.VISIBLE);
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
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
