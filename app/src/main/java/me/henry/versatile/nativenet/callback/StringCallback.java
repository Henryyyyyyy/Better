package me.henry.versatile.nativenet.callback;


import me.henry.versatile.nativenet.core.AbstractCallback;
import me.henry.versatile.nativenet.error.AppException;


public abstract class StringCallback extends AbstractCallback<String> {

    @Override
    protected String bindData(String result) throws AppException {
        return result;
    }
}
