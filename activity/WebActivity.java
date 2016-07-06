package cn.bjsxt.youhuo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.bjsxt.youhuo.R;
import cn.bjsxt.youhuo.view.MyNumProgressBar;

public class WebActivity extends BaseActivity {
    private MyNumProgressBar pb;
    private WebView web;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        url = intent.getStringExtra("URL");
        if (TextUtils.isEmpty(url)) {
            url = "http://www.baidu.com/";
        }
        initView();
        initWebView();
    }

    /**
     * 初始化webView
     */
    private void initWebView() {
        //设置 javaScript
        WebSettings settings = web.getSettings();
        settings.setJavaScriptEnabled(true);
        //双指放大
        settings.setSupportZoom(true);
        //自动加载图片
        settings.setLoadsImagesAutomatically(true);
        //设置连接
        web.setWebViewClient(new WebViewClient() {
            //兼容低版本
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        web.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                //拿到加载进度
                pb.setVisibility(View.VISIBLE);
                pb.startProgress(newProgress);
                if (newProgress == 100) {
                    pb.setVisibility(View.GONE);
                } else if (newProgress == 0) {
                    pb.setVisibility(View.VISIBLE);
                }
            }
        });
        web.loadUrl(url);
    }
    /**
     * 初始化控件
     */
    private void initView() {
        inflaterView(R.layout.activity_web,getResources().getColor(R.color.toolBar_bg));
        pb = (MyNumProgressBar) childView.findViewById(R.id.web_pb);
        web = (WebView) childView.findViewById(R.id.web_web);
    }
    @Override
    public void onBackPressed() {
        if (web.canGoBack()){//是否有历史记录
            web.goBack();//返回上一个历史纪录
            return;
        }
        super.onBackPressed();
    }
}
