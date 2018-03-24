package com.example.lib_native_net.itf;


import com.example.lib_native_net.error.AppException;

public interface OnGlobalExceptionListener {

    boolean handleException(AppException exception);
}
