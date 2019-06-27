package com.yunkang.chat.study.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.lzy.okgo.model.HttpParams;
import com.yunkang.chat.MyApplication;
import com.yunkang.chat.R;
import com.yunkang.chat.api.Api;
import com.yunkang.chat.net.NetUtils;
import com.yunkang.chat.net.OnMessageReceived;
import com.yunkang.chat.study.PlayVideoActivity;
import com.yunkang.chat.study.adapter.RecordVideoAdapter;
import com.yunkang.chat.study.model.CallbackVideo;
import com.yunkang.chat.study.model.RecordVideo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class RebackFragment extends Fragment {

    @BindView(R.id.rv_record)
    RecyclerView rv_record;
    RecordVideoAdapter mRecordVideoAdapter;
    List<RecordVideo> mRecordVideos;
    int page=1;
    int pageSize=10;
    boolean isLoadMore=false;
    String channelId;
    String photo;
    public static  RebackFragment newInstance(String  id,String photo) {
        RebackFragment rebackFragment=new RebackFragment();
        Bundle bundle=new Bundle();
        bundle.putString("id",id);
        bundle.putString("photo",photo);
        rebackFragment.setArguments(bundle);
        return  rebackFragment;
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_reback, container, false);
        ButterKnife.bind(this,view);
        channelId=getArguments().getString("id");
        photo=getArguments().getString("photo");
        initView();
        return view;
    }

    private void initView() {
        mRecordVideos=new ArrayList<>();
        mRecordVideoAdapter=new RecordVideoAdapter(R.layout.item_record_video,mRecordVideos);
        mRecordVideoAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                isLoadMore=true;
                getRecord();
            }
        },rv_record);
        mRecordVideoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                RecordVideo recordVideo= (RecordVideo) adapter.getItem(position);
                Intent intent=new Intent(getContext(), PlayVideoActivity.class);
                intent.putExtra("url",recordVideo.getUrl());
                startActivity(intent);
            }
        });
        rv_record.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_record.setAdapter(mRecordVideoAdapter);
        getRecord();
    }

    private void getRecord(){
        HttpParams httpParams=new HttpParams();
        httpParams.put("page",page);
        httpParams.put("channelId",channelId);
//        httpParams.put("pageSize",pageSize);
        //Log.e("params",channelId+"-"+MyApplication.token);
        NetUtils.getInstance().get(MyApplication.token, Api.Polyv.getChannelRecordFiles, httpParams, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                Log.e("response",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    JSONObject dataobject = jsonObject.getJSONObject("data");
                    JSONObject object=dataobject.getJSONObject("data");
                    if (code == 0) {
                        JSONArray data = object.getJSONArray("recordFiles");
                        List<RecordVideo> callbackVideos=new ArrayList<>();
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            RecordVideo liveVideo = new Gson().fromJson(item.toString(), RecordVideo.class);
                            liveVideo.setPhoto(photo);
                            mRecordVideos.add(liveVideo);
                            callbackVideos.add(liveVideo);
                        }
                        if(isLoadMore){
                            isLoadMore=false;
                            if(data.length()>0){
                                mRecordVideoAdapter.addData(callbackVideos);
                                mRecordVideoAdapter.loadMoreComplete();
                            }else{
                                mRecordVideoAdapter.loadMoreEnd(true);
                            }
                        }else{
                            mRecordVideoAdapter.setNewData(mRecordVideos);
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

}
