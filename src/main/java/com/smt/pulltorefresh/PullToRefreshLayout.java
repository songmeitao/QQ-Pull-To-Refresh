package com.smt.pulltorefresh;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smt.pulltorefresh.R;
import com.smt.pulltorefresh.pullableview.Pullable;


public class PullToRefreshLayout extends RelativeLayout {
    public static final String TAG = "PullToRefreshLayout";

    public static final int INIT = 0;

    public static final int RELEASE_TO_REFRESH = 1;

    public static final int REFRESHING = 2;

    public static final int RELEASE_TO_LOAD = 3;

    public static final int LOADING = 4;

    public static final int DONE = 5;

    private int state = INIT;

    private OnRefreshListener mListener;

    public static final int SUCCEED = 0;

    public static final int FAIL = 1;

    private float downY, lastY;

    public float pullDownY = 0;

    private float pullUpY = 0;

    private float refreshDist = 200;

    private float loadmoreDist = 200;

    private MyTimer timer;

    public float MOVE_SPEED = 8;

    private boolean isLayout = false;

    private boolean isTouch = false;

    private float radio = 2;


    private RotateAnimation rotateAnimation;

    private RotateAnimation refreshingAnimation;

    private View refreshView;

    private View pullView;

    private View refreshingView;

    private View refreshStateImageView;

    private TextView refreshStateTextView;

    private View loadmoreView;

    private View pullUpView;

    private View loadingView;

    private View loadStateImageView;

    private TextView loadStateTextView;

    private View pullableView;

    private int mEvents;

    private boolean canPullDown = true;
    private boolean canPullUp = true;


    Handler updateHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            MOVE_SPEED = (float) (8 + 5 * Math.tan(Math.PI / 2
                    / getMeasuredHeight() * (pullDownY + Math.abs(pullUpY))));
            if (!isTouch) {

                if (state == REFRESHING && pullDownY <= refreshDist) {
                    pullDownY = refreshDist;
                    timer.cancel();
                } else if (state == LOADING && -pullUpY <= loadmoreDist) {
                    pullUpY = -loadmoreDist;
                    timer.cancel();
                }

            }
            if (pullDownY > 0)
                pullDownY -= MOVE_SPEED;
            else if (pullUpY < 0)
                pullUpY += MOVE_SPEED;
            if (pullDownY < 0) {

                pullDownY = 0;
                pullView.clearAnimation();

                if (state != REFRESHING && state != LOADING)
                    changeState(INIT);
                timer.cancel();
            }
            if (pullUpY > 0) {

                pullUpY = 0;
                pullUpView.clearAnimation();

                if (state != REFRESHING && state != LOADING)
                    changeState(INIT);
                timer.cancel();
            }

