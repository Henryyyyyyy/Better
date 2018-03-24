package me.henry.versatile.view.video;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import me.henry.versatile.R;
import me.henry.versatile.utils.CanuteLog;
import me.henry.versatile.utils.DimenUtil;

/**
 * Created by henry on 2017/12/27.
 */

public class XueErVideoView extends RelativeLayout implements TextureView.SurfaceTextureListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnInfoListener, MediaPlayer.OnErrorListener, MediaPlayer.OnBufferingUpdateListener {
    private static final String TAG = "XueErVideoView";

    public int viewWidth, viewHeigh;
    private TextureView texture_video;
    private ProgressBar pb_loading;
    private ImageView iv_cover;
    private ImageView iv_playorstop;
    private Context mContext;
    //---
    private static final int LOAD_TOTAL_COUNT = 3;
    private static final int STATE_ERROR = -1;
    private static final int STATE_IDLE = 0;//闲置状态，相当于停止，重新开始？
    private static final int STATE_PLAYING = 1;
    private static final int STATE_PAUSING = 2;
    private static final int STATE_LOADING = 3;
    private Surface mVideoSurface;
    public String mVideoUrl;
    private boolean isMute = false;
    private int mCurrentCount = 0;
    private int playerState = STATE_IDLE;
    private ScreenEventReceiver mScreenReceiver;
    private MediaPlayer mediaPlayer;
    public static float VIDEO_HEIGHT_PERCENT = 9 / 16.0f;


    public XueErVideoView(Context context) {
        this(context, null);
    }

