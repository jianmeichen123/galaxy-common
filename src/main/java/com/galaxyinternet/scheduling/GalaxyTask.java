package com.galaxyinternet.scheduling;

import com.galaxyinternet.framework.core.exception.BusinessException;

public interface GalaxyTask 
{
	public void execute() throws BusinessException;
}
