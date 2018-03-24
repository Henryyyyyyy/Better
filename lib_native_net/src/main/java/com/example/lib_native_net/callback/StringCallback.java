package com.example.lib_native_net.callback;


import com.example.lib_native_net.core.AbstractCallback;
import com.example.lib_native_net.error.AppException;

public abstract class StringCallback extends AbstractCallback<String> {

    @Override
    protected String bindData(String result) throws AppException {
        return result;
    }
}
