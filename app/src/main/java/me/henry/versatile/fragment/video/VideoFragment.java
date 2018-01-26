package me.henry.versatile.fragment.video;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.animation.AlphaInAnimation;

import java.util.ArrayList;

import me.henry.versatile.R;
import me.henry.versatile.adapter.VideoAdapter;
import me.henry.versatile.api.VideoApi;
import me.henry.versatile.base.RefreshListFragment;
import me.henry.versatile.model.video.VideoModel;
import me.henry.versatile.nativenet.callback.JsonArrayReaderCallback;
import me.henry.versatile.nativenet.error.AppException;
import me.henry.versatile.utils.CanuteLog;
import me.henry.versatile.view.video.XueErVideoView;

/**
 * Created by henry on 2017/12/20.
 */

public class VideoFragment extends RefreshListFragment<VideoModel, VideoAdapter> {
    public VideoApi videoApi;

    private enum LoadType {
        Init, Refresh, LoadMore
    }

    @Override
    public int initItemLayout() {
        return R.layout.item_video;
    }
    @Override
    public RecyclerView.LayoutManager initLayoutManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mAppContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        return linearLayoutManager;
    }

    @Override
    public void getInitDataList() {
        videoApi = new VideoApi();
    }

    @Override
    public void onLazyInitDataList() {
        rv_dataList.setBackgroundColor(_mActivity.getResources().getColor(R.color.bg_gray));
        ArrayList<VideoModel> datas = mDataCache.getVideoList();
        if (null != datas && datas.size() > 0) {
            mPageIndex = mDataCache.getVideoIndex();
            mAdapter.openLoadAnimation(new AlphaInAnimation(0f));
            mAdapter.setNewData(datas);
        } else {
            mRefreshLayout.autoRefresh();

        }
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });

        rv_dataList.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
                XueErVideoView videoview = view.findViewById(R.id.xueer_videoview);

                videoview.onInitMediaPlayer();

            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {

                XueErVideoView videoview = view.findViewById(R.id.xueer_videoview);

                videoview.onDestroyMediaPlayer();
            }
        });


    }

    public void requestData(int index, final LoadType type) {
        videoApi.getVideoMsgList(index, new JsonArrayReaderCallback<VideoModel>() {
            @Override
            public void onSuccess(ArrayList<VideoModel> result) {
                switch (type) {
                    case Init:
                    case Refresh:
                        mDataCache.saveVideoList(result);
                        mAdapter.setNewData(result);
                        mPageIndex = 1;
                        mRefreshLayout.finishRefresh();
                        break;
                    case LoadMore:
                        mAdapter.addData(result);
                        mDataCache.saveVideoList(mAdapter.getData());
                        mPageIndex++;
                        mRefreshLayout.finishLoadmore();
                        break;
                }
            }

            @Override
            public void onFailure(AppException e) {
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
    }

    @Override
    public void onDataRefresh() {
        //todo 清空缓存文件夹里面所有的数据，再请求index=1的.tmp,再保存
        int tempPageIndex = 1;
        requestData(tempPageIndex, LoadType.Refresh);

    }

    @Override
    public void onDataLoadMore() {
        //todo 请求下一个index的
        int tempPageIndex = mPageIndex + 1;
        requestData(tempPageIndex, LoadType.LoadMore);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDataCache.saveVideoIndex(mPageIndex);
    }
}
