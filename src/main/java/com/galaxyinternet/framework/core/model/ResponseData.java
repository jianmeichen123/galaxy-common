package com.galaxyinternet.framework.core.model;

import com.galaxyinternet.framework.core.model.Result.Status;

/**
 * 提供给web、移动端的响应数据结构
 * 
 * @author kaihu
 *
 * @param <T>
 */
public class ResponseData<T extends BaseEntity> {

	private T entity;
	private Result result;
	private Page<T> pageList;
	private Long id;// 主键

	public ResponseData() {
		result = new Result(Status.OK, null);
		pageList = new Page<T>(null, null, null);
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public Page<T> getPageList() {
		return pageList;
	}

	public void setPageList(Page<T> pageList) {
		this.pageList = pageList;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public T getEntity() {
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
		if (null != entity) {
			this.id = entity.getId();
		}
	}
}
