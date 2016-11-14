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
	
	/**
	 * 是否有效
	 */
	protected String isValid;
	
	/**
	 * 排序
	 */
	protected Long sortNum;
	
	/**
	 * 版本
	 */
	protected String version;
	
	/**
	 * 备注
	 */
	protected String memo;
	
	
	public abstract Long getCreatedTime();

	public abstract void setCreatedTime(Long createdTime);

	public abstract Long getUpdatedTime();

	public abstract void setUpdatedTime(Long updatedTime);

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public Long getSortNum() {
		return sortNum;
	}

	public void setSortNum(Long sortNum) {
		this.sortNum = sortNum;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	
	
	
	
	

}
