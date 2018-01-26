package me.henry.versatile.model.video;

import android.util.JsonReader;

import java.io.IOException;
import java.io.Serializable;

import me.henry.versatile.nativenet.entities.JsonReaderable;
import me.henry.versatile.nativenet.error.AppException;
import me.henry.versatile.utils.CanuteLog;

/**
 * Created by henry on 2017/12/20.
 */

public class VideoModel implements JsonReaderable, Serializable {
    public static final String Node_dataType = "dataType";
    public static final String Node_id = "id";
    public static final String Node_title = "title";
    public static final String Node_description = "description";
    public static final String Node_category = "category";
    public static final String Node_author = "author";
    public static final String Node_detail = "detail";
    public static final String Node_cover = "cover";
    public static final String Node_playUrl = "playUrl";
    public static final String Node_duration = "duration";
    public static final String Node_releaseTime = "releaseTime";
    public String dataType;
    public int id;
    public String title;
    public String description;
    public String category;
    public VAuthor author;
    public String cover;//取detail字段
    public String playUrl;
    public int duration;
    public long releaseTime;


    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public VAuthor getAuthor() {
        return author;
    }

    public void setAuthor(VAuthor author) {
        this.author = author;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(long releaseTime) {
        this.releaseTime = releaseTime;
    }


    @Override
    public void readFromJson(JsonReader reader) throws AppException {
        try {
            reader.beginObject();
            String node;
            while (reader.hasNext()) {
                node = reader.nextName();
                if (Node_dataType.equalsIgnoreCase(node)) {
                    dataType = reader.nextString();
                } else if (Node_id.equalsIgnoreCase(node)) {
                    id = reader.nextInt();
                } else if (Node_title.equalsIgnoreCase(node)) {
                    title = reader.nextString();
                } else if (Node_description.equalsIgnoreCase(node)) {
                    description = reader.nextString();
                } else if (Node_category.equalsIgnoreCase(node)) {
                    category = reader.nextString();
                } else if (Node_author.equalsIgnoreCase(node)) {
                    author = new VAuthor();
                    author.readFromJson(reader);
                } else if (Node_cover.equalsIgnoreCase(node)) {
                    reader.beginObject();
                    while (reader.hasNext()) {
                        node = reader.nextName();
                        if (Node_detail.equalsIgnoreCase(node)) {
                            cover = reader.nextString();
                        } else {
                            reader.skipValue();
                        }
                    }
                    reader.endObject();
                } else if (Node_playUrl.equalsIgnoreCase(node)) {
                    playUrl = reader.nextString();
                } else if (Node_duration.equalsIgnoreCase(node)) {
                    duration = reader.nextInt();
                } else if (Node_releaseTime.equalsIgnoreCase(node)) {
                    releaseTime = reader.nextLong();
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
        } catch (IOException e) {
            throw new AppException(AppException.ErrorType.JSON, e.getMessage());
        }
    }
}
