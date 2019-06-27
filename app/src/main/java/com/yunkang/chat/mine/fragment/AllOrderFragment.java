package com.yunkang.chat.mine.fragment;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.iapppay.interfaces.callback.IPayResultCallback;
import com.iapppay.sdk.main.IAppPay;
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
import com.yunkang.chat.home.activity.WaittodoActivity;
import com.yunkang.chat.http.HttpProxy;
import com.yunkang.chat.http.ICallBack;

import com.yunkang.chat.mine.activity.MyOrderActivity;
import com.yunkang.chat.mine.activity.SettingsActivity;
import com.yunkang.chat.mine.adapter.OrderAdapter;
import com.yunkang.chat.mine.model.Order;
import com.yunkang.chat.net.NetUtils;
import com.yunkang.chat.net.OnMessageReceived;
import com.yunkang.chat.start.activity.LoginActivity;
import com.yunkang.chat.start.model.Medicine;
import com.yunkang.chat.tools.AppManager;
import com.yunkang.chat.tools.CheckDoubleClick;
import com.yunkang.chat.tools.ToastUtil;
import com.yunkang.chat.tools.base.NetWorkUtil;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import org.greenrobot.eventbus.EventBus;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class AllOrderFragment extends BaseLazyFragment {
    OrderAdapter orderAdapter;

    public SmartRefreshLayout getRefreshLayout() {
        return refreshLayout;
    }

    public void setRefreshLayout(SmartRefreshLayout refreshLayout) {
        this.refreshLayout = refreshLayout;
    }

    public OrderAdapter getOrderAdapter() {
        return orderAdapter;
    }

    public void setOrderAdapter(OrderAdapter orderAdapter) {
        this.orderAdapter = orderAdapter;
    }

    List<Order> mOrder;
    List<Order.OrderProductsBean> childList;
    @BindView(R.id.rv_order)
    RecyclerView rv_order;
    @BindView(R.id.refresh)
    SmartRefreshLayout refreshLayout;
    //    @BindView(R.id.rl_error)
//    RelativeLayout rl_error;
    int page = 1;
    String paySate = "";
    boolean isLoadMore = false;

    public static AllOrderFragment newInstance(String paySate) {
        AllOrderFragment allOrderFragment = new AllOrderFragment();
        Bundle bundle = new Bundle();
        bundle.putString("paySate", paySate);
        allOrderFragment.setArguments(bundle);
        return allOrderFragment;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_order, container, false);

        ButterKnife.bind(this, view);
        mIsprepared = true;
        lazyLoad();

        return view;
    }


    View view;

    KProgressHUD dialog;

    private void showLoading(String data) {
        dialog = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)

                .setCancellable(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface
                                                 dialogInterface) {

                    }
                });
        dialog.show();

    }

    private void initView() {


        IAppPay.init(getActivity(), IAppPay.PORTRAIT, "3016095896", "");
        paySate = getArguments().getString("paySate");
        mOrder = new ArrayList<>();
        childList = new ArrayList<>();
        orderAdapter = new OrderAdapter(R.layout.item_mine_order, mOrder);
        view = LayoutInflater.from(getContext()).inflate(R.layout.home_order_error, null);
        rv_order.setLayoutManager(new LinearLayoutManager(getContext()));
        orderAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                isLoadMore = true;

                getListData();
            }
        }, rv_order);
        orderAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (CheckDoubleClick.isFastDoubleClick()) {
                    return;
                }
                Order order = (Order) adapter.getItem(position);
                if (order != null) {
                    Intent intent = new Intent(getContext(), H5CommomActivity.class);
                    intent.putExtra("url", Constant.Commom.orderDetail + MyApplication.token
                            + "&id=" + order.getId());
                    startActivityForResult(intent, 2);
                }
            }
        });
        orderAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (CheckDoubleClick.isFastDoubleClick()) {
                    return;
                }
                Order order = (Order) adapter.getItem(position);
                if (order != null) {
                    if (view.getId() == R.id.tv_pay) {
                        Intent intent = new Intent(getContext(), H5CommomActivity.class);
                        switch (order.getPayState()) {
                            case "1":
                                getPayParams(order.getId());
                                break;
                            case "7":
                                String url = Constant.Commom.orderDiscuss + MyApplication.token + "&id=" + order.getId();
                                intent.putExtra("url", url);
                                startActivityForResult(intent, 3);
                                break;
                            case "6":
                                finishOrder(order.getId());
                                break;
                            case "10":
                                finishOrder(order.getId());
                                break;

                        }

                    } else if (view.getId() == R.id.tv_delete) {
                        showDeleteDialog(order.getId());
                    }
                }

            }
        });
        rv_order.setAdapter(orderAdapter);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                Log.e("refresh", "刷新");
                refresh();
            }
        });


        getListData();
    }


    private void getPayParams(String orderId) {

        Map<String, String> params = new HashMap<>();
        params.put("orderId", orderId);
        NetUtils.getInstance().get(MyApplication.token, Api.Order.payment, params, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                Log.e("orderId", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.getJSONObject("data");
                    String params = data.getString("data");
                    pay(params);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {

            }
        });

    }

    private void pay(String id) {
        String params = "transid=" + id + "&appid=3016095896";

        IAppPay.startPay(getActivity(), params, new IPayResultCallback() {
            @Override
            public void onPayResult(int resultCode, String signValue, String resultInfo) {
                switch (resultCode) {
                    case IAppPay.PAY_SUCCESS:
                        //请尽量采用服务端验签方式  ，以下验签方法不建议
                        Toast.makeText(getContext(), "支付成功", Toast.LENGTH_LONG).show();
                        refresh();
                        break;
                    default:
                        Toast.makeText(getContext(), resultInfo, Toast.LENGTH_LONG).show();
                        break;
                }
                //Log.d(TAG, "requestCode:" + resultCode + ",signvalue:" + signValue + ",resultInfo:" + resultInfo);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refresh();
    }


    public void refresh() {
        page = 1;
        mOrder.clear();
        orderAdapter.setNewData(null);
        orderAdapter.setEmptyView(R.layout.empty_loading, (ViewGroup) rv_order.getParent());
        getListData();
    }

    private void showDeleteDialog(String id) {

        Dialog dialog = new Dialog(getContext(), R.style.common_dialog_style);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_logout, null);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        TextView tv_confirm = view.findViewById(R.id.tv_confirm);
        TextView tv_cancel = view.findViewById(R.id.tv_cancel);
        TextView tv_title = view.findViewById(R.id.tv_title);
        tv_title.setText("确认删除该订单？");
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                deleteOrder(id);
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        Window window = dialog.getWindow();
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.width = (int) (display.getWidth() * 0.7);
        window.setAttributes(layoutParams);

    }

    private void deleteOrder(String id) {

        Map<String, String> params = new HashMap<>();
        params.put("orderId", id);
        NetUtils.getInstance().post(Api.Order.delOrderById, params, MyApplication.token, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                Log.e("result", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (response.contains("data")) {
                        int code = jsonObject.getInt("code");
                        if (code == 0) {
                            ToastUtil.showToast(getContext(), "操作成功");
                            refresh();
//                            MyOrderActivity myOrderActivity= (MyOrderActivity) getActivity();
//                            myOrderActivity.refresh();
                        }
                    } else {
                        String msg = jsonObject.getString("message");
                        ToastUtil.showToast(getContext(), msg);
                        // refresh();
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

    private void finishOrder(String id) {

        Map<String, String> params = new HashMap<>();
        params.put("orderId", id);
        NetUtils.getInstance().post(Api.Order.updateOrderToComplete, params, MyApplication.token, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                Log.e("result", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (response.contains("data")) {
                        int code = jsonObject.getInt("code");
                        if (code == 0) {
                            ToastUtil.showToast(getContext(), "操作成功");
                            refresh();
                        }
                    } else {
                        String msg = jsonObject.getString("message");
                        ToastUtil.showToast(getContext(), msg);
                        // refresh();
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

    private boolean isAdd(Order order) {
        for (int i = 0; i < order.getOrderProducts().size(); i++) {
            Order.OrderProductsBean orderProductsBean = order.getOrderProducts().get(i);
            if (!"0".equals(orderProductsBean.getRefundType())) {
                return false;
            }
        }
        return true;
    }

    boolean isAsk = false;

    public boolean isAsk() {
        return isAsk;
    }

    public void setAsk(boolean ask) {
        isAsk = ask;
    }

    private void getListData() {
        if (page == 1)
            showLoading("加载中");
        isAsk = true;


        Map<String, String> params = new HashMap<>();
        params.put("page", page + "");
        if (!"0".equals(paySate)) {
            params.put("payState", paySate);
        }
        Log.e("params", params.toString() + "" + paySate);
        NetUtils.getInstance().get(MyApplication.token, Api.Order.getOrderByUser, params, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                if(dialog!=null)
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                refreshLayout.finishRefresh();
                Log.e("getOrderByUser", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject dataobject = jsonObject.getJSONObject("data");
                    dataobject = dataobject.getJSONObject("data");
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        JSONArray data = dataobject.getJSONArray("beanList");
                        if(page==1&&data.length()==0){
                            orderAdapter.setEmptyView(view);
                        }else{
                            List<Order> mOrders = new ArrayList<>();
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject item = data.getJSONObject(i);
                                Order order = new Gson().fromJson(item.toString(), Order.class);
                                if (isAdd(order)) {
                                    mOrders.add(order);
                                    mOrder.add(order);
                                }
                            }
                            if (isLoadMore) {
                                isLoadMore = false;
                                orderAdapter.addData(mOrders);
                                if (data.length() > 0)
                                    orderAdapter.loadMoreComplete();
                                else {
                                    orderAdapter.loadMoreEnd(true);
                                }
                            } else {
                                orderAdapter.setNewData(mOrder);

                            }
                        }


                    }
                    isAsk = false;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                if(dialog!=null)
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                isAsk = false;

            }
        });
    }

    @Override
    protected void lazyLoad() {
        if (!mIsprepared || !mIsVisible || mHasLoadedOnce) {
            return;
        }
        mHasLoadedOnce = true;
        initView();
    }
}
