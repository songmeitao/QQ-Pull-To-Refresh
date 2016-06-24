package com.smt.pulltorefresh;

import android.os.Handler;
import android.os.Message;

import com.smt.pulltorefresh.PullToRefreshLayout.OnRefreshListener;

public class MyListener implements OnRefreshListener {

    @Override
    public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {

        new Handler() {
            @Override
            public void handleMessage(Message msg) {

                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }
        }.sendEmptyMessageDelayed(0, 5000);
    }

    @Override
    public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {

        new Handler() {
            @Override
            public void handleMessage(Message msg) {

                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
        }.sendEmptyMessageDelayed(0, 5000);
    }

}
