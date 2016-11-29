package com.galaxyinternet.framework.core.task;

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
	private static ExecutorService pool = GalaxyThreadPool.getExecutorService();
	private QueueTaskExecutor(){}
	
	public static void add(final GalaxyTask task)
	{
		pool.execute(new Runnable(){
			@Override
			public void run()
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
			
		});
	}
}
