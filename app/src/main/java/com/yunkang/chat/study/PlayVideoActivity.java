package com.yunkang.chat.study;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.yunkang.chat.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

public class PlayVideoActivity extends AppCompatActivity {
    String url;
    @BindView(R.id.videoplayer)
    JZVideoPlayerStandard videoplayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        ButterKnife.bind(this);
        url=getIntent().getStringExtra("url");
        Log.e("url",url);
        play();
    }
    void play() {
        videoplayer.setUp(url
                , JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "回播");
        videoplayer.startButton.performClick();
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
}
