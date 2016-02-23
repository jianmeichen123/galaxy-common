package com.galaxyinternet.framework.core.model;

import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

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
	private Page<T> page;
	private Long id;// 主键

	public ResponseData() {
		result = new Result(Status.OK, null);
		page = new PageImpl<>(new ArrayList<T>(0));
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public Page<T> getPage() {
		return page;
	}

	public void setPage(Page<T> page) {
		this.page = page;
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
