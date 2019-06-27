package com.yunkang.chat.mine.activity;

import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.azhon.appupdate.config.UpdateConfiguration;
import com.azhon.appupdate.listener.OnButtonClickListener;
import com.azhon.appupdate.listener.OnDownloadListener;
import com.azhon.appupdate.manager.DownloadManager;
import com.blankj.utilcode.util.AppUtils;
import com.google.gson.Gson;
import com.yunkang.chat.MyApplication;
import com.yunkang.chat.R;
import com.yunkang.chat.api.Api;
import com.yunkang.chat.http.HttpProxy;
import com.yunkang.chat.http.ICallBack;
import com.yunkang.chat.net.NetUtils;
import com.yunkang.chat.net.OnMessageReceived;
import com.yunkang.chat.start.activity.MainActivity;
import com.yunkang.chat.start.model.Version;
import com.yunkang.chat.tools.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VersioninfoActivity extends AppCompatActivity {
    DownloadManager manager;
    @BindView(R.id.tv_status)
    TextView tv_status;
    @BindView(R.id.tv_update)
    TextView tv_update;
    @BindView(R.id.tv_content)
    TextView tv_content;
    Version version=new Version();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_versioninfo);
        ButterKnife.bind(this);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.tv_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUpdate2(version);
            }
        });
        version.setParkey("1.0");
        getNewVersion();
    }
    private void startUpdate2(Version version) {
        if(TextUtils.isEmpty(version.getRemarks())||!version.getRemarks().contains("apk")){
            ToastUtil.showToast(this,"apk链接不正确");
            //version.setRemarks("https://raw.githubusercontent.com/azhon/AppUpdate/master/apk/appupdate.apk");
            return;
        }
        try {
            DownloadManager.getInstance().release();

        } catch (Exception e) {
            e.printStackTrace();
        }
        manager = DownloadManager.getInstance(this);
        manager.setApkName("appupdate.apk")
                .setApkUrl(version.getRemarks())
                .setDownloadPath(Environment.getExternalStorageDirectory() + "/AppUpdate")
                .setSmallIcon(R.mipmap.ic_logo)
                .download();
    }
    public static int compareAppVersion(String newVersion, String oldVersion) {
        if (newVersion == null || oldVersion == null) {
            throw new RuntimeException("版本号不能为空");
        }
        // 注意此处为正则匹配，不能用.
        String[] versionArray1 = newVersion.split("\\.");
        String[] versionArray2 = oldVersion.split("\\.");
        int idx = 0;
        // 取数组最小长度值
        int minLength = Math.min(versionArray1.length, versionArray2.length);
        int diff = 0;
        // 先比较长度，再比较字符
        while (idx < minLength
                && (diff = versionArray1[idx].length() - versionArray2[idx].length()) == 0
                && (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0) {
            ++idx;
        }
        // 如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大
        diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;
        return diff;
    }
    private  void getNewVersion(){
//        Map<String,Object> params=new HashMap<>();
//        params.put("type","2");
//        HttpProxy.obtain().get(Api.Veision.getVersion, params, new ICallBack() {
//            @Override
//            public void OnSuccess(String result) {
//                Log.e("version",result);
//                try {
//                    JSONObject jsonObject=new JSONObject(result);
//                    jsonObject=jsonObject.getJSONObject("data");
//                    JSONArray data=jsonObject.getJSONArray("data");
//
//                    for (int i = 0; i <data.length() ; i++) {
//                        if(i==0){
//                            JSONObject item=data.getJSONObject(i);
//                            version =new Gson().fromJson(item.toString(),Version.class);
//                            break;
//                        }
//                    }
//                    if(compareAppVersion(version.getParkey(), AppUtils.getAppVersionName())>0){
//                        tv_status.setText("有新版本，版本号为"+version.getParkey());
//                        tv_content.setText(version.getParvalue());
//                        tv_update.setEnabled(true);
//                        //startUpdate2(version);
//                    }else{
//                        tv_status.setText("已是最新版本");
//                        tv_content.setText(version.getParvalue());
//                        tv_update.setEnabled(false);
//                        tv_update.setText("暂无更新");
//                        tv_update.setBackgroundResource(R.color.color_ccc);
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//            @Override
//            public void onFailure(String error) {
//
//            }
//        });

        Map<String,String> params=new HashMap<>();
        params.put("type","2");
        NetUtils.getInstance().get(MyApplication.token, Api.Veision.getVersion, params, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                Log.e("version",response);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    jsonObject=jsonObject.getJSONObject("data");
                    JSONArray data=jsonObject.getJSONArray("data");

                    for (int i = 0; i <data.length() ; i++) {
                        if(i==0){
                            JSONObject item=data.getJSONObject(i);
                            version =new Gson().fromJson(item.toString(),Version.class);
                            break;
                        }
                    }
                    if(compareAppVersion(version.getParkey(), AppUtils.getAppVersionName())>0){
                        tv_status.setText("有新版本，版本号为"+version.getParkey());
                        tv_content.setText(version.getParvalue());
                        tv_update.setEnabled(true);
                        tv_update.setText("立即更新");
                        //startUpdate2(version);
                    }else{
                        tv_status.setText("已是最新版本");
                        tv_content.setText(version.getParvalue());
                        tv_update.setEnabled(false);
                        tv_update.setText("暂无更新");
                        tv_update.setBackgroundResource(R.color.color_ccc);
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
