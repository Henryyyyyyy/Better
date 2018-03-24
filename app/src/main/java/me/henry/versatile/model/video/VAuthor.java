package me.henry.versatile.model.video;

import android.util.JsonReader;

import com.example.lib_native_net.entities.JsonReaderable;
import com.example.lib_native_net.error.AppException;

import java.io.IOException;
import java.io.Serializable;


/**
 * Created by henry on 2017/12/20.
 */

public class VAuthor implements JsonReaderable, Serializable {
    public int id;
    public String icon;
    public String name;
    public String description;
    public static final String Node_id = "id";
    public static final String Node_icon = "icon";
    public static final String Node_name = "name";
    public static final String Node_description = "description";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void readFromJson(JsonReader reader) throws AppException {

        try {
            String node;
            reader.beginObject();
            while (reader.hasNext()) {
                node = reader.nextName();
                if (Node_id.equalsIgnoreCase(node)) {
                    id = reader.nextInt();
                } else if (Node_icon.equalsIgnoreCase(node)) {
                    icon = reader.nextString();
                } else if (Node_name.equalsIgnoreCase(node)) {
                    name = reader.nextString();
                } else if (Node_description.equalsIgnoreCase(node)) {
                    description = reader.nextString();
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
