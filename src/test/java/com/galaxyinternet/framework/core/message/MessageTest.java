package com.galaxyinternet.framework.core.message;

import java.io.IOException;

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
	
	@Test
	public void testSub()
	{
		String key = container.subscribe(new MessageListener(){

			@Override
			public void onMessage(Message message, String channel)
			{
				System.out.println("sub1-1 :"+message);
				
			}
			
		}, "channel1");
		System.out.println("key = "+key);
		
		key = container.subscribe(new MessageListener(){

			@Override
			public void onMessage(Message message, String channel)
			{
				System.out.println("sub1-2:"+message);
				
			}
			
		}, "channel1");
		System.out.println("key = "+key);
		
		key = container.subscribe(new MessageListener(){

			@Override
			public void onMessage(Message message, String channel)
			{
				System.out.println("sub2-1:"+message);
				
			}
			
		}, "channel2");
		System.out.println("key = "+key);
		
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
		Message message = new Message();
		container.publish("channel1", message);
		sleep(5000);
		message = new Message();
		container.publish("channel1", message);
		sleep(5000);
		message = new Message();
		container.publish("channel1", message);
		sleep(5000);
		message = new Message();
		container.publish("channel2", message);
		
	}
	@Test
	public void testUnsub()
	{
		String key = "mq:channel1:1";
		container.unsubscribe(key);
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
