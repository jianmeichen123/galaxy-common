package com.galaxyinternet.framework.core.model;

public class PagableEntity extends BaseEntity implements Pagable {

	private static final long serialVersionUID = 1L;
	
	private String messageType;
	
	protected Integer pageSize;
	protected Integer pageNum;
	protected String direction;// asc,desc
	protected String property;// 排序的字段名称

	@Override
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize==null?10:pageSize;
	}

	@Override
	public Integer getPageSize() {
		return this.pageSize;
	}

	@Override
	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum==null?0:pageNum;

	}

	@Override
	public Integer getPageNum() {
		return this.pageNum;
	}

	@Override
	public void setDirection(String direction) {
		this.direction = direction;
	}

	@Override
	public String getDirection() {
		return this.direction;
	}

	@Override
	public void setProperty(String property) {
		this.property = property;
	}

	@Override
	public String getProperty() {
		return this.property;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	
	
}
