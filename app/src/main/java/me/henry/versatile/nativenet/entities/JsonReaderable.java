package me.henry.versatile.nativenet.entities;


import android.util.JsonReader;

import me.henry.versatile.nativenet.error.AppException;


public interface JsonReaderable {
    void readFromJson(JsonReader reader) throws AppException;

}
