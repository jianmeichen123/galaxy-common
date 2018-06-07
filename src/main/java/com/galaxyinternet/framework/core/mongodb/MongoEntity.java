package com.galaxyinternet.framework.core.mongodb;

import java.io.Serializable;

public abstract class MongoEntity<ID extends Serializable> implements Serializable {

	private static final long serialVersionUID = 1L;
	protected ID _id;

	public abstract ID get_id();

	public abstract void set_id(ID _id);
}
