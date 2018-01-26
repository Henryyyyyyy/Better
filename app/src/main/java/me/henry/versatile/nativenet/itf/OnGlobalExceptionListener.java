package me.henry.versatile.nativenet.itf;


import me.henry.versatile.nativenet.error.AppException;


public interface OnGlobalExceptionListener {

    boolean handleException(AppException exception);
}
