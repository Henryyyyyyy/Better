package me.henry.versatile.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;

import butterknife.BindView;
import me.henry.versatile.R;

import me.henry.versatile.utils.CanuteLog;
import me.henry.versatile.utils.cache.DataCache;

/**
 * Created by henry on 2017/11/28.
 */

public abstract class RefreshListFragment<DATA,ADAPTER extends BaseQuickAdapter<DATA,BaseViewHolder>> extends VFragment {


    @BindView(R.id.srl_refresh)
    protected RefreshLayout mRefreshLayout;
    @BindView(R.id.rv_dataList)
    public RecyclerView rv_dataList;
    protected ADAPTER mAdapter;
    protected DataCache mDataCache;
    // 分页加载
    protected int mPageIndex = 1;                      // 当面页码
    protected static final int PAGE_MAX_COUNT = 9;       // 每页个数

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataCache=new DataCache(getMainActivity());
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_refresh_list;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        onLazyInitDataList();
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        CanuteLog.e("refreshlist","bindview");
        //设置可以loadmore
        mRefreshLayout.setEnableLoadmore(true);
        //内容没满也可以上拉
        mRefreshLayout.setEnableLoadmoreWhenContentNotFull(true);
        mRefreshLayout.setEnableHeaderTranslationContent(false);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                onDataRefresh();
            }
        });
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                onDataLoadMore();
            }
        });

        Class clz;
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        clz=(Class<ADAPTER>) type.getActualTypeArguments()[1];
        Constructor<?>[] cons = clz.getConstructors();
        Constructor<?> constructor = cons[0];
        try {
            mAdapter=(ADAPTER)constructor.newInstance(initItemLayout(), new ArrayList<DATA>()) ;
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //mAdapter = new GankDataAdapter(initItemLayout(), new ArrayList<DATA>());
        rv_dataList.setLayoutManager(initLayoutManager());
        rv_dataList.setAdapter(mAdapter);

        getInitDataList();

    }


    public abstract int initItemLayout();

    public abstract RecyclerView.LayoutManager initLayoutManager();

    public abstract void getInitDataList();

    public abstract void onLazyInitDataList();

    public abstract void onDataRefresh();

    public abstract void onDataLoadMore();

}
