package com.yunkang.chat.start.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import com.lzy.okgo.model.HttpParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yunkang.chat.MyApplication;
import com.yunkang.chat.R;
import com.yunkang.chat.api.Api;
import com.yunkang.chat.constant.Constant;
import com.yunkang.chat.h5.H5CommomActivity;
import com.yunkang.chat.http.HttpProxy;
import com.yunkang.chat.http.ICallBack;
import com.yunkang.chat.mine.activity.AfterSaleActivity;
import com.yunkang.chat.mine.activity.MyOrderActivity;
import com.yunkang.chat.mine.activity.MyWalletActivity;
import com.yunkang.chat.mine.activity.SettingsActivity;
import com.yunkang.chat.mine.activity.UpdateNicknameActivity;
import com.yunkang.chat.mine.activity.UserInfoActivity;
import com.yunkang.chat.net.NetUtils;
import com.yunkang.chat.net.OnMessageReceived;
import com.yunkang.chat.start.activity.MainActivity;
import com.yunkang.chat.start.model.UserInfo;
import com.yunkang.chat.tools.MathUtil;
import com.yunkang.chat.tools.ToastUtil;
import com.yunkang.chat.view.CircleImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.rl_tickets)
    RelativeLayout rl_tickets;
    @BindView(R.id.rl_mycode)
    RelativeLayout rl_mycode;
    @BindView(R.id.rl_device)
    RelativeLayout rl_device;
    @BindView(R.id.rl_setting)
    RelativeLayout rl_setting;
    @BindView(R.id.rl_wuliu)
    RelativeLayout rl_wuliu;
    @BindView(R.id.refresh)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rl_shouhou)
    RelativeLayout rl_shouhou;
    @BindView(R.id.rl_address)
    RelativeLayout rl_address;
    @BindView(R.id.rl_invate)
    RelativeLayout rl_invate;
    @BindView(R.id.ll_money)
    LinearLayout ll_money;
    @BindView(R.id.ll_unpay)
    LinearLayout ll_unpay;
    @BindView(R.id.ll_wallet)
    LinearLayout ll_wallet;
    @BindView(R.id.ll_daichu)
    LinearLayout ll_daichu;
    @BindView(R.id.ll_finish)
    LinearLayout ll_finish;
    @BindView(R.id.ll_myorder)
    LinearLayout ll_myorder;
    @BindView(R.id.ll_coupon)
    LinearLayout ll_coupon;
    @BindView(R.id.ll_jifen)
    LinearLayout ll_jifen;
    @BindView(R.id.ll_top)
    LinearLayout ll_top;
    @BindView(R.id.iv_head)
    CircleImageView iv_head;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_renzhen)
    TextView tv_renzhen;
    @BindView(R.id.tv_money)
    TextView tv_money;
    @BindView(R.id.tv_youhui)
    TextView tv_youhui;
    @BindView(R.id.iv_edit)
    ImageView iv_edit;
    @BindView(R.id.tv_jifen)
    TextView tv_jifen;

    public MineFragment() {
        // Required empty public constructor
    }

    boolean isChange = false;
    UserInfo mUserInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.bind(this, view);
        initView();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserInfo();
        getCount();
    }

    private void getUserInfo() {
//        HttpProxy.obtain().get(Api.User.getUserDtoByToken, MyApplication.token, new ICallBack() {
//            @Override
//            public void OnSuccess(String result) {
//                Log.e("getUserDtoByToken", result);
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    int code = jsonObject.getInt("code");
//                    if (code == 0) {
//                        JSONObject data = jsonObject.getJSONObject("data");
//                        data = data.getJSONObject("data");
//                        mUserInfo = new Gson().fromJson(data.toString(), UserInfo.class);
//                        MyApplication.setUserInfo(mUserInfo);
//                        tv_jifen.setText(doubleTranString(mUserInfo.getIntegral())+"");
//                        tv_name.setText(mUserInfo.getNickName());
//                        tv_money.setText(doubleTranString(mUserInfo.getBalance())+"");
//                        if (!TextUtils.isEmpty(MyApplication.getUserInfo().getHeadPortrait())) {
//                            Glide.with(getContext()).load(MyApplication.getUserInfo().getHeadPortrait()).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(iv_head);
//                            //Glide.with(getContext()).load(MyApplication.getUserInfo().getHeadPortrait()).into(iv_head);
//                        }
//                        if ("2".equals(MyApplication.getUserInfo().getType())||"1".equals(MyApplication.getUserInfo().getRealNameStatus())) {
//                            tv_renzhen.setText("已认证");
//                        }else{
//                            tv_renzhen.setText("未认证");
//                        }
//                    }
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
        NetUtils.getInstance().get(MyApplication.token, Api.User.getUserDtoByToken, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                Log.e("getUserDtoByToken", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        data = data.getJSONObject("data");
                        mUserInfo = new Gson().fromJson(data.toString(), UserInfo.class);
                        MyApplication.setUserInfo(mUserInfo);
                        tv_jifen.setText(doubleTranString(mUserInfo.getIntegral()) + "");
                        tv_name.setText(mUserInfo.getNickName());
                        tv_money.setText(doubleTranString(mUserInfo.getBalance()) + "");
                        if (!TextUtils.isEmpty(MyApplication.getUserInfo().getHeadPortrait())) {
                            Glide.with(getContext()).load(MyApplication.getUserInfo().getHeadPortrait()).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(iv_head);
                            //Glide.with(getContext()).load(MyApplication.getUserInfo().getHeadPortrait()).into(iv_head);
                        }
                        if ("2".equals(MyApplication.getUserInfo().getType()) || "1".equals(MyApplication.getUserInfo().getRealNameStatus())) {
                            tv_renzhen.setText("已认证");
                        } else {
                            tv_renzhen.setText("未认证");
                        }
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

    public static String doubleTranString(double num) {
        if (num % 1.0 == 0) {
            return String.valueOf((long) num);
        }

        return String.valueOf(num);
    }

    private void getCount() {
        HttpParams httpParams = new HttpParams();
        httpParams.put("couponStatus", 0);
        NetUtils.getInstance().get(MyApplication.token, Api.Order.getCouponDetailByUserId, httpParams, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                Log.e("getCouponDetailByUserId", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray jsonArray = data.getJSONArray("data");
                        tv_youhui.setText(jsonArray.length() + "");
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

    private void initView() {
        ll_wallet.setOnClickListener(this);
        rl_shouhou.setOnClickListener(this);
        rl_tickets.setOnClickListener(this);
        rl_device.setOnClickListener(this);
        rl_setting.setOnClickListener(this);
        rl_address.setOnClickListener(this);
        ll_unpay.setOnClickListener(this);
        ll_daichu.setOnClickListener(this);
        ll_finish.setOnClickListener(this);
        ll_myorder.setOnClickListener(this);
        ll_coupon.setOnClickListener(this);
        rl_wuliu.setOnClickListener(this);
        ll_jifen.setOnClickListener(this);
        rl_invate.setOnClickListener(this);
        ll_money.setOnClickListener(this);
        ll_top.setOnClickListener(this);
        rl_mycode.setOnClickListener(this);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getCount();
                getUserInfo();
                refreshLayout.finishRefresh(1000);
            }
        });
        iv_edit.setOnClickListener(this);
//        if (MyApplication.getUserInfo() != null) {
//            tv_name.setText(MyApplication.getUserInfo().getNickName());
//            if (!TextUtils.isEmpty(MyApplication.getUserInfo().getHeadPortrait())) {
//                Glide.with(getContext()).load(MyApplication.getUserInfo().getHeadPortrait()).into(iv_head);
//            }
//        }


        getCount();
    }

    public static String formatDouble3(double d) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        // 保留两位小数
        nf.setMaximumFractionDigits(1);
        // 如果不需要四舍五入，可以使用RoundingMode.DOWN
        nf.setRoundingMode(RoundingMode.UP);

        return nf.format(d);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_mycode:
                Log.e("custId",MyApplication.getUserInfo().getCustId());
                if ("2".equals(MyApplication.getUserInfo().getType()))

                    startActivity(new Intent(getContext(), H5CommomActivity.class).putExtra("url", Constant.Commom.myQrcode + MyApplication.getUserInfo().getCustId()));
//                    else{
//                        ToastUtil.showToast(getContext(),"您还未认证，请先进行认证");
//                    }
                else {
                    ToastUtil.showToast(getContext(), "你没有该操作的权限");
                }
                break;
            case R.id.rl_invate:
                startActivity(new Intent(getContext(), H5CommomActivity.class).putExtra("url", Constant.Commom.inviteCode + MyApplication.token));
                break;
            case R.id.ll_jifen:
                startActivity(new Intent(getContext(), H5CommomActivity.class).putExtra("url", Constant.Commom.integration + MyApplication.token));
                break;
            case R.id.ll_money:
                startActivity(new Intent(getContext(), H5CommomActivity.class).putExtra("url", Constant.Commom.cash + MyApplication.token));
                break;
            case R.id.rl_shouhou:
                Intent intent1 = new Intent(getContext(), AfterSaleActivity.class);
                startActivity(intent1);
                break;
            case R.id.rl_tickets:
                startActivity(new Intent(getContext(), H5CommomActivity.class).putExtra("url", Constant.Commom.invoince + MyApplication.token));
                break;
            case R.id.rl_device:
                startActivity(new Intent(getContext(), H5CommomActivity.class).putExtra("url", Constant.Commom.device + MyApplication.token));
                break;
            case R.id.rl_address:
                startActivity(new Intent(getContext(), H5CommomActivity.class).putExtra("url", Constant.Commom.address + MyApplication.token));
                break;
            case R.id.ll_coupon:
                startActivity(new Intent(getContext(), H5CommomActivity.class).putExtra("url", Constant.Commom.coupon + MyApplication.token));
                break;
            case R.id.rl_setting:
                Intent intent = new Intent(getContext(), SettingsActivity.class);
                startActivityForResult(intent, 9);
                break;
            case R.id.ll_unpay:
                Intent intent2 = new Intent(getContext(), MyOrderActivity.class);
                intent2.putExtra("type", 1);
                startActivity(intent2);
                break;
            case R.id.ll_wallet:

                startActivity(new Intent(getContext(), MyWalletActivity.class));
                break;
            case R.id.ll_daichu:
                Intent intent3 = new Intent(getContext(), MyOrderActivity.class);
                intent3.putExtra("type", 2);
                startActivity(intent3);
                break;
            case R.id.ll_finish:
                Intent intent4 = new Intent(getContext(), MyOrderActivity.class);
                intent4.putExtra("type", 3);
                startActivity(intent4);
                break;
            case R.id.ll_myorder:
                startActivity(new Intent(getContext(), MyOrderActivity.class));
                break;
            case R.id.rl_wuliu:
                startActivity(new Intent(getContext(), H5CommomActivity.class).putExtra("url", Constant.Commom.callLogister + MyApplication.token));
                //startActivity(new Intent(getContext(), CallLogisticsActivity.class));
                break;
            case R.id.iv_edit:
                startActivityForResult(new Intent(getContext(), UpdateNicknameActivity.class), 1);
                break;
            case R.id.ll_top:
                startActivity(new Intent(getContext(), UserInfoActivity.class));


        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode ==9&&data!=null) {

            isChange = data.getBooleanExtra("isChange", false);
            LogUtils.e(isChange+"");
            if (isChange) {

                MainActivity activity= (MainActivity) getActivity();
                activity.refresh();
            }

        }
    }

}
