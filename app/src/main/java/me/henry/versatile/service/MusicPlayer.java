package me.henry.versatile.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;


import me.henry.versatile.IMusicInterface;
import me.henry.versatile.model.music.MusicInfo;

import static android.content.Context.BIND_AUTO_CREATE;


/**
 * Created by zj on 2017/4/13.
 * me.henry.betterme.betterme.service
 */

public class MusicPlayer {
    public static IMusicInterface mService;

    /**
     * 绑定音乐服务
     * @param context
     */
    public static void bindToService(Context context){
        Intent intent = new Intent().setComponent(new ComponentName("me.henry.versatile", "me.henry.versatile.service.MusicService"));
        context.startService(intent);
        context.bindService(intent, mConnection, BIND_AUTO_CREATE);
    }


    /**
     *连接的时候会有延迟，不能马上执行service里面的方法
     */
    private static ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = IMusicInterface.Stub.asInterface(service);
        }
        @Override
        public void onServiceDisconnected(ComponentName className) {
            mService = null;

        }
    };
    public static void PlayOrPause(){
        try {
            mService.playOrPause();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    public static void next(){
        try {
            mService.next();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    public static void previous(){
        try {
            mService.previous();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    public static void playMusic(MusicInfo music, int index){
        try {
            mService.playMusic(music,index);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    public static MusicInfo getCurrentMusicInfo(){
        try {
            if (mService==null){
                return null;
            }else {
                return mService.getCurrentMusicInfo();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void setPlayMode(int mode){
        try {
         mService.setPlayMode(mode);
        } catch (RemoteException e) {
            e.printStackTrace();

        }
    }
    public static boolean getPlayState(){
        try {
            return mService.getPlayState();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }
}
