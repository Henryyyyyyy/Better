package me.henry.versatile.api;

import android.os.Environment;

import java.io.File;

import me.henry.versatile.fragment.gank.GankType;
import me.henry.versatile.model.video.VideoModel;
import me.henry.versatile.nativenet.Request;
import me.henry.versatile.nativenet.RequestManager;
import me.henry.versatile.nativenet.callback.JsonArrayReaderCallback;
import me.henry.versatile.nativenet.callback.JsonCallback;
import me.henry.versatile.nativenet.callback.StringCallback;
import me.henry.versatile.utils.CanuteLog;

/**
 * Created by henry on 2018/1/26.
 */

public class GankApi {
    protected static final int PAGE_MAX_COUNT = 9;       // 每页个数
    public void getGankData(int index, int gankType ,StringCallback callback){
        String url = "http://gank.io/api/"+"data/" + GankType.getTypeDes(gankType) + "/" + PAGE_MAX_COUNT + "/" + index;
        Request request = new Request(url, Request.RequestMethod.GET, Request.RequestTool.OKHTTP);
        request.setCallback(callback);
        RequestManager.getInstance().performRequest(request);
    }
}
