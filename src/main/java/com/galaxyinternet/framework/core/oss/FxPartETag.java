package com.galaxyinternet.framework.core.oss;

import java.io.Serializable;
import com.aliyun.oss.model.PartETag;

/**
 * 封装PartETag,用于序列化
 */
public class FxPartETag implements Serializable {

	private static final long serialVersionUID = 1L;

	private int partNumber;

	private String eTag;

	public FxPartETag(PartETag partETag) {
		super();
		this.partNumber = partETag.getPartNumber();
		this.eTag = partETag.getETag();
	}

	public int getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(int partNumber) {
		this.partNumber = partNumber;
	}

	public String geteTag() {
		return eTag;
	}

	public void seteTag(String eTag) {
		this.eTag = eTag;
	}
}