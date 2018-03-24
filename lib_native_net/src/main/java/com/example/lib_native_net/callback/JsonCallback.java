package com.example.lib_native_net.callback;



import com.example.lib_native_net.core.AbstractCallback;
import com.example.lib_native_net.error.AppException;
import com.google.gson.Gson;


import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;



public abstract class JsonCallback<T> extends AbstractCallback<T> {
    @Override
    protected T bindData(String result) throws AppException {
        try {
//
            JSONObject json = new JSONObject(result);
            Object data = json.opt("data");
            Gson gson = new Gson();
            Type type = ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            return gson.fromJson(data.toString(), type);
        } catch (JSONException e) {
            throw new AppException(AppException.ErrorType.JSON,e.getMessage());
        }
    }
}
