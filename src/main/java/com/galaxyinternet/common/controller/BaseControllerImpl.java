package com.galaxyinternet.common.controller;

import java.util.Arrays;

import javax.validation.Valid;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.galaxyinternet.framework.core.constants.Constants;
import com.galaxyinternet.framework.core.model.BaseEntity;
import com.galaxyinternet.framework.core.model.ControllerPath;
import com.galaxyinternet.framework.core.model.PagableEntity;
import com.galaxyinternet.framework.core.model.Page;
import com.galaxyinternet.framework.core.model.PageRequest;
import com.galaxyinternet.framework.core.model.ResponseData;
import com.galaxyinternet.framework.core.model.Result;
import com.galaxyinternet.framework.core.model.Result.Status;
import com.galaxyinternet.framework.core.service.BaseService;
import com.galaxyinternet.framework.core.validator.ValidatorResultHandler;

/**
 * 基础控制器接口实现类
 */
public abstract class BaseControllerImpl<T extends BaseEntity, Q extends T> implements BaseController<T, Q> {
	private Logger log = LoggerFactory.getLogger(BaseControllerImpl.class);
	/**
	 * @fields path 页面路径信息
	 */
	protected ControllerPath path = new ControllerPath(this.getClass());

	/**
	 * 获取基础的服务
	 * 
	 * @return BaseService
	 */
	protected abstract BaseService<T> getBaseService();

	@Override
	@ResponseBody
	@RequestMapping(value = "/multidelete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseData<T> deleteList(Long[] ids) {
		ResponseData<T> responseBody = new ResponseData<T>();
		Result result = null;
		if (ArrayUtils.isEmpty(ids)) {
			log.error("未设置批量删除对象的ID号！对象：{}", path.getEntityName());
			result = new Result(Status.ERROR, "没有传入要删除的ID号数组！");
			responseBody.setResult(result);
			return responseBody;
		}
		try {
			getBaseService().deleteByIdInBatch(Arrays.asList(ids));
		} catch (Exception e) {
			log.error("批量删除对象失败！对象:" + path.getEntityName(), e);
			result = new Result(Status.ERROR, "批量删除失败！");
			responseBody.setResult(result);
			return responseBody;
		}
		responseBody.setResult(new Result(Status.OK, ids.length));
		return responseBody;
	}

	@Override
	@ResponseBody
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseData<T> deleteOne(@PathVariable("id") Long id) {
		ResponseData<T> responseBody = new ResponseData<T>();
		Result result = null;
		if (id == null) {
			log.error("要删除的ID号为null或空字符串！对象：{}", path.getEntityName());
			result = new Result(Status.ERROR, "没有传入要删除的ID号！");
			responseBody.setResult(result);
			return responseBody;
		}
		int count = getBaseService().deleteById(id);
		if (count == 0) {
			result = new Result(Status.ERROR, "要删除的记录不存在！");
			responseBody.setResult(result);
			return responseBody;
		}
		responseBody.setResult(new Result(Status.OK, count));
		return responseBody;
	}

	@Override
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public ResponseData<T> addOne(@RequestBody Q entity) {
		Long id = getBaseService().insert(entity);
		ResponseData<T> responseBody = new ResponseData<T>();
		responseBody.setId(id);
		return responseBody;
	}

	@Override
	@RequestMapping(value = "/queryList", method = RequestMethod.GET)
	@ResponseBody
	@Deprecated
	public ResponseData<T> selectList(Q query, PageRequest pageable) {
		Page<T> pageList = getBaseService().queryPageList(query, pageable);
		ResponseData<T> responseBody = new ResponseData<T>();
		responseBody.setPageList(pageList);
		return responseBody;
	}

	@Override
	@RequestMapping(value = "/selectList", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseData<T> selectList(Q query) {
		Pageable pageable = null;
		if (query instanceof PagableEntity) {
			PagableEntity entity = ((PagableEntity) query);
			pageable = new PageRequest(entity.getPageNum(), entity.getPageSize());
		} else {
			pageable = new PageRequest(0, Constants.DEFAULT_PAGE_SIZE);
		}
		Page<T> pageList = getBaseService().queryPageList(query, pageable);
		ResponseData<T> responseBody = new ResponseData<T>();
		responseBody.setPageList(pageList);
		return responseBody;
	}

	@Override
	@RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseData<T> viewOne(@PathVariable("id") Long id) {
		T obj = getBaseService().queryById(id);
		ResponseData<T> responseBody = new ResponseData<T>();
		responseBody.setEntity(obj);
		return responseBody;
	}

	@Override
	@ResponseBody
	@RequestMapping(value = "/edit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseData<T> editOne(@RequestBody Q entity) {
		getBaseService().updateById(entity);
		ResponseData<T> responseBody = new ResponseData<T>();
		responseBody.setResult(new Result(Status.OK, entity));
		return responseBody;
	}

	@Override
	@RequestMapping(value = "/{path}/{page}")
	public String forwardPage(@PathVariable("path") String path, @PathVariable("page") String page) {
		return path + "/" + page;
	}

	@Override
	@RequestMapping(value = "/addValid", method = RequestMethod.POST)
	@ResponseBody
	public ResponseData<T> addOne(@RequestBody @Valid Q entity, BindingResult result) {
		ResponseData<T> responseBody = new ResponseData<T>();
		Result validationResult = ValidatorResultHandler.handle(result);
		if (validationResult.getStatus() == Status.ERROR) {
			responseBody.setResult(validationResult);
			return responseBody;
		}
		return this.addOne(entity);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = "/editValid", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseData<T> editOne(@RequestBody @Valid Q entity, BindingResult result) {
		ResponseData<T> responseBody = new ResponseData<T>();
		Result validationResult = ValidatorResultHandler.handle(result);
		if (validationResult.getStatus() == Status.ERROR) {
			responseBody.setResult(validationResult);
			return responseBody;
		}
		return this.editOne(entity);
	}
}
