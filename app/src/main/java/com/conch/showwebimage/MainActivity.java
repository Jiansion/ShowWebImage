package com.conch.showwebimage;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.conch.showwebimage.widget.ShowImageWebView;

public class MainActivity extends AppCompatActivity {
    private ShowImageWebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = (ShowImageWebView) findViewById(R.id.showImageView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // web 页面加载完成，添加监听图片的点击 js 函数
                webView.setImageClickListener();
                //解析 HTML
                webView.parseHTML(view);
            }
        });
        webView.loadUrl("http://smartlock.qianjia.com/html/2017-07/10_272637.html");

    }
}
