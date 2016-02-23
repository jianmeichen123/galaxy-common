package com.galaxyinternet.framework.core.id;

/**
 * @author kaihu
 */
public interface IdCreator {
	public Long nextId(String sKey) throws Exception;
}
