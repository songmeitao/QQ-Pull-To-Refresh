package com.smt.pulltorefresh.activity;

import com.smt.pulltorefresh.MyListener;
import com.smt.pulltorefresh.PullToRefreshLayout;
import com.smt.pulltorefresh.R;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class PullableWebViewActivity extends Activity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ((PullToRefreshLayout) findViewById(R.id.refresh_view))
                .setOnRefreshListener(new MyListener());
        webView = (WebView) findViewById(R.id.content_view);
        webView.loadUrl("https://github.com/songmeitao");
    }
}
