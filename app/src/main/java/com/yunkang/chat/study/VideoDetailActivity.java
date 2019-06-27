package com.yunkang.chat.study;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;
import com.lzy.okgo.model.HttpParams;
import com.yunkang.chat.MyApplication;
import com.yunkang.chat.R;
import com.yunkang.chat.api.Api;
import com.yunkang.chat.net.NetUtils;
import com.yunkang.chat.net.OnMessageReceived;
import com.yunkang.chat.study.fragment.CommentFragment;
import com.yunkang.chat.study.fragment.X5DetailFragment;
import com.yunkang.chat.study.model.CallbackVideo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

public class VideoDetailActivity extends AppCompatActivity {
    @BindView(R.id.slidingTabLayout)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tv_num)
    TextView tv_num;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.et_comment)
    EditText et_comment;
    @BindView(R.id.videoplayer)
    JZVideoPlayerStandard videoplayer;

    ArrayList<Fragment> mFragments;
    CallbackVideo mCallbackVideo;
    String[] mTitles = {"评论", "详情"};
    CommentFragment mCommentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);
        ButterKnife.bind(this);
        mCallbackVideo= (CallbackVideo) getIntent().getSerializableExtra("data");
        initView();
        play();
        addPlayCount();
    }
    @OnClick({R.id.tv_send,R.id.back})
    void OnCLick(View view){
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.tv_send:
                String comment=et_comment.getEditableText().toString();
                if(!TextUtils.isEmpty(comment)){
                    addComment(comment);
                }
                break;
        }
    }
    private void addComment(String comment){
        Map<String,Object> params=new HashMap<>();
        params.put("comment",comment);
        params.put("typeStatus","1");
        params.put("videoId",mCallbackVideo.getId());
        params.put("custId",MyApplication.getUserInfo().getCustId());
        String json=new Gson().toJson(params);
        NetUtils.getInstance().post(Api.Video.addParentVideoComment, json, MyApplication.token, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                LogUtils.e(response);
                et_comment.setText("");
                ToastUtils.showShort("评价成功");
                mCommentFragment.refresh();
            }

            @Override
            public void onError(String error) {

            }
        });
    }
    void play() {
        videoplayer.setUp(mCallbackVideo.getVideoUrl()
                , JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, mCallbackVideo.getVideoTittle());
        Glide.with(this).load(mCallbackVideo.getVideoPhoto()).into( videoplayer.thumbImageView);
        videoplayer.startButton.performClick();
    }
    private void addPlayCount(){
        Map<String,Object> params=new HashMap<>();
        params.put("id",mCallbackVideo.getId());
        String json=new Gson().toJson(params);
        NetUtils.getInstance().post( Api.Video.palyVideo, json,MyApplication.token, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                LogUtils.e(response);
            }

            @Override
            public void onError(String error) {

            }
        });
    }
    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() { //选择适当的声明周期释放
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }

    private void initView() {
        tv_title.setText(mCallbackVideo.getVideoTittle());
        tv_num.setText(""+mCallbackVideo.getPlayNum());
        initTab();
    }

    private void initTab() {
        mFragments = new ArrayList<>();
        mCommentFragment=CommentFragment.newInstance(mCallbackVideo.getId());
        mFragments.add(mCommentFragment);
        mFragments.add(X5DetailFragment.newInstance(mCallbackVideo.getVideoDescription()));
        mTabLayout.setViewPager(viewPager, mTitles, this, mFragments);
    }
}
