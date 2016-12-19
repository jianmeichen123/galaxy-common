package com.galaxyinternet.framework.core.task;

import com.galaxyinternet.framework.cache.Cache;
import com.galaxyinternet.framework.core.utils.BeanContextUtils;

public abstract class AbstractRepeatableTask implements GalaxyTask
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**最后执行时间**/
	protected Long latestRunTime = null;
	/**执行间隔**/
	protected Long minPeriod = 5000L;
	/**执行次数**/
	protected int count=0;
	/**最大执行次数**/
	protected int maxCount = 100;
	/**是否再次执行**/
	protected boolean repeatable = false;
	protected Cache cache = BeanContextUtils.getBean(Cache.class);
	@Override
	public final void execute()
	{
		Long currTime = System.currentTimeMillis();
		if(latestRunTime != null && currTime-latestRunTime < minPeriod)
		{
			QueueTaskExecutor.add(this);
			return;
		}
		latestRunTime = currTime;
		
		if(count>maxCount) return;
		
		count++;
		executeInteral();
		if(isRepeatable())
		{
			QueueTaskExecutor.add(this);
		}
	}
	
	protected abstract void executeInteral();

	@Override
	public void onSuccess()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onComplete(Throwable ex)
	{
		// TODO Auto-generated method stub

	}

	public Long getLatestRunTime()
	{
		return latestRunTime;
	}

	public void setLatestRunTime(Long latestRunTime)
	{
		this.latestRunTime = latestRunTime;
	}

	public Long getMinPeriod()
	{
		return minPeriod;
	}

	public void setMinPeriod(Long minPeriod)
	{
		this.minPeriod = minPeriod;
	}

	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
	}

	public int getMaxCount()
	{
		return maxCount;
	}

	public void setMaxCount(int maxCount)
	{
		this.maxCount = maxCount;
	}

	public boolean isRepeatable()
	{
		return repeatable;
	}

	public void setRepeatable(boolean repeatable)
	{
		this.repeatable = repeatable;
	}

	
	

}
