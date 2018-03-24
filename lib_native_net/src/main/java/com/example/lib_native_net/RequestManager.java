package com.example.lib_native_net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class RequestManager {
    private static RequestManager mInstance;
    private final ExecutorService mExecutors;
    private HashMap<String, ArrayList<Request>> mCachedRequest;

    public static RequestManager getInstance() {
        if (mInstance == null) {
            mInstance = new RequestManager();
        }
        return mInstance;
    }

    private RequestManager() {

        mCachedRequest = new HashMap<String, ArrayList<Request>>();
        mExecutors = Executors.newFixedThreadPool(5);

    }

    public void performRequest(Request request) {
        request.execute(mExecutors);
        if (request.tag == null) {
            return;// no need to cache the request
        }
        if (!mCachedRequest.containsKey(request.tag)) {
            ArrayList<Request> requests = new ArrayList<Request>();
            mCachedRequest.put(request.tag, requests);
        }
        mCachedRequest.get(request.tag).add(request);
    }

    public void cancelRequest(String tag) {
        cancelRequest(tag, false);
    }

    /**
     * @param tag
     * @param force true cancel task with no callback, false cancel task with callback as CancelException
     */
    public void cancelRequest(String tag, boolean force) {
        if (tag == null || "".equals(tag.trim())) {
            return;
        }
        if (mCachedRequest.containsKey(tag)) {
            ArrayList<Request> requests = mCachedRequest.remove(tag);
            for (Request request : requests) {
                if (!request.isCompleted && !request.isCancelled) {
                    request.cancel(force);
                }
            }
        }

    }

    public void cancelAll() {
        for (Map.Entry<String, ArrayList<Request>> entry : mCachedRequest.entrySet()) {
            ArrayList<Request> requests = entry.getValue();
            for (Request request : requests) {
                if (!request.isCompleted && !request.isCancelled) {
                    request.cancel(true);
                }
            }
        }
        mCachedRequest.clear();
    }


}
