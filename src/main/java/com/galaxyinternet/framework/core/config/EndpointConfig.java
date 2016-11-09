package com.galaxyinternet.framework.core.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.galaxyinternet.framework.cache.Cache;

public class EndpointConfig implements InitializingBean
{
	private String name;
	private String endpoint;
	@Autowired
	private Cache cache;
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getEndpoint()
	{
		return endpoint;
	}
	public void setEndpoint(String endpoint)
	{
		this.endpoint = endpoint;
	}
	@Override
	public void afterPropertiesSet() throws Exception
	{
		cache.hset("endpoints", name, endpoint);
	}
	
}
