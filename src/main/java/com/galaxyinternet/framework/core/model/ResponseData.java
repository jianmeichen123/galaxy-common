package com.galaxyinternet.framework.core.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.galaxyinternet.framework.core.model.Result.Status;

/**
 * 提供给web、移动端的响应数据结构
 * 
 * @author kaihu
 *
 * @param <T>
 */
public class ResponseData<T extends BaseEntity> implements Serializable{

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
	private Long id;

	/**
	 * 这里存储请求相关的关键数据
	 */
	private Header header;

	private String queryParamsJsonStr;
	
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

	public List<T> getEntityList() {
		return entityList;
	}

	public void setEntityList(List<T> entityList) {
		this.entityList = entityList;
	}

	private Map<String,Object> userData = new HashMap<String,Object>();

	public Map<String, Object> getUserData() {
		return userData;
	}

	public void setUserData(Map<String, Object> userData) {
		this.userData = userData;
	}
	public void putAttachmentItem(String key, Object value){
		userData.put(key, value);
	}

	public String getQueryParamsJsonStr() {
		return queryParamsJsonStr;
	}

	public void setQueryParamsJsonStr(String queryParamsJsonStr) {
		this.queryParamsJsonStr = queryParamsJsonStr;
	}
	
}
