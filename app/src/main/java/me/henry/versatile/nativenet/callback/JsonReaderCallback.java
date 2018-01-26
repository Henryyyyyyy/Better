package me.henry.versatile.nativenet.callback;



import android.util.JsonReader;

import java.io.FileReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import me.henry.versatile.nativenet.core.AbstractCallback;
import me.henry.versatile.nativenet.entities.JsonReaderable;
import me.henry.versatile.nativenet.error.AppException;

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
