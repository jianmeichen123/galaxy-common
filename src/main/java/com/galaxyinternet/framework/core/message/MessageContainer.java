package com.galaxyinternet.framework.core.message;

import java.util.Set;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.galaxyinternet.framework.cache.Cache;
import com.galaxyinternet.framework.core.thread.GalaxyThreadPool;
import com.galaxyinternet.framework.core.utils.ObjectUtils;



public class MessageContainer
{
	private Logger logger = LoggerFactory.getLogger(MessageContainer.class);
	@Autowired
	private Cache cache;
	private ExecutorService pool = GalaxyThreadPool.getExecutorService();
	private static final String PREFIX = "mq:";
	private static final String CHANNEL_SUB_MEMBER_PREFIX = PREFIX+"sub_member:";
	public void publish(String channel, Message message)
	{
		Set<String> members = cache.smembers(CHANNEL_SUB_MEMBER_PREFIX+channel);
		for(String member : members)
		{
			cache.lpush((PREFIX+member).getBytes(), ObjectUtils.serialize(message));
		}
	}
	public String subscribe(final MessageListener listener, final String channel)
	{
		Long seq = cache.incrBy(PREFIX+channel, 1);
		final String key = PREFIX+channel+"/"+seq;
		cache.sadd(CHANNEL_SUB_MEMBER_PREFIX+channel, key);
		if (logger.isDebugEnabled())
		{
			logger.debug("Subscriber: "+key);
		}
		pool.execute(new SubscribeTask(key,listener));
		return key;
	}
	public void unsubscribe(String key)
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Unsubscribe "+key);
		}
		Message endMessage = new Message(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public boolean stop()
			{
				return true;
			}
		};
		cache.rpush((PREFIX+key).getBytes(), ObjectUtils.serialize(endMessage));
	}
	public Cache getCache()
	{
		return cache;
	}
	public void setCache(Cache cache)
	{
		this.cache = cache;
	}
	
	private final class SubscribeTask implements Runnable
	{
		private String key;
		private MessageListener listener;
		private String channel;
		

		public SubscribeTask(String key, MessageListener listener)
		{
			this.key = key;
			this.listener = listener;
			this.channel = key.split("/")[0];
		}


		@Override
		public void run()
		{
			while(true)
			{
				byte[] bytes = cache.rpop(key.getBytes());
				if(bytes == null)
				{
					try
					{
						Thread.sleep(3000);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
					continue;
				}
				Object obj = ObjectUtils.deserialize(bytes);
				if(Message.class.isInstance(obj))
				{
					Message message = (Message)obj;
					if(message.stop())
					{
						cache.removeRedisKeyOBJ(key);
						if(logger.isDebugEnabled())
						{
							logger.debug(key+" undescribe "+channel);
						}
						break;
					}
					listener.onMessage((Message)obj, channel);
				}
				
			}
			
		}
		
	}
	
}
