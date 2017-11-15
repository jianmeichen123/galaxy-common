package com.galaxyinternet.framework.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 文件序列化工具类
 * 
 * @au
import org.slf4j.LoggerFactory;thor keifer
 */
public class FileSerializableUtil {

	public static final Logger LOGGER = LoggerFactory.getLogger(FileSerializableUtil.class);

	public static void serialize(Object object, String filePath) {
		File file = new File(filePath);
		if (!new File(file.getParent()).exists())
			new File(file.getParent()).mkdirs();
		if (file.exists())
			file.delete();
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(object);
			oos.close();
		} catch (IOException e) {
			LOGGER.error("serialization failure", e);
			try {
				if (oos != null)
					oos.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			file.delete();
		}
	}

	public static Object deserialize(String filePath) {
		File file = new File(filePath);
		if (!file.exists())
			return null;
		else {
			LOGGER.debug("load: " + file);
		}
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
			Object object = ois.readObject();
			ois.close();
			file.delete();
			return object;
		} catch (Exception e) {
			LOGGER.error("deserialization failure", e);
			return null;
		}
	}

	public static boolean delSerlzFile(String filePath) {
		File file = new File(filePath);
		if (file.exists())
			return file.delete();
		return true;
	}
}
