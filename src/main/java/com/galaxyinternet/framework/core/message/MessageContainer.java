package com.galaxyinternet.framework.core.message;

import org.springframework.beans.factory.annotation.Autowired;

import com.galaxyinternet.framework.cache.Cache;



public class MessageContainer
{
	@Autowired
	private Cache cache;
	
	public QueueClient createQueueClient(String queueName)
	{
		return new QueueClient(queueName, cache);
	}
	
	public TopicClient createTopicClient(String topicName)
	{
		return new TopicClient(topicName, cache);
	}
	
}
