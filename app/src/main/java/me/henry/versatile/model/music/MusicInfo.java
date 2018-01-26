package me.henry.versatile.model.music;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zj on 2017/4/7.
 * me.henry.betterme.betterme.model
 */

public class MusicInfo implements Parcelable,Comparable<MusicInfo> {
    public static final String KEY_SONG_ID = "songid";
    public static final String KEY_ALBUM_ID = "albumid";
    public static final String KEY_ALBUM_NAME = "albumname";
    public static final String KEY_ALBUM_DATA = "albumdata";
    public static final String KEY_DURATION = "duration";
    public static final String KEY_MUSIC_NAME = "musicname";
    public static final String KEY_ARTIST = "artist";
    public static final String KEY_ARTIST_ID = "artist_id";
    public static final String KEY_DATA = "data";
    public static final String KEY_FOLDER = "folder";
    public static final String KEY_SIZE = "size";
    public static final String KEY_LRC = "lrc";
    public static final String KEY_ISLOCAL = "islocal";
    public static final String KEY_SORT = "sort";

    /**
     * 数据库中的_id
     */
    public long songId = -1;
    public int albumId = -1;
    public String albumName;
    public String albumData;
    public int duration;
    public String musicName;
    public String artist;
    public long artistId;
    public String data;
    public String folder;
    public String lrc;
    public boolean islocal;
    public String sort;
    public int size;



    public static final Creator<MusicInfo> CREATOR = new Creator<MusicInfo>() {
        @Override
        public MusicInfo createFromParcel(Parcel in) {
            MusicInfo music = new MusicInfo();
            Bundle bundle = in.readBundle();
            music.songId = bundle.getLong(KEY_SONG_ID);
            music.albumId = bundle.getInt(KEY_ALBUM_ID);
            music.albumName = bundle.getString(KEY_ALBUM_NAME);
            music.duration = bundle.getInt(KEY_DURATION);
            music.musicName = bundle.getString(KEY_MUSIC_NAME);
            music.artist = bundle.getString(KEY_ARTIST);
            music.artistId = bundle.getLong(KEY_ARTIST_ID);
            music.data = bundle.getString(KEY_DATA);
            music.folder = bundle.getString(KEY_FOLDER);
            music.albumData = bundle.getString(KEY_ALBUM_DATA);
            music.size = bundle.getInt(KEY_SIZE);
            music.lrc = bundle.getString(KEY_LRC);
            music.islocal = bundle.getBoolean(KEY_ISLOCAL);
            music.sort = bundle.getString(KEY_SORT);
            return music;
        }

        @Override
        public MusicInfo[] newArray(int size) {
            return new MusicInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();
        bundle.putLong(KEY_SONG_ID, songId);
        bundle.putInt(KEY_ALBUM_ID, albumId);
        bundle.putString(KEY_ALBUM_NAME, albumName);
        bundle.putString(KEY_ALBUM_DATA, albumData);
        bundle.putInt(KEY_DURATION, duration);
        bundle.putString(KEY_MUSIC_NAME, musicName);
        bundle.putString(KEY_ARTIST, artist);
        bundle.putLong(KEY_ARTIST_ID, artistId);
        bundle.putString(KEY_DATA, data);
        bundle.putString(KEY_FOLDER, folder);
        bundle.putInt(KEY_SIZE, size);
        bundle.putString(KEY_LRC, lrc);
        bundle.putBoolean(KEY_ISLOCAL, islocal);
        bundle.putString(KEY_SORT, sort);
        dest.writeBundle(bundle);
    }

    @Override
    public String toString() {
        return "MusicInfo{" +
                "songId=" + songId +
                ", albumId=" + albumId +
                ", albumName='" + albumName + '\'' +
                ", albumData='" + albumData + '\'' +
                ", duration=" + duration +
                ", musicName='" + musicName + '\'' +
                ", artist='" + artist + '\'' +
                ", artistId=" + artistId +
                ", data='" + data + '\'' +
                ", folder='" + folder + '\'' +
                ", lrc='" + lrc + '\'' +
                ", islocal=" + islocal +
                ", sort='" + sort + '\'' +
                ", size=" + size +
                '}';
    }
    @Override
    public int compareTo(MusicInfo o) {
        return compare(this.songId, o.songId);
    }
    //-1（是正确顺序），0不变，1变
    public static int compare(long x, long y) {
        return (x > y) ? -1 : ((x == y) ? 0 : 1);
    }
}