    public XueErVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XueErVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = getContext();
        initView();
        setListeners();
    }


    RelativeLayout.LayoutParams params;

    private void initView() {
        setBackgroundColor(Color.BLACK);
        //添加textureview
        int mScreenWidth = DimenUtil.getScreenWidth();
        int height = (int) (mScreenWidth * VIDEO_HEIGHT_PERCENT);
        params = new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height);
        texture_video = new TextureView(mContext);
        addView(texture_video, params);
        //添加imageview  cover
        iv_cover = new ImageView(mContext);
        iv_cover.setScaleType(ImageView.ScaleType.CENTER_CROP);
        addView(iv_cover, params);
        //添加progressbar
        pb_loading = new ProgressBar(mContext, null, android.R.attr.progressBarStyle);
        params = new LayoutParams(DimenUtil.dp2px(mContext, 30f), DimenUtil.dp2px(mContext, 30f));
        params.addRule(CENTER_IN_PARENT);
        pb_loading.setVisibility(GONE);
        addView(pb_loading, params);
        //添加播放暂停按钮
        iv_playorstop = new ImageView(mContext);
        params = new LayoutParams(DimenUtil.dp2px(mContext, 20f), DimenUtil.dp2px(mContext, 20f));
        params.topMargin = DimenUtil.dp2px(mContext, 15);
        params.leftMargin = DimenUtil.dp2px(mContext, 15);
        iv_playorstop.setImageResource(R.mipmap.icon_play);
        addView(iv_playorstop, params);


    }


    private void setListeners() {
        iv_playorstop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playerState == STATE_PLAYING) {
                    pause();
                } else if (playerState == STATE_PAUSING) {
                    start();
                } else if (playerState == STATE_IDLE) {
                    load();
                }
            }
        });
    }

    /**
     * 在onChildViewAttachedToWindow调用
     */
    public void onInitMediaPlayer() {

        CanuteLog.d(TAG, "onInitMediaPlayer");
        if (texture_video != null) {
            texture_video.setSurfaceTextureListener(this);
        }
        registerBroadcastReceiver();
    }


    /**
     * 在onChildViewDetachedFromWindow调用
     * 停止，释放资源,重新加载
     */
    public void onDestroyMediaPlayer() {
        CanuteLog.e(TAG, "destroy-----------");
        if (this.mediaPlayer != null) {
            this.mediaPlayer.reset();
            this.mediaPlayer.setOnSeekCompleteListener(null);
            this.mediaPlayer.stop();
            this.mediaPlayer.release();
            this.mediaPlayer = null;
        }
        mVideoSurface = null;
        setCurrentPlayState(STATE_IDLE);
        unRegisterBroadcastReceiver();
        mScreenReceiver = null;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        mVideoSurface = new Surface(surfaceTexture);
        createMediaPlayer();
        if (mediaPlayer != null) {
            mediaPlayer.setSurface(mVideoSurface);
        }
    }

    private synchronized void createMediaPlayer() {

        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.reset();
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnInfoListener(this);
            mediaPlayer.setOnErrorListener(this);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            if (mVideoSurface != null && mVideoSurface.isValid()) {
                mediaPlayer.setSurface(mVideoSurface);
            }
        }
    }

    public void load() {
        CanuteLog.e(TAG, "load...");
        if (this.playerState != STATE_IDLE) {
            return;
        }
        setCurrentPlayState(STATE_LOADING);
        try {

            mute(false);
            mediaPlayer.setDataSource(mVideoUrl);
            mediaPlayer.prepareAsync(); //开始异步加载
        } catch (Exception e) {
            setCurrentPlayState(STATE_ERROR);
            CanuteLog.e(TAG, e.getMessage());
            // reload();
            //  stop(); //error以后重新调用stop加载
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer = mp;
        if (mediaPlayer != null) {
            CanuteLog.e(TAG, "onprepare and start...");
            mediaPlayer.setOnBufferingUpdateListener(this);
            mCurrentCount = 0;
            start();

        }
    }

    public void start() {
        if (!isPlaying()) {
            setCurrentPlayState(STATE_PLAYING);
            mediaPlayer.setOnSeekCompleteListener(null);
            mediaPlayer.start();
        } else {
        }
    }

    public void pause() {
        if (this.playerState != STATE_PLAYING) {
            return;
        }
        CanuteLog.d(TAG, "do pause");
        setCurrentPlayState(STATE_PAUSING);
        if (isPlaying()) {
            mediaPlayer.pause();

        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        playBack();
    }

    //播放完成后回到初始状态
    public void playBack() {
        CanuteLog.d(TAG, " do playBack");
        setCurrentPlayState(STATE_PAUSING);
        if (mediaPlayer != null) {
            mediaPlayer.setOnSeekCompleteListener(null);
            mediaPlayer.seekTo(0);
            mediaPlayer.pause();
        }
    }


    public void setCover(String url) {
        if (iv_cover != null) {
            Glide.with(mContext)
                    .load(url)
                    .into(iv_cover);
        }
    }

    public void setDataSource(String url) {
        mVideoUrl = url;

    }

    /**
     * true is no voice
     * 默认是false，有声音
     *
     * @param mute
     */
    public void mute(boolean mute) {
        isMute = mute;
        if (mediaPlayer != null) {
            float volume = isMute ? 0.0f : 1.0f;
            mediaPlayer.setVolume(volume, volume);
        }
    }

    private void registerBroadcastReceiver() {
        if (mScreenReceiver == null) {
            mScreenReceiver = new ScreenEventReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            filter.addAction(Intent.ACTION_USER_PRESENT);
            getContext().registerReceiver(mScreenReceiver, filter);
        }
    }

    private void unRegisterBroadcastReceiver() {
        if (mScreenReceiver != null) {
            getContext().unregisterReceiver(mScreenReceiver);
        }
    }


    /**
     * 监听锁屏事件的广播接收器
     */
    private class ScreenEventReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //主动锁屏时 pause, 主动解锁屏幕时，resume
            switch (intent.getAction()) {
                case Intent.ACTION_USER_PRESENT:
                    if (playerState == STATE_PAUSING) {
                        if (mediaPlayer != null)
                            if (mediaPlayer.getCurrentPosition() != 0)
                                start();
                    }
                    break;
                case Intent.ACTION_SCREEN_OFF:
                    if (playerState == STATE_PLAYING) {
                        pause();
                    }
                    break;
            }
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int i1) {
        CanuteLog.e(TAG, "do error:" + what);
        this.playerState = STATE_ERROR;
        mediaPlayer = mp;
        if (mediaPlayer != null) {
            mediaPlayer.reset();
        }
        if (mCurrentCount >= LOAD_TOTAL_COUNT) {
            setCurrentPlayState(STATE_ERROR);
        }
        return true;
    }

    /**
     * 判断是否在播放
     *
     * @return
     */
    public boolean isPlaying() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }


    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {

    }

    private void setCurrentPlayState(int state) {
        playerState = state;
        switch (state) {
            case STATE_IDLE:
                iv_playorstop.setImageResource(R.mipmap.icon_play);
                iv_cover.setVisibility(VISIBLE);
                pb_loading.setVisibility(GONE);
                break;
            case STATE_PAUSING:
                iv_playorstop.setImageResource(R.mipmap.icon_play);
                iv_cover.setVisibility(GONE);
                pb_loading.setVisibility(GONE);
                break;

            case STATE_PLAYING:
                iv_playorstop.setImageResource(R.mipmap.icon_stop);
                iv_cover.setVisibility(GONE);
                pb_loading.setVisibility(GONE);
                break;
            case STATE_LOADING:
                iv_playorstop.setImageResource(R.mipmap.icon_stop);
                iv_cover.setVisibility(GONE);
                pb_loading.setVisibility(VISIBLE);
                break;
            case STATE_ERROR:
                iv_playorstop.setImageResource(R.mipmap.icon_play);
                iv_cover.setVisibility(VISIBLE);
                pb_loading.setVisibility(GONE);
                break;
        }
    }
}
