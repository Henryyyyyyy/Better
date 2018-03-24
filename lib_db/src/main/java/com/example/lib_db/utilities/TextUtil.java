package com.example.lib_db.utilities;

import java.util.ArrayList;


public class TextUtil {
	public static boolean isValidate(String content){
		if(content != null && !"".equals(content.trim())){
			return true;
		}
		return false;
	}
	
	public static boolean isValidatelist(ArrayList list){
		if (list != null && list.size() >0) {
			return true;
		}
		return false;
	}
}
