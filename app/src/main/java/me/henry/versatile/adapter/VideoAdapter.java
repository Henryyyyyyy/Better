package me.henry.versatile.adapter;

import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import me.henry.versatile.R;
import me.henry.versatile.model.video.VideoModel;
import me.henry.versatile.view.video.CustomVideoView;
import me.henry.versatile.view.video.XueErVideoView;

/**
 * Created by henry on 2017/12/21.
 */

public class VideoAdapter extends BaseQuickAdapter<VideoModel, BaseViewHolder> {
    public VideoAdapter(int layoutResId, @Nullable List<VideoModel> data) {
        super(layoutResId, data);
    }

    CustomVideoView mCustomVideoView;
    XueErVideoView mVideoView;

    @Override
    protected void convert(BaseViewHolder helper, VideoModel item) {
        Glide.with(mContext)
                .load(item.getAuthor().getIcon())
                .placeholder(R.mipmap.ic_launcher)
                .into((ImageView) helper.getView(R.id.iv_author_avatar));

        helper.setText(R.id.tv_author_name, item.getAuthor().getName())
                .setText(R.id.tv_author_des, item.getAuthor().getDescription())
                .setText(R.id.tv_category, item.getCategory())
                .setText(R.id.tv_video_title, item.getTitle())
                // .addOnClickListener()
                .setText(R.id.tv_video_des, item.getDescription());
        mVideoView = helper.getView(R.id.xueer_videoview);
        mVideoView.setCover(item.getCover());
        mVideoView.setDataSource(item.getPlayUrl());

    }
}
