package com.yunkang.chat.home.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.blankj.utilcode.util.NetworkUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yunkang.chat.MyApplication;
import com.yunkang.chat.R;
import com.yunkang.chat.api.Api;
import com.yunkang.chat.constant.Constant;
import com.yunkang.chat.h5.H5CommomActivity;
import com.yunkang.chat.home.activity.NewReportActivity;
import com.yunkang.chat.home.adapter.ReportAdapter;
import com.yunkang.chat.home.model.Report;
import com.yunkang.chat.http.HttpProxy;
import com.yunkang.chat.http.ICallBack;
import com.yunkang.chat.net.NetUtils;
import com.yunkang.chat.net.OnMessageReceived;
import com.yunkang.chat.tools.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportFragment extends Fragment {
    @BindView(R.id.rv_report)
    RecyclerView rv_report;
    @BindView(R.id.refresh)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.tv_num)
    TextView tvnum;
    @BindView(R.id.tv_start)
    TextView tvstart;
    @BindView(R.id.tv_end)
    TextView tvend;
    @BindView(R.id.tv_search)
    TextView tvsearch;
    @BindView(R.id.rl_right)
    RelativeLayout    rl_right;
    @BindView(R.id.rl_left)
    RelativeLayout rl_left;
    List<Report> reports;
    ReportAdapter reportAdapter;
    boolean isSearch = false;
    TimePickerView startPicker;
    TimePickerView endPicker;
    int type;
    View empty;

    public static  ReportFragment newInstance(int type) {
        ReportFragment reportFragment=new ReportFragment();
        Bundle bundle=new Bundle();
        bundle.putInt("type",type);
        reportFragment.setArguments(bundle);
        return  reportFragment;
        // Required empty public constructor
    }
    public void refresh() {
        tvstart.setText("开始时间");
        tvend.setText("结束时间");
        isSearch=false;
        reports.clear();
        reportAdapter.setNewData(reports);
        reportAdapter.isUseEmpty(false);
        getListData(type);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_report, container, false);
        ButterKnife.bind(this,view);
        type=getArguments().getInt("type");
        initView();
        return view;
    }
    boolean isAsk=false;
    public  void compareDate(Date date1, Date date2) {

         if (date1.after(date2)) {
            ToastUtil.showToast(getContext(),"开始日期不能大于结束日期");
            return;
        }
    }
    public  void compareDate(String date1, String date2) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date3 = format.parse(date1);
            Date date4 = format.parse(date2);
            //compareDate(date3,date4);//方式一
            compareDate(date3, date4);//方式二
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    KProgressHUD dialog;
    private void showLoading(String data){
        dialog= KProgressHUD.create(getContext())
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


        tvstart.setText("开始时间");
        tvend.setText("结束时间");

        rl_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectStarTime();
            }
        });
        rl_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectEndTime();
            }
        });
        tvsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("开始时间".equals(tvstart.getText().toString())){
                    ToastUtil.showToast(getContext(),"请选择开始时间");
                    return;
                }
                if("结束时间".equals(tvend.getText().toString())){
                    ToastUtil.showToast(getContext(),"请选择结束时间");
                    return;
                }
                compareDate(tvstart.getText().toString()+" 00:00:00",tvend.getText().toString()+" 23:59:59");
                if(isAsk){
                    return;
                }
                reports.clear();
                isSearch=true;
                reportAdapter.notifyDataSetChanged();
                getListData(type);
            }
        });
        NewReportActivity activity= (NewReportActivity) getActivity();
        tvnum.setText(activity.finishNum+activity.unFinishNum+"");
        empty = LayoutInflater.from(getContext()).inflate(R.layout.home_report_error, null);
        reports = new ArrayList<>();
        reportAdapter = new ReportAdapter(R.layout.item_home_report, reports);

        reportAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                Intent intent2 = new Intent(getContext(), H5CommomActivity.class);
                intent2.putExtra("url", Constant.Commom.reportDetail
                        + MyApplication.token + "&id=" + reports.get(position).getId());
                startActivity(intent2);
            }
        });

        rv_report.setLayoutManager(new LinearLayoutManager(getContext()));

        rv_report.setAdapter(reportAdapter);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                Log.e("refresh","刷新");
                refresh();
                refreshLayout.finishRefresh(1000);
            }
        });
        getListData(type);
    }
    private void selectStarTime() {
        //时间选择器
        startPicker = new TimePickerBuilder(getContext(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                tvstart.setText(ConverToString(date));
            }
        }).setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")
                .isCenterLabel(false).build();
        startPicker.show();
    }

    public static String ConverToString(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        return df.format(date);
    }

    private void selectEndTime() {
        //时间选择器
        endPicker = new TimePickerBuilder(getContext(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                tvend.setText(ConverToString(date));
            }
        }).setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")
                .isCenterLabel(false).build();
        endPicker.show();
    }

    private void getListData(int type) {
        isAsk=true;
        Map<String, String> params = new HashMap<>();
        params.put("type", type + "");
        if (isSearch) {
            params.put("startTime", tvstart.getText().toString()+" 00:00:00");
            params.put("endTime", tvend.getText().toString()+" 23:59:59");
            isSearch = false;
        }
        Log.e("startTime", tvstart.getText().toString()+" 00:00:00");
        Log.e("endTime", tvend.getText().toString()+" 23:59:59");
        showLoading("加载中");
//        HttpProxy.obtain().get(Api.Order.getPDFUploadingList, params, MyApplication.token, new ICallBack() {
//            @Override
//            public void OnSuccess(String result) {
//                Log.e("getPDFUploadingList", result);
//                if(dialog.isShowing())
//                    dialog.dismiss();
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    JSONObject dataobject = jsonObject.getJSONObject("data");
//                    int code = jsonObject.getInt("code");
//                    if (code == 0) {
//                        JSONArray data = dataobject.getJSONArray("data");
//                        if(data.length()==0){
//                            reportAdapter.setEmptyView(empty);
//                            reportAdapter.isUseEmpty(true);
//                        }
//                        for (int i = 0; i < data.length(); i++) {
//                            JSONObject item = data.getJSONObject(i);
//                            Report report = new Gson().fromJson(item.toString(), Report.class);
//                            reports.add(report);
//                        }
//                        reportAdapter.setNewData(reports);
//
//                    } else {
//                        // reportAdapter.setEmptyView(empty);
//                    }
//                    isAsk=false;
//
//                } catch (JSONException e) {
//                    ///reportAdapter.setEmptyView(empty);
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(String error) {
//                isAsk=false;
//                if(dialog.isShowing())
//                    dialog.dismiss();
//                if(!NetworkUtils.isConnected()){
//                    View view=getLayoutInflater().inflate(R.layout.no_web_error,null);
//                    reportAdapter.setEmptyView(view);
//                }
//            }
//        });

        NetUtils.getInstance().get(MyApplication.token, Api.Order.getPDFUploadingList, params, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                Log.e("getPDFUploadingList", response);
                if(dialog.isShowing())
                    dialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject dataobject = jsonObject.getJSONObject("data");
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        JSONArray data = dataobject.getJSONArray("data");
                        if(data.length()==0){
                            reportAdapter.setEmptyView(empty);
                            reportAdapter.isUseEmpty(true);
                        }
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            Report report = new Gson().fromJson(item.toString(), Report.class);
                            reports.add(report);
                        }
                        reportAdapter.setNewData(reports);

                    } else {
                        // reportAdapter.setEmptyView(empty);
                    }
                    isAsk=false;

                } catch (JSONException e) {
                    ///reportAdapter.setEmptyView(empty);
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                isAsk=false;
                if(dialog.isShowing())
                    dialog.dismiss();
                if(!NetworkUtils.isConnected()){
                    View view=getLayoutInflater().inflate(R.layout.no_web_error,null);
                    reportAdapter.setEmptyView(view);
                }
            }
        });

    }
}
