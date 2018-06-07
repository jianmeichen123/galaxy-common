package com.galaxyinternet.framework.core.dao;

import java.util.List;

import com.galaxyinternet.framework.core.exception.MongoDBException;

/**
 * 通用基础DAO
 */
public interface MongodbBaseDao<T, ID> {
	
	/**
	 * 持久化对象到MongoDB中
	 * @param bean
	 * @throws MongoDBException
	 */
	public void save(T bean) throws MongoDBException;
	
	/**
	 * 根据条件删除文档
	 * @param query
	 * @throws MongoDBException
	 */
	public void deleteByCondition(T query) throws MongoDBException;
	
	/**
	 * 根据ID删除文档
	 * @param id   目前只支持key为"id"
	 * @throws MongoDBException
	 */
	public void deleteById(ID id) throws MongoDBException;
	
	/**
	 * 根据ID更新指定的文档
	 * @param id   目前只支持key为"id"
	 * @param t
	 * @throws MongoDBException
	 */
	public void updateById(ID id, T t) throws MongoDBException;
	
	/**
	 * 根据条件查询文档集合
	 * @param t
	 * @return
	 */
	public List<T> find(T t) throws MongoDBException;
	
	/**
	 * 根据条件查询指定的文档
	 * @param id
	 * @return
	 * @throws MongoDBException
	 */
	public T findOne(T t) throws MongoDBException;
	/**
	 * 根据ID查询指定的文档
	 * @param id
	 * @return
	 * @throws MongoDBException
	 */
	public T findOneById(ID id) throws MongoDBException;
	
}
