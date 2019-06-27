package com.yunkang.chat.home.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yunkang.chat.MyApplication;
import com.yunkang.chat.R;
import com.yunkang.chat.api.Api;
import com.yunkang.chat.category.activity.CategoryGoodsActivity;
import com.yunkang.chat.constant.Constant;
import com.yunkang.chat.h5.H5CommomActivity;
import com.yunkang.chat.home.adapter.CollectAdapter;
import com.yunkang.chat.home.adapter.HotTagAdapter;
import com.yunkang.chat.home.adapter.SearchAdapter;
import com.yunkang.chat.home.adapter.SearchTypeAdapter;
import com.yunkang.chat.home.model.Collect;
import com.yunkang.chat.home.model.HomeCategory;
import com.yunkang.chat.home.model.Report;
import com.yunkang.chat.home.model.SearchResult;
import com.yunkang.chat.http.HttpProxy;
import com.yunkang.chat.http.ICallBack;
import com.yunkang.chat.mine.model.Order;
import com.yunkang.chat.net.NetUtils;
import com.yunkang.chat.net.OnMessageReceived;
import com.yunkang.chat.start.activity.GuideActivity;
import com.yunkang.chat.start.adapter.MedicineAdapter;
import com.yunkang.chat.start.model.Medicine;
import com.yunkang.chat.tools.SharePreferenceUtils;
import com.yunkang.chat.tools.ToastUtil;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagFlowLayout;
import com.zyyoona7.popup.EasyPopup;
import com.zyyoona7.popup.XGravity;
import com.zyyoona7.popup.YGravity;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.tv_search)
    TextView tv_search;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.ll_data)
    LinearLayout ll_data;
    @BindView(R.id.tv_type)
    TextView tv_type;
    @BindView(R.id.ll_down)
    LinearLayout ll_down;
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.iv_delRent)
    ImageView iv_delRent;
    @BindView(R.id.iv_delHot)
    ImageView iv_delHot;
    @BindView(R.id.fl_hot)
    TagFlowLayout fl_hot;
    @BindView(R.id.rl_top)
    RelativeLayout rl_top;
    @BindView(R.id.fl_recent)
    TagFlowLayout fl_recen;
    @BindView(R.id.ll_history)
    LinearLayout ll_history;
    @BindView(R.id.rv_search)
    RecyclerView rv_search;
    @BindView(R.id.rl_type)
    RelativeLayout ll_type;
    @BindView(R.id.ll_empty)
    LinearLayout ll_empty;
    MedicineAdapter mCollectAdapter;
    List<Medicine> mCollects;
    int page = 1;
    boolean isLoadMore = false;
    HotTagAdapter mHotTagAdapter;
    HotTagAdapter mHistoryTagAdapter;
    List<String> hotString;
    List<String> mHistoryKeywords;

    String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initView();
    }
    private void refresh(){
        page=1;
        mCollects.clear();
        mCollectAdapter.setNewData(mCollects);
        getListData(page,et_name.getEditableText().toString());
    }
    private void getStatus() {
//        HttpProxy.obtain().get(Api.Product.getStatus, MyApplication.token, new ICallBack() {
//            @Override
//            public void OnSuccess(String result) {
//                Log.e("getStatus", result);
//                try {
//                    int type;
//                    JSONObject jsonObject = new JSONObject(result);
//                    JSONObject dataobject = jsonObject.getJSONObject("data");
//                    int code = jsonObject.getInt("code");
//                    if (code == 0) {
//                        type = dataobject.getInt("data");
//                        MyApplication.isPt=type;
//                        refresh();
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
//
//            }
//        });

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
    private void getCategory() {
//        if(dialog==null)
//        showLoading("loading...");
//        Map<String, Object> params = new HashMap<>();
//        params.put("rank", 1);
//        HttpProxy.obtain().get(Api.Product.getHomeCategoryList, params, new ICallBack() {
//            @Override
//            public void OnSuccess(String result) {
//                if(dialog.isShowing()){
//                    dialog.dismiss();
//                }
//                Log.e("result", result);
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    JSONObject dataobject = jsonObject.getJSONObject("data");
//                    int code = jsonObject.getInt("code");
//                    if (code == 0) {
//                        JSONArray data = dataobject.getJSONArray("data");
//                        for (int i = 0; i < data.length(); i++) {
//                            JSONObject item = data.getJSONObject(i);
//                            HomeCategory homeCategory = new Gson().fromJson(item.toString(), HomeCategory.class);
//                            mHomeCategories.add(homeCategory);
//                        }
//                        if (data.length() > 0)
//                            categoryId = mHomeCategories.get(0).getId();
//                        tv_type.setText(mHomeCategories.get(0).getName());
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
//                if(dialog.isShowing()){
//                    dialog.dismiss();
//                }
//            }
//        });

        if(dialog==null)
            showLoading("loading...");
        Map<String, String> params = new HashMap<>();
        params.put("rank", "1");
        NetUtils.getInstance().get(MyApplication.token, Api.Product.getHomeCategoryList, params, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                if(dialog.isShowing()){
                    dialog.dismiss();
                }
                Log.e("result", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject dataobject = jsonObject.getJSONObject("data");
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        JSONArray data = dataobject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            HomeCategory homeCategory = new Gson().fromJson(item.toString(), HomeCategory.class);
                            mHomeCategories.add(homeCategory);
                        }
                        if (data.length() > 0)
                            categoryId = mHomeCategories.get(0).getId();
                        tv_type.setText(mHomeCategories.get(0).getName());
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                if(dialog.isShowing()){
                    dialog.dismiss();
                }
            }
        });

    }

    private void getHotWords() {
//        Map<String, Object> params = new HashMap<>();
//        params.put("type", 1);
//        HttpProxy.obtain().get(Api.Product.getSearchHotWords, params, "", new ICallBack() {
//            @Override
//            public void OnSuccess(String result) {
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    JSONObject dataobject = jsonObject.getJSONObject("data");
//                    int code = jsonObject.getInt("code");
//                    if (code == 0) {
//                        JSONArray data = dataobject.getJSONArray("data");
//                        for (int i = 0; i < data.length(); i++) {
//                            String word = data.getString(i);
//                            hotString.add(word);
//                        }
//                        mHotTagAdapter.notifyDataChanged();
//                    } else {
//
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

        Map<String, String> params = new HashMap<>();
        params.put("type", "1");
        NetUtils.getInstance().get(MyApplication.token, Api.Product.getSearchHotWords, params, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject dataobject = jsonObject.getJSONObject("data");
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        JSONArray data = dataobject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            String word = data.getString(i);
                            hotString.add(word);
                        }
                        mHotTagAdapter.notifyDataChanged();
                    } else {

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
        initSearchHistory();
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getStatus();
            }
        });
        hotString = new ArrayList<>();
        mHotTagAdapter = new HotTagAdapter(hotString);
        mHomeCategories = new ArrayList<>();
        fl_hot.setAdapter(mHotTagAdapter);
        fl_hot.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {

                String name = hotString.get(position);
                page = 1;
                mCollects.clear();
                ll_history.setVisibility(View.GONE);
                ll_data.setVisibility(View.VISIBLE);
                et_name.setText(hotString.get(position));
                et_name.setSelection(hotString.get(position).length());
                getListData(page, hotString.get(position));
                String oldhistory = SharePreferenceUtils.getString(SearchActivity.this, "history");
                if (!TextUtils.isEmpty(name) && (!oldhistory.contains(name))) {
                    if (mHistoryKeywords.size() > 5) {

                    } else {
                        mHistoryKeywords.add(0, name);
                        mHistoryTagAdapter.notifyDataChanged();
                        if (TextUtils.isEmpty(oldhistory))
                            SharePreferenceUtils.putString(SearchActivity.this, "history", name);
                        else {
                            String newHistory = name + "," + oldhistory;
                            SharePreferenceUtils.putString(SearchActivity.this, "history", newHistory);
                        }
                    }

                }
                return false;
            }
        });

        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String name = s.toString();
                if (TextUtils.isEmpty(name)) {
                    ll_history.setVisibility(View.VISIBLE);
                    ll_data.setVisibility(View.GONE);
                    ll_empty.setVisibility(View.GONE);
                }
            }
        });
        tv_search.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        iv_delHot.setOnClickListener(this);
        iv_delRent.setOnClickListener(this);
        ll_down.setOnClickListener(this);
        mCollects = new ArrayList<>();
        mCollectAdapter = new MedicineAdapter(R.layout.item_home_medicine, mCollects);
        mCollectAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mCollectAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                isLoadMore = true;
                String name = et_name.getEditableText().toString();
                Log.e("load", "load");
                getListData(page, name);
            }
        }, rv_search);
        mCollectAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Medicine medicine = (Medicine) adapter.getItem(position);
                Intent intent = new Intent(SearchActivity.this, H5CommomActivity.class);
                intent.putExtra("url", Constant.Commom.goodsDetail + MyApplication.token + "&id=" + medicine.getId());
                startActivity(intent);
            }
        });
        mCollectAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_luru) {
                    Order order = (Order) adapter.getItem(position);
                    Intent intent = new Intent(SearchActivity.this, H5CommomActivity.class);
                    intent.putExtra("url", Constant.Commom.bulu
                            + MyApplication.token + "&id=" + order.getId());
                    startActivity(intent);
                }
            }
        });
       // mCollectAdapter.setEmptyView(view);
        rv_search.setLayoutManager(new LinearLayoutManager(this));
        rv_search.setAdapter(mCollectAdapter);
        getHotWords();
        getCategory();
    }

    List<HomeCategory> mHomeCategories;
    SearchTypeAdapter searchTypeAdapter;
    String categoryId = "";

    private void showDialog(ViewGroup view) {
        EasyPopup mCirclePop = EasyPopup.create()
                .setContentView(this, R.layout.dialog_search)

                //是否允许点击PopupWindow之外的地方消失
                .setFocusAndOutsideEnable(true)
                //允许背景变暗
                .setBackgroundDimEnable(true)
                //变暗的透明度(0-1)，0为完全透明
                .setDimValue(0.4f)
                //变暗的背景颜色
                .setDimColor(Color.BLACK)
                //指定任意 ViewGroup 背景变暗
                .setDimView(view)
                .apply();
        mCirclePop.showAtAnchorView(rl_top, YGravity.BELOW, XGravity.CENTER, 0, 0);
        ListView lv_search = mCirclePop.findViewById(R.id.lv_search);

        searchTypeAdapter = new SearchTypeAdapter(this, mHomeCategories);
        lv_search.setAdapter(searchTypeAdapter);
        lv_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mCirclePop.dismiss();
                tv_type.setText(mHomeCategories.get(position).getName());
                categoryId = mHomeCategories.get(position).getId();
            }
        });

    }

    boolean isAsk = false;
    boolean isShowPt=false;
    public void getListData(int page, String name) {
        isAsk = true;
        if(page==1)
           showLoading("loading...");
        hide();
        String token = "";
        if (MyApplication.isPt == 1) {
            if (MyApplication.getUserInfo().getType().equals("2")) {
                url = Api.Product.getListForFirstSearchWithPtOn;
                isShowPt=true;
                token = MyApplication.token;
            } else {
                url = Api.Product.getListForFirstSearch;
            }
        } else {
            url = Api.Product.getListForFirstSearch;
        }
        Map<String, String> params = new HashMap<>();
        params.put("categoryId", categoryId);
        params.put("page", page + "");
        if (!TextUtils.isEmpty(name)) {
            params.put("name", name);
        }

//        HttpProxy.obtain().get(url, params, token, new ICallBack() {
//            @Override
//            public void OnSuccess(String result) {
//                if(dialog.isShowing()){
//                    dialog.dismiss();
//                }
//                refresh.finishRefresh();
//                Log.e("record", result);
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    JSONObject dataobject = jsonObject.getJSONObject("data");
//                    dataobject = dataobject.getJSONObject("data");
//                    int code = jsonObject.getInt("code");
//                    if (code == 0) {
//                        JSONArray data = dataobject.getJSONArray("beanList");
//                        if(page==1&&data.length()==0){
//                            ll_empty.setVisibility(View.VISIBLE);
//                            ll_data.setVisibility(View.GONE);
//                        }else{
//                            ll_empty.setVisibility(View.GONE);
//                            ll_data.setVisibility(View.VISIBLE);
//                        }
//                        List<Medicine> orders = new ArrayList<>();
//                        for (int i = 0; i < data.length(); i++) {
//                            JSONObject item = data.getJSONObject(i);
//                            Medicine homeCategory = new Gson().fromJson(item.toString(), Medicine.class);
//                            if(isShowPt){
//                                homeCategory.setShowPt(true);
//                            }
//                            homeCategory.setHide(true);
//                            mCollects.add(homeCategory);
//                            orders.add(homeCategory);
//                        }
//                        if (isLoadMore) {
//                            isLoadMore = false;
//                            if (data.length() > 0) {
//                                mCollectAdapter.addData(orders);
//                                mCollectAdapter.loadMoreComplete();
//                            } else {
//                                mCollectAdapter.loadMoreEnd(true);
//                            }
//                        } else {
//                            mCollectAdapter.setNewData(mCollects);
//                            // orderAdapter.loadMoreEnd(true);
//                        }
//
//                    } else {
//
//                    }
//                    isAsk = false;
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(String error) {
//                if(dialog.isShowing()){
//                    dialog.dismiss();
//                }
//            }
//        });

        NetUtils.getInstance().get(token, url, params, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                if(dialog.isShowing()){
                    dialog.dismiss();
                }
                refresh.finishRefresh();
                Log.e("record", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject dataobject = jsonObject.getJSONObject("data");
                    dataobject = dataobject.getJSONObject("data");
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        JSONArray data = dataobject.getJSONArray("beanList");
                        if(page==1&&data.length()==0){
                            ll_empty.setVisibility(View.VISIBLE);
                            ll_data.setVisibility(View.GONE);
                        }else{
                            ll_empty.setVisibility(View.GONE);
                            ll_data.setVisibility(View.VISIBLE);
                        }
                        List<Medicine> orders = new ArrayList<>();
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            Medicine homeCategory = new Gson().fromJson(item.toString(), Medicine.class);
                            if(isShowPt){
                                homeCategory.setShowPt(true);
                            }
                            homeCategory.setHide(true);
                            mCollects.add(homeCategory);
                            orders.add(homeCategory);
                        }
                        if (isLoadMore) {
                            isLoadMore = false;
                            if (data.length() > 0) {
                                mCollectAdapter.addData(orders);
                                mCollectAdapter.loadMoreComplete();
                            } else {
                                mCollectAdapter.loadMoreEnd(true);
                            }
                        } else {
                            mCollectAdapter.setNewData(mCollects);
                            // orderAdapter.loadMoreEnd(true);
                        }

                    } else {

                    }
                    isAsk = false;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                if(dialog.isShowing()){
                    dialog.dismiss();
                }
            }
        });

    }

    public void initSearchHistory() {
        mHistoryKeywords = new ArrayList<>();
        String history = SharePreferenceUtils.getString(SearchActivity.this, "history");
        if (!TextUtils.isEmpty(history)) {
            List<String> list = new ArrayList<String>();
            for (Object o : history.split(",")) {
                list.add((String) o);
            }
            mHistoryKeywords = list;
        }
        mHistoryTagAdapter = new HotTagAdapter(mHistoryKeywords);

        fl_recen.setAdapter(mHistoryTagAdapter);
        fl_recen.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {

                et_name.setText(mHistoryKeywords.get(position));
                et_name.setSelection(mHistoryKeywords.get(position).length());
                page = 1;
                mCollects.clear();
                ll_history.setVisibility(View.GONE);
                ll_data.setVisibility(View.VISIBLE);
                getListData(page, mHistoryKeywords.get(position));
                return false;
            }
        });
    }

    public static void setEditTextInhibitInputSpace(EditText editText){
        InputFilter filter=new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                if(" ".equals(source)){
                    return "";
                }else{
                    return null;
                }
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_down:
                showDialog(ll_type);
                break;
            case R.id.iv_delRent:
                SharePreferenceUtils.putString(SearchActivity.this, "history", "");
                mHistoryKeywords.clear();
                mHistoryTagAdapter.notifyDataChanged();
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_search:

                String name = et_name.getEditableText().toString();
                //setEditTextInhibitInputSpace(et_name);
                if("".equals(et_name.getText().toString().trim())){
                    ToastUtil.showToast(this, "搜索内容不能为空");
                    return;
                }
                if (isAsk) {
                    return;
                }
                page = 1;
                mCollects = new ArrayList<>();
                ll_history.setVisibility(View.GONE);
                ll_data.setVisibility(View.VISIBLE);
                getListData(page, name);
                String oldhistory = SharePreferenceUtils.getString(SearchActivity.this, "history");
                if (!TextUtils.isEmpty(name) && (!oldhistory.contains(name))) {
                    if (mHistoryKeywords.size() > 5) {
                        //最多保存条数
                        return;
                    }
                    mHistoryKeywords.add(0, name);
                    mHistoryTagAdapter.notifyDataChanged();
                    if (TextUtils.isEmpty(oldhistory))
                        SharePreferenceUtils.putString(SearchActivity.this, "history", name);
                    else {
                        String newHistory = name + "," + oldhistory;
                        SharePreferenceUtils.putString(SearchActivity.this, "history", newHistory);
                    }
                }
                break;
        }
    }

    private void hide() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(et_name.getWindowToken(), 0);

    }
}
