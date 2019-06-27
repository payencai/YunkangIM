package com.yunkang.chat.home.activity;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;
import com.yunkang.chat.MyApplication;
import com.yunkang.chat.R;
import com.yunkang.chat.api.Api;
import com.yunkang.chat.home.fragment.ReportFragment;
import com.yunkang.chat.http.HttpProxy;
import com.yunkang.chat.http.ICallBack;
import com.yunkang.chat.net.NetUtils;
import com.yunkang.chat.net.OnMessageReceived;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewReportActivity extends AppCompatActivity implements View.OnClickListener{

    ArrayList<Fragment> mFragments;
    @BindView(R.id.vp_report)
    ViewPager vp_report;
    @BindView(R.id.tab_report)
    SlidingTabLayout tab_report;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    public static int finishNum = 0;
    public static int unFinishNum = 0;
    String []mTitles=new String[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_report);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

        mFragments=new ArrayList<>();
        tv_title.setText("检测报告");
        iv_back.setOnClickListener(this);
        showLoading();
        getFinishNum();
    }
    KProgressHUD dialog;
    private void showLoading(){
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
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;

        }
    }
    private void getFinishNum() {
//        Map<String, Object> params = new HashMap<>();
//        params.put("type", 1);
//        HttpProxy.obtain().get(Api.Order.getPDFNum, params, MyApplication.token, new ICallBack() {
//            @Override
//            public void OnSuccess(String result) {
//
//                Log.e("result", result);
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    JSONObject dataobject = jsonObject.getJSONObject("data");
//                    int code = jsonObject.getInt("code");
//                    if (code == 0) {
//                        int data = dataobject.getInt("data");
//                        finishNum = data;
//                        mTitles[0]="已出报告("+finishNum+")";
//                        getUnFinishNum();
//                    } else {
//                        //mCollectAdapter.setEmptyView(view);
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
//            }
//        });

        Map<String, String> params = new HashMap<>();
        params.put("type", "1");
        NetUtils.getInstance().get(MyApplication.token, Api.Order.getPDFNum, params, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                Log.e("result", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject dataobject = jsonObject.getJSONObject("data");
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        int data = dataobject.getInt("data");
                        finishNum = data;
                        mTitles[0]="已出报告("+finishNum+")";
                        getUnFinishNum();
                    } else {
                        //mCollectAdapter.setEmptyView(view);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                if(dialog.isShowing())
                    dialog.dismiss();
            }
        });

    }
    private void getUnFinishNum() {
//        Map<String, Object> params = new HashMap<>();
//        params.put("type", 2);
//        HttpProxy.obtain().get(Api.Order.getPDFNum, params, MyApplication.token, new ICallBack() {
//            @Override
//            public void OnSuccess(String result) {
//                if(dialog.isShowing())
//                    dialog.dismiss();
//                Log.e("result", result);
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    JSONObject dataobject = jsonObject.getJSONObject("data");
//                    int code = jsonObject.getInt("code");
//                    if (code == 0) {
//                        int data = dataobject.getInt("data");
//                        unFinishNum = data;
//                        mTitles[1]="未出报告("+unFinishNum+")";
//                        iniTag();
//                    } else {
//                        //mCollectAdapter.setEmptyView(view);
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
//            }
//        });

        Map<String, String> params = new HashMap<>();
        params.put("type", "2");
        NetUtils.getInstance().get(MyApplication.token, Api.Order.getPDFNum, params, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                if(dialog.isShowing())
                    dialog.dismiss();
                Log.e("result", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject dataobject = jsonObject.getJSONObject("data");
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        int data = dataobject.getInt("data");
                        unFinishNum = data;
                        mTitles[1]="未出报告("+unFinishNum+")";
                        iniTag();
                    } else {
                        //mCollectAdapter.setEmptyView(view);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                if(dialog.isShowing())
                    dialog.dismiss();
            }
        });

    }
    private void iniTag(){
        for (int i = 1; i <3 ; i++) {
            ReportFragment reportFragment=ReportFragment.newInstance(i);
            mFragments.add(reportFragment);
        }
        tab_report.setViewPager(vp_report,mTitles,this,mFragments);
    }
}
