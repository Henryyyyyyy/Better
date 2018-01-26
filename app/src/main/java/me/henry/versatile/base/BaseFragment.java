package me.henry.versatile.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.henry.versatile.activity.MainActivity;
import me.henry.versatile.app.Canute;
import me.yokeyword.fragmentation.SupportFragment;
import me.yokeyword.fragmentation_swipeback.SwipeBackActivity;
import me.yokeyword.fragmentation_swipeback.SwipeBackFragment;

/**
 * Created by henry on 2017/11/22.
 */

public abstract class BaseFragment extends SupportFragment {
    public abstract Object setLayout();

    protected Context mAppContext;
    private Unbinder mUnbinder = null;
    protected View mRootView;

    public abstract void onBindView(@Nullable Bundle savedInstanceState, View rootView);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (setLayout() instanceof Integer) {
            mRootView = inflater.inflate((Integer) setLayout(), container, false);
        } else if (setLayout() instanceof View) {
            mRootView = (View) setLayout();
        } else {

            throw new ClassCastException("setlayout must be int or view");
        }

        if (mRootView != null) {
            mUnbinder = ButterKnife.bind(this, mRootView);
            mAppContext = Canute.getApplicationContext();
            onBindView(savedInstanceState, mRootView);
        }
        return mRootView;
    }

    public final MainActivity getMainActivity() {
        //这activity是它fragmentation自带的
        return (MainActivity) _mActivity;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }
}
