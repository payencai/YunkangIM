package com.yunkang.chat.home.fragment;


import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;

import com.lzy.okgo.model.HttpParams;
import com.umeng.commonsdk.debug.W;
import com.yunkang.chat.MyApplication;
import com.yunkang.chat.R;
import com.yunkang.chat.api.Api;
import com.yunkang.chat.constant.Constant;
import com.yunkang.chat.h5.H5CommomActivity;
import com.yunkang.chat.home.OnHomeCheckItemEvent;
import com.yunkang.chat.home.adapter.LabsAdapter;
import com.yunkang.chat.home.model.Labs;
import com.yunkang.chat.http.HttpProxy;
import com.yunkang.chat.http.ICallBack;
import com.yunkang.chat.net.NetUtils;
import com.yunkang.chat.net.OnMessageReceived;
import com.yunkang.chat.start.adapter.MedicineAdapter;
import com.yunkang.chat.start.model.Medicine;
import com.yunkang.chat.tools.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckItemFragment extends Fragment {

    List<Medicine> mMedicines;
    LabsAdapter mLabsAdapter;
    List<Labs> mLabs;

    MedicineAdapter mMedicineAdapter;


    @BindView(R.id.rv_checkitem)
    RecyclerView rv_checkitem;

    int page = 1;
    boolean isLoadMore = false;
    View empty;
    String url;
    String id;
    int flag = -1;
    int type = 1;

    public CheckItemFragment() {
        // Required empty public constructor
    }

    public static CheckItemFragment newInstance(int type, String id, int flag) {
        CheckItemFragment checkItemFragment = new CheckItemFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putInt("flag", flag);
        bundle.putInt("type", type);
        checkItemFragment.setArguments(bundle);
        return checkItemFragment;
    }

//    KProgressHUD dialog;
//
//    private void showLoading(String data) {
//        dialog = KProgressHUD.create(getContext())
//                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
////                .setLabel(data)
//                .setCancellable(new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel(DialogInterface
//                                                 dialogInterface) {
//
//                    }
//                });
//        dialog.show();
//        getContext();
//    }

    public void refresh() {
        page = 1;
        mMedicines = new ArrayList<>();
        if (mMedicineAdapter != null)
            mMedicineAdapter.setNewData(mMedicines);
        else
            mMedicineAdapter = new MedicineAdapter(R.layout.item_home_medicine, mMedicines);
        getListData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_check_item, container, false);
        ButterKnife.bind(this, view);
        id = getArguments().getString("id");
        flag = getArguments().getInt("flag");
        type = getArguments().getInt("type");
        initView();
        return view;
        //   return inflater.inflate(R.layout.fragment_check_item, container, false);
    }

    private void initView() {
        page = 1;
        mMedicines = new ArrayList<>();
        if (type == 1) {
            mMedicineAdapter = new MedicineAdapter(R.layout.item_home_medicine, mMedicines);
            rv_checkitem.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            mMedicineAdapter = new MedicineAdapter(R.layout.item_med_two, mMedicines);
            rv_checkitem.setLayoutManager(new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false));
        }
        empty = LayoutInflater.from(getContext()).inflate(R.layout.home_web_error, null);

        mMedicineAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Medicine medicine = (Medicine) adapter.getItem(position);
                Intent intent = new Intent(getContext(), H5CommomActivity.class);
                intent.putExtra("url", Constant.Commom.goodsDetail + MyApplication.token + "&id=" + medicine.getId());
                startActivity(intent);
            }
        });
        mMedicineAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                isLoadMore = true;
                getListData();
            }
        }, rv_checkitem);
        mMedicineAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                if (view.getId() == R.id.iv_add) {
                    Medicine medicine = (Medicine) adapter.getItem(position);
                    if (medicine != null)
                        showBottomDialog(medicine.getId());
                }
            }
        });
        rv_checkitem.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //Log.e("newState",startX+"");
