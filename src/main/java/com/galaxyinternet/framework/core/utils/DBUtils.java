package com.galaxyinternet.framework.core.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class DBUtils {
	private static DBUtils dbUtils = null;
	
	
	public static synchronized DBUtils get(){
		if(dbUtils==null){
			DBUtils.dbUtils = new DBUtils();
		}
		return dbUtils;
	}
	
	@SuppressWarnings("rawtypes")
    public Class getSuperClassGenericType(final Class clazz, final int index){  
        Type genericType = clazz.getGenericSuperclass();  
        if(!(genericType instanceof ParameterizedType)){  
            return Object.class;  
        }  
        Type[] params = ((ParameterizedType) genericType).getActualTypeArguments();  
          
          
        if(index>=params.length || index < 0){  
            return Object.class;  
        }  
        if(!(params[index] instanceof Class)){  
            return Object.class;  
        }  
        return (Class)params[index];   
    }  

}
