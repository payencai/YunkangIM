package com.yunkang.chat.mine.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;
import com.yunkang.chat.MyApplication;
import com.yunkang.chat.R;
import com.yunkang.chat.api.Api;
import com.yunkang.chat.constant.Constant;
import com.yunkang.chat.h5.H5CommomActivity;
import com.yunkang.chat.http.HttpProxy;
import com.yunkang.chat.http.ICallBack;
import com.yunkang.chat.net.NetUtils;
import com.yunkang.chat.net.OnMessageReceived;
import com.yunkang.chat.start.model.UserInfo;
import com.yunkang.chat.tools.BitmapUtil;
import com.yunkang.chat.tools.MathUtil;
import com.yunkang.chat.tools.MyGlideEngine;
import com.yunkang.chat.tools.PicassoImageLoader;
import com.yunkang.chat.tools.ToastUtil;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_nickname)
    TextView tv_nickname;
    @BindView(R.id.tv_phone)
    TextView tv_phone;
    @BindView(R.id.tv_alipay)
    TextView tv_alipay;
    @BindView(R.id.rl_phone)
    RelativeLayout rl_phone;
    @BindView(R.id.rl_alipay)
    RelativeLayout rl_alipay;
    @BindView(R.id.rl_head)
    RelativeLayout rl_head;
    @BindView(R.id.rl_realname)
    RelativeLayout rl_realname;
    @BindView(R.id.rl_nick)
    RelativeLayout rl_nick;
    @BindView(R.id.rl_bank)
    RelativeLayout rl_bank;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        initView();
    }

    private void updateHead(String head) {
//        Map<String, Object> params = new HashMap<>();
//        params.put("headPortrait", head);
//        params.put("id", MyApplication.getUserInfo().getId());
//        // params.put("type", MyApplication.getUserInfo().getType());
//        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
//        String json = gson.toJson(params);
//        Log.e("json", json);
//        HttpProxy.obtain().post(Api.User.updateUserByUser, MyApplication.token, json, new ICallBack() {
//            @Override
//            public void OnSuccess(String result) {
//                if (dialog.isShowing())
//                    dialog.dismiss();
//
//                Log.e("upload", result);
//                try {
//                    JSONObject object = new JSONObject(result);
//                    int resultCode = object.getInt("code");
//                    if (resultCode == 0) {
//                        ToastUtil.showToast(UserInfoActivity.this, "更新成功");
//                    } else {
//                        String msg = object.getString("message");
//                        ToastUtil.showToast(UserInfoActivity.this, msg);
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(String error) {
//                if (dialog.isShowing())
//                    dialog.dismiss();
//            }
//        });

        Map<String, String> params = new HashMap<>();
        params.put("headPortrait", head);
        params.put("id", MyApplication.getUserInfo().getId());
        NetUtils.getInstance().post(Api.User.updateUserByUser, params,MyApplication.token, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                if (dialog.isShowing())
                    dialog.dismiss();

                Log.e("upload", response);
                try {
                    JSONObject object = new JSONObject(response);
                    int resultCode = object.getInt("code");
                    if (resultCode == 0) {
                        ToastUtil.showToast(UserInfoActivity.this, "更新成功");
                    } else {
                        String msg = object.getString("message");
                        ToastUtil.showToast(UserInfoActivity.this, msg);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                if (dialog.isShowing())
                    dialog.dismiss();
            }
        });

    }
    KProgressHUD dialog;
    private void showLoading(String data){
        dialog= KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(data)
                .setCancellable(new DialogInterface.OnCancelListener()
                {
                    @Override public void onCancel(DialogInterface
                                                           dialogInterface)
                    {

                    }
                });
        dialog.show();

    }
    private void initView() {

        tv_title.setText("个人信息");
        iv_back.setOnClickListener(this);
        rl_alipay.setOnClickListener(this);
        rl_phone.setOnClickListener(this);
        rl_head.setOnClickListener(this);
        rl_realname.setOnClickListener(this);
        rl_nick.setOnClickListener(this);
        rl_bank.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
    }
    private void getUserInfo() {
//        HttpProxy.obtain().get(Api.User.getUserDtoByToken, MyApplication.token, new ICallBack() {
//            @Override
//            public void OnSuccess(String result) {
//                Log.e("getUserDtoByToken", result);
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    int code = jsonObject.getInt("code");
//                    if (code == 0) {
//                        JSONObject data = jsonObject.getJSONObject("data");
//                        data = data.getJSONObject("data");
//                        UserInfo mUserInfo = new Gson().fromJson(data.toString(), UserInfo.class);
//                        MyApplication.setUserInfo(mUserInfo);
//                        tv_alipay.setText(mUserInfo.getZfbAccount());
//                        tv_nickname.setText(mUserInfo.getNickName());
//                        tv_name.setText(mUserInfo.getName());
//                        tv_phone.setText(mUserInfo.getTelephone());
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

        NetUtils.getInstance().get(MyApplication.token, Api.User.getUserDtoByToken, new HashMap<>(), new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                Log.e("getUserDtoByToken", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        data = data.getJSONObject("data");
                        UserInfo mUserInfo = new Gson().fromJson(data.toString(), UserInfo.class);
                        MyApplication.setUserInfo(mUserInfo);
                        tv_alipay.setText(mUserInfo.getZfbAccount());
                        tv_nickname.setText(mUserInfo.getNickName());
                        tv_name.setText(mUserInfo.getName());
                        tv_phone.setText(mUserInfo.getTelephone());
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
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_bank:
                Intent intent4 = new Intent(UserInfoActivity.this, H5CommomActivity.class);
                intent4.putExtra("url", Constant.Commom.bandlist
                        + MyApplication.token);
                startActivity(intent4);
                break;
            case R.id.rl_nick:

                startActivityForResult(new Intent(UserInfoActivity.this, UpdateNicknameActivity.class), 3);
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_realname:
                Intent intent3 = new Intent(UserInfoActivity.this, H5CommomActivity.class);
                intent3.putExtra("url", Constant.Commom.realName
                        + MyApplication.token);
                startActivity(intent3);
                break;
            case R.id.rl_head:
                RxPermissions rxPermissions = new RxPermissions(UserInfoActivity.this);
                rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                        .subscribe(new Observer<Boolean>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Boolean aBoolean) {
                                if (aBoolean) {
                                    openPhoto(200);
//                                    Toast.makeText(UserInfoActivity.this, "已获取权限，可以干想干的咯", Toast.LENGTH_LONG)
//                                            .show();
                                } else {
                                    //只有用户拒绝开启权限，且选了不再提示时，才会走这里，否则会一直请求开启
                                    Toast.makeText(UserInfoActivity.this, "主人，我被禁止啦，去设置权限设置那把我打开哟", Toast.LENGTH_LONG)
                                            .show();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });

                break;
            case R.id.rl_phone:
                //startActivity(new Intent(UserInfoActivity.this,UpdateBindActivity.class));
                break;
            case R.id.rl_alipay:
                String alipay=MyApplication.getUserInfo().getZfbAccount();
                if(TextUtils.isEmpty(alipay)){
                    Intent intent5 = new Intent(UserInfoActivity.this, H5CommomActivity.class);
                    intent5.putExtra("url", Constant.Commom.bandZFB
                            + MyApplication.token);
                    startActivity(intent5);
                }else{
                    ToastUtil.showToast(UserInfoActivity.this,"你已经绑定支付宝了！");
                }

               // startActivity(new Intent(UserInfoActivity.this, BindAlipayActivity.class));
                break;
        }
    }
    private void openPhoto(int code) {
        PictureSelector.create(UserInfoActivity.this)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .enablePreviewAudio(false) // 是否可播放音频 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                .enableCrop(false)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
                .withAspectRatio(16, 9)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .cropCompressQuality(90)// 裁剪压缩质量 默认90 int
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .forResult(code);//结果回调onActivityResult code
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            Log.e("result",requestCode+"");
            if (data != null) {
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                String url = BitmapUtil.compressImage(selectList.get(0).getPath());
                File file = new File(url);
                Log.e("result",selectList.get(0).getPath());
                if (selectList != null && selectList.size() > 0) {
                    upImage(Api.User.uploadImage, file);
                }
            } else {
                //Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == 3) {
            tv_nickname.setText(MyApplication.getUserInfo().getNickName());
        }
    }

    public void upImage(String url, File file) {
        showLoading("上传中");
        OkHttpClient mOkHttpClent = new OkHttpClient();

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", "image",
                        RequestBody.create(MediaType.parse("image/png"), file));
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Call call = mOkHttpClent.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(UserInfoActivity.this, "上传超时");
                        if (dialog.isShowing())
                            dialog.dismiss();
                    }
                });

                Log.e("upload", "onResponse: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.e("upload", "onResponse: " + string);
                try {
                    JSONObject object = new JSONObject(string);
                    int resultCode = object.getInt("code");
                    JSONObject jsonObject = object.getJSONObject("data");
                    String data = jsonObject.getString("data");
                    if (resultCode == 0)
                        updateHead(data);
                    else{
                        if (dialog.isShowing())
                            dialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}