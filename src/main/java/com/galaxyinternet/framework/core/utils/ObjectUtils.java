package com.galaxyinternet.framework.core.utils;

public class ObjectUtils
{
	public static boolean notNull(Object... objs)
	{
		for(Object obj : objs)
		{
			if(obj == null) return false;
		}
		return true;
	}
}
