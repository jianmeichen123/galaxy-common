package com.galaxyinternet.framework.core.thread;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author keifer
 */
public class GalaxyThreadPool {
	final static Logger logger = LoggerFactory.getLogger(GalaxyThreadPool.class);
	static int cpu_core_size = Runtime.getRuntime().availableProcessors();
	static ExecutorService executorService;
	private static final ForkJoinPool forkJoinPool;
	static {
		forkJoinPool = new ForkJoinPool();
		executorService = getThreadPoolExector();
	}

	public static ExecutorService getExecutorService() {
		return executorService;
	}

	private static ThreadPoolExecutor getThreadPoolExector() {
		final ThreadPoolExecutor result = new ThreadPoolExecutor(cpu_core_size * 2, cpu_core_size * 4, 5,
				TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(cpu_core_size * 6),
				new ThreadPoolExecutor.CallerRunsPolicy());
		result.setThreadFactory(new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				Thread t = new Thread(r);
				t.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
					@Override
					public void uncaughtException(Thread t, Throwable e) {
						e.printStackTrace();
						logger.error("Thread exception: " + t.getName(), e);
						//result.shutdown();
					}
				});
				return t;
			}
		});
		return result;
	}

	private GalaxyThreadPool() {
	}
	
	public static ForkJoinPool getForkJoinPool()
	{
		return forkJoinPool;
	}
}
