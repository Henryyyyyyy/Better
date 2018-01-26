package me.henry.versatile.nativenet.callback;


import android.util.JsonReader;

import java.io.FileReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

import me.henry.versatile.nativenet.core.AbstractCallback;
import me.henry.versatile.nativenet.entities.JsonReaderable;
import me.henry.versatile.nativenet.error.AppException;


public abstract class JsonArrayReaderCallback<T extends JsonReaderable> extends AbstractCallback<ArrayList<T>> {
    @Override
    protected ArrayList<T> bindData(String path) throws AppException {
        try {

            Type type = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            ArrayList<T> ts = new ArrayList<T>();
            T t;

            FileReader in = new FileReader(path);
            JsonReader reader = new JsonReader(in);
            String node;
            boolean isSuitType = false;
            reader.beginObject();
            while (reader.hasNext()) {
                node = reader.nextName();
                if ("itemList".equalsIgnoreCase(node)) {
                    reader.beginArray();//开始itemlist
                    while (reader.hasNext()) {
                        reader.beginObject();//开始itemlist obj
                        isSuitType = false;
                        while (reader.hasNext()) {
                            node = reader.nextName();
                            if ("type".equalsIgnoreCase(node)) {
                                if ("video".equalsIgnoreCase(reader.nextString())) {
                                    isSuitType = true;//如果是video type的话，就给他读
                                } else {
                                    isSuitType = false;
                                }

                            } else if ("data".equalsIgnoreCase(node)) {
                                //todo 遍历自己需要的
                                if (isSuitType){
                                    t = ((Class<T>) type).newInstance();
                                    t.readFromJson(reader);
                                    ts.add(t);
                                }

                            } else {
                                reader.skipValue();
                            }

                        }
                        reader.endObject();//结束itemlist obj

                    }
                    reader.endArray();//结束itemlist
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
            return ts;

        } catch (Exception e) {
            throw new AppException(AppException.ErrorType.JSON, e.getMessage());
        }
    }
}