//                startX = iv_cart.getTranslationX();
//                if (dy % 5 == 0) {
//                    if (startX < 0) {
//                        hideShopCar(iv_cart);
//                    }
//                }
            }
        });


        rv_checkitem.setAdapter(mMedicineAdapter);
        getListData();
    }

    int num = 1;

    private void showBottomDialog(String id) {
        mLabs = new ArrayList<>();
        int pos = 0;

        mLabsAdapter = new LabsAdapter(getContext(), mLabs);
        Dialog dialog = new Dialog(getContext(), R.style.common_dialog_style);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_shopcar,
                null);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        TextView tv_confirm = view.findViewById(R.id.tv_confirm);
        TextView tv_num = view.findViewById(R.id.tv_num);
        num = Integer.parseInt(tv_num.getText().toString());
        ImageView iv_delete = view.findViewById(R.id.iv_delete);
        ImageView iv_add = view.findViewById(R.id.iv_add);
        ImageView iv_close = view.findViewById(R.id.iv_close);
        ListView lv_labs = view.findViewById(R.id.lv_labs);
        lv_labs.setAdapter(mLabsAdapter);
        lv_labs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mLabsAdapter.setPos(position);
                mLabsAdapter.notifyDataSetChanged();
            }
        });
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num > 1) {
                    num--;
                    tv_num.setText(num + "");
                }
            }
        });
        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num++;
                tv_num.setText(num + "");
            }
        });
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = Integer.parseInt(tv_num.getText().toString());
                if (dialog != null)
                    dialog.dismiss();
                if (!mLabs.isEmpty()) {
                    String labsId = mLabs.get(mLabsAdapter.getPos()).getId();
                    addToShopCar(id, labsId, num);
                }
            }
        });

        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        window.setAttributes(layoutParams);
        getLabs(id);
    }

    private void addToShopCar(String id, String labsid, int count) {

        Map<String,Object> params = new HashMap<>();
        params.put("pInfoId", id);
        params.put("pLabId", labsid);
        params.put("num", count);
        String json=new Gson().toJson(params);
        NetUtils.getInstance().post( Api.Product.addToShopCar, json,MyApplication.token, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                Log.e("result", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        ToastUtil.showToast(getContext(), "添加成功");
                    } else {
                        String msg = jsonObject.getString("msg");
                        ToastUtil.showToast(getContext(), msg);
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

    private void getLabs(String id) {

        Map<String, String> params = new HashMap<>();
        params.put("productId", id);
        NetUtils.getInstance().get(Api.Product.getLabsByProductId, params, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                Log.e("result", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject dataobject = jsonObject.getJSONObject("data");
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        int pos = 0;
                        JSONArray data = dataobject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            Labs homeCategory = new Gson().fromJson(item.toString(), Labs.class);
                            mLabs.add(homeCategory);
                            if (homeCategory.getIsDefaoult() == 1) {
                                pos = i;
                            }
                        }
                        mLabsAdapter.setPos(pos);
                        mLabsAdapter.notifyDataSetChanged();
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

    boolean isShowPt = false;

    public void getListData() {
//        if (flag == 0 && page == 1) {
//            showLoading("loading");
//        }
        String token = "";
        if (MyApplication.isPt == 1) {
            if (MyApplication.getUserInfo().getType().equals("2")) {
                url = Api.Product.getHomeGoodsListWithPt;
                isShowPt = true;
                token = MyApplication.token;
            } else {
                url = Api.Product.getHomeGoodsList;
            }
        } else {
            url = Api.Product.getHomeGoodsList;
        }


        Map<String, String> params = new HashMap<>();
        params.put("categoryId", id);
        params.put("page", page + "");
        Log.d("tag", "开始网络请求");
        long startTime = System.currentTimeMillis();
        if (token.isEmpty()) {
            NetUtils.getInstance().get(url, params, new OnMessageReceived() {
                @Override
                public void onSuccess(String response) {
//                    if (dialog != null)
//                        if (dialog.isShowing())
//                            dialog.dismiss();

                    long endTime = System.currentTimeMillis();
                    long time = (endTime - startTime);
                    //ToastUtil.showToast(getContext(),"请求时间"+time);
                    Log.d("tag", "结束请求");
                    Log.d("tag", "时间" + time);
                    //Log.e("data", id+"-"+page);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject dataobject = jsonObject.getJSONObject("data");
                        dataobject = dataobject.getJSONObject("data");
                        int code = jsonObject.getInt("code");
                        if (code == 0) {
                            JSONArray data = dataobject.getJSONArray("beanList");
                            List<Medicine> medicines = new ArrayList<>();
                            if (page == 1 && data.length() == 0) {
                                mMedicineAdapter.setEmptyView(empty);
                            }
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject item = data.getJSONObject(i);
                                Medicine homeCategory = new Gson().fromJson(item.toString(), Medicine.class);
                                if (isShowPt) {
                                    homeCategory.setShowPt(true);
                                }
                                medicines.add(homeCategory);
                                mMedicines.add(homeCategory);
                            }
                            if (isLoadMore) {
                                isLoadMore = false;
                                mMedicineAdapter.addData(medicines);
                                if (data.length() > 0)
                                    mMedicineAdapter.loadMoreComplete();
                                else {
                                    mMedicineAdapter.loadMoreEnd(true);
                                }
                            } else {
                                mMedicineAdapter.setNewData(medicines);

                            }
                        }
                        EventBus.getDefault().post(new OnHomeCheckItemEvent());


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(String error) {
//                    if (dialog != null)
//                        if (dialog.isShowing())
//                            dialog.dismiss();
                }
            });
        } else {
            NetUtils.getInstance().get(token, url, params, new OnMessageReceived() {
                @Override
                public void onSuccess(String response) {
//                    if (dialog != null)
//                        if (dialog.isShowing())
//                            dialog.dismiss();

                    long endTime = System.currentTimeMillis();
                    long time = (endTime - startTime);
                    //ToastUtil.showToast(getContext(),"请求时间"+time);
                    Log.d("tag", "结束请求");
                    Log.d("tag", "时间" + time);
                    //Log.e("data", id+"-"+page);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject dataobject = jsonObject.getJSONObject("data");
                        dataobject = dataobject.getJSONObject("data");
                        int code = jsonObject.getInt("code");
                        if (code == 0) {
                            JSONArray data = dataobject.getJSONArray("beanList");
                            List<Medicine> medicines = new ArrayList<>();
                            if (page == 1 && data.length() == 0) {
                                mMedicineAdapter.setEmptyView(empty);
                            }
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject item = data.getJSONObject(i);
                                Medicine homeCategory = new Gson().fromJson(item.toString(), Medicine.class);
                                if (isShowPt) {
                                    homeCategory.setShowPt(true);
                                }
                                medicines.add(homeCategory);
                                mMedicines.add(homeCategory);
                            }
                            if (isLoadMore) {
                                isLoadMore = false;
                                mMedicineAdapter.addData(medicines);
                                if (data.length() > 0)
                                    mMedicineAdapter.loadMoreComplete();
                                else {
                                    mMedicineAdapter.loadMoreEnd(true);
                                }
                            } else {
                                mMedicineAdapter.setNewData(medicines);

                            }
                        }
                        EventBus.getDefault().post(new OnHomeCheckItemEvent());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(String error) {
//                    if (dialog != null)
//                        if (dialog.isShowing())
//                            dialog.dismiss();
                }
            });

        }

    }

}
