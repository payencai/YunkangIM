package com.yunkang.chat.h5;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iapppay.interfaces.callback.IPayResultCallback;
import com.iapppay.sdk.main.IAppPay;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.qiyukf.unicorn.api.ConsultSource;
import com.qiyukf.unicorn.api.Unicorn;
import com.soundcloud.android.crop.Crop;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;
import com.yunkang.chat.MyApplication;
import com.yunkang.chat.R;
import com.yunkang.chat.api.Api;
import com.yunkang.chat.http.HttpProxy;
import com.yunkang.chat.http.ICallBack;
import com.yunkang.chat.mine.activity.MyOrderActivity;
import com.yunkang.chat.net.NetUtils;
import com.yunkang.chat.net.OnMessageReceived;
import com.yunkang.chat.start.activity.MainActivity;
import com.yunkang.chat.tools.BitmapUtil;
import com.yunkang.chat.tools.MyGlideEngine;
import com.yunkang.chat.tools.ToastUtil;
import com.yunkang.chat.tools.base.StringUtils;
import com.yunkang.chat.view.X5WebView;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class H5CommomActivity extends AppCompatActivity {


    private final static int FILE_CHOOSER_RESULT_CODE = 10000;
    X5WebView mX5WebView;
    int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_renzhen);
        mX5WebView = findViewById(R.id.wb_shop);
        url = getIntent().getStringExtra("url");

        IAppPay.init(this, IAppPay.PORTRAIT, "3016095896", "");
        initWebView();

    }

    LoadingDialog mDialog;
    Uri outputUri;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 188 && data != null) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            String url = selectList.get(0).getPath();
            Luban.with(this)
                    .load(url)
                    .ignoreBy(100)
                    .filter(new CompressionPredicate() {
                        @Override
                        public boolean apply(String path) {
                            return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                        }
                    })
                    .setCompressListener(new OnCompressListener() {
                        @Override
                        public void onStart() {
                            // TODO 压缩开始前调用，可以在方法内启动 loading UI
                        }

                        @Override
                        public void onSuccess(File file) {
                            if (type == 1) {
                                upImage(Api.User.uploadImage, file);
                                //Crop.of(inputUri, outputUri).asSquare().start(H5CommomActivity.this);
                            } else if (type == 2) {
                                Log.e("orc", "orc");
                                upOrcImage(Api.User.uploadOCRImage, file);
                            }
                            // TODO 压缩成功后调用，返回压缩后的图片文件
                        }

                        @Override
                        public void onError(Throwable e) {
                            // TODO 当压缩过程出现问题时调用
                        }
                    }).launch();


        }
