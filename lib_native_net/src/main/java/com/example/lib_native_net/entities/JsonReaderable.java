package com.example.lib_native_net.entities;


import android.util.JsonReader;

import com.example.lib_native_net.error.AppException;


public interface JsonReaderable {
    void readFromJson(JsonReader reader) throws AppException;

}
