package com.galaxyinternet.framework.core.mongodb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.galaxyinternet.framework.core.model.Header;
import com.galaxyinternet.framework.core.model.Page;
import com.galaxyinternet.framework.core.model.Result;
import com.galaxyinternet.framework.core.model.Result.Status;

public class MongoRespData<T extends MongoEntity<String>> {

	/**
	 * 单记录操作存储对象
	 */
	private T entity;
	/**
	 * 处理后的结果，成功还是失败
	 */
	private Result result;
	/**
	 * 分页的列表数据(T类型的集合)
	 */
	private Page<T> pageList;

	/**
	 * 分页的列表数据(不分类型)
	 */
	private Page<?> pageVoList;

	/**
	 * 非分页的对象集合
	 */
	private List<T> entityList;

	/**
	 * 如果单记录操作时为记录的主键
	 */
	private String _id;

	/**
	 * 这里存储请求相关的关键数据
	 */
	private Header header;

	public MongoRespData() {
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

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public T getEntity() {
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
		if (null != entity) {
			this._id = entity.get_id();
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

	public List<T> getEntityList() {
		return entityList;
	}

	public void setEntityList(List<T> entityList) {
		this.entityList = entityList;
	}

	private Map<String, Object> userData = new HashMap<String, Object>();

	public Map<String, Object> getUserData() {
		return userData;
	}

	public void setUserData(Map<String, Object> userData) {
		this.userData = userData;
	}
}
