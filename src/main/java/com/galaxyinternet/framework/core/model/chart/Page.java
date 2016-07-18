package com.galaxyinternet.framework.core.model.chart;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Page implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 总数
	 */
	private Long total;
	/**
	 * 列表数据
	 */
	private List<Map<String, Object>> content;
	/**
	 * 图表数据
	 */
	private List<Map<String, Object>> chart;
	
	private boolean isHHR;
	
	public Page(Long total, List<Map<String, Object>> content) {
		super();
		this.total = total;
		this.content = content;
	}
	
	public Page(Long total, List<Map<String, Object>> content, List<Map<String, Object>> chart) {
		super();
		this.total = total;
		this.content = content;
		this.chart = chart;
	}
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public List<Map<String, Object>> getContent() {
		return content;
	}
	public void setContent(List<Map<String, Object>> content) {
		this.content = content;
	}
	public List<Map<String, Object>> getChart() {
		return chart;
	}
	public void setChart(List<Map<String, Object>> chart) {
		this.chart = chart;
	}

	public boolean isHHR() {
		return isHHR;
	}

	public void setHHR(boolean isHHR) {
		this.isHHR = isHHR;
	}	
	
}
