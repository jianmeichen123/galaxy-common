package com.galaxyinternet.framework.core.task;

import java.io.Serializable;
/**
 * 任务接口，
 * 尽量不要采用内部类形式，内部类序列化比较麻烦
 * @author WangSong
 *
 */
public interface GalaxyTask extends Serializable
{

	public void execute();
	public void onSuccess();
	public void onComplete(Throwable ex);
}
