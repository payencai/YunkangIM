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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.lzy.okgo.model.HttpParams;
import com.yunkang.chat.MyApplication;
import com.yunkang.chat.R;
import com.yunkang.chat.api.Api;
import com.yunkang.chat.net.NetUtils;
import com.yunkang.chat.net.OnMessageReceived;
import com.yunkang.chat.study.CommentDetailActivity;
import com.yunkang.chat.study.adapter.VideoCommentAdapter;
import com.yunkang.chat.study.model.CallbackVideo;
import com.yunkang.chat.study.model.VideoComment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentFragment extends Fragment {
    int page = 1;
    boolean isLoadMore = false;
    @BindView(R.id.rv_comment)
    RecyclerView rv_comment;
    VideoCommentAdapter mVideoCommentAdapter;
    List<VideoComment> mVideoComments;
    String  id;
    public static  CommentFragment newInstance(String  id) {
        CommentFragment commentFragment=new CommentFragment();
        Bundle bundle=new Bundle();
        bundle.putString("id",id);
        commentFragment.setArguments(bundle);
        return  commentFragment;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comment, container, false);
        ButterKnife.bind(this, view);
        id=getArguments().getString("id");
        initView();
        return view;
    }
    public  void refresh(){

       page=1;
       mVideoComments.clear();
       mVideoCommentAdapter.setNewData(mVideoComments);
       getComment();
       rv_comment.setFocusable(true);
    }
    private void initView() {
        mVideoComments = new ArrayList<>();

        mVideoCommentAdapter = new VideoCommentAdapter(R.layout.item_video_comment, mVideoComments);
        mVideoCommentAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                isLoadMore=true;
                getComment();
            }
        },rv_comment);
        mVideoCommentAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                VideoComment videoComment= (VideoComment) adapter.getItem(position);
                Intent intent=new Intent(getContext(), CommentDetailActivity.class);
                intent.putExtra("data",videoComment);
                startActivityForResult(intent,1);
            }
        });
        rv_comment.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_comment.setAdapter(mVideoCommentAdapter);
        getComment();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        page=1;
        mVideoComments.clear();
        mVideoCommentAdapter.setNewData(mVideoComments);
        getComment();
    }

    private void getComment() {
        HttpParams httpParams = new HttpParams();
        httpParams.put("page", page);
        httpParams.put("videoId", id);
        NetUtils.getInstance().get(MyApplication.token, Api.Video.getParentVideoComment, httpParams, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {

                Log.e("getParentVideoComment", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    JSONObject dataobject = jsonObject.getJSONObject("data");
                    JSONObject object = dataobject.getJSONObject("data");
                    if (code == 0) {
                        JSONArray data = object.getJSONArray("beanList");
                        List<VideoComment> videoComments = new ArrayList<>();
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            VideoComment liveVideo = new Gson().fromJson(item.toString(), VideoComment.class);
                            mVideoComments.add(liveVideo);
                            videoComments.add(liveVideo);
                        }
                        if (isLoadMore) {
                            isLoadMore = false;
                            if (data.length() > 0) {
                                mVideoCommentAdapter.addData(videoComments);
                                mVideoCommentAdapter.loadMoreComplete();
                            } else {
                                mVideoCommentAdapter.loadMoreEnd(true);
                            }
                        } else {
                            mVideoCommentAdapter.setNewData(mVideoComments);
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
