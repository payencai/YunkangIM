package com.yunkang.chat.study.fragment;


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
import android.widget.ListView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easefun.polyv.businesssdk.service.PolyvLoginManager;
import com.easefun.polyv.businesssdk.vodplayer.PolyvVodSDKClient;
import com.easefun.polyv.cloudclass.config.PolyvLiveSDKClient;
import com.easefun.polyv.cloudclass.model.PolyvLiveStatusVO;
import com.easefun.polyv.cloudclass.net.PolyvApiManager;
import com.easefun.polyv.foundationsdk.net.PolyvResponseBean;
import com.easefun.polyv.foundationsdk.net.PolyvResponseExcutor;
import com.easefun.polyv.foundationsdk.net.PolyvrResponseCallback;
import com.easefun.polyv.linkmic.PolyvLinkMicClient;
import com.google.android.exoplayer.ExoPlayer;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.lzy.okgo.model.HttpParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yunkang.chat.MyApplication;
import com.yunkang.chat.R;
import com.yunkang.chat.api.Api;
import com.yunkang.chat.http.HttpProxy;
import com.yunkang.chat.http.ICallBack;
import com.yunkang.chat.net.NetUtils;
import com.yunkang.chat.net.OnMessageReceived;
import com.yunkang.chat.study.VideoContentActivity;
import com.yunkang.chat.study.VideoDetailActivity;
import com.yunkang.chat.study.adapter.CallbackVideoAdapter;
import com.yunkang.chat.study.adapter.LiveVideoAdapter;
import com.yunkang.chat.study.model.CallbackVideo;
import com.yunkang.chat.study.model.LiveVideo;
import com.yunkang.chat.study.model.VideoLabel;
import com.yunkang.chat.watch.PolyvCloudClassHomeActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fj.dropdownmenu.lib.concat.DropdownI;
import fj.dropdownmenu.lib.ion.ViewInject;
import fj.dropdownmenu.lib.ion.ViewUtils;
import fj.dropdownmenu.lib.pojo.DropdownItemObject;
import fj.dropdownmenu.lib.utils.DropdownUtils;
import fj.dropdownmenu.lib.view.DropdownButton;
import fj.dropdownmenu.lib.view.DropdownColumnView;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class CallbackVideoFragment extends Fragment implements DropdownI.SingleRow{
    @ViewInject(R.id.btnType)
    @BindView(R.id.btnType)
    DropdownButton btnType;
    @BindView(R.id.mask)
    View mask;
    @ViewInject(R.id.lvType)
    @BindView(R.id.lvType)
    DropdownColumnView lvType;
    @BindView(R.id.refresh)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.rv_video)
    RecyclerView rv_video;
    CallbackVideoAdapter mCallbackVideoAdapter;
    List<CallbackVideo> mCallbackVideos;
    List<DropdownItemObject> mDropdownItemObjects;
    List<VideoLabel> mVideoLabels;
    String labelName;
    int page=1;
    long labelId=-1;
    boolean isLoadMore=false;
    public CallbackVideoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_callback_video, container, false);
        ButterKnife.bind(this, view);
        DropdownUtils.initFragment(getActivity(),this,view, mask);
        ViewUtils.injectFragmentViews(this,view, mask);
        initView();
        return view;
    }
    private void initDrop(){
        lvType.setSingleRow(this)
                .setSingleRowList(mDropdownItemObjects, -1)  //单列数据
                .setButton(btnType) //按钮
                .show();
    }
    private void getVideoLabel(){
        Log.e("start","start");


        NetUtils.getInstance().get(MyApplication.token, Api.Video.getVideoLabel, new HashMap<>(), new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                Log.e("getVideoLabel", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    JSONObject dataobject = jsonObject.getJSONObject("data");
                    if (code == 0) {
                        JSONArray data = dataobject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            VideoLabel videoLabel = new Gson().fromJson(item.toString(), VideoLabel.class);
                            mVideoLabels.add(videoLabel);
                            mDropdownItemObjects.add(new DropdownItemObject(i,videoLabel.getVideoLabelName(),videoLabel.getVideoLabelName()+""));
                        }
                        initDrop();
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
        mDropdownItemObjects=new ArrayList<>();
        mVideoLabels=new ArrayList<>();
        mCallbackVideos = new ArrayList<>();
        mCallbackVideoAdapter = new CallbackVideoAdapter(R.layout.item_reback_video, mCallbackVideos);
        mCallbackVideoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.e("data","onClick1");
                CallbackVideo callbackVideo= (CallbackVideo) adapter.getItem(position);
                Intent intent=new Intent(getContext(), VideoDetailActivity.class);
                intent.putExtra("data",callbackVideo);
                startActivity(intent);
            }
        });
        mCallbackVideoAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                isLoadMore=true;
                getVideo();
            }
        },rv_video);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
               refresh();
            }
        });
        rv_video.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_video.setAdapter(mCallbackVideoAdapter);
        DropdownItemObject typeObj = new DropdownItemObject(-1, "全部", "全部");
        mDropdownItemObjects.add(typeObj);
        getVideoLabel();

        getVideo();
    }

    @Override
    public void onSingleChanged(DropdownItemObject dropdownItemObject) {
         int id=dropdownItemObject.id;
         if(id>=0){
             labelId= Long.parseLong(mVideoLabels.get(id).getId());
         }else{
             labelId=-1;
         }
        refresh();
    }
    private void refresh(){
        page=1;
        mCallbackVideos.clear();
        mCallbackVideoAdapter.setNewData(mCallbackVideos);
        getVideo();
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
    private void getVideo(){
        showLoading("");
        HttpParams httpParams=new HttpParams();
        httpParams.put("page",page);
        if(labelId>0){
            httpParams.put("labelId",labelId);
        }
        NetUtils.getInstance().get(MyApplication.token, Api.Video.getVideoByLabel, httpParams, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                if(dialog!=null)
                    dialog.dismiss();
                Log.e("getVideoByLabel", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    JSONObject dataobject = jsonObject.getJSONObject("data");
                    JSONObject object=dataobject.getJSONObject("data");
                    if (code == 0) {
                        JSONArray data = object.getJSONArray("beanList");
                        List<CallbackVideo> callbackVideos=new ArrayList<>();
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            CallbackVideo liveVideo = new Gson().fromJson(item.toString(), CallbackVideo.class);
                            mCallbackVideos.add(liveVideo);
                            callbackVideos.add(liveVideo);
                        }
                        if(isLoadMore){
                            isLoadMore=false;
                            if(data.length()>0){
                                mCallbackVideoAdapter.addData(callbackVideos);
                                mCallbackVideoAdapter.loadMoreComplete();
                            }else{
                                mCallbackVideoAdapter.loadMoreEnd(true);
                            }
                        }else{
                            mCallbackVideoAdapter.setNewData(mCallbackVideos);
                        }

                    }
                    mRefreshLayout.finishRefresh();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                if(dialog!=null)
                    dialog.dismiss();
            }
        });

    }
}
