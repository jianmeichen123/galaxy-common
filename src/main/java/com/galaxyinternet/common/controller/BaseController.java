package com.galaxyinternet.common.controller;

import com.galaxyinternet.framework.core.model.BaseEntity;
import com.galaxyinternet.framework.core.model.ResponseData;
import com.galaxyinternet.framework.core.model.Result;

/**
 * 基础控制器接口
 */
public interface BaseController<T extends BaseEntity, Q extends T> {
	/**
	 * 根据ID列表删除对象，如果idList 为空或者空列表则直接返回{@link Result},状态为OK
	 * 
	 * @param idList
	 *            要删除对象的ID列表
	 */
	public ResponseData<T> deleteList(Long[] ids);

	/**
	 * 删除一条记录
	 */
	public ResponseData<T> deleteOne(Long id);

	/**
	 * 添加一条实体，实体不能为null
	 */
	public ResponseData<T> addOne(Q entity);

	/**
	 * 查询对象列表，返回页面 listXXX页面
	 * 
	 * @param query
	 *            查询对象
	 * @param pageable
	 *            分页参数与排序参数
	 */
	public ResponseData<T> selectList(Q query, org.springframework.data.domain.Pageable pageable);

	/**
	 * 根据ID查询一个对象，返回页面为viewXXX页面
	 * 
	 * @param id
	 *            不能为null，则跳转到错误页面
	 */
	public ResponseData<T> viewOne(Long id);

	/**
	 * 更新一个实体，实体不能为null
	 * 
	 * @param entity
	 *            要更新的实体
	 */
	public ResponseData<T> editOne(Q entity);

}
