package com.galaxyinternet.framework.core.message;

public interface MessageListener
{
	public void onMessage(Message message, String channel);
}
