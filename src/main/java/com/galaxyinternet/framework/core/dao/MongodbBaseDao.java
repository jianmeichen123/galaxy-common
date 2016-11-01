package com.galaxyinternet.framework.core.dao;

import java.util.List;

import com.galaxyinternet.framework.core.exception.MongoDBException;

/**
 * 通用基础DAO
 * 
 * @author keifer
 * @param <T>
 */
public interface MongodbBaseDao<T> {

	public void save(T bean) throws MongoDBException;
	
	public void deleteByCondition(T query) throws MongoDBException;
	
	public <ID> void deleteById(ID id) throws MongoDBException;
	
	public void update(T query, T bean) throws MongoDBException;
	
	public void updateById(String id, T t) throws MongoDBException;
	
	public List<T> find(T t);
	
	public List<T> findByCondition(T t);
	
	public T findOne(T t);
	
	public T get(String id);
	
	 public T get(String id, String collectionName);
}
