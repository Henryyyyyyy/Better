package com.example.lib_db.utilities;

import com.google.gson.Gson;

import java.lang.reflect.Type;


/** 
 * @author Stay  
 * @version create time：Nov 14, 2014 5:31:43 PM 
 */
public class JsonUtil {

	public static String toJson(Object object) {
		Gson gson = new Gson();
		return gson.toJson(object);
	}
	
	public static Object fromJson(String content,Type type){
		Gson gson = new Gson();
		return gson.fromJson(content, type);
	}

}
