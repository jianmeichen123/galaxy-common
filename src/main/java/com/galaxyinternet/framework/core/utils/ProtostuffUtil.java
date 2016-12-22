package com.galaxyinternet.framework.core.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

public class ProtostuffUtil
{
	private static Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<>();
	
	@SuppressWarnings("unchecked")
	private static <T> Schema<T> getSchema(Class<T> clazz)
	{
		Schema<T> schema = (Schema<T>) cachedSchema.get(clazz);
		if(schema == null)
		{
			schema = RuntimeSchema.getSchema(clazz);
			if(schema != null)
			{
				cachedSchema.put(clazz, schema);
			}
		}
		return schema;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> byte[] serialize(T target)
	{
		Class<T> clazz = (Class<T>) target.getClass();
		LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
		
		try
		{
			Schema<T> schema = getSchema(clazz);
			return ProtostuffIOUtil.toByteArray(target, schema, buffer);
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			buffer.clear();
		}
	}
	
	public static <T> T deserialize(byte[] data, Class<T> clazz)
	{
		try
		{
			T rtn = clazz.newInstance();
			Schema<T> schema = getSchema(clazz);
			ProtostuffIOUtil.mergeFrom(data, rtn, schema);
			return rtn;
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
}
