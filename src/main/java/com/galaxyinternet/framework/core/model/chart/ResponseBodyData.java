package com.galaxyinternet.framework.core.model.chart;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.galaxyinternet.framework.core.model.Result;
import com.galaxyinternet.framework.core.model.Result.Status;

public class ResponseBodyData implements Serializable{
	/**
	 * 单记录操作存储对象
	 */
	private Object entity;
	
	/**
	 * 处理后的结果，成功还是失败
	 */
	private Result result;
	
	/**
	 * 数据总量
	 */
	private Page pageList;
	
	/**
	 * 列表数据
	 */
	private List<Map<String,Object>> mapList;
	
	public ResponseBodyData(){
		result = new Result(Status.OK, null);
		pageList = new Page(null, null, null);
	}

	public Object getEntity() {
		return entity;
	}

	public void setEntity(Object entity) {
		this.entity = entity;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public List<Map<String, Object>> getMapList() {
		return mapList;
	}

	public void setMapList(List<Map<String, Object>> mapList) {
		this.mapList = mapList;
	}

	public Page getPageList() {
		return pageList;
	}

	public void setPageList(Page pageList) {
		this.pageList = pageList;
	}

	
	

}
