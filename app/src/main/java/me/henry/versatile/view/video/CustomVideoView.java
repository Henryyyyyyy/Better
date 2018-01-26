package me.henry.versatile.view.video;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import butterknife.internal.Utils;
import me.henry.versatile.R;
import me.henry.versatile.utils.CanuteLog;

/**
 * Created by henry on 2017/12/25.
 */

public class CustomVideoView extends RelativeLayout implements TextureView.SurfaceTextureListener, View.OnClickListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnInfoListener, MediaPlayer.OnErrorListener, MediaPlayer.OnBufferingUpdateListener {
    private static final String TAG = "CustomVideoView";
    private static final int LOAD_TOTAL_COUNT = 3;
    private static final int STATE_ERROR = -1;
    private static final int STATE_IDLE = 0;//闲置状态，相当于停止，重新开始？
    private static final int STATE_PLAYING = 1;
    private static final int STATE_PAUSING = 2;
    private static final int STATE_LOADING = 3;
    private RelativeLayout mParentContainer;//listview的容器
    private RelativeLayout mPlayerView;//视频样式itemview
    private TextureView texture_video;
    private ProgressBar pb_loading;
    private ImageView iv_cover;
    private ImageView iv_playorstop;
    private AudioManager mAudioManager;
    private Surface mVideoSurface;
    private int mScreenWidth, mDestationHeight;
    public static float VIDEO_HEIGHT_PERCENT = 10 / 16.0f;
    /**
     * Status状态保护
     */
    private String mUrl;
    private boolean isMute = false;
    private int mCurrentCount = 0;
    private int playerState = STATE_IDLE;
    private ScreenEventReceiver mScreenReceiver;

    private MediaPlayer mediaPlayer;

    public CustomVideoView(Context context, RelativeLayout parentContainer) {
        super(context);

        mParentContainer = parentContainer;
        mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
        mDestationHeight = (int) (mScreenWidth * VIDEO_HEIGHT_PERCENT);
        initDataAndView();
    }


    private void initDataAndView() {
        if (mParentContainer.getChildCount() == 0) {
            LayoutInflater inflater = LayoutInflater.from(this.getContext());
            mPlayerView = (RelativeLayout) inflater.inflate(R.layout.container_video, this);
            texture_video = mPlayerView.findViewById(R.id.texture_video);
            texture_video.setOnClickListener(this);
            texture_video.setKeepScreenOn(true);
            LayoutParams params = new LayoutParams(mScreenWidth, mDestationHeight);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            mPlayerView.setLayoutParams(params);
            iv_cover = mPlayerView.findViewById(R.id.iv_cover);
            iv_playorstop = mPlayerView.findViewById(R.id.iv_playorstop);
            pb_loading = mPlayerView.findViewById(R.id.pb_loading);
            pb_loading.setVisibility(GONE);
            iv_cover.setOnClickListener(this);
            iv_playorstop.setOnClickListener(this);
            mParentContainer.addView(this);
        }

    }


    /**
     * 在onChildViewAttachedToWindow调用
     */
    public void onInitMediaPlayer() {


        if (texture_video != null) {
            texture_video.setSurfaceTextureListener(this);
        }
        // registerBroadcastReceiver();
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
        mParentContainer.removeAllViews();
        setCurrentPlayState(STATE_IDLE);
    }


    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        CanuteLog.i(TAG, "onSurfaceTextureAvailable");
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_cover:
                break;
            case R.id.iv_playorstop:
                CanuteLog.e(TAG, "state=" + playerState);
                if (playerState == STATE_PLAYING) {
                    pause();
                } else if (playerState == STATE_PAUSING) {
                    start();
                } else if (playerState == STATE_IDLE) {
                    load();
                }

                break;
            case R.id.texture_video:
                break;
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
            mediaPlayer.setDataSource(mUrl);
            mediaPlayer.prepareAsync(); //开始异步加载
        } catch (Exception e) {
            setCurrentPlayState(STATE_ERROR);
            CanuteLog.e(TAG, e.getMessage());
            // reload();
            //  stop(); //error以后重新调用stop加载
        }
    }

    public void reload() {
        CanuteLog.d(TAG, "reload");
        if (this.mediaPlayer != null) {
            this.mediaPlayer.reset();
            this.mediaPlayer.setOnSeekCompleteListener(null);
            this.mediaPlayer.stop();
            this.mediaPlayer.release();
            this.mediaPlayer = null;
        }
        if (mCurrentCount < LOAD_TOTAL_COUNT) { //满足重新加载的条件
            mCurrentCount += 1;
            load();
        } else {
            setCurrentPlayState(STATE_ERROR); //显示错误状态
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


    /**
     * 返回封面imageview
     *
     * @return
     */
    public ImageView getCover() {
        return iv_cover;
    }

    public void setDataSource(String url) {
        this.mUrl = url;
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
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }


    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {

    }

    @Override
    public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
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
     * true is no voice
     * 默认是false，有声音
     *
     * @param mute
     */
    public void mute(boolean mute) {
        isMute = mute;
        if (mediaPlayer != null && this.mAudioManager != null) {
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
//                        if (mIsRealPause) {
//                            //手动点的暂停，回来后还暂停
//                            pause();
//                        }
                    }
                    break;
                case Intent.ACTION_SCREEN_OFF:
                    if (playerState == STATE_PLAYING) {
                        // pause();
                    }
                    break;
            }
        }
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

    /**
     * 判断封面是否隐藏
     *
     * @return
     */
    public boolean isCoverHidden() {
        return iv_cover.getVisibility() == View.VISIBLE ? false : true;
    }

    public int getCurrentPosition() {
        if (this.mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public int getDuration() {
        if (mediaPlayer != null) {
            return mediaPlayer.getDuration();
        }
        return 0;
    }

}
