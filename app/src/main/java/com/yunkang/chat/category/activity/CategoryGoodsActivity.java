package com.yunkang.chat.category.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;


import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yunkang.chat.MyApplication;

import com.yunkang.chat.api.Api;
import com.yunkang.chat.category.adapter.CategoryTagAdapter;
import com.yunkang.chat.category.model.CategoryTag;
import com.yunkang.chat.constant.Constant;
import com.yunkang.chat.h5.H5CommomActivity;
import com.yunkang.chat.net.NetUtils;
import com.yunkang.chat.net.OnMessageReceived;
import com.yunkang.chat.start.adapter.MedicineAdapter;
import com.yunkang.chat.start.model.Medicine;
import com.yunkang.chat.tools.ToastUtil;
import com.yunkang.chat.R;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryGoodsActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.dl_category)
    DrawerLayout dl_category;
    @BindView(R.id.rl_choose)
    RelativeLayout rl_choose;
    @BindView(R.id.rl_new)
    RelativeLayout rl_new;
    @BindView(R.id.rl_price)
    RelativeLayout rl_price;
    @BindView(R.id.rl_sale)
    RelativeLayout rl_sale;
    @BindView(R.id.id_flowlayout)
    TagFlowLayout flowlayout;
    @BindView(R.id.rv_goods)
    RecyclerView rv_medicine;
    @BindView(R.id.tv_new)
    TextView tv_new;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_sale)
    TextView tv_sale;
    @BindView(R.id.tv_price)
    TextView tv_price;
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.tv_confirm)
    TextView tv_confirm;
    @BindView(R.id.tv_reset)
    TextView tv_reset;
    @BindView(R.id.iv_sortprice)
    ImageView iv_sortprice;
    @BindView(R.id.iv_sale)
    ImageView iv_sale;
    @BindView(R.id.iv_change)
    ImageView iv_change;
    @BindView(R.id.et_min)
    EditText et_min;
    @BindView(R.id.et_max)
    EditText et_max;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    List<CategoryTag> mCategoryTags;
    List<Medicine> mMedicines;
    MedicineAdapter mMedicineAdapter;
    int page = 1;
    boolean isLoadMore = false;
    String id;
    int orderBy = 1;
    String name;
    ArrayList<String> ids;
    int maxPrice = 0;
    int minPrice = 0;
    int type=1;
    private void getStatus() {


        NetUtils.getInstance().get(MyApplication.token, Api.Product.getStatus, new HashMap<>(), new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                Log.e("getStatus", response);
                try {
                    int type;
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject dataobject = jsonObject.getJSONObject("data");
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        type = dataobject.getInt("data");
                        MyApplication.isPt=type;
                        refresh();
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_goods);
        ButterKnife.bind(this);
        id = getIntent().getStringExtra("id");

        initView();
        getListData();
    }

    KProgressHUD dialog;
    private void showLoading(String data){
        dialog= KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
//                .setLabel(data)
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
    String url;
    boolean isAsk=false;
    boolean isShowPt=false;
    private void refresh(){
        page=1;
        mMedicines.clear();
        mMedicineAdapter.setNewData(mMedicines);
        getListData();
    }
    private void getListData() {

        isAsk=true;
        if(isAsk)
            showLoading("loading...");
        String token="";
        Map<String, Object> params = new HashMap<>();
        params.put("categoryId", Long.parseLong(id) + "");
        params.put("page", page + "");
        params.put("orderBy", orderBy + "");
        if (maxPrice > 0){
            params.put("maxPrice", maxPrice + "");
        }
        if (minPrice > 0){
            params.put("minPrice", minPrice + "");
        }
        if (ids.size() > 0) {
            params.put("labelIds",ids.toArray());
        }
        if (!TextUtils.isEmpty(name)){
            params.put("name", name);
        }
        String json = new Gson().toJson(params);
        Log.e("params", json);
        if(MyApplication.isPt==1){

            url=Api.Product.getHomeCategoryListWithPtOn;
            isShowPt=true;
            NetUtils.getInstance().post(url,json,MyApplication.token,new OnMessageReceived() {
                @Override
                public void onSuccess(String response) {
                    Log.e("PtOn", response);
                    isAsk=false;
                    if(dialog!=null)
                        if(dialog.isShowing()){
                            dialog.dismiss();
                        }
                    refresh.finishRefresh();

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject dataobject = jsonObject.getJSONObject("data");
                        dataobject = dataobject.getJSONObject("data");
                        int code = jsonObject.getInt("code");
                        if (code == 0) {
                            JSONArray data = dataobject.getJSONArray("beanList");
                            if(page==1&&data.length()==0){
                                mMedicineAdapter.setEmptyView(empty);
                            }
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject item = data.getJSONObject(i);
                                Medicine homeCategory = new Gson().fromJson(item.toString(), Medicine.class);
                                if(isShowPt){
                                    homeCategory.setShowPt(true);
                                }
                                homeCategory.setHide(true);
                                mMedicines.add(homeCategory);
                            }
                            if (isLoadMore) {
                                isLoadMore = false;
                                mMedicineAdapter.addData(mMedicines);
                                mMedicineAdapter.loadMoreComplete();
                            } else {
                                mMedicineAdapter.setNewData(mMedicines);
                                mMedicineAdapter.loadMoreEnd(true);
                            }

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(String error) {
                    isAsk=false;
                    if(dialog!=null)
                        if(dialog.isShowing()){
                            dialog.dismiss();
                        }
                    if(!NetworkUtils.isConnected()){
                        mMedicineAdapter.setEmptyView(noWeb);
                    }
                }
            });
        }else{
            url=Api.Product.getListForAppCategory;
            NetUtils.getInstance().post(url, json, new OnMessageReceived() {
                @Override
                public void onSuccess(String response) {
                    isAsk=false;
                    if(dialog!=null)
                        if(dialog.isShowing()){
                            dialog.dismiss();
                        }
                    refresh.finishRefresh();
                    Log.e("result", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject dataobject = jsonObject.getJSONObject("data");
                        dataobject = dataobject.getJSONObject("data");
                        int code = jsonObject.getInt("code");
                        if (code == 0) {
                            JSONArray data = dataobject.getJSONArray("beanList");
                            if(page==1&&data.length()==0){
                                mMedicineAdapter.setEmptyView(empty);
                            }
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject item = data.getJSONObject(i);
                                Medicine homeCategory = new Gson().fromJson(item.toString(), Medicine.class);
                                if(isShowPt){
                                    homeCategory.setShowPt(true);
                                }
                                homeCategory.setHide(true);
                                mMedicines.add(homeCategory);
                            }
                            if (isLoadMore) {
                                isLoadMore = false;
                                mMedicineAdapter.addData(mMedicines);
                                mMedicineAdapter.loadMoreComplete();
                            } else {
                                mMedicineAdapter.setNewData(mMedicines);
                                mMedicineAdapter.loadMoreEnd(true);
                            }

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(String error) {
                    isAsk=false;
                    if(dialog!=null)
                        if(dialog.isShowing()){
                            dialog.dismiss();
                        }
                    if(!NetworkUtils.isConnected()){
                        mMedicineAdapter.setEmptyView(noWeb);
                    }
                }
            });
        }




    }

    CategoryTagAdapter categoryTagAdapter;
    private void getTagList() {



        NetUtils.getInstance().get(MyApplication.token, Api.Product.getListForApp, new HashMap<>(), new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                Log.e("gettab", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject dataobject = jsonObject.getJSONObject("data");
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        JSONArray data = dataobject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            CategoryTag homeCategory = new Gson().fromJson(item.toString(), CategoryTag.class);
                            mCategoryTags.add(homeCategory);
                        }
                        categoryTagAdapter=new CategoryTagAdapter(mCategoryTags);
                        flowlayout.setAdapter(categoryTagAdapter);
                        flowlayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
                            @Override
                            public void onSelected(Set<Integer> selectPosSet) {

                                ids.clear();
                                for (Integer i : selectPosSet) {
                                    ids.add(mCategoryTags.get(i).getId());
                                }
                                Log.e("size", "" + ids.toString());
                            }
                        });
                        flowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                            @Override
                            public boolean onTagClick(View view, int position, FlowLayout parent) {
                                return false;
                            }
                        });
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
    View empty;
    private void initView() {
        findViewById(R.id.rl_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type==1){
                    type=2;
                    iv_change.setImageResource(R.mipmap.ic_change_one);
                    mMedicineAdapter = new MedicineAdapter(R.layout.item_med_two, mMedicines);
                    rv_medicine.setLayoutManager(new GridLayoutManager(CategoryGoodsActivity.this,2, LinearLayoutManager.VERTICAL,false));
                }else{
                    type=1;
                    mMedicineAdapter = new MedicineAdapter(R.layout.item_home_medicine, mMedicines);
                    iv_change.setImageResource(R.mipmap.ic_change_two);
                    rv_medicine.setLayoutManager(new LinearLayoutManager(CategoryGoodsActivity.this));
                }
                mMedicineAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        Intent intent = new Intent(CategoryGoodsActivity.this, H5CommomActivity.class);
                        intent.putExtra("url", Constant.Commom.goodsDetail+MyApplication.token+"&id="+mMedicines.get(position).getId());
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
                }, rv_medicine);
                rv_medicine.setAdapter(mMedicineAdapter);
                refresh();
            }
        });
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getStatus();
            }
        });

        et_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {

                    String keytag = et_name.getEditableText().toString().trim();

                    if (TextUtils.isEmpty(keytag)) {
                        Toast.makeText(CategoryGoodsActivity.this, "请输入搜索关键字", Toast.LENGTH_SHORT).show();

                        return false;
                    }
                    name =keytag;
                    page=1;
                    mMedicines.clear();
                    mMedicineAdapter.setNewData(mMedicines);
                    //mMedicineAdapter.notifyDataSetChanged();
                    getListData();
                    // 搜索功能主体

                    return true;
                }
                return false;
            }
        });

        tv_confirm.setOnClickListener(this);
        rl_choose.setOnClickListener(this);
        rl_new.setOnClickListener(this);
        rl_price.setOnClickListener(this);
        rl_sale.setOnClickListener(this);
        tv_reset.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        mCategoryTags = new ArrayList<>();
        mMedicines = new ArrayList<>();
        empty= LayoutInflater.from(this).inflate(R.layout.home_web_error,null);
        noWeb= LayoutInflater.from(this).inflate(R.layout.no_web_error,null);
        mMedicineAdapter = new MedicineAdapter(R.layout.item_home_medicine, mMedicines);
        mMedicineAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(CategoryGoodsActivity.this, H5CommomActivity.class);
                intent.putExtra("url", Constant.Commom.goodsDetail+MyApplication.token+"&id="+mMedicines.get(position).getId());
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
        }, rv_medicine);
        rv_medicine.setLayoutManager(new LinearLayoutManager(this));
        rv_medicine.setAdapter(mMedicineAdapter);

        ids = new ArrayList<>();
        getTagList();
    }

    int priceType=1;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_choose:

                if (dl_category.isDrawerOpen(Gravity.RIGHT))
                    dl_category.closeDrawer(Gravity.RIGHT);
                else {
                    dl_category.openDrawer(Gravity.RIGHT);
                }
                if(maxPrice>0){
                    et_max.setText((int)maxPrice+"");
                }
                if(minPrice>0){
                    et_min.setText((int) minPrice+"");
                }
                break;
            case R.id.rl_new:
                priceType=1;
                iv_sale.setImageResource(R.mipmap.ic_unsort);
                iv_sortprice.setImageResource(R.mipmap.ic_unsort);
                tv_new.setTextColor(getResources().getColor(R.color.color_blue));
                tv_price.setTextColor(getResources().getColor(R.color.color_666));
                tv_sale.setTextColor(getResources().getColor(R.color.color_666));
                orderBy = 1;
                mMedicines.clear();
                mMedicineAdapter.setNewData(mMedicines);
                getListData();
                break;
            case R.id.rl_sale:
                priceType=1;

                tv_sale.setTextColor(getResources().getColor(R.color.color_blue));
                tv_new.setTextColor(getResources().getColor(R.color.color_666));
                tv_price.setTextColor(getResources().getColor(R.color.color_666));
                orderBy = 2;
                iv_sale.setImageResource(R.mipmap.ic_sel_up);
                iv_sortprice.setImageResource(R.mipmap.ic_unsort);
                mMedicines.clear();
                mMedicineAdapter.setNewData(mMedicines);
                getListData();
                break;
            case R.id.rl_price:

                iv_sale.setImageResource(R.mipmap.ic_unsort);
                if(priceType==1){
                    priceType=2;
                    orderBy = 4;
                    iv_sortprice.setImageResource(R.mipmap.ic_sel_up);
                }else{
                    priceType=1;
                    orderBy=3;
                    iv_sortprice.setImageResource(R.mipmap.ic_sel_down);
                }

                tv_price.setTextColor(getResources().getColor(R.color.color_blue));
                tv_new.setTextColor(getResources().getColor(R.color.color_666));
                tv_sale.setTextColor(getResources().getColor(R.color.color_666));

                mMedicines.clear();
                mMedicineAdapter.setNewData(mMedicines);
                getListData();
                break;
            case R.id.tv_confirm:
                String min=et_min.getEditableText().toString();
                String max=et_max.getEditableText().toString();
                if(!TextUtils.isEmpty(min))
                    minPrice = Integer.parseInt(min);
                if(!TextUtils.isEmpty(max))
                    maxPrice = Integer.parseInt(max);
                if(!TextUtils.isEmpty(max)&&maxPrice<=0){
                    ToastUtil.showToast(this,"最高价格必须大于0");
                    return;
                }
                if(minPrice>maxPrice&&!TextUtils.isEmpty(max)){
                    ToastUtil.showToast(this,"最低价格不能高于最高价格！");
                    return;
                }
                dl_category.closeDrawer(Gravity.RIGHT);
                mMedicines.clear();
                mMedicines.clear();
                mMedicineAdapter.setNewData(mMedicines);
                getListData();
                break;
            case R.id.tv_reset:
                ids.clear();
                mCategoryTags.clear();
                maxPrice=0;
                minPrice=0;
                categoryTagAdapter.notifyDataChanged();
                getTagList();
                et_max.setText("");
                et_min.setText("");
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
