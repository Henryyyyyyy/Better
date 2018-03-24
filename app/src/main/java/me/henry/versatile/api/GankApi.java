package me.henry.versatile.api;

import android.os.Environment;

import com.example.lib_native_net.Request;
import com.example.lib_native_net.RequestManager;
import com.example.lib_native_net.callback.StringCallback;

import me.henry.versatile.fragment.gank.GankType;

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
