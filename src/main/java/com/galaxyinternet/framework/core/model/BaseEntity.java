package com.galaxyinternet.framework.core.model;

/**
 * @author kerfer
 */
public abstract class BaseEntity extends PrimaryKeyObject<Long>{

	private static final long serialVersionUID = 1L;

	@Override
	public Long getCreatedTime() {
		return createdTime;
	}

	@Override
	public void setCreatedTime(Long createdTime) {
		this.createdTime = createdTime;
	}

	@Override
	public Long getUpdatedTime() {
		return updatedTime;
	}

	@Override
	public void setUpdatedTime(Long updatedTime) {
		this.updatedTime = updatedTime;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}
}
