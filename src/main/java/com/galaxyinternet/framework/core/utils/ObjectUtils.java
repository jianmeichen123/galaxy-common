package com.galaxyinternet.framework.core.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectUtils
{
	public static boolean notNull(Object... objs)
	{
		for (Object obj : objs)
		{
			if (obj == null)
				return false;
		}
		return true;
	}

	public static byte[] serialize(Object value)
	{
		if (value == null)
		{
			return null;
		}

		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream outputStream;
		try
		{
			outputStream = new ObjectOutputStream(arrayOutputStream);

			outputStream.writeObject(value);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				arrayOutputStream.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		return arrayOutputStream.toByteArray();
	}

	public static Object deserialize(byte[] bytes)
	{
		if (bytes == null || bytes.length == 0)
		{
			return null;
		}

		try
		{
			ObjectInputStream inputStream;
			inputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
			Object obj = inputStream.readObject();
			return obj;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}

		return null;
	}
}
