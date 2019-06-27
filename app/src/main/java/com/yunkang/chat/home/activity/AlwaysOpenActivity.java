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
import com.yunkang.chat.home.adapter.AlwaysOpenAdapter;
import com.yunkang.chat.home.adapter.CollectAdapter;
import com.yunkang.chat.home.model.AlwaysOpen;
import com.yunkang.chat.home.model.Collect;
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
import me.leefeng.promptlibrary.PromptDialog;

public class AlwaysOpenActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.rv_always)
    RecyclerView rv_collect;
    @BindView(R.id.refresh)
    SmartRefreshLayout refreshLayout;
    AlwaysOpenAdapter mCollectAdapter;
    List<Medicine> mMedicines;
    View empty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_always_open);
        ButterKnife.bind(this);
        initView();
    }
    private void refresh(){
        mMedicines.clear();
        View empty=mCollectAdapter.getEmptyView();
        if(empty!=null){
            empty.setVisibility(View.GONE);
        }
        mCollectAdapter.setNewData(mMedicines);
        getListData();
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
    View noWeb;
    private void initView() {


        empty= LayoutInflater.from(this).inflate(R.layout.home_always_error, null);
        tv_title.setText("常开检测");
        iv_back.setOnClickListener(this);
        mMedicines=new ArrayList<>();
//        for (int i = 0; i <10 ; i++) {
//            mMedicines.add(new Medicine());
//        }
        noWeb=getLayoutInflater().inflate(R.layout.no_web_error,null);
        mCollectAdapter=new AlwaysOpenAdapter(R.layout.item_home_always,mMedicines);
        mCollectAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);

        mCollectAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(view.getId()==R.id.tv_open){
                    Medicine medicine = (Medicine) adapter.getItem(position);
                    Intent intent = new Intent(AlwaysOpenActivity.this, H5CommomActivity.class);
                    intent.putExtra("url", Constant.Commom.goodsDetail+MyApplication.token+"&id="+medicine.getId());
                    startActivity(intent);
                }
            }
        });
        rv_collect.setLayoutManager(new LinearLayoutManager(this));
        rv_collect.setAdapter(mCollectAdapter);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                Log.e("refresh","刷新");
                refresh();

            }
        });
        getListData();
    }
    private void getListData(){
        showLoading("加载中");
//        HttpProxy.obtain().get(Api.Product.getMyUsualbuys, MyApplication.token, new ICallBack() {
//            @Override
//            public void OnSuccess(String result) {
//                if(dialog.isShowing())
//                    dialog.dismiss();
//                refreshLayout.finishRefresh();
//                Log.e("getMyUsualbuys", result);
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    JSONObject dataobject = jsonObject.getJSONObject("data");
//                    int code = jsonObject.getInt("code");
//                    if (code == 0) {
//                        JSONArray data = dataobject.getJSONArray("data");
//                        if (data.length() == 0) {
//                            mCollectAdapter.setEmptyView(empty);
//                            mCollectAdapter.getEmptyView().setVisibility(View.VISIBLE);
//                        } else {
//                            for (int i = 0; i < data.length(); i++) {
//                                JSONObject item = data.getJSONObject(i);
//                                Medicine homeCategory = new Gson().fromJson(item.toString(), Medicine.class);
//                                mMedicines.add(homeCategory);
//                            }
//                            mCollectAdapter.setNewData(mMedicines);
//                        }
//                    } else {
//                        mCollectAdapter.setEmptyView(empty);
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
//                    mCollectAdapter.setEmptyView(noWeb);
//                    mCollectAdapter.getEmptyView().setVisibility(View.VISIBLE);
//                }
//            }
//        });

        NetUtils.getInstance().get(MyApplication.token, Api.Product.getMyUsualbuys, new HashMap<>(), new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                if(dialog.isShowing())
                    dialog.dismiss();
                refreshLayout.finishRefresh();
                Log.e("getMyUsualbuys", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject dataobject = jsonObject.getJSONObject("data");
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        JSONArray data = dataobject.getJSONArray("data");
                        if (data.length() == 0) {
                            mCollectAdapter.setEmptyView(empty);
                            mCollectAdapter.getEmptyView().setVisibility(View.VISIBLE);
                        } else {
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject item = data.getJSONObject(i);
                                Medicine homeCategory = new Gson().fromJson(item.toString(), Medicine.class);
                                mMedicines.add(homeCategory);
                            }
                            mCollectAdapter.setNewData(mMedicines);
                        }
                    } else {
                        mCollectAdapter.setEmptyView(empty);
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
                    mCollectAdapter.setEmptyView(noWeb);
                    mCollectAdapter.getEmptyView().setVisibility(View.VISIBLE);
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
