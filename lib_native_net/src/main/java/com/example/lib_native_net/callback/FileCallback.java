package com.example.lib_native_net.callback;


import com.example.lib_native_net.core.AbstractCallback;
import com.example.lib_native_net.error.AppException;

public abstract class FileCallback extends AbstractCallback<String> {

    @Override
    protected String bindData(String path) throws AppException {
        return path;
    }
}
