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
import android.widget.RelativeLayout;
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
import com.yunkang.chat.home.model.Collect;
import com.yunkang.chat.http.HttpProxy;
import com.yunkang.chat.http.ICallBack;
import com.yunkang.chat.mine.model.Order;
import com.yunkang.chat.net.NetUtils;
import com.yunkang.chat.net.OnMessageReceived;
import com.yunkang.chat.start.model.Medicine;
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
import me.leefeng.promptlibrary.PromptDialog;

public class CollectActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.rv_collect)
    RecyclerView rv_collect;
    @BindView(R.id.refresh)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rl_error)
    RelativeLayout rl_error;
    CollectAdapter mCollectAdapter;
    List<Collect> mCollects;
    int page=1;
    boolean isLoadMore=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        ButterKnife.bind(this);
        initView();
    }
    View view;
    public void refresh() {
        page = 1;
        mCollects.clear();
        View empty=mCollectAdapter.getEmptyView();
        if(empty!=null){
            empty.setVisibility(View.GONE);
        }
        mCollectAdapter.setNewData(mCollects);
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
    private void initView() {


        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                Log.e("refresh","刷新");
                refresh();
                refreshLayout.finishRefresh(1000);
            }
        });
        tv_title.setText("收藏夹");
        iv_back.setOnClickListener(this);
        mCollects=new ArrayList<>();
        view= LayoutInflater.from(this).inflate(R.layout.home_collect_error,null);
        noWeb=getLayoutInflater().inflate(R.layout.no_web_error,null);
        mCollectAdapter=new CollectAdapter(R.layout.item_home_collect,mCollects);
        mCollectAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);

        mCollectAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                isLoadMore = true;
                Log.e("load", "load");
                getListData(page);
            }
        }, rv_collect);
        mCollectAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(view.getId()==R.id.tv_cancel){
                      Collect collect= (Collect) adapter.getItem(position);
                      deleteCollect(collect.getId());
                }
            }
        });
        mCollectAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Collect medicine = (Collect) adapter.getItem(position);
                Intent intent = new Intent(CollectActivity.this, H5CommomActivity.class);
                intent.putExtra("url", Constant.Commom.goodsDetail+MyApplication.token+"&id="+medicine.getId());
                startActivity(intent);
            }
        });
        rv_collect.setLayoutManager(new LinearLayoutManager(this));
        rv_collect.setAdapter(mCollectAdapter);

         getListData(page);

    }
    private void deleteCollect(String id) {
//        Map<String,Object> params=new HashMap<>();
//        params.put("productId",id);
//        HttpProxy.obtain().post(Api.Product.deleteCollect,MyApplication.token, params, new ICallBack() {
//            @Override
//            public void OnSuccess(String result) {
//                Log.e("getMyCollections", result);
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    ToastUtil.showToast(CollectActivity.this,"取消成功");
//                    page=1;
//                    mCollects.clear();
//                    mCollectAdapter.setNewData(mCollects);
//                    getListData(page);
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

        Map<String,String> params=new HashMap<>();
        params.put("productId",id);
        NetUtils.getInstance().post(MyApplication.token, Api.Product.deleteCollect, params, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                Log.e("getMyCollections", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    ToastUtil.showToast(CollectActivity.this,"取消成功");
                    page=1;
                    mCollects.clear();
                    mCollectAdapter.setNewData(mCollects);
                    getListData(page);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {

            }
        });

    }

    private void getListData(int page) {
//        Map<String,Object> params=new HashMap<>();
//        params.put("pageNum",page);
//        Log.e("token", MyApplication.token);
//        if(page==1){
//            showLoading("加载中");
//        }
//        HttpProxy.obtain().get(Api.Product.getMyCollections, params,MyApplication.token, new ICallBack() {
//            @Override
//            public void OnSuccess(String result) {
//                if(dialog.isShowing())
//                    dialog.dismiss();
//                Log.e("getMyCollections", result);
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    JSONObject dataobject = jsonObject.getJSONObject("data");
//                    dataobject = dataobject.getJSONObject("data");
//                    int code = jsonObject.getInt("code");
//                    if (code == 0) {
//                        JSONArray data = dataobject.getJSONArray("beanList");
//                        if (page == 1 && data.length() == 0) {
//                            mCollectAdapter.setEmptyView(view);
//                            mCollectAdapter.getEmptyView().setVisibility(View.VISIBLE);
//                        } else {
//                            List<Collect> orders=new ArrayList<>();
//                            for (int i = 0; i < data.length(); i++) {
//                                JSONObject item = data.getJSONObject(i);
//                                Collect homeCategory = new Gson().fromJson(item.toString(), Collect.class);
//                                mCollects.add(homeCategory);
//                                orders.add(homeCategory);
//                            }
//                            if (isLoadMore) {
//                                isLoadMore = false;
//                                mCollectAdapter.addData(orders);
//                                if (data.length() > 0)
//                                    mCollectAdapter.loadMoreComplete();
//                                else {
//                                    mCollectAdapter.loadMoreEnd(true);
//                                }
//                            } else {
//                                mCollectAdapter.setNewData(mCollects);
//                                // orderAdapter.loadMoreEnd(true);
//                            }
//                        }
//                    } else {
//
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
//                if(!NetworkUtils.isConnected()){
//
//                    mCollectAdapter.setEmptyView(noWeb);
//                    mCollectAdapter.getEmptyView().setVisibility(View.VISIBLE);
//                }
//                if(dialog.isShowing())
//                    dialog.dismiss();
//            }
//        });

        Map<String,String> params=new HashMap<>();
        params.put("pageNum",page + "");
        Log.e("token", MyApplication.token);
        if(page==1){
            showLoading("加载中");
        }
        NetUtils.getInstance().get(MyApplication.token, Api.Product.getMyCollections, params, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                if(dialog.isShowing())
                    dialog.dismiss();
                Log.e("getMyCollections", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject dataobject = jsonObject.getJSONObject("data");
                    dataobject = dataobject.getJSONObject("data");
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        JSONArray data = dataobject.getJSONArray("beanList");
                        if (page == 1 && data.length() == 0) {
                            mCollectAdapter.setEmptyView(view);
                            mCollectAdapter.getEmptyView().setVisibility(View.VISIBLE);
                        } else {
                            List<Collect> orders=new ArrayList<>();
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject item = data.getJSONObject(i);
                                Collect homeCategory = new Gson().fromJson(item.toString(), Collect.class);
                                mCollects.add(homeCategory);
                                orders.add(homeCategory);
                            }
                            if (isLoadMore) {
                                isLoadMore = false;
                                mCollectAdapter.addData(orders);
                                if (data.length() > 0)
                                    mCollectAdapter.loadMoreComplete();
                                else {
                                    mCollectAdapter.loadMoreEnd(true);
                                }
                            } else {
                                mCollectAdapter.setNewData(mCollects);
                                // orderAdapter.loadMoreEnd(true);
                            }
                        }
                    } else {

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                if(!NetworkUtils.isConnected()){

                    mCollectAdapter.setEmptyView(noWeb);
                    mCollectAdapter.getEmptyView().setVisibility(View.VISIBLE);
                }
                if(dialog.isShowing())
                    dialog.dismiss();
            }
        });

    }
    View noWeb;

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
