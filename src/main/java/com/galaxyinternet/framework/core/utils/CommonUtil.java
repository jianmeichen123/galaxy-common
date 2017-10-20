package com.galaxyinternet.framework.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class CommonUtil {
	public static boolean isValidDate(String str) {
       boolean convertSuccess=true;
       SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
       try {
           format.setLenient(false);
           format.parse(str);
       } catch (ParseException e) {
           convertSuccess=false;
       } 
           return convertSuccess;
	}
}
