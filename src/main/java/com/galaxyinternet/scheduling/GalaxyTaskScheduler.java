package com.galaxyinternet.scheduling;

import java.util.concurrent.ScheduledFuture;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.ScheduledMethodRunnable;

public class GalaxyTaskScheduler extends ThreadPoolTaskScheduler 
{
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(GalaxyTaskScheduler.class);
	
	private boolean disabled = false;
	
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	
	
	@PostConstruct
	public void initPoolsize()
	{
		super.setPoolSize(Runtime.getRuntime().availableProcessors());
		logger.debug("PoolSize = "+getPoolSize());
	}

	@Override
	public ScheduledFuture<?> schedule(Runnable task, Trigger trigger) {
		Runnable decorateTask = decorateTask(task);
		return super.schedule(decorateTask, trigger);
	}

	
	private Runnable decorateTask(Runnable task)
	{
		if(task instanceof ScheduledMethodRunnable)
		{
			GalaxyRunnableTask target = new GalaxyRunnableTask((ScheduledMethodRunnable)task);
			target.setScheduler(this);
			return target;
		}
		return task;
	}
	
	
	
}
