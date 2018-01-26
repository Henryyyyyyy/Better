package me.henry.versatile.fragment.gank;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import butterknife.BindView;
import me.henry.versatile.R;
import me.henry.versatile.base.VFragment;

/**
 * Created by henry on 2017/11/23.
 */

public class GankDetailFragment extends VFragment{
    String mUrl;
    public static final String DATA_URL= "DATA_URL";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        if (args != null) {
            mUrl = args.getString(DATA_URL);
        }
    }

    public static GankDetailFragment newInstance(String url) {
        final Bundle args = new Bundle();
        args.putString(DATA_URL, url);
        final GankDetailFragment fragment = new GankDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public Object setLayout() {
        return R.layout.fragment_gank_detail;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {

    }
}
