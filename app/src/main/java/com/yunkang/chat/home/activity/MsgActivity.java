package com.yunkang.chat.home.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;

import com.qiyukf.unicorn.api.ConsultSource;
import com.qiyukf.unicorn.api.UICustomization;
import com.qiyukf.unicorn.api.Unicorn;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yunkang.chat.MyApplication;
import com.yunkang.chat.R;
import com.yunkang.chat.api.Api;
import com.yunkang.chat.home.adapter.MsgAdapter;
import com.yunkang.chat.home.model.Msg;
import com.yunkang.chat.http.HttpProxy;
import com.yunkang.chat.http.ICallBack;
import com.yunkang.chat.mine.view.DeleteRecyclerView;
import com.yunkang.chat.net.NetUtils;
import com.yunkang.chat.net.OnMessageReceived;
import com.yunkang.chat.start.activity.BaseActivity;
import com.yunkang.chat.start.activity.MainActivity;
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

public class MsgActivity extends BaseActivity {
    @BindView(R.id.rv_msg)
    RecyclerView rv_msg;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.refresh)
    SmartRefreshLayout refreshLayout;
    MsgAdapter mMsgAdapter;
    List<Msg> mMsgs;

    boolean isHasService = false;
    boolean isHasHudong = false;
    boolean isHasAccount = false;
    boolean isHasJiaoyi = false;
    boolean isHasWuliu = false;
    boolean isHasUnreadService = false;
    boolean isHasUnreadHudong = false;
    boolean isHasUnreadAccount = false;
    boolean isHasUnreadJiaoyi = false;
    boolean isHasUnreadWuliu = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);
        ButterKnife.bind(this);
        initView();
        getData();
    }
    public void refresh() {

        mMsgs.clear();
        View empty=mMsgAdapter.getEmptyView();
        if(empty!=null){
            empty.setVisibility(View.GONE);
        }
        mMsgAdapter.setNewData(mMsgs);
        getData();
    }
    Msg Hudong;
    Msg Service;
    Msg Account;
    Msg Jiaoyi;
    Msg Wuliu;
    View noWeb;
    private void getData() {
        showLoading("加载中");


        NetUtils.getInstance().get(MyApplication.token, Api.Message.getPushMassage, new HashMap<>(), new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                if(dialog.isShowing())
                    dialog.dismiss();
                Log.e("result", response);
                if (response.contains("data")) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        jsonObject = jsonObject.getJSONObject("data");
                        JSONArray data = jsonObject.getJSONArray("data");
                        if(data.length()==0){
                            mMsgAdapter.setEmptyView(empty);
                        }
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            Msg msg = new Gson().fromJson(item.toString(), Msg.class);
                            switch (msg.getMassageType()) {
                                case "1":
                                    Hudong=msg;
                                    if (!isHasHudong) {
                                        mMsgs.add(msg);
                                    }
                                    if (msg.getReadStatus().equals("0")) {
                                        isHasUnreadHudong = true;
                                    }
                                    isHasHudong = true;
                                    break;
                                case "2":
                                    Service=msg;
                                    if (msg.getReadStatus().equals("0")) {
                                        isHasUnreadService = true;
                                    }
                                    if (!isHasService) {
                                        mMsgs.add(msg);
                                    }
                                    isHasService = true;
                                    break;
                                case "3":
                                    Account=msg;
                                    if (msg.getReadStatus().equals("0")) {
                                        isHasUnreadAccount = true;
                                    }
                                    if (!isHasAccount) {
                                        mMsgs.add(msg);
                                    }
                                    isHasAccount = true;
                                    break;
                                case "4":
                                    Jiaoyi=msg;
                                    if (msg.getReadStatus().equals("0")) {
                                        isHasUnreadJiaoyi = true;
                                    }
                                    if (!isHasJiaoyi) {
                                        mMsgs.add(msg);
                                    }
                                    isHasJiaoyi = true;
                                    break;
                                case "5":
                                    Wuliu=msg;
                                    if (msg.getReadStatus().equals("0")) {
                                        isHasUnreadWuliu = true;
                                    }
                                    if (!isHasWuliu) {
                                        mMsgs.add(msg);
                                    }
                                    isHasWuliu = true;
                                    break;
                            }
                        }
                        List<Msg> newMsg = new ArrayList<>();
                        if(Hudong!=null){
                            newMsg.add(Hudong);
                        }
                        if(Service!=null){
                            newMsg.add(Service);
                        }
                        if(Account!=null){
                            newMsg.add(Account);
                        }
                        if(Jiaoyi!=null){
                            newMsg.add(Jiaoyi);
                        }
                        if(Wuliu!=null){
                            newMsg.add(Wuliu);
                        }
                        mMsgAdapter.setNewData(newMsg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }

            @Override
            public void onError(String error) {
                if(dialog.isShowing())
                    dialog.dismiss();
                if(!NetworkUtils.isConnected()){
                    mMsgAdapter.setEmptyView(noWeb);
                    mMsgAdapter.getEmptyView().setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void delete(String type) {
//        Map<String,Object> params=new HashMap<>();
//        params.put("delStatus","1");
//        params.put("massageType",type);
//        String json=new Gson().toJson(params);
//        HttpProxy.obtain().post(Api.Message.delAllPushMassage,  MyApplication.token,json, new ICallBack() {
//            @Override
//            public void OnSuccess(String result) {
//                Log.e("reslut",result);
//                if (result.contains("data")) {
//                    ToastUtil.showToast(MsgActivity.this,"删除成功");
//                }else{
//                    ToastUtil.showToast(MsgActivity.this,"删除出错");
//                }
//            }
//
//            @Override
//            public void onFailure(String error) {
//
//            }
//        });

        Map<String,String> params=new HashMap<>();
        params.put("delStatus","1");
        params.put("massageType",type);
        NetUtils.getInstance().post(MyApplication.token, Api.Message.delAllPushMassage, params, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                Log.e("reslut",response);
                if (response.contains("data")) {
                    ToastUtil.showToast(MsgActivity.this,"删除成功");
                }else{
                    ToastUtil.showToast(MsgActivity.this,"删除出错");
                }
            }

            @Override
            public void onError(String error) {

            }
        });

    }
    View empty;
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


        tv_title.setText("消息");
        mMsgs = new ArrayList();
        empty = LayoutInflater.from(this).inflate(R.layout.home_msg_error, null);
        noWeb=getLayoutInflater().inflate(R.layout.no_web_error,null);
        mMsgAdapter = new MsgAdapter(R.layout.item_home_msg, mMsgs);
        mMsgAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Msg msg = mMsgAdapter.getItem(position);
                if (view.getId() == R.id.btnDelete) {

                    adapter.getData().remove(position);
                    mMsgAdapter.setNewData(adapter.getData());
                    delete(msg.getMassageType());
                }
                if(view.getId() ==R.id.ll_content){
                    Intent intent=new Intent(MsgActivity.this,MsgDetailActivity.class);
                    intent.putExtra("type",msg.getMassageType());
                    startActivity(intent);
//                    String title = "客服";
//
//                    ConsultSource source = new ConsultSource("msg", "客户", "custom information string");
//                    Unicorn.openServiceActivity(MsgActivity.this, title, source);
                }
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
        rv_msg.setLayoutManager(new LinearLayoutManager(this));
        rv_msg.setAdapter(mMsgAdapter);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
