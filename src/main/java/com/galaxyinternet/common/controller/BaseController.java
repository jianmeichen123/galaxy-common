package com.galaxyinternet.common.controller;

import org.springframework.validation.BindingResult;

import com.galaxyinternet.framework.core.model.BaseEntity;
import com.galaxyinternet.framework.core.model.PageRequest;
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
	 * 添加一条实体，实体不能为null,此方法支持表单字段校验
	 * 
	 * @param result
	 *            验证结果绑定对象
	 */
	public ResponseData<T> addOne(Q entity, BindingResult result);

	/**
	 * 查询对象列表，返回页面 listXXX页面
	 * 
	 * @param query
	 *            查询对象
	 * @param pageable
	 *            分页参数与排序参数
	 */
	@Deprecated
	public ResponseData<T> selectList(Q query, PageRequest pageable);

	/**
	 * 查询对象列表，返回页面 listXXX页面
	 * 
	 * @param query
	 *            查询对象 该查询对象必须实现Pagable接口，本系统中统一继承PagableEntity即可支持分页
	 */
	public ResponseData<T> selectList(Q query);

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

	/**
	 * 更新一个实体，实体不能为null,此方法支持表单字段校验
	 * 
	 * @param entity
	 *            要更新的实体
	 * @param result
	 *            验证结果绑定对象
	 */
	public ResponseData<T> editOne(Q entity, BindingResult result);

	/**
	 * 跳转页面方法<br/>
	 * 如果有需要跳转页面的需求，应该覆盖此方法。
	 * 
	 * @return
	 */
	public String forwardPage(String path, String page);
}
