package com.yunkang.chat.study;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lzy.okgo.model.HttpParams;
import com.yunkang.chat.MyApplication;
import com.yunkang.chat.R;
import com.yunkang.chat.api.Api;
import com.yunkang.chat.net.NetUtils;
import com.yunkang.chat.net.OnMessageReceived;
import com.yunkang.chat.study.adapter.ReplyAdapter;
import com.yunkang.chat.study.model.VideoComment;
import com.yunkang.chat.view.CircleImageView;

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

public class CommentDetailActivity extends AppCompatActivity {
    VideoComment mVideoComment;
    @BindView(R.id.iv_head)
    CircleImageView iv_head;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.et_comment)
    EditText et_comment;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.tv_content)
    TextView tv_content;
    @BindView(R.id.tv_num)
    TextView tv_num;
    @BindView(R.id.ll_num)
    LinearLayout ll_num;
    @BindView(R.id.tv_status)
    TextView tv_status;
    @BindView(R.id.rv_reply)
    RecyclerView rv_reply;
    ReplyAdapter mReplyAdapter;
    List<VideoComment> mVideoComments;
    int page=1;
    boolean isLoadMore=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_detail);
        ButterKnife.bind(this);
        mVideoComment= (VideoComment) getIntent().getSerializableExtra("data");
        initView();
    }

    private void initView() {
        mVideoComments=new ArrayList<>();
        mReplyAdapter=new ReplyAdapter(R.layout.item_video_comment,mVideoComments);
        rv_reply.setLayoutManager(new LinearLayoutManager(this));
        rv_reply.setAdapter(mReplyAdapter);
        Glide.with(this).load(mVideoComment.getHeading()).into(iv_head);
        tv_name.setText(mVideoComment.getNickName());
        tv_time.setText(mVideoComment.getCreateDate().substring(0,10));
        tv_content.setText(mVideoComment.getComment());
        tv_num.setText(""+mVideoComment.getComments());
        if(Integer.parseInt(mVideoComment.getComments())>0){
            ll_num.setVisibility(View.VISIBLE);
            getReply();
        }else{
            ll_num.setVisibility(View.GONE);
            tv_status.setVisibility(View.GONE);
        }
    }
    @OnClick({R.id.tv_send,R.id.back})
    void OnCLick(View view){
        switch (view.getId()){
            case R.id.tv_send:
                String comment=et_comment.getEditableText().toString();
                if(!TextUtils.isEmpty(comment)){
                    addComment(comment);
                }
                break;
            case R.id.back:
                finish();
                break;
        }
    }
    private void addComment(String comment){
        Map<String,Object> params=new HashMap<>();
        params.put("comment",comment);
        params.put("parent",mVideoComment.getId());
        params.put("typeStatus","1");
        params.put("videoId",mVideoComment.getVideoId());
        params.put("custId",MyApplication.getUserInfo().getCustId());
        String json=new Gson().toJson(params);
        NetUtils.getInstance().post(Api.Video.addSonVideoComment, json, MyApplication.token, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                LogUtils.e(response);
                et_comment.setText("");
                ToastUtils.showShort("回复成功");
                page=1;
                mVideoComments.clear();
                mReplyAdapter.setNewData(mVideoComments);
                getReply();
            }

            @Override
            public void onError(String error) {

            }
        });
    }
    private void getReply() {
        HttpParams httpParams = new HttpParams();
        httpParams.put("page", page);
        httpParams.put("videoId", mVideoComment.getVideoId());
        httpParams.put("parentId", mVideoComment.getId());
        NetUtils.getInstance().get(MyApplication.token, Api.Video.getSonVideoComment, httpParams, new OnMessageReceived() {
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
                                mReplyAdapter.addData(videoComments);
                                mReplyAdapter.loadMoreComplete();
                            } else {
                                mReplyAdapter.loadMoreEnd(true);
                            }
                        } else {
                            mReplyAdapter.setNewData(mVideoComments);
                        }

                    }
                    tv_status.setVisibility(View.VISIBLE);


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