            requestLayout();
        }

    };

    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }

    public PullToRefreshLayout(Context context) {
        super(context);
        initView(context);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        timer = new MyTimer(updateHandler);
        rotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(
                context, R.anim.reverse_anim);
        refreshingAnimation = (RotateAnimation) AnimationUtils.loadAnimation(
                context, R.anim.rotating);

        LinearInterpolator lir = new LinearInterpolator();
        rotateAnimation.setInterpolator(lir);
        refreshingAnimation.setInterpolator(lir);
    }

    private void hide() {
        timer.schedule(5);
    }


    public void refreshFinish(int refreshResult) {
        refreshingView.clearAnimation();
        refreshingView.setVisibility(View.GONE);
        switch (refreshResult) {
            case SUCCEED:
                // ˢ�³ɹ
                refreshStateImageView.setVisibility(View.VISIBLE);
                refreshStateTextView.setText(R.string.refresh_succeed);
                refreshStateImageView
                        .setBackgroundResource(R.drawable.refresh_succeed);
                break;
            case FAIL:
            default:

                refreshStateImageView.setVisibility(View.VISIBLE);
                refreshStateTextView.setText(R.string.refresh_fail);
                refreshStateImageView
                        .setBackgroundResource(R.drawable.refresh_failed);
                break;
        }

        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                changeState(DONE);
                hide();
            }
        }.sendEmptyMessageDelayed(0, 1000);
    }


    public void loadmoreFinish(int refreshResult) {
        loadingView.clearAnimation();
        loadingView.setVisibility(View.GONE);
        switch (refreshResult) {
            case SUCCEED:

                loadStateImageView.setVisibility(View.VISIBLE);
                loadStateTextView.setText(R.string.load_succeed);
                loadStateImageView.setBackgroundResource(R.drawable.load_succeed);
                break;
            case FAIL:
            default:

                loadStateImageView.setVisibility(View.VISIBLE);
                loadStateTextView.setText(R.string.load_fail);
                loadStateImageView.setBackgroundResource(R.drawable.load_failed);
                break;
        }

        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                changeState(DONE);
                hide();
            }
        }.sendEmptyMessageDelayed(0, 1000);
    }

    private void changeState(int to) {
        state = to;
        switch (state) {
            case INIT:

                refreshStateImageView.setVisibility(View.GONE);
                refreshStateTextView.setText(R.string.pull_to_refresh);
                pullView.clearAnimation();
                pullView.setVisibility(View.VISIBLE);

                loadStateImageView.setVisibility(View.GONE);
                loadStateTextView.setText(R.string.pullup_to_load);
                pullUpView.clearAnimation();
                pullUpView.setVisibility(View.VISIBLE);
                break;
            case RELEASE_TO_REFRESH:

                refreshStateTextView.setText(R.string.release_to_refresh);
                pullView.startAnimation(rotateAnimation);
                break;
            case REFRESHING:

                pullView.clearAnimation();
                refreshingView.setVisibility(View.VISIBLE);
                pullView.setVisibility(View.INVISIBLE);
                refreshingView.startAnimation(refreshingAnimation);
                refreshStateTextView.setText(R.string.refreshing);
                break;
            case RELEASE_TO_LOAD:

                loadStateTextView.setText(R.string.release_to_load);
                pullUpView.startAnimation(rotateAnimation);
                break;
            case LOADING:

                pullUpView.clearAnimation();
                loadingView.setVisibility(View.VISIBLE);
                pullUpView.setVisibility(View.INVISIBLE);
                loadingView.startAnimation(refreshingAnimation);
                loadStateTextView.setText(R.string.loading);
                break;
            case DONE:

                break;
        }
    }


    private void releasePull() {
        canPullDown = true;
        canPullUp = true;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downY = ev.getY();
                lastY = downY;
                timer.cancel();
                mEvents = 0;
                releasePull();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:

                mEvents = -1;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mEvents == 0) {
                    if (((Pullable) pullableView).canPullDown() && canPullDown
                            && state != LOADING) {

                        pullDownY = pullDownY + (ev.getY() - lastY) / radio;
                        if (pullDownY < 0) {
                            pullDownY = 0;
                            canPullDown = false;
                            canPullUp = true;
                        }
                        if (pullDownY > getMeasuredHeight())
                            pullDownY = getMeasuredHeight();
                        if (state == REFRESHING) {

                            isTouch = true;
                        }
                    } else if (((Pullable) pullableView).canPullUp() && canPullUp
                            && state != REFRESHING) {

                        pullUpY = pullUpY + (ev.getY() - lastY) / radio;
                        if (pullUpY > 0) {
                            pullUpY = 0;
                            canPullDown = true;
                            canPullUp = false;
                        }
                        if (pullUpY < -getMeasuredHeight())
                            pullUpY = -getMeasuredHeight();
                        if (state == LOADING) {

                            isTouch = true;
                        }
                    } else
                        releasePull();
                } else
                    mEvents = 0;
                lastY = ev.getY();

                radio = (float) (2 + 2 * Math.tan(Math.PI / 2 / getMeasuredHeight()
                        * (pullDownY + Math.abs(pullUpY))));
                requestLayout();
                if (pullDownY <= refreshDist && state == RELEASE_TO_REFRESH) {

                    changeState(INIT);
                }
                if (pullDownY >= refreshDist && state == INIT) {

                    changeState(RELEASE_TO_REFRESH);
                }

                if (-pullUpY <= loadmoreDist && state == RELEASE_TO_LOAD) {
                    changeState(INIT);
                }
                if (-pullUpY >= loadmoreDist && state == INIT) {
                    changeState(RELEASE_TO_LOAD);
                }

                if ((pullDownY + Math.abs(pullUpY)) > 8) {

                    ev.setAction(MotionEvent.ACTION_CANCEL);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (pullDownY > refreshDist || -pullUpY > loadmoreDist)

                    isTouch = false;
                if (state == RELEASE_TO_REFRESH) {
                    changeState(REFRESHING);

                    if (mListener != null)
                        mListener.onRefresh(this);
                } else if (state == RELEASE_TO_LOAD) {
                    changeState(LOADING);

                    if (mListener != null)
                        mListener.onLoadMore(this);
                }
                hide();
            default:
                break;
        }

        super.dispatchTouchEvent(ev);
        return true;
    }

    private void initView() {

        pullView = refreshView.findViewById(R.id.pull_icon);
        refreshStateTextView = (TextView) refreshView
                .findViewById(R.id.state_tv);
        refreshingView = refreshView.findViewById(R.id.refreshing_icon);
        refreshStateImageView = refreshView.findViewById(R.id.state_iv);

        pullUpView = loadmoreView.findViewById(R.id.pullup_icon);
        loadStateTextView = (TextView) loadmoreView
                .findViewById(R.id.loadstate_tv);
        loadingView = loadmoreView.findViewById(R.id.loading_icon);
        loadStateImageView = loadmoreView.findViewById(R.id.loadstate_iv);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!isLayout) {

            refreshView = getChildAt(0);
            pullableView = getChildAt(1);
            loadmoreView = getChildAt(2);
            isLayout = true;
            initView();
            refreshDist = ((ViewGroup) refreshView).getChildAt(0)
                    .getMeasuredHeight();
            loadmoreDist = ((ViewGroup) loadmoreView).getChildAt(0)
                    .getMeasuredHeight();
        }

        refreshView.layout(0,
                (int) (pullDownY + pullUpY) - refreshView.getMeasuredHeight(),
                refreshView.getMeasuredWidth(), (int) (pullDownY + pullUpY));
        pullableView.layout(0, (int) (pullDownY + pullUpY),
                pullableView.getMeasuredWidth(), (int) (pullDownY + pullUpY)
                        + pullableView.getMeasuredHeight());
        loadmoreView.layout(0,
                (int) (pullDownY + pullUpY) + pullableView.getMeasuredHeight(),
                loadmoreView.getMeasuredWidth(),
                (int) (pullDownY + pullUpY) + pullableView.getMeasuredHeight()
                        + loadmoreView.getMeasuredHeight());
    }

    class MyTimer {
        private Handler handler;
        private Timer timer;
        private MyTask mTask;

        public MyTimer(Handler handler) {
            this.handler = handler;
            timer = new Timer();
        }

        public void schedule(long period) {
            if (mTask != null) {
                mTask.cancel();
                mTask = null;
            }
            mTask = new MyTask(handler);
            timer.schedule(mTask, 0, period);
        }

        public void cancel() {
            if (mTask != null) {
                mTask.cancel();
                mTask = null;
            }
        }

        class MyTask extends TimerTask {
            private Handler handler;

            public MyTask(Handler handler) {
                this.handler = handler;
            }

            @Override
            public void run() {
                handler.obtainMessage().sendToTarget();
            }

        }
    }

    /**
     *
     * @author smt
     */
    public interface OnRefreshListener {

        void onRefresh(PullToRefreshLayout pullToRefreshLayout);


        void onLoadMore(PullToRefreshLayout pullToRefreshLayout);
    }

}
