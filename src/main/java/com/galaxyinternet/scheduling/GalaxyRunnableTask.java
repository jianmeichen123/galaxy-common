package com.galaxyinternet.scheduling;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;

import org.springframework.scheduling.support.ScheduledMethodRunnable;
import org.springframework.util.ReflectionUtils;

public class GalaxyRunnableTask implements Runnable {

	private final Object target;
	private final Method method;
	private GalaxyTaskScheduler scheduler;
	
	public GalaxyRunnableTask(ScheduledMethodRunnable configJob)
	{
		this.target = configJob.getTarget();
		this.method = configJob.getMethod();
	}
	
	public Object getTarget() {
		return target;
	}
	public Method getMethod() {
		return method;
	}
	
	
	public GalaxyTaskScheduler getScheduler() {
		return scheduler;
	}
	public void setScheduler(GalaxyTaskScheduler scheduler) {
		this.scheduler = scheduler;
	}
	
	
	
	@Override
	public void run() {
		try {
			if(scheduler != null && scheduler.isDisabled() == true)
			{
				return;
			}
			ReflectionUtils.makeAccessible(this.method);
			this.method.invoke(this.target);
		}
		catch (InvocationTargetException ex) {
			ReflectionUtils.rethrowRuntimeException(ex.getTargetException());
		}
		catch (IllegalAccessException ex) {
			throw new UndeclaredThrowableException(ex);
		}
	}
	

}
