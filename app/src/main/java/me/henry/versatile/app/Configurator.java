package me.henry.versatile.app;

import android.app.Activity;
import android.os.Handler;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Interceptor;



public final class Configurator {

    private static final HashMap<Object, Object> CANUTE_CONFIGS = new HashMap<>();
    private static final Handler HANDLER = new Handler();

    private Configurator() {
        CANUTE_CONFIGS.put(ConfigKeys.CONFIG_READY, false);
        CANUTE_CONFIGS.put(ConfigKeys.HANDLER, HANDLER);
    }

    static Configurator getInstance() {
        return Holder.INSTANCE;
    }

    final HashMap<Object, Object> getCanuteConfigs() {
        return CANUTE_CONFIGS;
    }

    private static class Holder {
        private static final Configurator INSTANCE = new Configurator();
    }

    public final void configure() {
        CANUTE_CONFIGS.put(ConfigKeys.CONFIG_READY, true);
    }

    public final Configurator withApiHost(String host) {
        CANUTE_CONFIGS.put(ConfigKeys.API_HOST, host);
        return this;
    }


    public final Configurator withActivity(Activity activity) {
        CANUTE_CONFIGS.put(ConfigKeys.ACTIVITY, activity);
        return this;
    }

  

    private void checkConfiguration() {
        final boolean isReady = (boolean) CANUTE_CONFIGS.get(ConfigKeys.CONFIG_READY);
        if (!isReady) {
            throw new RuntimeException("Configuration is not ready,call configure");
        }
    }

    @SuppressWarnings("unchecked")
    final <T> T getConfiguration(Object key) {
        checkConfiguration();
        final Object value = CANUTE_CONFIGS.get(key);
        if (value == null) {
            throw new NullPointerException(key.toString() + " IS NULL");
        }
        return (T) CANUTE_CONFIGS.get(key);
    }
}
