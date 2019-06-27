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
import com.yunkang.chat.home.adapter.RecordAdapter;
import com.yunkang.chat.home.adapter.WaitodoAdapter;
import com.yunkang.chat.home.model.Record;
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

public class RecordActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.rv_record)
    RecyclerView rv_record;
    @BindView(R.id.refresh)
    SmartRefreshLayout refreshLayout;
    RecordAdapter mRecordAdapter;
    List<Order> mWaitToDos=new ArrayList<>();

    int page=1;
    boolean isLoadMore=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    public void refresh() {
        page = 1;
        mWaitToDos.clear();
        View empty=mRecordAdapter.getEmptyView();
        if(empty!=null){
            empty.setVisibility(View.GONE);
        }
        mRecordAdapter.setNewData(mWaitToDos);
        getListData();
    }

    View noWeb;
    private void initView() {


        page=1;
        tv_title.setText("检测补录");
        iv_back.setOnClickListener(this);
        view=LayoutInflater.from(this).inflate(R.layout.home_undo_error, null);
        noWeb=getLayoutInflater().inflate(R.layout.no_web_error,null);
        mRecordAdapter=new RecordAdapter(R.layout.item_home_record,mWaitToDos);
        mRecordAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mRecordAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                isLoadMore = true;
                Log.e("load", "load");
                getListData();
            }
        }, rv_record);
        mRecordAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(view.getId()==R.id.tv_luru){
                    Intent intent = new Intent(RecordActivity.this, H5CommomActivity.class);
                    Order order= (Order) adapter.getItem(position);
                    intent.putExtra("url", Constant.Commom.bulu
                            + MyApplication.token + "&id=" + order.getId());
                    startActivityForResult(intent,1);
                }
            }
        });
        rv_record.setLayoutManager(new LinearLayoutManager(this));
        rv_record.setAdapter(mRecordAdapter);
        getListData();
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                Log.e("refresh","刷新");
                refresh();
                refreshLayout.finishRefresh(1000);
            }
        });
    }

    View view;
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
    private void getListData() {
//        if(page==1){
//            showLoading("加载中");
//        }
//        Map<String, Object> params = new HashMap<>();
//        params.put("page", page);
//        params.put("payState", "4");
//       // Log.e("token",MyApplication.getUserInfo().getToken());
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
//                            mRecordAdapter.setEmptyView(view);
//                            mRecordAdapter.getEmptyView().setVisibility(View.VISIBLE);
//                        } else {
//                            List<Order> orders=new ArrayList<>();
//                            for (int i = 0; i < data.length(); i++) {
//                                JSONObject item = data.getJSONObject(i);
//                                Order homeCategory = new Gson().fromJson(item.toString(), Order.class);
//                                mWaitToDos.add(homeCategory);
//                                orders.add(homeCategory);
//                            }
//                            if (isLoadMore) {
//                                isLoadMore = false;
//                                mRecordAdapter.addData(orders);
//                                if (data.length() > 0)
//                                    mRecordAdapter.loadMoreComplete();
//                                else {
//                                    mRecordAdapter.loadMoreEnd(true);
//                                }
//                            } else {
//                                mRecordAdapter.setNewData(mWaitToDos);
//
//                                // orderAdapter.loadMoreEnd(true);
//                            }
//                        }
//                    } else {
//                        mRecordAdapter.setEmptyView(view);
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
//                if(dialog.isShowing())
//                    dialog.dismiss();
//                if(!NetworkUtils.isConnected()){
//
//                    mRecordAdapter.setEmptyView(noWeb);
//                    mRecordAdapter.getEmptyView().setVisibility(View.VISIBLE);
//                }
//            }
//        });

        if(page==1){
            showLoading("加载中");
        }
        Map<String, String> params = new HashMap<>();
        params.put("page", page + "");
        params.put("payState", "4");
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
                            mRecordAdapter.setEmptyView(view);
                            mRecordAdapter.getEmptyView().setVisibility(View.VISIBLE);
                        } else {
                            List<Order> orders=new ArrayList<>();
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject item = data.getJSONObject(i);
                                Order homeCategory = new Gson().fromJson(item.toString(), Order.class);
                                mWaitToDos.add(homeCategory);
                                orders.add(homeCategory);
                            }
                            if (isLoadMore) {
                                isLoadMore = false;
                                mRecordAdapter.addData(orders);
                                if (data.length() > 0)
                                    mRecordAdapter.loadMoreComplete();
                                else {
                                    mRecordAdapter.loadMoreEnd(true);
                                }
                            } else {
                                mRecordAdapter.setNewData(mWaitToDos);

                                // orderAdapter.loadMoreEnd(true);
                            }
                        }
                    } else {
                        mRecordAdapter.setEmptyView(view);
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

                    mRecordAdapter.setEmptyView(noWeb);
                    mRecordAdapter.getEmptyView().setVisibility(View.VISIBLE);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
          refresh();
        }
    }
}
