package me.henry.versatile.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import me.henry.versatile.R;

import me.henry.versatile.model.music.MusicInfo;
import me.henry.versatile.utils.music.MusicUtil;

/**
 * Created by henry on 2017/12/29.
 */

public class MusicAdapter extends BaseQuickAdapter<MusicInfo,MusicAdapter.MusicHolder>{


    public MusicAdapter(int layoutResId, @Nullable List<MusicInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(MusicHolder helper, MusicInfo item) {
        helper.tvName.setText(item.musicName);
        helper.tvSinger.setText(item.artist);
        helper.addOnClickListener(R.id.rlContent);
        Glide.with(mContext)
                .load(MusicUtil.getAlbumArtUri(item.albumId))
                .placeholder(R.mipmap.icon_music_default)
                .into(helper.ivCover);
    }

    class MusicHolder extends BaseViewHolder {
        ImageView ivCover;
        TextView tvName;
        TextView tvSinger;
        RelativeLayout rlContent;
        public MusicHolder(View itemView) {
            super(itemView);
            ivCover= (ImageView) itemView.findViewById(R.id.ivCover);
            tvName= (TextView) itemView.findViewById(R.id.tvName);
            tvSinger= (TextView) itemView.findViewById(R.id.tvSinger);
            rlContent= (RelativeLayout) itemView.findViewById(R.id.rlContent);
        }

    }
}
