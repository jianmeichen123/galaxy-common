package com.galaxyinternet.framework.core.message;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.galaxyinternet.framework.cache.Cache;
import com.galaxyinternet.framework.core.thread.GalaxyThreadPool;
import com.galaxyinternet.framework.core.utils.ObjectUtils;

public class TopicClient
{
	private Logger logger = LoggerFactory.getLogger(TopicClient.class);
	private String topicName;
	private Cache cache;
	private ExecutorService threadPool = GalaxyThreadPool.getExecutorService();
	private Map<String,MessageListener> listenerMap = new ConcurrentHashMap<>();
	private AtomicBoolean start = new AtomicBoolean(false);
	public TopicClient(String topicName, Cache cache)
	{
		this.topicName = topicName;
		this.cache = cache;
	}
	public void publish(Message message)
	{
		Set<String> members = cache.smembers(GalaxyMQConstants.TOPIC_MEMBER_PREFIX+topicName);
		for(String member : members)
		{
			cache.lpush(member.getBytes(), ObjectUtils.serialize(message));
		}
	}
	public String subscribe(MessageListener listener)
	{
		Long seq = cache.incrBy(GalaxyMQConstants.TOPIC_SEQ_PREFIX+topicName, 1);
		final String key = GalaxyMQConstants.PREFIX+topicName+":"+seq;
		cache.sadd(GalaxyMQConstants.TOPIC_MEMBER_PREFIX+topicName, key);
		listenerMap.put(key, listener);
		if(start.compareAndSet(false, true))
		{
			logger.debug("Subscriber("+key+") for topic "+topicName);
			threadPool.submit(new Runnable(){
				@Override
				public void run()
				{
					while(true)
					{
						if(!start.get())
						{
							logger.debug("All subscribers was unsubscribed topic "+topicName);
							break;
						}
						for(Entry<String, MessageListener> entry : listenerMap.entrySet())
						{
							final String key = entry.getKey();
							final MessageListener listener = entry.getValue();
							byte[] bytes = cache.rpop(key.getBytes());
							if(bytes == null)
							{
								try
								{
									Thread.sleep(500);
								}
								catch (InterruptedException e)
								{
									e.printStackTrace();
								}
								continue;
							}
							Message message = (Message)ObjectUtils.deserialize(bytes);
							listener.onMessage(message, topicName);
						}
					}
				}
				
			});
		}
		return key;
	}
	public synchronized void unsubscribe(String key)
	{
		if(listenerMap.containsKey(key))
		{
			logger.debug(key+" unsubscribe "+topicName);
			listenerMap.remove(key);
			if(listenerMap.size() == 0)
			{
				start.set(false);
			}
		}
	}
	
	
}
