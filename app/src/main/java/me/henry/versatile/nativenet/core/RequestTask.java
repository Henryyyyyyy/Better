package me.henry.versatile.nativenet.core;

import android.os.AsyncTask;



import java.net.HttpURLConnection;

import me.henry.versatile.nativenet.Request;
import me.henry.versatile.nativenet.error.AppException;
import me.henry.versatile.nativenet.itf.OnProgressUpdatedListener;

public class RequestTask extends AsyncTask<Void, Integer, Object> {

    private Request request;

    public RequestTask(Request request) {
        this.request = request;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Void... params) {
        if (request.iCallback != null) {
            Object o = request.iCallback.preRequest();
            if (o != null) {
                return o;
            }
        }
        return request(0);
    }


    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    public Object request(int retry) {
        try {
//                FIXME: for HttpUrlConnection
            HttpURLConnection connection = null;
            if (request.tool == Request.RequestTool.URLCONNECTION) {
                connection = HttpUrlConnectionUtil.execute(request, !request.enableProgressUpdated ? null : new OnProgressUpdatedListener() {
                    @Override
                    public void onProgressUpdated(int curLen, int totalLen) {
                        publishProgress(Request.STATE_UPLOAD, curLen, totalLen);
                    }
                });
            } else {
//                FIXME : for OkHttpUrlConnection request
                connection = OKHttpUrlConnectionUtil.execute(request, !request.enableProgressUpdated ? null : new OnProgressUpdatedListener() {
                    @Override
                    public void onProgressUpdated(int curLen, int totalLen) {
                        publishProgress(Request.STATE_UPLOAD, curLen, totalLen);
                    }
                });
            }
            if (request.enableProgressUpdated) {
                return request.iCallback.parse(connection, new OnProgressUpdatedListener() {
                    @Override
                    public void onProgressUpdated(int curLen, int totalLen) {
                        publishProgress(Request.STATE_DOWNLOAD, curLen, totalLen);
                    }
                });
            } else {
                return request.iCallback.parse(connection);
            }
        } catch (AppException e) {
            if (e.type == AppException.ErrorType.TIMEOUT) {
                if (retry < request.maxRetryCount) {
                    retry++;
                    return request(retry);
                }
            }
            return e;
        }
    }


    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        request.isCompleted = true;
        if (o instanceof AppException) {
            if (request.onGlobalExceptionListener != null) {
                if (!request.onGlobalExceptionListener.handleException((AppException) o)) {
                    request.iCallback.onFailure((AppException) o);
                }
            } else {
                request.iCallback.onFailure((AppException) o);
            }
        } else {
            request.iCallback.onSuccess(o);
        }


    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        request.iCallback.onProgressUpdated(values[0], values[1], values[2]);

    }
}
