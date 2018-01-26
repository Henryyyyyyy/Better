package me.henry.versatile.api;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;

import me.henry.versatile.model.video.VideoModel;
import me.henry.versatile.nativenet.Request;
import me.henry.versatile.nativenet.RequestManager;
import me.henry.versatile.nativenet.callback.JsonArrayReaderCallback;
import me.henry.versatile.nativenet.error.AppException;
import me.henry.versatile.nativenet.itf.ICallback;
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
