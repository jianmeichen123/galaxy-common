package com.galaxyinternet.framework.core.task;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;

import com.galaxyinternet.framework.core.thread.GalaxyThreadPool;
/**
 * 任务队列执行工具
 * @see com.galaxyinternet.framework.core.task.GalaxyTask
 * @author WangSong
 *
 */
public class QueueTaskExecutor
{
	private Status status = Status.stopped;
	private static QueueTaskExecutor instance = null;
	private ConcurrentLinkedQueue<GalaxyTask> queue = new ConcurrentLinkedQueue<>();
	private ExecutorService pool = GalaxyThreadPool.getExecutorService();
	private QueueTaskExecutor(){}
	private static void init()
	{
		if(instance == null)
		{
			instance = new QueueTaskExecutor();
		}
	}
	
	public static void add(GalaxyTask task)
	{
		init();
		instance.queue.add(task);
		if(Status.stopped.equals(instance.status))
		{
			instance.execute();
		}
	}
	
	private void execute()
	{
		while(true)
		{
			final GalaxyTask task = queue.poll();
			if(task == null)
			{
				status = Status.stopped;
				break;
			}
			status = Status.started;
			if(AsyncTask.class.isInstance(task))
			{
				pool.execute(new Runnable(){
					public void run()
					{
						executeTask(task);
					}
				});
			}
			else
			{
				executeTask(task);
			}
		}
	}
	
	private void executeTask(GalaxyTask task)
	{
		Throwable ex = null;
		try
		{
			task.execute();
			task.onSuccess();
		}
		catch (Exception e)
		{
			ex = e;
		}
		finally
		{
			task.onComplete(ex);
		}
	}
	
	enum Status
	{
		started,stopped;
	}
	
	
}
