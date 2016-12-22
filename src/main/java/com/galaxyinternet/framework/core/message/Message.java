package com.galaxyinternet.framework.core.message;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Message implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Map<String,Object> data = new HashMap<>();
	private boolean stop;
	
	public void setStop(boolean stop)
	{
		this.stop = stop;
	}
	public boolean stop()
	{
		return stop;
	}
	
	public void addData(String key, Object value)
	{
		data.put(key, value);
	}
	@SuppressWarnings("unchecked")
	public <T> T getData(String key)
	{
		if(data.containsKey(key))
		{
			return (T)data.get(key);
		}
		return null;
	}
}
