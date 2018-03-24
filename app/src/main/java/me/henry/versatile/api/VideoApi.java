package me.henry.versatile.api;

import android.os.Environment;

import com.example.lib_native_net.Request;
import com.example.lib_native_net.RequestManager;
import com.example.lib_native_net.callback.JsonArrayReaderCallback;

import java.io.File;
import java.util.ArrayList;

import me.henry.versatile.model.video.VideoModel;

import me.henry.versatile.utils.CanuteLog;

/**
 * Created by henry on 2017/12/21.
 */

public class VideoApi {
private static final int MAX_NUM=10;
    public void getVideoMsgList(int page, JsonArrayReaderCallback<VideoModel> callback){
        CanuteLog.e("mma","getVideoMsgList");
        int start=1+(page-1)*10;
        String url = "http://baobab.kaiyanapp.com/api/v4/discovery/hot?start="+start+"&num="+MAX_NUM;
        Request request = new Request(url, Request.RequestMethod.GET, Request.RequestTool.OKHTTP);
        String path = Environment.getExternalStorageDirectory() + File.separator + "versatilejson.tmp";
        request.setCallback(callback.setCachePath(path));
        RequestManager.getInstance().performRequest(request);
    }
}
