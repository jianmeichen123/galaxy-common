package com.galaxyinternet.framework.core.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import com.galaxyinternet.framework.cache.Cache;
import com.galaxyinternet.framework.core.constants.Constants;
import com.galaxyinternet.framework.core.utils.PropertiesUtils;

/**
 * 
 *用户初始化配置数据的bean
 */
public class ConfigBean implements BeanFactoryAware {

	private String file;

	private String redisKey;

	private Cache cache;

	private List<String> keys;

	public ConfigBean(String file, List<String> keys, Cache cache) {
		super();
		this.file = file;
		this.keys = keys;
		this.redisKey = Constants.GALAXYINTERNET_FX_ENDPOINT;
		this.cache = cache;
		init();
	}

	/**
	 * 初始化数据到redis
	 */
	@SuppressWarnings("unchecked")
	private void init() {
		if (StringUtils.isNotBlank(this.file) && CollectionUtils.isNotEmpty(keys)
				&& StringUtils.isNotBlank(this.redisKey) && null != cache) {
			Properties properties = PropertiesUtils.getProperties(this.file);
			Map<String, Object> configs = null;
			Object object = this.cache.get(this.redisKey);
			if (null != object) {
				configs = (Map<String, Object>) object;
			} else {
				configs = new HashMap<String, Object>();
			}
			for (String key : keys) {
				String value = properties.getProperty(key);
				configs.put(key, value);
			}
			this.cache.set(this.redisKey, configs);
		} else {
			this.destory();
		}
	}

	@SuppressWarnings("unused")
	private void show() {
		for (String key : keys) {
			System.out.println("cache value=" + this.cache.get(this.redisKey));
		}
	}

	/**
	 * 如果传递的参数不对就销毁资源
	 */
	private void destory() {
		this.file = null;
		this.keys = null;
		this.redisKey = null;
		/*String[] names = beanFactory.getBeanDefinitionNames();
		List<String> nameList = Arrays.asList(names);
		for (String name : names) {
			System.out.println(name);
		}
		if (nameList.contains(this.getClass().getName())) {
			beanFactory.removeBeanDefinition(this.getClass().getName());
		}
		Object bean = beanFactory.getBean(this.getClass());
		if (null != bean) {
			beanFactory.removeBeanDefinition(this.getClass().getName() + "#0");
		}*/
	}

	DefaultListableBeanFactory beanFactory;

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = (DefaultListableBeanFactory) beanFactory;
	}
}
