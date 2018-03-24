package com.example.lib_native_net.core;

import android.webkit.URLUtil;

import com.example.lib_native_net.Request;
import com.example.lib_native_net.error.AppException;
import com.example.lib_native_net.itf.OnProgressUpdatedListener;
import com.example.lib_native_net.upload.UploadUtil;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;



//import com.squareup.okhttp.OkHttpClient;
//import com.squareup.okhttp.OkUrlFactory;
//import com.stay4it.http.Request;
//import com.stay4it.http.error.AppException;
//import com.stay4it.http.itf.OnProgressUpdatedListener;
//import com.stay4it.http.upload.UploadUtil;
import okhttp3.OkHttpClient;
import okhttp3.OkUrlFactory;
import okhttp3.internal.huc.OkHttpURLConnection;

import static com.example.lib_native_net.Request.RequestMethod.GET;


public class OKHttpUrlConnectionUtil {
    private static OkHttpClient mClient;

    public synchronized static HttpURLConnection execute(Request request, OnProgressUpdatedListener listener) throws AppException {
        if (!URLUtil.isNetworkUrl(request.url)) {
            throw new AppException(AppException.ErrorType.MANUAL,"the url :" + request.url + " is not valid");
        }
        if (mClient == null){
            initializeOkHttp();
        }
        switch (request.method) {
            case GET:
            case DELETE:
                return get(request);
            case POST:
            case PUT:
                return post(request,listener);
        }

        return null;
    }

    private static void initializeOkHttp() {
        mClient = new OkHttpClient();
    }


    private static HttpURLConnection get(Request request) throws AppException {
        try {

            request.checkIfCancelled();

//            HttpURLConnection connection = new OkUrlFactory(mClient).open(new URL(request.url));
            HttpURLConnection connection = new OkHttpURLConnection(new URL(request.url),mClient);

            connection.setRequestMethod(request.method.name());
            connection.setConnectTimeout(15 * 3000);
            connection.setReadTimeout(15 * 3000);

            addHeader(connection, request.headers);

            request.checkIfCancelled();
            return connection;
        } catch (MalformedURLException e) {
            throw new AppException(AppException.ErrorType.SERVER, e.getMessage());
        } catch (ProtocolException e) {
            throw new AppException(AppException.ErrorType.SERVER, e.getMessage());
        }
    }


    private static HttpURLConnection post(Request request,OnProgressUpdatedListener listener) throws AppException {
        HttpURLConnection connection = null;
        OutputStream os = null;
        try {
            request.checkIfCancelled();

            connection = new OkUrlFactory(mClient).open(new URL(request.url));
            connection.setRequestMethod(request.method.name());
            connection.setConnectTimeout(15 * 3000);
            connection.setReadTimeout(15 * 3000);
            connection.setDoOutput(true);


            addHeader(connection, request.headers);
            request.checkIfCancelled();

             os = connection.getOutputStream();
            if (request.filePath != null){
                UploadUtil.upload(os, request.filePath);
            }else if(request.fileEntities != null){
                UploadUtil.upload(os,request.content,request.fileEntities,listener);
            }else if(request.content != null){
                os.write(request.content.getBytes());
            }else {
                throw new AppException(AppException.ErrorType.MANUAL,"the post request has no post content");
            }

            request.checkIfCancelled();
        } catch (InterruptedIOException e) {
            throw new AppException(AppException.ErrorType.TIMEOUT, e.getMessage());
        } catch (IOException e) {
            throw new AppException(AppException.ErrorType.SERVER, e.getMessage());
        }finally {
            try {
                os.flush();
                os.close();
            } catch (IOException e) {
                throw new AppException(AppException.ErrorType.IO, "the post outputstream can't be closed");
            }
        }

        return connection;
    }

    private static void addHeader(HttpURLConnection connection, Map<String, String> headers) {
        if (headers == null || headers.size() == 0)
            return;

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            connection.addRequestProperty(entry.getKey(), entry.getValue());
        }
    }
}
