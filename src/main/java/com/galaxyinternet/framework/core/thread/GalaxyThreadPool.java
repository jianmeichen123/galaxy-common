package com.galaxyinternet.framework.core.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author keifer
 */
public class GalaxyThreadPool {

	static int CPU_CORE_SIZE = Runtime.getRuntime().availableProcessors();
	static final ExecutorService EXECUTOR_SERVICE;
	static {
		EXECUTOR_SERVICE = new ThreadPoolExecutor(CPU_CORE_SIZE * 2, CPU_CORE_SIZE * 4, 5, TimeUnit.MINUTES,
				new ArrayBlockingQueue<Runnable>(CPU_CORE_SIZE * 50), new ThreadPoolExecutor.CallerRunsPolicy());
	}

	public static ExecutorService getExecutorService() {
		return EXECUTOR_SERVICE;
	}

	private GalaxyThreadPool() {
	}
}
