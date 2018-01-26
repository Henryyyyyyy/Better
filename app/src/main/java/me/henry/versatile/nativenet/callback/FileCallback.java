package me.henry.versatile.nativenet.callback;


import me.henry.versatile.nativenet.core.AbstractCallback;
import me.henry.versatile.nativenet.error.AppException;


public abstract class FileCallback extends AbstractCallback<String> {

    @Override
    protected String bindData(String path) throws AppException {
        return path;
    }
}
