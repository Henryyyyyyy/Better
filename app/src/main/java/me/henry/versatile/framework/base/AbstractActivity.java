package me.henry.versatile.framework.base;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import java.lang.ref.WeakReference;

import me.henry.versatile.R;
import me.henry.versatile.framework.swipecomponent.SwipeBackActivity;


public abstract class AbstractActivity extends AppCompatActivity {
    protected SafeHandler mHandler;
    protected AbstractActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setContentView());
        dealOnWithDataBinding(savedInstanceState);
        mContext = this;
        if (savedInstanceState != null) {
            restore(savedInstanceState);
        }
       // setSwipeBackEnable(setSwipeBack());
    }
    protected boolean setSwipeBack() {
        return Build.VERSION.SDK_INT < 26;
    }
    public abstract int setContentView();

    public abstract void dealOnWithDataBinding(Bundle savedInstanceState);

    public SafeHandler initSafeHandler() {
        mHandler = new SafeHandler(this);
        return mHandler;
    }

    /**
     * 页面恢复处理
     *
     * @param savedInstanceState 保存的数据
     */
    protected void restore(Bundle savedInstanceState) {

        // 会导致只要变化方向也会回到主页
    }


    static class SafeHandler extends Handler {
        WeakReference<AbstractActivity> reference;

        SafeHandler(AbstractActivity activity) {
            this.reference = new WeakReference(activity);
        }

        public void handleMessage(Message msg) {
            if (this.reference != null) {
                AbstractActivity activity = (AbstractActivity) this.reference.get();
                if (activity != null) {
                    activity.handleMessage(msg);
                }

            }
        }
    }
    @Override
    public final void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_old);
    }

    @Override
    public final void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_old, R.anim.slide_left_to_right);
    }
    protected void handleMessage(Message message) {
    }

}
