package com.galaxyinternet.framework.core.oss;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.galaxyinternet.framework.core.file.BucketName;
import com.galaxyinternet.framework.core.file.DownloadFileResult;
import com.galaxyinternet.framework.core.file.FileResult;
import com.galaxyinternet.framework.core.file.OSSHelper;
import com.galaxyinternet.framework.core.id.IdGenerator;
import com.galaxyinternet.framework.core.utils.GSONUtil;

public class OSSTest {

	private String key;

	@Before
	public void setup() {
		key = String.valueOf(IdGenerator.generateId(OSSTest.class));
		System.out.println("key="+key);
	}

	@Test
	private static void testDownload2() throws Exception {
		String key = "3948241456726026";
		DownloadFileResult result = OSSHelper.simpleDownloadByOSS(BucketName.DEV.getName(), key);
		System.out.println(GSONUtil.toJson(result));
		InputStream inputStream = result.getInput();
		FileOutputStream outStream = new FileOutputStream("D:\\temp\\aaa.png");
		// 通过available方法取得流的最大字符数

		byte[] inOutb = new byte[inputStream.available()];
		inputStream.read(inOutb); // 读入流,保存在byte数组
		outStream.write(inOutb); // 写出流,保存在文件中
		inputStream.close();
		outStream.close();
	}

	@Test
	private static void testDownload1() throws Exception {
		String key = "3948241456726026";
		DownloadFileResult result = OSSHelper.simpleDownloadByOSS(new File("D:\\temp\\aaa.png"),
				BucketName.DEV.getName(), key);
		System.out.println(GSONUtil.toJson(result));
	}

	@Test
	private static void testUpload() throws Exception {
		String key = "3948241456726026";
		OSSHelper.simpleUploadByOSS(new File("C:\\Users\\Administrator\\Desktop\\test.png"), BucketName.DEV.getName(),
				key);
	}

	@Test
	private static void testDelete() {
		String key = "3948241456726026";
		FileResult result = OSSHelper.deleteFile(BucketName.DEV.getName(), key);
		System.err.println(GSONUtil.toJson(result));
	}

	@Test
	private static void testDeleteMultiple() {
		String key = "3948241456726026";
		List<String> keys = new ArrayList<String>();
		keys.add("3949336065212425");
		keys.add("3949336065212424");
		FileResult result = OSSHelper.deleteMultipleFiles(BucketName.DEV.getName(), keys);
		// System.err.println(GSONUtil.toJson(result));
	}

	@Test
	private static void createBucketName() {
		String devBucketName = BucketName.DEV.getName();
		String testBucketName = BucketName.TEST.getName();
		String proBucketName = BucketName.PRODUCT.getName();
		OSSFactory.getBucketName(devBucketName);
		OSSFactory.getBucketName(testBucketName);
		OSSFactory.getBucketName(proBucketName);
	}
}
