package com.galaxyinternet.framework.core.model;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.annotation.Transient;

import com.galaxyinternet.framework.core.utils.StringEx;

/**
 * @author kerfer
 */
public abstract class BaseEntity extends PrimaryKeyObject<Long>{

	private static final long serialVersionUID = 1L;
	/**
	 * 是否包含转义字符
	 */
	@Transient
	protected Boolean escapeChar;
	
	/**
	 * 模糊查询关键字
	 */
	protected String keyword;
	
	/**
	 * @return the keyword
	 */
	public String getKeyword() {
		if(escapeChar == null){
			escapeChar = false;
		}
		if(StringUtils.isNotEmpty(keyword)&&escapeChar==false){
			String newkeyword = StringEx.checkSql(keyword);
			if(!keyword.equals(newkeyword)){
				escapeChar = true;
				keyword = newkeyword;
			}
			this.setEscapeChar(escapeChar);
			System.out.println("-------escapeChar:"+escapeChar);
		}
		return keyword;
	}

	/**
	 * @param keyword the keyword to set
	 */
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

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

	public Boolean getEscapeChar() {
		return escapeChar;
	}

	public void setEscapeChar(Boolean escapeChar) {
		this.escapeChar = escapeChar;
	}
}
