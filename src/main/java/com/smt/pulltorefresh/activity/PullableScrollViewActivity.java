package com.smt.pulltorefresh.activity;

import com.smt.pulltorefresh.MyListener;
import com.smt.pulltorefresh.PullToRefreshLayout;
import com.smt.pulltorefresh.R;

import android.app.Activity;
import android.os.Bundle;

public class PullableScrollViewActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrollview);
        ((PullToRefreshLayout) findViewById(R.id.refresh_view))
                .setOnRefreshListener(new MyListener());
    }
}
