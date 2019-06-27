package com.yunkang.chat.category.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.yunkang.chat.R;
import com.yunkang.chat.api.Api;
import com.yunkang.chat.category.adapter.CheckSecondAdapter;
import com.yunkang.chat.category.adapter.ChectFirstAdapter;
import com.yunkang.chat.category.activity.CategoryGoodsActivity;
import com.yunkang.chat.home.model.HomeCategory;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryMedicineFragment extends Fragment {

    List<HomeCategory> mCategoryFirsts;
    List<HomeCategory> mCategorySeconds;
    @BindView(R.id.lv_category)
    ListView lv_category;
    @BindView(R.id.gv_cateory)
    GridView gv_cateory;

    ChectFirstAdapter mCategoryFirstAdapter;
    CheckSecondAdapter mCategorySecondAdapter;
    String id;
    int type=-1;
    public CategoryMedicineFragment() {
        // Required empty public constructor
    }
    public static CategoryMedicineFragment newInstance(String id,int type){
        CategoryMedicineFragment categoryMedicineFragment=new CategoryMedicineFragment();
        Bundle bundle=new Bundle();
        bundle.putString("id",id);
        bundle.putInt("type",type);
        categoryMedicineFragment.setArguments(bundle);
        return  categoryMedicineFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category_medicine, container, false);
        ButterKnife.bind(this, view);
        id=getArguments().getString("id");
        type=getArguments().getInt("type");
        if(type==0)
            showLoading("loading...");
        initView();
        return view;
    }
    KProgressHUD dialog;
    private void showLoading(String data){
        dialog= KProgressHUD.create(getContext())
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
    boolean isAsk=false;
    private void initView() {

        mCategoryFirsts = new ArrayList<>();
        mCategorySeconds = new ArrayList<>();

        mCategorySecondAdapter = new CheckSecondAdapter(getContext(), mCategorySeconds);
        mCategoryFirstAdapter = new ChectFirstAdapter(getContext(), mCategoryFirsts);
        lv_category.setAdapter(mCategoryFirstAdapter);
        gv_cateory.setAdapter(mCategorySecondAdapter);
        lv_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mCategoryFirstAdapter.setSelectedPosition(i);
                mCategoryFirstAdapter.notifyDataSetChanged();
                mCategorySeconds.clear();
                getThirdCategory(mCategoryFirsts.get(i).getId());
            }
        });
        gv_cateory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getContext(), CategoryGoodsActivity.class);
                intent.putExtra("id",mCategorySeconds.get(i).getId());
                 startActivity(intent);
            }
        });
        if(!isAsk)
        getSecCategory();

    }

    private void getThirdCategory(String id) {
//        Map<String, Object> params = new HashMap<>();
//        params.put("rank", 3);
//        params.put("parentId", id);
//        HttpProxy.obtain().get(Api.Product.getHomeCategoryList, params, new ICallBack() {
//            @Override
//            public void OnSuccess(String result) {
//                if(dialog!=null)
//                if(dialog.isShowing())
//                    dialog.dismiss();
//                Log.e("getCategory", result);
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    JSONObject dataobject = jsonObject.getJSONObject("data");
//                    int code = jsonObject.getInt("code");
//                    if (code == 0) {
//                        JSONArray data = dataobject.getJSONArray("data");
//                        for (int i = 0; i < data.length(); i++) {
//                            JSONObject item = data.getJSONObject(i);
//                            HomeCategory homeCategory = new Gson().fromJson(item.toString(), HomeCategory.class);
//                            mCategorySeconds.add(homeCategory);
//                        }
//                        mCategorySecondAdapter.notifyDataSetChanged();
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
//                if(dialog!=null)
//                if(dialog.isShowing())
//                    dialog.dismiss();
//            }
//        });

        Map<String, String> params = new HashMap<>();
        params.put("rank", "3");
        params.put("parentId", id);
        NetUtils.getInstance().get(Api.Product.getHomeCategoryList, params, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                if(dialog!=null)
                    if(dialog.isShowing())
                        dialog.dismiss();
                Log.e("getCategory", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject dataobject = jsonObject.getJSONObject("data");
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        JSONArray data = dataobject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            HomeCategory homeCategory = new Gson().fromJson(item.toString(), HomeCategory.class);
                            mCategorySeconds.add(homeCategory);
                        }
                        mCategorySecondAdapter.notifyDataSetChanged();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                if(dialog!=null)
                    if(dialog.isShowing())
                        dialog.dismiss();
            }
        });

    }

    private void getSecCategory() {
//        isAsk=true;
//        Map<String, Object> params = new HashMap<>();
//        params.put("rank", 2);
//        params.put("parentId", id);
//        HttpProxy.obtain().get(Api.Product.getHomeCategoryList, params, new ICallBack() {
//            @Override
//            public void OnSuccess(String result) {
//                isAsk=false;
//                Log.e("getCategory", result);
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    JSONObject dataobject = jsonObject.getJSONObject("data");
//                    int code = jsonObject.getInt("code");
//                    if (code == 0) {
//                        JSONArray data = dataobject.getJSONArray("data");
//                        for (int i = 0; i < data.length(); i++) {
//                            JSONObject item = data.getJSONObject(i);
//                            HomeCategory homeCategory = new Gson().fromJson(item.toString(), HomeCategory.class);
//                            mCategoryFirsts.add(homeCategory);
//                            if(i==0){
//                                getThirdCategory(homeCategory.getId());
//                            }
//                        }
//                        mCategoryFirstAdapter.notifyDataSetChanged();
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
//                isAsk=false;
//            }
//        });

        isAsk=true;
        Map<String, String> params = new HashMap<>();
        params.put("rank", "2");
        params.put("parentId", id);
        NetUtils.getInstance().get(Api.Product.getHomeCategoryList, params, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                isAsk=false;
                Log.e("getCategory", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject dataobject = jsonObject.getJSONObject("data");
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        JSONArray data = dataobject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            HomeCategory homeCategory = new Gson().fromJson(item.toString(), HomeCategory.class);
                            mCategoryFirsts.add(homeCategory);
                            if(i==0){
                                getThirdCategory(homeCategory.getId());
                            }
                        }
                        mCategoryFirstAdapter.notifyDataSetChanged();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                isAsk=false;

            }
        });
    }
}