//        if (requestCode == 188 && data != null) {
//            List<String> result = Matisse.obtainPathResult(data);
//            String url = BitmapUtil.compressImage(result.get(0));
//            File file = new File(url);
//            if (type == 1) {
//                file = new File(result.get(0));
//                upImage(Api.User.uploadImage, file);
//                //Crop.of(inputUri, outputUri).asSquare().start(H5CommomActivity.this);
//            } else if (type == 2) {
//                Log.e("orc", "orc");
//                upOrcImage(Api.User.uploadOCRImage, file);
//            }
//
//
//            // getPicture("gggg");
//            return;
//        }
        if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
            String path = outputUri.getPath();
            Luban.with(this)
                    .load(path)
                    .ignoreBy(100)
                    .filter(new CompressionPredicate() {
                        @Override
                        public boolean apply(String path) {
                            return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                        }
                    })
                    .setCompressListener(new OnCompressListener() {
                        @Override
                        public void onStart() {
                            // TODO 压缩开始前调用，可以在方法内启动 loading UI
                        }

                        @Override
                        public void onSuccess(File file) {
                            upImage(Api.User.uploadImage, file);
                            // TODO 压缩成功后调用，返回压缩后的图片文件
                        }

                        @Override
                        public void onError(Throwable e) {
                            // TODO 当压缩过程出现问题时调用
                        }
                    }).launch();

        }

    }

    private void openPhoto(int code) {
        PictureSelector.create(H5CommomActivity.this)
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


    private void showFailDialog() {
        Dialog dialog = new Dialog(this, R.style.common_dialog_style);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_logout, null);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        TextView tv_confirm = view.findViewById(R.id.tv_confirm);
        TextView tv_cancel = view.findViewById(R.id.tv_cancel);
        TextView tv_title = view.findViewById(R.id.tv_title);
        tv_cancel.setVisibility(View.GONE);

        tv_title.setText("未识别出身份证号码，请手动输入");
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        Window window = dialog.getWindow();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();

        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.width = (int) (display.getWidth() * 0.7);
        window.setAttributes(layoutParams);

    }

    public void upOrcImage(String url, File file) {
        mDialog = new LoadingDialog(this).setLoadingText("上传中");
        mDialog.show();
        NetUtils.getInstance().upLoadImage(url, file, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                if (mDialog != null)
                    mDialog.close();
                Log.e("upload", "onResponse: " + response);
                try {
                    JSONObject object = new JSONObject(response);
                    int resultCode = object.getInt("code");
                    JSONObject jsonObject = object.getJSONObject("data");
                    JSONObject data = jsonObject.getJSONObject("data");
                    String id = data.getString("idcardNo");
                    if (TextUtils.isEmpty(id)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showFailDialog();
                            }
                        });

                    }
                    Map<String, Object> parmas = new HashMap<>();
                    parmas.put("type", 2);
                    parmas.put("data", id);
                    String json = new Gson().toJson(parmas);
                    getPicture(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {

                if (mDialog != null)
                    mDialog.close();

                ToastUtil.showToast(H5CommomActivity.this, "上传超时");
            }
        });

    }

    public static String object2json(Object obj) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        return gson.toJson(obj);
    }

    public void upImage(String url, File file) {
        mDialog = new LoadingDialog(this).setLoadingText("上传中");
        mDialog.show();
        NetUtils.getInstance().upLoadImage(url, file, new OnMessageReceived() {
            @Override
            public void onSuccess(String response) {
                String string = response;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mDialog != null)
                            mDialog.close();
                    }
                });
                Log.e("upload", "onResponse: " + string);
                try {
                    JSONObject object = new JSONObject(string);
                    int resultCode = object.getInt("code");
                    JSONObject jsonObject = object.getJSONObject("data");
                    String data = jsonObject.getString("data");
                    try {
                        data = URLEncoder.encode(data, "UTF-8");
                        Map<String, Object> parmas = new HashMap<>();
                        parmas.put("type", 1);
                        parmas.put("url", data);
                        String json = object2json(parmas);
                        getPicture(json);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mDialog != null)
                                    mDialog.close();
                            }
                        });
                        ToastUtil.showToast(H5CommomActivity.this, "上传超时");
                    }
                });

                //Log.e("upload", "onResponse: " + e.getMessage());
            }
        });

    }


    @Override
    public void onBackPressed() {
        if (mX5WebView.canGoBack()) {
            mX5WebView.goBack();
        } else {
            finish();
        }
    }

    private void initWebView() {
//
        WebSettings settings = mX5WebView.getSettings();           //和系统webview一样
//        settings.setJavaScriptEnabled(true);                    //支持Javascript 与js交互
//        settings.setJavaScriptCanOpenWindowsAutomatically(true);//支持通过JS打开新窗口
//        settings.setAllowFileAccess(true);
//
//        //设置可以访问文件
//        settings.setSupportZoom(true);                          //支持缩放
//        settings.setBuiltInZoomControls(true);                  //设置内置的缩放控件
//        settings.setUseWideViewPort(true);                      //自适应屏幕
//        settings.setSupportMultipleWindows(true);               //多窗口
//        settings.setDefaultTextEncodingName("utf-8");            //设置编码格式
//        settings.setAppCacheEnabled(true);
//        settings.setDomStorageEnabled(true);
//        settings.setAppCacheMaxSize(Long.MAX_VALUE);
//        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //缓存模式
        String agent = settings.getUserAgentString();
        String value = "/ykversion:" + AppUtils.getAppVersionName() + "YKAndroidBS";
        settings.setUserAgentString(agent + value);
        Log.e("agent", settings.getUserAgentString());
        mX5WebView.setWebViewClient(new WebViewClient() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);

            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                webView.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
//                super.onReceivedSslError(webView, sslErrorHandler, sslError);
                sslErrorHandler.proceed();//忽略SSL证书错误
            }
        });
        mX5WebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onJsAlert(WebView webView, String s, String s1, JsResult jsResult) {
                return super.onJsAlert(webView, s, s1, jsResult);
            }

            @Override
            public void onReceivedTitle(WebView webView, String s) {
                super.onReceivedTitle(webView, s);
            }

            @Override
            public void onProgressChanged(WebView webView, int progress) {
                super.onProgressChanged(webView, progress);

            }
        });
        mX5WebView.addJavascriptInterface(new Pay(), "Go");//AndroidtoJS类对象映射到js的test对象
        mX5WebView.loadUrl(url);
    }


    private void getPayParams(String orderId) {
        Map<String, Object> params = new HashMap<>();
        params.put("orderId", orderId);
        HttpProxy.obtain().get(Api.Order.payment, params, MyApplication.token, new ICallBack() {
            @Override
            public void OnSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject data = jsonObject.getJSONObject("data");
                    String params = data.getString("data");
                    pay(params);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(String error) {

            }
        });
    }


    private void pay(String id) {
        String params = "transid=" + id + "&appid=3016095896";
        IAppPay.startPay(this, params, new IPayResultCallback() {
            @Override
            public void onPayResult(int resultCode, String signValue, String resultInfo) {
                switch (resultCode) {
                    case IAppPay.PAY_SUCCESS:
                        //请尽量采用服务端验签方式  ，以下验签方法不建议
                        Toast.makeText(H5CommomActivity.this, "支付成功", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(H5CommomActivity.this, MyOrderActivity.class);
                        intent.putExtra("type", 2);
                        startActivity(intent);
                        finish();
                        break;
                    default:
                        Toast.makeText(H5CommomActivity.this, resultInfo, Toast.LENGTH_LONG).show();
                        break;
                }
                //Log.d(TAG, "requestCode:" + resultCode + ",signvalue:" + signValue + ",resultInfo:" + resultInfo);
            }
        });
    }

    public class Pay {


        // 打开浏览器
        @JavascriptInterface
        public void openBrowser(String url) {
            Log.e("openBrowser", url);
            if (StringUtils.isEmpty(url)) {
                ToastUtil.showToast(H5CommomActivity.this, "地址为空");
                return;
            }
            if (!url.startsWith("http://")) {
                ToastUtil.showToast(H5CommomActivity.this, "地址格式有误");
                return;
            }
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            startActivity(intent);
//            getPayParams(order);
            // return order;
        }

        // 有返回结果
        @JavascriptInterface
        public void payMent(String order) {
            Log.e("payMent", order);
            getPayParams(order);
            // return order;
        }

        @JavascriptInterface
        public void browser(String url) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }

        @JavascriptInterface
        public void kefu() {
            String title = "客服";


            ConsultSource source = new ConsultSource("msg", "客户", "custom information string");
            Unicorn.openServiceActivity(H5CommomActivity.this, title, source);


            // startActivity(new Intent(H5CommomActivity.this, ServiceActivity.class));
        }

        // 返回结果
        @JavascriptInterface
        public void picture() {
            //test();
            //testJS();
            //openImageChooserActivity();
            //finish();
            type = 1;
            openPhoto(188);
            //selectImage(188);
        }

        @JavascriptInterface
        public void Orc() {
            //test();
            //testJS();
            //openImageChooserActivity();
            //finish();
            type = 2;
            openPhoto(188);
        }

        // 返回结果
        @JavascriptInterface
        public void back() {
            finish();
        }

        @JavascriptInterface
        public void balancePay() {
            Intent intent = new Intent(H5CommomActivity.this, MyOrderActivity.class);
            intent.putExtra("type", 2);
            startActivity(intent);
            finish();
        }

        @JavascriptInterface
        public void share(String json) {
            Log.e("link", json);
            try {
                JSONObject jsonObject = new JSONObject(json);
                String title = jsonObject.getString("title");
                String picture = jsonObject.getString("picture");
                String content = jsonObject.getString("content");
                String link = jsonObject.getString("link");
                //showShareDialog();
                openShare(title, link, content, picture);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // finish();
        }

        @JavascriptInterface
        public void payShare(String json) {
            Log.e("link", json);
            try {
                JSONObject jsonObject = new JSONObject(json);
                String title = jsonObject.getString("title");
                String picture = jsonObject.getString("picture");
                String content = jsonObject.getString("content");
                String link = jsonObject.getString("link");
                //showShareDialog();
                firendShare(title, link, content, picture);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // finish();
        }
    }

    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 判断qq是否可用
     *
     * @param context
     * @return
     */
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

    private void selectImage(int code) {
        Matisse.from(this)
                .choose(MimeType.ofImage())//图片类型
                .countable(false)//true:选中后显示数字;false:选中后显示对号
                .maxSelectable(1)//可选的最大数
                .thumbnailScale(0.85f)
                .capture(true)//选择照片时，是否显示拍照
                .captureStrategy(new CaptureStrategy(true, "com.example.xx.fileprovider"))//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                .imageEngine(new MyGlideEngine())//图片加载引擎
                .forResult(code);//

    }

    private void getPicture(String json) {
        Log.e("json", json);
        mX5WebView.post(new Runnable() {
            @Override
            public void run() {
                mX5WebView.loadUrl("javascript:window.getPicture('" + json + "')");
            }
        });
    }

    UMShareListener mUMShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {

        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {

        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {

        }
    };

    private void firendShare(String title, String WebUrl, String description, String image) {
        new ShareAction(this)
                .setDisplayList(SHARE_MEDIA.WEIXIN)
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                        UMWeb web = new UMWeb(WebUrl);//连接地址
                        web.setTitle(title);//标题
                        if (TextUtils.isEmpty(image)) {
                            web.setThumb(new UMImage(H5CommomActivity.this, R.mipmap.ic_logo));
                        } else {
                            web.setThumb(new UMImage(H5CommomActivity.this, image));
                        }
                        if (TextUtils.isEmpty(description)) {
                            web.setDescription("点击查看详情");//描述
                        } else {
                            web.setDescription(description);//描述
                        }

                        if (share_media == SHARE_MEDIA.QQ) {
                            Log.e("result", "点击QQ");
                            if (!isQQClientAvailable(H5CommomActivity.this)) {
                                ToastUtil.showToast(H5CommomActivity.this, "未安装QQ客户端");
                            }
                            new ShareAction(H5CommomActivity.this).setPlatform(SHARE_MEDIA.QQ)
                                    .withMedia(web)
                                    .setCallback(mUMShareListener)
                                    .share();
                        } else if (share_media == SHARE_MEDIA.WEIXIN) {
                            if (!isWeixinAvilible(H5CommomActivity.this)) {
                                ToastUtil.showToast(H5CommomActivity.this, "未安装微信客户端");
                            }
                            Log.e("result", "点击微信");
                            new ShareAction(H5CommomActivity.this).setPlatform(SHARE_MEDIA.WEIXIN)
                                    .withMedia(web)
                                    .setCallback(mUMShareListener)
                                    .share();
                        } else if (share_media == SHARE_MEDIA.QZONE) {
                            if (!isQQClientAvailable(H5CommomActivity.this)) {
                                ToastUtil.showToast(H5CommomActivity.this, "未安装QQ客户端");

                            }

                            new ShareAction(H5CommomActivity.this).setPlatform(SHARE_MEDIA.QZONE)
                                    .withMedia(web)
                                    .setCallback(mUMShareListener)
                                    .share();
                        } else if (share_media == SHARE_MEDIA.WEIXIN_CIRCLE) {
                            if (!isWeixinAvilible(H5CommomActivity.this)) {
                                ToastUtil.showToast(H5CommomActivity.this, "未安装微信客户端");
                            }
                            new ShareAction(H5CommomActivity.this).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                                    .withMedia(web)
                                    .setCallback(mUMShareListener)
                                    .share();
                        }
                    }
                }).open();
    }

    private void openShare(String title, String WebUrl, String description, String image) {
        new ShareAction(this)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                        UMWeb web = new UMWeb(WebUrl);//连接地址
                        web.setTitle(title);//标题
                        if (TextUtils.isEmpty(image)) {
                            web.setThumb(new UMImage(H5CommomActivity.this, R.mipmap.ic_logo));
                        } else {
                            web.setThumb(new UMImage(H5CommomActivity.this, image));
                        }
                        if (TextUtils.isEmpty(description)) {
                            web.setDescription("点击查看详情");//描述
                        } else {
                            web.setDescription(description);//描述
                        }

                        if (share_media == SHARE_MEDIA.QQ) {
                            Log.e("result", "点击QQ");
                            if (!isQQClientAvailable(H5CommomActivity.this)) {
                                ToastUtil.showToast(H5CommomActivity.this, "未安装QQ客户端");
                            }
                            new ShareAction(H5CommomActivity.this).setPlatform(SHARE_MEDIA.QQ)
                                    .withMedia(web)
                                    .setCallback(mUMShareListener)
                                    .share();
                        } else if (share_media == SHARE_MEDIA.WEIXIN) {
                            if (!isWeixinAvilible(H5CommomActivity.this)) {
                                ToastUtil.showToast(H5CommomActivity.this, "未安装微信客户端");
                            }
                            Log.e("result", "点击微信");
                            new ShareAction(H5CommomActivity.this).setPlatform(SHARE_MEDIA.WEIXIN)
                                    .withMedia(web)
                                    .setCallback(mUMShareListener)
                                    .share();
                        } else if (share_media == SHARE_MEDIA.QZONE) {
                            if (!isQQClientAvailable(H5CommomActivity.this)) {
                                ToastUtil.showToast(H5CommomActivity.this, "未安装QQ客户端");

                            }

                            new ShareAction(H5CommomActivity.this).setPlatform(SHARE_MEDIA.QZONE)
                                    .withMedia(web)
                                    .setCallback(mUMShareListener)
                                    .share();
                        } else if (share_media == SHARE_MEDIA.WEIXIN_CIRCLE) {
                            if (!isWeixinAvilible(H5CommomActivity.this)) {
                                ToastUtil.showToast(H5CommomActivity.this, "未安装微信客户端");
                            }
                            new ShareAction(H5CommomActivity.this).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                                    .withMedia(web)
                                    .setCallback(mUMShareListener)
                                    .share();
                        }
                    }
                }).open();
    }


    private void showShareDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_share_muti, null);
        Dialog dialog = new Dialog(this, R.style.common_dialog_style);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        dialog.setContentView(view);
        dialog.show();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        window.setAttributes(layoutParams);
    }


    String url = "";
}
