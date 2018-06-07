package com.galaxyinternet.framework.core.model;

public interface Pagable {

	public void setPageSize(Integer pageSize);

	public Integer getPageSize();

	public void setPageNum(Integer pageNum);

	public Integer getPageNum();

	public void setDirection(String direction);

	public String getDirection();

	public void setProperty(String property);

	public String getProperty();

}
