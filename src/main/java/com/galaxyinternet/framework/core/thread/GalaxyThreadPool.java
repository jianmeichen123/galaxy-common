package com.galaxyinternet.framework.core.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author keifer
 */
public class GalaxyThreadPool {

	static int cpu_core_size = Runtime.getRuntime().availableProcessors();
	static ExecutorService executorService;
	static {
		executorService = new ThreadPoolExecutor(cpu_core_size * 2, cpu_core_size * 4, 5, TimeUnit.MINUTES,
				new ArrayBlockingQueue<Runnable>(cpu_core_size * 20), new ThreadPoolExecutor.CallerRunsPolicy());
	}

	public static ExecutorService getExecutorService() {
		return executorService;
	}

	private GalaxyThreadPool() {
	}
}
