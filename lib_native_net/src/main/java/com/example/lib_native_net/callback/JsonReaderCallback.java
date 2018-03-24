package com.example.lib_native_net.callback;



import android.util.JsonReader;

import com.example.lib_native_net.core.AbstractCallback;
import com.example.lib_native_net.entities.JsonReaderable;
import com.example.lib_native_net.error.AppException;

import java.io.FileReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;



public abstract class JsonReaderCallback<T extends JsonReaderable> extends AbstractCallback<T> {
    @Override
    protected T bindData(String path) throws AppException {
        try {
            Type type = ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            T t = ((Class<T>)type).newInstance();

            FileReader in = new FileReader(path);
            JsonReader reader = new JsonReader(in);
            String node;
            reader.beginObject();
            while(reader.hasNext()){
                node = reader.nextName();
                if ("data".equalsIgnoreCase(node)){
                    t.readFromJson(reader);
                }else {
                    reader.skipValue();
                }
            }
            reader.endObject();
            return t;

        } catch (Exception e) {
            throw new AppException(AppException.ErrorType.JSON,e.getMessage());
        }
    }
}
