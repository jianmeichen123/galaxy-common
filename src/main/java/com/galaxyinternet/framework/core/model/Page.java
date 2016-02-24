package com.galaxyinternet.framework.core.model;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Pageable;

public class Page<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long total;
	private Pageable pageable;
	private List<T> content;

	public Page(List<T> content, Pageable pageable, Long total) {
		super();
		this.total = total;
		this.pageable = pageable;
		this.content = content;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Pageable getPageable() {
		return pageable;
	}

	public void setPageable(Pageable pageable) {
		this.pageable = pageable;
	}

	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}
}
