package com.yunkang.chat.home.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.NetworkUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.qiyukf.unicorn.api.ConsultSource;
import com.qiyukf.unicorn.api.Unicorn;
import com.yunkang.chat.MyApplication;
import com.yunkang.chat.R;
import com.yunkang.chat.api.Api;
import com.yunkang.chat.constant.Constant;
import com.yunkang.chat.h5.H5CommomActivity;
import com.yunkang.chat.home.adapter.MsgAdapter;
import com.yunkang.chat.home.adapter.MsgDetailAdapter;
import com.yunkang.chat.home.model.Msg;
import com.yunkang.chat.home.model.MsgDetail;
import com.yunkang.chat.http.HttpProxy;
import com.yunkang.chat.http.ICallBack;
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

public class MsgDetailActivity extends AppCompatActivity {
    @BindView(R.id.rv_msg)
    RecyclerView rv_msg;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    MsgDetailAdapter mMsgAdapter;
    List<Msg> mMsgs;
    String massageType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_detail);
        ButterKnife.bind(this);
        massageType=getIntent().getStringExtra("type");
        initView();
        getData();
    }

    private void getData() {

        showLoading("加载中");
        Map<String,String> params=new HashMap<>();
        params.put("massageType",massageType);
        NetUtils.getInstance().get(MyApplication.token, Api.Message.getPushMassage, params, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                if(dialog.isShowing())
                    dialog.dismiss();
                if (response.contains("data")) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        jsonObject = jsonObject.getJSONObject("data");
                        JSONArray data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            Msg msg = new Gson().fromJson(item.toString(), Msg.class);
                            mMsgs.add(msg);
                        }
                        mMsgAdapter.setNewData(mMsgs);
                        Log.e("getPushMassage", response);
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
                }
            }
        });

    }
    View noWeb;
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



        tv_title.setText("消息详情");
        mMsgs = new ArrayList();
        mMsgAdapter = new MsgDetailAdapter(R.layout.item_msg_detail, mMsgs);
        mMsgAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Msg item= (Msg) adapter.getItem(position);

                int flag=1;
                switch (item.getPushNode()){
                    case "51":
                        startActivity(new Intent(MsgDetailActivity.this, H5CommomActivity.class).putExtra("url", Constant.Commom.orderDetail+MyApplication.token+"&id="+item.getBelongId()));
                        break;
                    case "41":
                        //tv_title.setText("订单已付款");
                        startActivity(new Intent(MsgDetailActivity.this, H5CommomActivity.class).putExtra("url", Constant.Commom.orderDetail+MyApplication.token+"&id="+item.getBelongId()));
                        break;
                    case "33":
                        if(!TextUtils.isEmpty(item.getMassageContent())){
                            if(item.getMassageContent().contains("拒绝")){
                                flag=0;
                            }
                        }
                        startActivity(new Intent(MsgDetailActivity.this, H5CommomActivity.class).putExtra("url", Constant.Commom.newDetail+"type="+33+"&flag="+flag));
                        //认证详情
                        break;
                    case "32":
                        if(!TextUtils.isEmpty(item.getMassageContent())){
                            if(item.getMassageContent().contains("拒绝")){
                                flag=0;
                            }
                        }
                        startActivity(new Intent(MsgDetailActivity.this, H5CommomActivity.class).putExtra("url", Constant.Commom.newDetail+"type="+32+"&flag="+flag));
                        //认证详情
                        break;
                    case "31":
                        startActivity(new Intent(MsgDetailActivity.this, H5CommomActivity.class).putExtra("url", Constant.Commom.cash+MyApplication.token));
                        //tv_title.setText("提现反馈");
                        break;
                    case "21":
                        startActivity(new Intent(MsgDetailActivity.this, H5CommomActivity.class).putExtra("url", Constant.Commom.reportDetail+MyApplication.token+"&id="+item.getBelongId()));
                        //tv_title.setText("报告提醒");
                        break;
                    case "11":
                        startActivity(new Intent(MsgDetailActivity.this, H5CommomActivity.class).putExtra("url", Constant.Commom.disDetail+MyApplication.token+"&id="+item.getBelongId()));
                        //tv_title.setText("评论回复");//评论详情
                        break;
                    case "12":
                       // tv_title.setText("好友提醒");//邀请详情
                        startActivity(new Intent(MsgDetailActivity.this, H5CommomActivity.class).putExtra("url", Constant.Commom.invinteList+MyApplication.token));
                        break;
                        default:
                            break;
                }
            }
        });
        noWeb=getLayoutInflater().inflate(R.layout.no_web_error,null);
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
