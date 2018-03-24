package com.example.lib_native_net.callback;


import com.example.lib_native_net.core.AbstractCallback;
import com.example.lib_native_net.error.AppException;

public abstract class XMLCallback<T> extends AbstractCallback<T> {

    @Override
    protected T bindData(String result) throws AppException {


        return null;
    }
}
