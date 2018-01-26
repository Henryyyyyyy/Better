package me.henry.versatile.utils.cache;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.LruCache;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import me.henry.versatile.model.gank.GankData;
import me.henry.versatile.model.video.VideoModel;

/**
 * 数据缓存工具
 */
public class DataCache {
    private static int M = 1024 * 1024;
    ACache mDiskCache;
    LruCache<String, Object> mLruCache;

    public DataCache(Context context) {
        mDiskCache = ACache.get(context, "versatile");
        mLruCache = new LruCache<>(5 * M);
    }

    public <T extends Serializable> void saveListData(String key, List<T> data) {
        ArrayList<T> datas = (ArrayList<T>) data;
        mLruCache.put(key, datas);
        mDiskCache.put(key, datas, ACache.TIME_WEEK);     // 数据缓存时间为 1 周
    }

    public <T extends Serializable> void saveData(@NonNull String key, @NonNull T data) {
        mLruCache.put(key, data);
        mDiskCache.put(key, data, ACache.TIME_WEEK);     // 数据缓存时间为 1 周
    }

    public <T extends Serializable> T getData(@NonNull String key) {
        T result = (T) mLruCache.get(key);
        if (result == null) {
            result = (T) mDiskCache.getAsObject(key);
            if (result != null) {
                mLruCache.put(key, result);
            }
        }
        return result;
    }

    public <T extends Serializable> T getData(@NonNull String key, @Nullable T defaultValue) {
        T result = (T) mLruCache.get(key);
        if (result != null) {
            return result;
        }
        result = (T) mDiskCache.getAsObject(key);
        if (result != null) {
            mLruCache.put(key, result);
            return result;
        }
        return defaultValue;
    }

    public void removeData(String key) {
        mDiskCache.remove(key);
    }

    /**
     * *******************干货列表 缓存操作******************
     */
    private static String Key_Gankdata_List = "gankdata_list_";

    public void saveGankDataList(int type, List<GankData> data) {
        ArrayList<GankData> gankdata = new ArrayList<>(data);
        saveData(Key_Gankdata_List + type, gankdata);
    }

    public ArrayList<GankData> getGankDataList(int type) {
        return getData(Key_Gankdata_List + type);
    }
    /**
     * *******************各类干货列表index 缓存操作******************
     */
    private static String Key_GankData_PageIndex = "Key_GankData_PageIndex_";

    public void saveGankDataPageIndex(int dataType, Integer pageIndex) {
        saveData(Key_GankData_PageIndex + dataType, pageIndex);
    }

    public Integer getGankDataPageIndex(int dataType) {
        return getData(Key_GankData_PageIndex + dataType, 1);
    }
    /**
     * *******************开眼视频列表 缓存操作******************
     */
    private static String Key_Video_List = "video_list";

    public void saveVideoList(List<VideoModel> data) {
        ArrayList<VideoModel> videodata = new ArrayList<>(data);
        saveData(Key_Video_List, videodata);
    }

    public ArrayList<VideoModel> getVideoList() {
        return getData(Key_Video_List);
    }
    /**
     * *******************开眼视频列表index 缓存操作******************
     */
    private static String Key_Video_Index = "video_index";

    public void saveVideoIndex(Integer pageIndex) {
        saveData(Key_Video_Index, pageIndex);
    }

    public Integer getVideoIndex() {
        return getData(Key_Video_Index, 1);
    }


}
