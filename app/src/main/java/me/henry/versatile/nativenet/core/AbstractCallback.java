package me.henry.versatile.nativenet.core;



import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import me.henry.versatile.nativenet.error.AppException;
import me.henry.versatile.nativenet.itf.ICallback;
import me.henry.versatile.nativenet.itf.OnProgressUpdatedListener;

public abstract class AbstractCallback<T> implements ICallback<T> {


    private String path;
    private volatile boolean isCancelled;

    @Override
    public T parse(HttpURLConnection connection) throws AppException {
        return parse(connection, null);
    }

    @Override
    public T parse(HttpURLConnection connection, OnProgressUpdatedListener listener) throws AppException {
        try {
            checkIfCancelled();
            InputStream is = null;
            int status = connection.getResponseCode();
//            TODO normally 200 represent success, but you should know 200-299 all means success, please ensure with your backend colleagues
            if (status == HttpURLConnection.HTTP_OK) {

//                support gzip || deflate
                String encoding = connection.getContentEncoding();
                if (encoding != null && "gzip".equalsIgnoreCase(encoding))
                    is = new GZIPInputStream(connection.getInputStream());
                else if (encoding != null
                        && "deflate".equalsIgnoreCase(encoding))
                    is = new InflaterInputStream(connection.getInputStream());
                else {
                    is = connection.getInputStream();
                }


                if (path == null) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    byte[] buffer = new byte[2048];
                    int len;
                    while ((len = is.read(buffer)) != -1) {
                        checkIfCancelled();
                        out.write(buffer, 0, len);
                    }
                    is.close();
                    out.flush();
                    out.close();
                    String result = new String(out.toByteArray());
                    T t = bindData(result);
                    return postRequest(t);
                } else {
                    FileOutputStream out = new FileOutputStream(path);

                    int totalLen = connection.getContentLength();
                    int curLen = 0;
                    byte[] buffer = new byte[2048];
                    int len;
                    int percent = 0;
                    while ((len = is.read(buffer)) != -1) {
                        checkIfCancelled();
                        out.write(buffer, 0, len);
                        if (curLen * 100l / totalLen > percent) {
                            curLen += len;
                            if (listener != null) {
                                listener.onProgressUpdated(curLen, totalLen);
                            }
                            percent = (int) (curLen * 100l / totalLen);
                        }
                    }
                    is.close();
                    out.flush();
                    out.close();
                    T t = bindData(path);
                    return postRequest(t);
                }
            } else {
//                connection.getResponseMessage();
//                connection.getErrorStream();
                throw new AppException(status, connection.getResponseMessage());
            }
        } catch (IOException e) {
            throw new AppException(AppException.ErrorType.SERVER, e.getMessage());
        }
    }

    protected void checkIfCancelled() throws AppException {
        if (isCancelled) {
            throw new AppException(AppException.ErrorType.CANCEL, "the request has been cancelled");
        }
    }

    @Override
    public void cancel() {
        isCancelled = true;
    }

    @Override
    public void onProgressUpdated(int state, int curLen, int totalLen) {

    }

    @Override
    public T postRequest(T t) {
        return t;
    }

    @Override
    public T preRequest() {
        return null;
    }

    protected abstract T bindData(String result) throws AppException;


    public ICallback setCachePath(String path) {
        this.path = path;
        return this;
    }

}
