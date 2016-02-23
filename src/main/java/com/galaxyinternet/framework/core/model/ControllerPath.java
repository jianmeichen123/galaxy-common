package com.galaxyinternet.framework.core.model;

import org.apache.commons.lang.StringUtils;

import com.galaxyinternet.framework.core.constants.PagePrefix;
import com.galaxyinternet.framework.core.utils.BeanUtils;
import com.galaxyinternet.framework.core.utils.StringEx;

/**
 * Controller路径构建起
 */
public class ControllerPath {

	/**
	 * @fields entityClass 简单的实体类
	 */
	private Class<?> entityClass = null;
	/**
	 * @fields URL_SEPARATOR 路径分隔符
	 */
	private static final String URL_SEPARATOR = "/";

	/**
	 * @fields entityName 实体名称
	 */
	private String entityName = null;
	/**
	 * @fields words 实体类路径
	 */
	private String[] words = null;

	public ControllerPath(Class<?> genericClass) {
		if (genericClass == null) throw new IllegalArgumentException("[genericClass] - must not be null!");

		entityClass = BeanUtils.getGenericClass(genericClass);

		if (entityClass == null) throw new IllegalArgumentException(genericClass.getName() + "不是泛型类型！");

		words = getWords(entityClass.getSimpleName());
		entityName = words[words.length - 1];
	}

	/**
	 * 获取显示页面路径  
	 * @return String "sys/dictionary/viewDictionary" 
	 */
	public String getOneViewPath() {
		StringBuilder sb = new StringBuilder();
		sb.append(getBasePath());
		sb.append(PagePrefix.VIEW);
		sb.append(StringEx.toUpperCaseFirstOne(entityName));
		return sb.toString();
	}

	/**
	 * 显示列表路径
	 * @return String "sys/dictionary/listDictionary" 
	 */
	public String getListViewPath() {
		StringBuilder sb = new StringBuilder();
		sb.append(getBasePath());
		sb.append(PagePrefix.LIST);
		sb.append(StringEx.toUpperCaseFirstOne(entityName));
		return sb.toString();
	}

	/**
	 * 添加页面路径信息
	 * @return
	 */
	public String getAddViewPath() {
		StringBuilder sb = new StringBuilder();
		sb.append(getBasePath());
		sb.append(PagePrefix.ADD);
		sb.append(StringEx.toUpperCaseFirstOne(entityName));
		return sb.toString();
	}

	/**
	 * 添加页面路径信息
	 * @return
	 */
	public String getEditViewPath() {
		StringBuilder sb = new StringBuilder();
		sb.append(getBasePath());
		sb.append(PagePrefix.EDIT);
		sb.append(StringEx.toUpperCaseFirstOne(entityName));
		return sb.toString();
	}

	/**
	 * 获取删除返回路径,默认重定向到列表页面
	 * @return
	 */
	public String getRedirectListPath() {
		return "redirect:/" + getBasePath();
	}

	/**
	 * 获取实体的名称，全小写
	 * @return
	 */
	public String getEntityName() {
		return entityName;
	}

	/**
	 * 以字符串中的大写字母为标示拆分字符串,如果字符串为null或空则返回null
	 * @param str
	 * @return String[] 拆分后的字符串，已转换为全小写
	 */
	private String[] getWords(String str) {
		if (StringUtils.isEmpty(str)) return null;
		String[] words = str.split("(?<!^)(?=[A-Z])");
		for (int i = 0; i < words.length; i++) {
			words[i] = StringUtils.lowerCase(words[i]);
		}
		return words;
	}

	/**
	 * 获取类名路径信息，例如：SysDictionary 则返回  "sys/dictionary/"
	 * @param clazz 类
	 * @return String 类名路径信息
	 */
	private String getBasePath() {
		StringBuffer sb = new StringBuffer();
		for (String word : words) {
			sb.append(word).append(URL_SEPARATOR);
		}
		return sb.toString();
	}

}
