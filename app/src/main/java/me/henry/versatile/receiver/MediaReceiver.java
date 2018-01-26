package me.henry.versatile.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

import me.henry.versatile.service.MusicPlayer;


/**
 * Created by zj on 2017/11/2.
 * me.henry.betterme.betterme.common
 */

public class MediaReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //处理耳机调节歌曲的上一首，下一首事件
        if (intent.getAction().equals(Intent.ACTION_MEDIA_BUTTON)){
            KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            int keycode = event.getKeyCode();

                switch (keycode) {
                    case KeyEvent.KEYCODE_MEDIA_NEXT:
                        //播放下一首
                        MusicPlayer.next();
                        break;
                    case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                        //播放上一首
                        MusicPlayer.previous();
                        break;
                    case KeyEvent.KEYCODE_HEADSETHOOK:
                        //中间按钮,暂停or播放
                        break;
                    default:
                        break;
                }



        }

    }
}
