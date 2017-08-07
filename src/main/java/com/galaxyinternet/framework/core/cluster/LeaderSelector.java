package com.galaxyinternet.framework.core.cluster;

import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.InitializingBean;

import com.galaxyinternet.framework.core.id.IdGenerator;
import com.galaxyinternet.framework.core.utils.ZKUtils;


public class LeaderSelector implements Watcher, InitializingBean
{
	private static final Logger logger = Logger.getLogger(LeaderSelector.class);
	private String address;
	private String path;
	private String id;
	private String leaderId;
	private boolean isLeader;
	ZooKeeper zk = null;

	public LeaderSelector(String address, String product)
	{
		super();
		this.address = address;
		this.path = "/leader_"+product;
		this.id = IdGenerator.generateId(LeaderSelector.class)+"_"+new Date().getTime();
	}

	public void start()
	{
		try
		{
			zk = ZKUtils.connect(address);
			try
			{
				zk.create(path, id.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
			} catch (KeeperException e)
			{
				logger.debug(String.format("节点[%s]已经存在", path));
			}
			byte[] value = zk.getData(path, this, null);
			leaderId = new String(value);
			if(leaderId.equals(id))
			{
				isLeader = true;
			}
			logger.debug(String.format("选主结束,当前ID=%s, Leader节点ID=%s, 当前节点是否为Leader=%s", id,leaderId,isLeader));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void stop()
	{
		try
		{
			ZKUtils.close(zk);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void process(WatchedEvent event)
	{
		if(event.getType() == Event.EventType.NodeDeleted)
		{
			logger.debug(String.format("Leader节点[ID=%s]失效，开始重新选取", leaderId));
			start();
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		start();
	}

	public String getId()
	{
		return id;
	}

	public String getLeaderId()
	{
		return leaderId;
	}

	public boolean isLeader()
	{
		return isLeader;
	}
}
