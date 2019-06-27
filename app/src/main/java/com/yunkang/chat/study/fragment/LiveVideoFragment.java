package com.yunkang.chat.study.fragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.blankj.utilcode.util.ToastUtils;
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
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.lzy.okgo.model.HttpParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnMultiPurposeListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yunkang.chat.MyApplication;
import com.yunkang.chat.R;
import com.yunkang.chat.api.Api;
import com.yunkang.chat.http.HttpCallback;
import com.yunkang.chat.http.HttpProxy;
import com.yunkang.chat.http.ICallBack;
import com.yunkang.chat.mine.model.Order;
import com.yunkang.chat.net.NetUtils;
import com.yunkang.chat.net.OnMessageReceived;
import com.yunkang.chat.study.adapter.LiveVideoAdapter;
import com.yunkang.chat.study.model.LiveVideo;
import com.yunkang.chat.study.model.RecordVideo;
import com.yunkang.chat.watch.PolyvCloudClassHomeActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class LiveVideoFragment extends Fragment {

    @BindView(R.id.rv_video)
    RecyclerView rv_video;
    @BindView(R.id.refresh)
    SmartRefreshLayout mSmartRefreshLayout;
    LiveVideoAdapter mLiveVideoAdapter;
    List<LiveVideo> mLiveVideos;
    Disposable getTokenDisposable, verifyDispose;
    private String USERID = "b67d349621";
    private String CHANNALID = "307758";
    private String APPID = "fb3a33quez";
    private String APPSCRET = "6f604c691f3348f980d79e31e053f60d";
    int page = 1;
    boolean isLoadMore = false;
    boolean isVip=false;
    public LiveVideoFragment() {
        // Required empty public constructor

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_live_video, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void getChannelOnline(String channelIds){
        HttpParams httpParams=new HttpParams();
        httpParams.put("channelIds",channelIds+"");
        NetUtils.getInstance().get(MyApplication.token, Api.Polyv.getChannelViewers, httpParams, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                Log.e("getChannelViewers", response+channelIds);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    JSONObject dataobject = jsonObject.getJSONObject("data");
                    JSONObject object=dataobject.getJSONObject("data");
                    if (code == 0) {
                        JSONArray data = object.getJSONArray("channelViewers");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            int  count=item.getInt("count");
                            int  channelId=item.getInt("channelId");
                            for (int j = 0; j <mLiveVideos.size() ; j++) {
                                if(channelId==mLiveVideos.get(j).getChannelId()){
                                    mLiveVideos.get(j).setCount(count);
                                    break;
                                }
                            }
                        }
                        mLiveVideoAdapter.setNewData(mLiveVideos);
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
    private void checkIsVip(LiveVideo liveVideo){
        HttpParams httpParams=new HttpParams();
        httpParams.put("page",1);
        httpParams.put("rank","1");
        //httpParams.put("channelId",liveVideo.getChannelId());
        NetUtils.getInstance().get(MyApplication.token, Api.Polyv.getWhiteList, httpParams, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                Log.e("getWhiteList", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    JSONObject dataobject = jsonObject.getJSONObject("data");
                    JSONObject object=dataobject.getJSONObject("data");
                    if (code == 0) {
                        JSONArray data = object.getJSONArray("contents");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            String phone=item.getString("phone");
                            if(MyApplication.getUserInfo()!=null)
                            if(MyApplication.getUserInfo().getTelephone().equals(phone)){
                                isVip=true;
                                break;
                            }
                        }
                        if(isVip){
                            login(liveVideo.getUserId(), APPSCRET, liveVideo.getChannelId() + "", null, APPID,liveVideo.getChannelLogoImage(),liveVideo.getName()+","+liveVideo.getPublisher());
                        }else{
                            ToastUtils.showShort("你不是白名单人员，不能观看此频道");
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
    private void initView() {
        mLiveVideos = new ArrayList<>();

        mLiveVideoAdapter = new LiveVideoAdapter(R.layout.item_live_video, mLiveVideos);

        mLiveVideoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.e("data", "onClick1");

                LiveVideo liveVideo = (LiveVideo) adapter.getItem(position);
                checkIsVip(liveVideo);
                //login(liveVideo.getUserId(), APPSCRET, liveVideo.getChannelId() + "", null, APPID,liveVideo.getChannelLogoImage(),liveVideo.getName()+","+liveVideo.getPublisher());
            }
        });
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mLiveVideos.clear();
                mLiveVideoAdapter.setNewData(mLiveVideos);
                getData();
            }
        });
        rv_video.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_video.setAdapter(mLiveVideoAdapter);
        getData();
    }
    String channelIds="";
    private void getData() {

        showLoading("");
        channelIds="";
        NetUtils.getInstance().get(MyApplication.token, Api.Polyv.getLiveChannel, new HashMap<>(), new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                mSmartRefreshLayout.finishRefresh();
                if(dialog!=null)
                    dialog.dismiss();
                Log.e("polyv", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    JSONObject dataobject = jsonObject.getJSONObject("data");
                    if (code == 0) {
                        JSONArray data = dataobject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            LiveVideo liveVideo = new Gson().fromJson(item.toString(), LiveVideo.class);
                            channelIds=channelIds+","+liveVideo.getChannelId();
                            mLiveVideos.add(liveVideo);
                        }
                        if(!TextUtils.isEmpty(channelIds))
                           getChannelOnline(channelIds.substring(1));
                        //mLiveVideoAdapter.setNewData(mLiveVideos);
                    }


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


    private void login(String userId, String appSecret, String channel, String vid, String appId,String photo,String name) {
        //请求token接口
        getTokenDisposable = PolyvLoginManager.checkLoginToken(userId, appSecret, appId,
                channel, vid,
                new PolyvrResponseCallback<PolyvResponseBean>() {
                    @Override
                    public void onSuccess(PolyvResponseBean responseBean) {
                        PolyvLinkMicClient.getInstance().setAppIdSecret(appId, appSecret);
                        PolyvLiveSDKClient.getInstance().setAppIdSecret(appId, appSecret);
                        PolyvVodSDKClient.getInstance().initConfig(appId, appSecret);
                        requestLiveStatus(userId, channel,photo,name);
                        Log.e("data", responseBean.getBody().toString());
                    }

                    @Override
                    public void onFailure(PolyvResponseBean<PolyvResponseBean> responseBean) {
                        super.onFailure(responseBean);
                        Log.e("data", "onClick3");
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Log.e("data", "onClick4");
                    }
                });
    }

    private void startActivityForLive(String userId, boolean isAlone, String channelId,String photo,String name) {
        PolyvCloudClassHomeActivity.startActivityForLive(getActivity(),
                channelId, userId, isAlone,photo,name);
    }

    private void requestLiveStatus(String userId, String channelId,String photo,String name) {
        verifyDispose = PolyvResponseExcutor.excuteUndefinData(PolyvApiManager.getPolyvLiveStatusApi().geLiveStatusJson(channelId)
                , new PolyvrResponseCallback<PolyvLiveStatusVO>() {
                    @Override
                    public void onSuccess(PolyvLiveStatusVO statusVO) {
                        String data = statusVO.getData();
                        String[] dataArr = data.split(",");
                        boolean isAlone = "alone".equals(dataArr[1]);//是否有ppt
                        startActivityForLive(userId, isAlone, channelId,photo,name);

                    }

                    @Override
                    public void onFailure(PolyvResponseBean<PolyvLiveStatusVO> responseBean) {
                        super.onFailure(responseBean);
                        Log.e("data", "onClick6");
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Log.e("data", e.getMessage());
                    }
                });
    }

}
