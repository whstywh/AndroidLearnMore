package com.wh.h5webview;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import java.util.HashMap;
import java.util.Map;


public class H5WebViewActivity extends AppCompatActivity {

    private WebView mWebView;

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h5_web_view);

        FrameLayout frameLayout = findViewById(R.id.web_layout);
        mWebView = new WebView(getApplicationContext());
        frameLayout.addView(mWebView);

        WebSettings webSettings = mWebView.getSettings();

        webSettings.setJavaScriptEnabled(true);//启用 JavaScript

        //自定义用户代理字符串，然后在网页中查询自定义用户代理，以验证请求网页的客户端实际上是您的Android 应用。
        if (!webSettings.getUserAgentString().contains("h5webview")) {
            webSettings.setUserAgentString(webSettings.getUserAgentString() + "h5webview");
        }

        webSettings.setJavaScriptEnabled(true); //是否开启JS支持
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //是否允许JS打开新窗口

        webSettings.setUseWideViewPort(true); //缩放至屏幕大小
        webSettings.setLoadWithOverviewMode(true); //缩放至屏幕大小
        webSettings.setSupportZoom(true); //是否支持缩放
        webSettings.setBuiltInZoomControls(true); //是否支持缩放变焦，前提是支持缩放
        webSettings.setDisplayZoomControls(false); //是否隐藏缩放控件

        webSettings.setAllowFileAccess(true); //是否允许访问文件
        //关闭无痕模式
        webSettings.setDomStorageEnabled(true); //是否节点缓存
        webSettings.setDatabaseEnabled(true); //是否数据缓存
        webSettings.setAppCacheEnabled(true); //是否应用缓存
//        webSettings.setAppCachePath(getExternalCacheDir().getPath()); //设置缓存路径

        webSettings.setMediaPlaybackRequiresUserGesture(false); //是否要手势触发媒体
        webSettings.setStandardFontFamily("sans-serif"); //设置字体库格式
        webSettings.setFixedFontFamily("monospace"); //设置字体库格式
        webSettings.setSansSerifFontFamily("sans-serif"); //设置字体库格式
        webSettings.setSerifFontFamily("sans-serif"); //设置字体库格式
        webSettings.setCursiveFontFamily("cursive"); //设置字体库格式
        webSettings.setFantasyFontFamily("fantasy"); //设置字体库格式
        webSettings.setTextZoom(100); //设置文本缩放的百分比
        webSettings.setMinimumFontSize(8); //设置文本字体的最小值(1~72)
        webSettings.setDefaultFontSize(16); //设置文本字体默认的大小

        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING); //布局规则
        webSettings.setLoadsImagesAutomatically(true); //是否自动加载图片
        webSettings.setDefaultTextEncodingName("UTF-8"); //设置编码格式
        webSettings.setNeedInitialFocus(true); //是否需要获取焦点
        webSettings.setGeolocationEnabled(true); //设置开启定位功能
        webSettings.setBlockNetworkLoads(true); //是否从网络获取资源

        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new MyWebChromeClient());
        mWebView.loadUrl("https://www.baidu.com");

        //jsToAndroid
        mWebView.addJavascriptInterface(this, "h5webview");
//        mWebview.removeJavascriptInterface("h5webview");
    }


    private void AndroidToJs() {
        // 调用有参无返回值的函数
        mWebView.loadUrl("javascript:setter('" + "name" + "');");
        //调用无参有返回值的函数
//        mWebview.evaluateJavascript("getter()", s //System.out.println("my name is "+s));
    }

    @JavascriptInterface
    public void jsToAndroid() {

    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.loadUrl("about:blank");
            ViewParent parent = mWebView.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(mWebView);
            }
            mWebView.stopLoading();
            mWebView.getSettings().setJavaScriptEnabled(false);
            mWebView.clearHistory();
            mWebView.clearCache(true);
            mWebView.removeAllViewsInLayout();
            mWebView.removeAllViews();
            mWebView.setWebChromeClient(null);
            mWebView.setWebViewClient(null);
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }

    static class MyWebViewClient extends WebViewClient {

        /*页面开始加载时调用，显示加载进度条*/
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        /*页面完成加载时调用，隐藏加载进度条*/
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        /*WebView加载url默认会调用系统的浏览器，通过重写该方法，实现在当前应用内完成页面加载。*/
        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, String url) {

            if (url.contains("wx.tenpay")) {
                Map<String, String> extraHeaders = new HashMap<>();
                if (url.contains("redirect_url")) {
                    extraHeaders.put("Referer", url.split("&redirect_url=")[1]);
                }
                view.loadUrl(url, extraHeaders);
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        /*页面加载发生错误时调用，可以跳转到自定义的错误提醒页面*/
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

        /*覆盖按键默认的响应事件，可以根据自身的需求在点击某些按键时加入相应的逻辑。*/
        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            return super.shouldOverrideKeyEvent(view, event);
        }

        /*页面的缩放比例发生变化时调用，可以根据当前的缩放比例来重新调整WebView中显示的内容，如修改字体大小、图片大小等*/
        @Override
        public void onScaleChanged(WebView view, float oldScale, float newScale) {
            super.onScaleChanged(view, oldScale, newScale);
        }

        /*根据请求携带的内容来判断是否需要拦截请求。*/
        @Nullable
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return super.shouldInterceptRequest(view, request);
        }
    }

    static class MyWebChromeClient extends WebChromeClient {

        /*页面加载进度发生变化时调用，如显示进度条等*/
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

        /*接收Web页面的图标*/
        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
        }

        /*接收Web页面的标题*/
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

        /*处理JS的Alert对话框*/
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }

        /*处理JS的Prompt对话框*/
        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }

        /*处理JS的Confirm对话框*/
        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            return super.onJsConfirm(view, url, message, result);
        }

        /*Web页面上传文件时调用*/
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
        }

        /*自定义媒体文件播放加载时的进度条*/
        @Nullable
        @Override
        public View getVideoLoadingProgressView() {
            return super.getVideoLoadingProgressView();
        }

        /*设置媒体文件默认的预览图*/
        @Nullable
        @Override
        public Bitmap getDefaultVideoPoster() {
            return super.getDefaultVideoPoster();
        }

        /*媒体文件进入全屏时调用*/
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            super.onShowCustomView(view, callback);
        }

        /*媒体文件退出全屏时调用*/
        @Override
        public void onHideCustomView() {
            super.onHideCustomView();
        }
    }
}