package me.henry.versatile.utils.music;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;


import com.github.promeg.pinyinhelper.Pinyin;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import me.henry.versatile.model.music.MusicInfo;


/**
 * Created by zj on 2017/4/7.
 * me.henry.betterme.betterme.utils
 */

public class MusicUtil  {
    public static final int FILTER_SIZE = 1 * 60* 1024 ;//60k      1MB --->1 * 1024 * 1024
    public static final int FILTER_DURATION = 1 * 60 * 1000;// 1分钟
    private static String[] proj_music = new String[]{
            MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ARTIST_ID, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.SIZE};

    public static ArrayList<MusicInfo> queryMusic(Context context) {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = context.getContentResolver();
        ArrayList<MusicInfo> musicList = new ArrayList<>();
        StringBuilder select = new StringBuilder(" ");
        select.append(MediaStore.Audio.Media.SIZE + " > " + FILTER_SIZE);
        select.append(" and " + MediaStore.Audio.Media.DURATION + " > " + FILTER_DURATION);
        Cursor cursor = contentResolver.query(uri, proj_music, select.toString(), null, MediaStore.Audio.AudioColumns.IS_MUSIC);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                MusicInfo music = new MusicInfo();
                music.songId = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                music.albumId = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                music.albumName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM));
                music.albumData = getAlbumArtUri(music.albumId) + "";
                music.duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                music.musicName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                music.artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                music.artistId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID));
                String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                music.data = filePath;
                music.folder = filePath.substring(0, filePath.lastIndexOf(File.separator));
                music.size = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
                music.islocal = true;
                music.sort = Pinyin.toPinyin(music.musicName.charAt(0)).substring(0, 1).toUpperCase();

                musicList.add(music);
            }
            // 释放资源
            cursor.close();
        }

        Collections.sort(musicList, new Comparator<MusicInfo>() {
            @Override
            public int compare(MusicInfo o1, MusicInfo o2) {
                return o1.compareTo(o2);
            }
        });
        return musicList;
    }

    public static Uri getAlbumArtUri(long albumId) {
        return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), albumId);
    }

    public static MusicInfo getMusicInfo(Context context, long id) {
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, proj_music, "_id = " + String.valueOf(id), null, null);
        if (cursor == null) {
            return null;
        }
        MusicInfo music = new MusicInfo();
        while (cursor.moveToNext()) {
            music.songId = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
            music.albumId = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
            music.albumName = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Albums.ALBUM));
            music.albumData = getAlbumArtUri(music.albumId) + "";
            music.duration = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DURATION));
            music.musicName = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.TITLE));
            music.size = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
            music.artist = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ARTIST));
            music.artistId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID));
            String filePath = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DATA));
            music.data = filePath;
            String folderPath = filePath.substring(0,
                    filePath.lastIndexOf(File.separator));
            music.folder = folderPath;
            music.sort = Pinyin.toPinyin(music.musicName.charAt(0)).substring(0, 1).toUpperCase();
        }
        cursor.close();
        return music;
    }
}
