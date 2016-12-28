package com.galaxyinternet.framework.core.message;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext-cache.xml","classpath*:spring-message.xml"})
public class MessageTest
{
	@Autowired
	MessageContainer container;
	ExecutorService pool = Executors.newFixedThreadPool(4);
	
	@Test
	public void testQueue()
	{
		final QueueClient queue = container.createQueueClient("testqueue");
		final AtomicInteger index = new AtomicInteger(0);
		int i = 0;
		for(;;)
		{
			if(i>300)
			{
				break;
			}
			pool.submit(new Runnable(){
				@Override
				public void run()
				{
					
					Message message = new Message();
					message.addData("testmessage", "message value "+index.incrementAndGet());
					System.out.println("put message"+index.get());
					queue.send(message);
				}
			});
			i++;
		}
		pool.submit(new Runnable(){
			@Override
			public void run()
			{
				while(true)
				{
					sleep(500);
					Message message = queue.receive();
					if(message == null) break;
					System.err.println(message.getData("testmessage"));
				}
			}
			
		});
		try
		{
			System.in.read();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testSub()
	{
		final TopicClient topic = container.createTopicClient("testtopic");
		String key1 = topic.subscribe(new MessageListener(){
			@Override
			public void onMessage(Message message, String topic)
			{
				System.out.println("sub 1 receive tipic =" +topic+" message= "+message.getData("testmessage"));
			}
		});
		sleep(10000);
		topic.unsubscribe(key1);
		
		String key2 = topic.subscribe(new MessageListener(){
			@Override
			public void onMessage(Message message, String topic)
			{
				System.out.println("sub 2 receive tipic =" +topic+" message= "+message.getData("testmessage"));
			}
		});
		sleep(10000);
		topic.unsubscribe(key2);
		
		try
		{
			System.in.read();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void testPub()
	{
		final TopicClient topic = container.createTopicClient("testtopic");
		Message message = null;
		int i=0;
		for(;;)
		{
			if(i>100) break;
			
			message = new Message();
			message.addData("testmessage", "test val"+i);
			topic.publish(message);
			sleep(100);
			i++;
			
		}
		
	}
	@Test
	public void testUnsub()
	{
		
	}
	
	private void sleep(long m)
	{
		try
		{
			Thread.sleep(m);
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
