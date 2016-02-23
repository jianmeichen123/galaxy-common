package com.galaxyinternet.framework.core.model;

import java.io.Serializable;

/**
 * 
 * @author kaihu
 * @param <PK>
 *            主键泛型类型
 */
public abstract class PrimaryKeyObject<PK extends Serializable> implements Serializable {

	private static final long serialVersionUID = 1L;
	protected PK id;

	public abstract PK getId();

	public abstract void setId(PK id);

	/**
	 * 创建时间
	 */
	protected Long createdTime;
	/**
	 * 更新时间
	 */
	protected Long updatedTime;

	public abstract Long getCreatedTime();

	public abstract void setCreatedTime(Long createdTime);

	public abstract Long getUpdatedTime();

	public abstract void setUpdatedTime(Long updatedTime);

}
