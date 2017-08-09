package com.galaxyinternet.framework.core.utils;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

public class ZKUtils
{
	public static ZooKeeper connect(String address) throws Exception
	{
		final CountDownLatch latch = new CountDownLatch(1);
		ZooKeeper zk = new ZooKeeper(address, 30000, new Watcher(){
			@Override
			public void process(WatchedEvent event)
			{
				if(event.getState() == KeeperState.SyncConnected)
				{
					latch.countDown();
				}
			}
		}); 
		latch.await();
		return zk;
	}
	
	public static void close(ZooKeeper zk) throws Exception
	{
		if(zk != null)
		{
			zk.close();
		}
	}
	
}
