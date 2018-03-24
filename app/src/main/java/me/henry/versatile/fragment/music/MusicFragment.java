package me.henry.versatile.fragment.music;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import me.henry.versatile.R;
import me.henry.versatile.activity.MainActivity;
import me.henry.versatile.adapter.MusicAdapter;
import me.henry.versatile.app.VConstants;
import me.henry.versatile.base.VFragment;
import me.henry.versatile.model.music.MusicInfo;
import me.henry.versatile.service.MusicPlayer;
import me.henry.versatile.utils.Utils;
import me.henry.versatile.utils.music.MusicUtil;

/**
 * Created by henry on 2017/11/23.
 */

public class MusicFragment extends VFragment implements View.OnClickListener {
    @BindView(R.id.rlMusicPanel)
    RelativeLayout rlMusicPanel;
    @BindView(R.id.ivCurMusicCover)
    CircleImageView ivCurMusicCover;
    @BindView(R.id.tvCurMusicName)
    TextView tvCurMusicName;
    @BindView(R.id.tvCurMusicSinger)
    TextView tvCurMusicSinger;
    @BindView(R.id.ivNext)
    ImageView ivNext;
    @BindView(R.id.ivPrevious)
    ImageView ivPrevious;
    @BindView(R.id.ivPlayOrPause)
    ImageView ivPlayOrPause;
    @BindView(R.id.ivMusicMode)
    ImageView ivMusicMode;
    private BroadcastReceiver mMusicBro;
    private MusicInfo currentMusicInfo;
    @BindView(R.id.rvMusicList)
    RecyclerView rvMusicList;
    private MusicAdapter mAdapter;
    //模式点击顺序:顺序播放，随机播放，循环播放
    private int modeIndex = 1;

    @Override
    public Object setLayout() {
        return R.layout.fragment_music;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        ivNext.setOnClickListener(this);
        ivPlayOrPause.setOnClickListener(this);
        ivPrevious.setOnClickListener(this);
        ivMusicMode.setOnClickListener(this);
        initBrocast();
        freshMusicPanel(MusicPlayer.getCurrentMusicInfo());
        //绑定音乐服务
        mAdapter = new MusicAdapter(R.layout.item_music, new ArrayList<MusicInfo>());
        rvMusicList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMusicList.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.rlContent) {
                    MusicInfo currentmusic = MusicPlayer.getCurrentMusicInfo();
                    if (currentmusic == null) {
                        MusicPlayer.playMusic(mAdapter.getItem(position), position);

                    } else {
                        if (currentmusic.songId != mAdapter.getItem(position).songId) {
                            MusicPlayer.playMusic(mAdapter.getItem(position), position);
                        }
                    }

                }
            }
        });
        //加载手机音乐数据
        loadData();
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {


    }

    private void loadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<MusicInfo> queryMusics = MusicUtil.queryMusic(_mActivity);
                (_mActivity).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (queryMusics != null) {
                            mAdapter.setNewData(queryMusics);
                            Utils.sendUpdateBrocastList(getActivity(), queryMusics);
                            currentMusicInfo = MusicPlayer.getCurrentMusicInfo();
                        }
                    }
                });
            }
        }).start();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivNext:
                MusicPlayer.next();
                break;
            case R.id.ivPrevious:
                MusicPlayer.previous();
                break;
            case R.id.ivPlayOrPause:
                MusicPlayer.PlayOrPause();
                break;
            case R.id.ivMusicMode:
                setPlayingMode();
                break;


        }
    }

    private void freshMusicPanel(MusicInfo info) {
        if (info != null) {
            tvCurMusicName.setText(info.musicName);
            tvCurMusicSinger.setText(info.artist);
            Glide.with(this)
                    .load(MusicUtil.getAlbumArtUri(info.albumId))
                    .placeholder(R.mipmap.icon_music_default)
                    .into(ivCurMusicCover);
        }
    }


    private void setPlayingMode() {
        if (modeIndex == 1) {
            modeIndex = modeIndex + 1;
            ivMusicMode.setImageResource(R.mipmap.play_icn_shuffle);
            MusicPlayer.setPlayMode(VConstants.PlayMode_Shuffle);
            return;
        } else if (modeIndex == 2) {
            modeIndex = modeIndex + 1;
            ivMusicMode.setImageResource(R.mipmap.play_icn_one);
            MusicPlayer.setPlayMode(VConstants.PlayMode_OnlyOne);
            return;
        } else if (modeIndex == 3) {
            modeIndex = 1;
            ivMusicMode.setImageResource(R.mipmap.play_icn_loop);
            MusicPlayer.setPlayMode(VConstants.PlayMode_Loop);
            return;
        }
    }

    private void initBrocast() {

        mMusicBro = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(VConstants.Action_updateMusicInfo)) {
                    currentMusicInfo = intent.getParcelableExtra("music");
                    freshMusicPanel(currentMusicInfo);
                }
                if (intent.getAction().equals(VConstants.Action_updatePlayState)) {
                    boolean isPlaying = intent.getBooleanExtra("isPlaying", true);
                    ivPlayOrPause.setImageResource(isPlaying ? R.mipmap.playbar_btn_pause : R.mipmap.playbar_btn_play);

                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(VConstants.Action_updateMusicInfo);
        filter.addAction(VConstants.Action_updatePlayState);
        _mActivity.registerReceiver(mMusicBro, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMusicBro != null) {
            _mActivity.unregisterReceiver(mMusicBro);
        }
    }
}
