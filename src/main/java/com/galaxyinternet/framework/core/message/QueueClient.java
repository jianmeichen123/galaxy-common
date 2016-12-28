package com.galaxyinternet.framework.core.message;

import com.galaxyinternet.framework.cache.Cache;
import com.galaxyinternet.framework.core.utils.ObjectUtils;

public class QueueClient
{
	private String queueName;
	private Cache cache;
	public QueueClient(String queueName, Cache cache)
	{
		this.queueName = queueName;
		this.cache = cache;
	}
	
	public void send(Message message)
	{
		cache.lpush(getQueueName().getBytes(), ObjectUtils.serialize(message));
	}

	public Message receive()
	{
		byte[] bytes = cache.rpop(getQueueName().getBytes());
		if(bytes != null)
		{
			return (Message)ObjectUtils.deserialize(bytes);
		}
		return null;
	}
	
	private String getQueueName()
	{
		return GalaxyMQConstants.QUEUE_PREFIX+queueName;
	}
	
	
	
	
}
