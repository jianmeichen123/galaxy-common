package com.galaxyinternet.framework.core.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;

public class ExtendObject {

	/**
	 * 将相同类型的对象的内容向右合并
	 * 
	 * @param beanType
	 *            返回对象的类型
	 * @param initObject
	 *            包含原始数据的对象
	 * @param updateObject
	 *            包含修改后数据的对象
	 * @return 
	 *         返回两个对象的合并,相同属性的值如果convertedObject中包含,且不为null的话取它的值,否则取returnedObject的值
	 */
	@SuppressWarnings("unchecked")
	public Object extendObject(Object beanType, Object initObject, Object updateObject) {
		Map<Object, Object> map1 = BeanToMap(initObject);
		Map<Object, Object> map2 = BeanToMap(updateObject);
		List<?> list = getMapKeySet(map1);
		for (int i = 0; i < list.size(); i++) {
			Object map2Value = map2.get(list.get(i));
			if (null != map2Value) {
				map1.put(list.get(i), map2Value);
			}
		}
		return MapToBean(beanType, map1);
	}

	public Object extendObject(Object beanType, Map<Object, Object> initObject, Map<Object, Object> updateObject) {
		List<?> list = getMapKeySet(initObject);
		for (int i = 0; i < list.size(); i++) {
			Object map2Value = updateObject.get(list.get(i));
			if (null != map2Value) {
				initObject.put(list.get(i), map2Value);
			}
		}
		return MapToBean(beanType, initObject);
	}
	
	public Map<String, Object> extendMap(Map<String, Object> sourceObject,Map<String, Object> sourceMainObject, Map<String, Object> targetObject) {
		List<?> keys = getMapKeySet(sourceObject);
		for (int i = 0; i < keys.size(); i++) {
			String key = (String) keys.get(i);
			targetObject.put(key, sourceObject.get(key));
		}
		List<?> list = getMapKeySet(sourceMainObject);
		for (int i = 0; i < list.size(); i++) {
			String key = (String) list.get(i);
			targetObject.put(key, sourceMainObject.get(key));
		}
		return targetObject;
	}

	/**
	 * 将map转化为bean
	 * 
	 * @param bean
	 *            将要转化成为的对象
	 * @param map
	 *            被转化的map对象
	 */
	public Object MapToBean(Object bean, Map<Object, Object> map) {
		Object type = null;
		Date date = null;
		try {
			type = bean.getClass().newInstance();
			BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
			for (PropertyDescriptor p : beanInfo.getPropertyDescriptors()) {
				String propertyName = p.getName();
				Object mapValue = map.get(propertyName);
				// 去掉键为'class'的键值对
				if (null != mapValue && !"class".equals(propertyName)) {
					// 判断该字符转是否为日期类型
					if (CheckType.isDateType((String) mapValue)) {
						String dateType = CheckType.getDateType((String) mapValue);
						if (dateType.equals("yyyy-MM-dd HH:mm:ss")) {
							date = new SimpleDateFormat(dateType).parse((String) mapValue);
							p.getWriteMethod().invoke(type, new Timestamp(date.getTime()));
						} else {
							p.getWriteMethod().invoke(type, date);
						}
						// 判断该字符串是否为整型,同时忽略值为数字,但是类型是字符串的Id们
					} else if (CheckType.isInt((String) mapValue) && (!Pattern.matches("\\w*Id", propertyName))) {
						p.getWriteMethod().invoke(type, Integer.getInteger((String) mapValue).intValue());
						// 默认剩下的类型都是字符串型
					} else {
						p.getWriteMethod().invoke(type, mapValue);
					}
				}
			}
		} catch (IntrospectionException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return type;
	}

	/**
	 * 将bean转化为map
	 * 
	 * @param object
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	public Map BeanToMap(Object object) {
		Map map = null;
		try {
			map = BeanUtils.describe(object);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 获得对应Map的键值
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List getMapKeySet(Map map) {
		List list = new ArrayList();
		Iterator iterator = map.keySet().iterator();
		while (iterator.hasNext()) {
			list.add(iterator.next());
		}
		return list;
	}
}
