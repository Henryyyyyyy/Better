package me.henry.versatile.fragment.gank;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.lib_native_net.callback.StringCallback;
import com.example.lib_native_net.error.AppException;

import java.util.ArrayList;
import java.util.List;

import me.henry.versatile.R;
import me.henry.versatile.adapter.GankDataAdapter;
import me.henry.versatile.api.GankApi;
import me.henry.versatile.base.RefreshListFragment;
import me.henry.versatile.base.web.WebFragment;
import me.henry.versatile.model.gank.GankData;
import me.henry.versatile.model.gank.GankJsonModel;

import me.henry.versatile.utils.CanuteLog;

/**
 * Created by henry on 2017/11/23.
 */

public class GankDataFragment extends RefreshListFragment<GankData, GankDataAdapter> {
    /**
     * 0:android
     * 1:ios
     * 2:web
     * 3:girls
     */
    public static final String DATA_TYPE = "DATA_TYPE";
    private int mGankType;

    private enum LoadType {
        Init, Refresh, LoadMore
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        if (args != null) {
            mGankType = args.getInt(DATA_TYPE);
        }
    }

    @Override
    public int initItemLayout() {
        if (mGankType == GankType.MeiZhi) {
            return R.layout.item_girlsimage;
        } else {
            return R.layout.item_gank_article;

        }
    }

    @Override
    public RecyclerView.LayoutManager initLayoutManager() {
        if (mGankType == GankType.MeiZhi) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            return staggeredGridLayoutManager;
        } else {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mAppContext);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            return linearLayoutManager;

        }

    }


    public static GankDataFragment newInstance(int type) {
        final Bundle args = new Bundle();
        args.putInt(DATA_TYPE, type);
        final GankDataFragment fragment = new GankDataFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void getInitDataList() {
gankApi=new GankApi();
        mAdapter.setDataType(mGankType);
        // 优先从缓存中获取数据，如果没有缓存则从网络加载
        ArrayList<GankData> datas = mDataCache.getGankDataList(mGankType);
        if (null != datas && datas.size() > 0) {

            mPageIndex = mDataCache.getGankDataPageIndex(mGankType);
            CanuteLog.d("miao", "from cache,,," + GankType.getTypeDes(mGankType) + ".pageindex=" + mPageIndex);
            mAdapter.setNewData(datas);
        } else {
            CanuteLog.d("miao", "from net,,,");

            int tempPageIndex = 1;
            mRefreshLayout.autoRefresh();

        }
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mGankType == GankType.MeiZhi) {

                } else {
                    startFragmentFromMain(WebFragment.newInstance(mAdapter.getData().get(position).getUrl()));
                }
            }
        });

    }

    @Override
    public void onLazyInitDataList() {

    }


    @Override
    public void onDataRefresh() {
        int tempPageIndex = 1;
        requestData(tempPageIndex, LoadType.Refresh);
//todo:pageindex=1,清理缓存数据(然后重新添加进去)
    }

    @Override
    public void onDataLoadMore() {
//todo:pageindex=1,清理缓存数据(然后重新添加进去),整理refreshlayout状态,注意error(index--),
        int tempPageIndex = mPageIndex + 1;
        requestData(tempPageIndex, LoadType.LoadMore);
    }


    /**
     * 请求网络，成功后添加进缓存
     *
     * @param index
     * @return
     */
    private GankApi gankApi;
    public void requestData(int index, final LoadType type) {
        gankApi.getGankData(index, mGankType, new StringCallback() {
            @Override
            public void onSuccess(String result) {
                CanuteLog.d("miao", "net data=,,," + result);

                GankJsonModel model = JSON.parseObject(result, GankJsonModel.class);
                List<GankData> results = model.getResults();
                switch (type) {
                    case Init:
                    case Refresh:
                        mDataCache.saveGankDataList(mGankType, results);
                        mAdapter.setNewData(results);
                        mPageIndex = 1;
                        mRefreshLayout.finishRefresh();
                        break;
                    case LoadMore:
                        mAdapter.addData(results);
                        mDataCache.saveGankDataList(mGankType, mAdapter.getData());
                        mPageIndex++;
                        mRefreshLayout.finishLoadmore();
                        break;
                }
            }

            @Override
            public void onFailure(AppException e) {
                CanuteLog.d("miao", "failed="+e.responseMessage);
                switch (type) {
                    case Init:
                    case Refresh:
                        mRefreshLayout.finishRefresh(false);
                        break;
                    case LoadMore:
                        mRefreshLayout.finishLoadmore(false);
                        break;
                }
            }
        });
//        RestClient.builder()
//                .service(RestCreator.getGankService())
//                .url("data/" + GankType.getTypeDes(mGankType) + "/" + PAGE_MAX_COUNT + "/" + index)//例如:data/Android/10/1
//                .success(new ISuccess() {
//                    @Override
//                    public void onSuccess(String response) {
//                        CanuteLog.d("miao", "net data=,,," + response);
//
//                        GankJsonModel model = JSON.parseObject(response, GankJsonModel.class);
//                        List<GankData> results = model.getResults();
//                        switch (type) {
//                            case Init:
//                            case Refresh:
//                                mDataCache.saveGankDataList(mGankType, results);
//                                mAdapter.setNewData(results);
//                                mPageIndex = 1;
//                                mRefreshLayout.finishRefresh();
//                                break;
//                            case LoadMore:
//                                mAdapter.addData(results);
//                                mDataCache.saveGankDataList(mGankType, mAdapter.getData());
//                                mPageIndex++;
//                                mRefreshLayout.finishLoadmore();
//                                break;
//                        }
//
//
//                    }
//                })
//                .failure(new IFailure() {
//                    @Override
//                    public void onFailure() {
//                        CanuteLog.d("miao", "failed");
//                        switch (type) {
//                            case Init:
//                            case Refresh:
//                                mRefreshLayout.finishRefresh(false);
//                                break;
//                            case LoadMore:
//                                mRefreshLayout.finishLoadmore(false);
//                                break;
//                        }
//                    }
//                })
//                .error(new IError() {
//                    @Override
//                    public void onError(int code, String msg) {
//                        CanuteLog.d("miao", "error msg=" + msg);
//                        switch (type) {
//                            case Init:
//                            case Refresh:
//                                mRefreshLayout.finishRefresh(false);
//                                break;
//                            case LoadMore:
//
//                                mRefreshLayout.finishLoadmore(false);
//                                break;
//                        }
//
//                    }
//                })
//                .build()
//                .get();
//

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDataCache.saveGankDataPageIndex(mGankType, mPageIndex);
    }
}
