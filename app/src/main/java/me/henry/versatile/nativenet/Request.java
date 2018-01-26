package me.henry.versatile.nativenet;

import android.os.Build;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import me.henry.versatile.nativenet.core.RequestTask;
import me.henry.versatile.nativenet.entities.FileEntity;
import me.henry.versatile.nativenet.error.AppException;
import me.henry.versatile.nativenet.itf.ICallback;
import me.henry.versatile.nativenet.itf.OnGlobalExceptionListener;


public class Request {
    public ICallback iCallback;
    public boolean enableProgressUpdated = false;
    public OnGlobalExceptionListener onGlobalExceptionListener;
    public String tag;
    private RequestTask task;
    public boolean isCompleted;

    public static final int STATE_UPLOAD = 1;
    public static final int STATE_DOWNLOAD = 2;


    public String filePath;
    public ArrayList<FileEntity> fileEntities;

    public void setCallback(ICallback iCallback) {
        this.iCallback = iCallback;
    }

    public void enableProgressUpdated(boolean enable) {
        this.enableProgressUpdated = enable;
    }

    public void setGlobalExceptionListener(OnGlobalExceptionListener onGlobalExceptionListener) {
        this.onGlobalExceptionListener = onGlobalExceptionListener;
    }

    public void checkIfCancelled() throws AppException {
        if (isCancelled){
            throw new AppException(AppException.ErrorType.CANCEL,"the request has been cancelled");
        }
    }

    public void cancel(boolean force) {
        isCancelled = true;
        iCallback.cancel();
        if (force && task != null){
            task.cancel(force);
        }
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void execute(Executor mExecutors) {
        task = new RequestTask(this);
        if (Build.VERSION.SDK_INT > 11){
            task.executeOnExecutor(mExecutors);
        }else {
            task.execute();
        }
    }




    public enum RequestMethod {GET, POST, PUT, DELETE}
    public enum RequestTool {OKHTTP, URLCONNECTION}

    public int maxRetryCount = 3;
    public String url;
    public String content;
    public Map<String, String> headers;

    public volatile boolean isCancelled;

    public RequestMethod method;
    public RequestTool tool;

    public Request(String url, RequestMethod method) {
        this.url = url;
        this.method = method;
        this.tool = RequestTool.URLCONNECTION;
    }

    public Request(String url, RequestMethod method, RequestTool tool){
        this.url = url;
        this.method = method;
        this.tool = tool;
    }

    public Request(String url) {
        this.url = url;
        this.method = RequestMethod.GET;
        this.tool = RequestTool.URLCONNECTION;
    }

    public Request(String url,RequestTool tool) {
        this.url = url;
        this.method = RequestMethod.GET;
        this.tool = tool;
    }

    public void addHeader(String key, String value) {
        if (headers == null){
            headers = new HashMap<String,String>();
        }
        headers.put(key,value);
    }
}
