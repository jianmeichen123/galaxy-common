package com.galaxyinternet.common.controller;

import java.io.File;
import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.galaxyinternet.framework.core.constants.Constants;
import com.galaxyinternet.framework.core.file.OSSHelper;
import com.galaxyinternet.framework.core.file.UploadFileResult;
import com.galaxyinternet.framework.core.model.BaseEntity;
import com.galaxyinternet.framework.core.model.BaseUser;
import com.galaxyinternet.framework.core.model.ControllerPath;
import com.galaxyinternet.framework.core.model.PagableEntity;
import com.galaxyinternet.framework.core.model.Page;
import com.galaxyinternet.framework.core.model.PageRequest;
import com.galaxyinternet.framework.core.model.ResponseData;
import com.galaxyinternet.framework.core.model.Result;
import com.galaxyinternet.framework.core.model.Result.Status;
import com.galaxyinternet.framework.core.oss.OSSConstant;
import com.galaxyinternet.framework.core.service.BaseService;
import com.galaxyinternet.framework.core.utils.FileUtils;
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
	
	
	/**
	 * 从Session中取用户
	 */
	protected BaseUser getUserFromSession(HttpServletRequest request){
		Object userObj = request.getSession().getAttribute(Constants.SESSION_USER_KEY);
		if (userObj == null) {
			return null;
		}
		return (BaseUser) userObj;
	}
	/**
	 * 获取sessionId
	 */
	protected String getSessionId(HttpServletRequest request){
		String sessionId = request.getHeader(Constants.SESSION_ID_KEY);
		if (StringUtils.isBlank(sessionId)) {
			sessionId = request.getParameter(Constants.SESSOPM_SID_KEY);
		}
		return sessionId;
	}
	/**
	 * 获取userId
	 */
	protected String getUserId(HttpServletRequest request){
		String userId = request.getHeader(Constants.REQUEST_HEADER_USER_ID_KEY);
		if (StringUtils.isBlank(userId)) {
			userId = request.getParameter(Constants.REQUEST_URL_USER_ID_KEY);
		}
		return userId;
	}
	/**
	 * 获取当前请求路径的前置部分
	 * https://www.galaxyinternet.com/galaxy-sop-server/galaxy/user/add
	 * 得
	 * https://www.galaxyinternet.com/galaxy-sop-server/
	 * 或
	 * https://www.galaxyinternet.com/galaxy/user/add
	 * 得
	 * https://www.galaxyinternet.com/
	 */
	protected String getCurrEndpoint(HttpServletRequest request) {
		String uri = request.getRequestURI();
		String url = request.getRequestURL().toString();
		String contextPath = request.getContextPath();
		String endpoint = null;
		if(contextPath == null || "".equals(contextPath.trim())){
			endpoint = url.substring(0, url.indexOf(uri) + 1);
		}else{
			endpoint = url.substring(0, url.indexOf(contextPath) + contextPath.length() + 1);
		}
		return endpoint;
	}
	
	/**
	 * 文件上传/更新
	 * @param fileKey  OSS对文件的唯一标识
	 * 注意点：对于上传操作，fileKey需要新生成；但对于更新操作，fileKey需要从sop_file表中获取到老的fileKey进行覆盖
	 * 类似于：
	    if(StringUtils.isBlank(sopFile.getFileKey())){
			sopFile.setFileKey(String.valueOf(IdGenerator.generateId(OSSHelper.class)));
		}
	 * @param tempfilePath  服务器保存文件的临时目录
	 * @return 不会出现null，只需要对result.getResult().getStatus().equals(Status.ERROR)验证即可知道操作结果状态
	 *         其包含文件fileKey、bucketName、文件名、文件后缀、文件大小
	 */
	protected UploadFileResult uploadFileToOSS(HttpServletRequest request,  String fileKey, String tempfilePath) {
		UploadFileResult result = null;
		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile multipartFile = multipartRequest.getFile("file");
			String fileName = multipartFile.getOriginalFilename();
			Map<String,String> nameMap = FileUtils.transFileNames(fileName);
			File tempFile = new File(tempfilePath, fileKey + "_" + nameMap.get("fileName"));
			if (!tempFile.exists()) {
				tempFile.mkdirs();
			}
			multipartFile.transferTo(tempFile);
			long asize = multipartFile.getSize(); 
			if(asize > OSSConstant.UPLOAD_PART_SIZE){//大文件线程池上传
				result = OSSHelper.uploadWithBreakpoint(fileName, tempFile, fileKey);
				if(result.getResult().getStatus()==null || result.getResult().getStatus().equals(Status.ERROR)){
					return result;
				}
			}else{
				result = OSSHelper.simpleUploadByOSS(tempFile, fileKey, OSSHelper.setRequestHeader(fileName, multipartFile.getSize())); //上传至阿里云
				//若文件上传成功
				if(result.getResult().getStatus()==null || result.getResult().getStatus().equals(Status.ERROR)){
					return result;
				}
				result.setFileName(nameMap.get("fileName"));
				result.setFileSuffix(nameMap.get("fileSuffix"));
			}
		} catch (Exception e) {
			result = new UploadFileResult();
			result.setResult(new Result(Status.ERROR, null, "异常"));
		}
		return result;
	}
}
