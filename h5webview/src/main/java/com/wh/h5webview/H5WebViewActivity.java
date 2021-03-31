package com.wh.h5webview;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class H5WebViewActivity extends AppCompatActivity {

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h5_web_view);

        WebView webview = findViewById(R.id.webview);

        WebSettings webSettings = webview.getSettings();
        //启用 JavaScript
        webSettings.setJavaScriptEnabled(true);
        //自定义用户代理字符串，然后在网页中查询自定义用户代理，以验证请求网页的客户端实际上是您的Android 应用。
        if (!webSettings.getUserAgentString().contains("h5webview")) {
            webSettings.setUserAgentString(webSettings.getUserAgentString() + "h5webview");
        }
        webview.setWebViewClient(new MyWebViewClient());

        webview.loadUrl("https://www.baidu.com");
    }


    static class MyWebViewClient extends WebViewClient {

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


    }
}