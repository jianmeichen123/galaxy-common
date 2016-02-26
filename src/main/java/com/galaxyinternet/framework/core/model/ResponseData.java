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

	/**
	 * 单记录操作存储对象
	 */
	private T entity;
	/**
	 * 处理后的结果，成功还是失败
	 */
	private Result result;
	/**
	 * 查询的列表数据
	 */
	private Page<T> pageList;
	
	/**
	 * page list of extending fields 
	 */
	private Page<?> pageVoList;
	
	/**
	 * 如果单记录操作时为记录的主键
	 */
	private Long id;

	/**
	 * 这里存储请求相关的关键数据
	 */
	private Header header;

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

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public Page<?> getPageVoList() {
		return pageVoList;
	}

	public void setPageVoList(Page<?> pageVoList) {
		this.pageVoList = pageVoList;
	}

}
