package com.galaxyinternet.framework.core.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.galaxyinternet.framework.core.exception.BaseException;

/**
 * 短信接口 Created by zhaoying on 2016/11/9.
 */
public class SmsUtil
{
	public final Logger logger = LoggerFactory.getLogger(SmsUtil.class);

	public static String TRUE = "true";
	public static String FALSE = "false";
	public static String FLAG = "flag";
	private static String UTF_8 = "utf-8";

	// 短信平台地址
	private String url;
	// 企业代码
	private String spCode="";
	// 账号
	private String loginName;
	// 密码
	private String password;
	// 产品编号
	private String productId;

	/**
	 * 发送普通短信
	 * 
	 * @param messages
	 * @param mobile
	 * @return
	 * @throws BaseException
	 */
	public boolean send(String messages, String mobile) throws BaseException
	{
		return send(messages, null, mobile);
	}

	public boolean send(String messages, String model, String mobile) throws BaseException
	{
		CloseableHttpClient client = null;
		CloseableHttpResponse response = null;
		try
		{
			client = HttpClientManager.getInstance().getHttpClient();
			// 建立HttpPost对象
			HttpPost httpPost = createPost(messages, mobile, model);
			// 发送Post,并返回一个HttpResponse对象
			response = client.execute(httpPost);
			// 如果状态码是200，则正常返回
			Map<String, String> map = passer(response);
			return TRUE.equals(map.get(FLAG));
		}
		catch (Exception e)
		{
			throw new BaseException("发送短信异常", e);
		}
		finally
		{
			if (null != response)
			{
				try
				{
					response.close();
				}
				catch (IOException e)
				{
					logger.error("HTTP 错误",e);
				}
			}
		}
	}
	public String sendAndGetStatus(String messages, String model, String mobile) throws BaseException
	{
		CloseableHttpClient client = null;
		CloseableHttpResponse response = null;
		try
		{
			client = HttpClientManager.getInstance().getHttpClient();
			// 建立HttpPost对象
			HttpPost httpPost = createPost(messages, mobile, model);
			// 发送Post,并返回一个HttpResponse对象
			response = client.execute(httpPost);
			// 如果状态码是200，则正常返回
			HttpEntity entity = response.getEntity();
			if (entity != null)
			{
				InputStream instream = entity.getContent();
				try
				{
					String content = EntityUtils.toString(entity);
					System.err.println(content);
					return DomUtils.getNodeValue(content, "/*[name()='CSubmitState']/*[name()='State']");
				}
				finally
				{
					instream.close();
				}
			}
		}
		catch (Exception e)
		{
			throw new BaseException("发送短信异常", e);
		}
		finally
		{
			if (null != response)
			{
				try
				{
					response.close();
				}
				catch (IOException e)
				{
					logger.error("HTTP 错误",e);
				}
			}
		}
		return null;
	}
	private HttpPost createPost(String messages, String mobile, String model) throws UnsupportedEncodingException
	{
		String phoneNumber = mobile;
		if(mobile != null && mobile.length() == 12 && mobile.startsWith("0"))
		{
			phoneNumber = mobile.substring(1);
		}
		HttpPost httpPost = new HttpPost(url);
		// 建立一个NameValuePair数组，用于存储欲传递的参数
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		// 添加参数
		nvps.add(new BasicNameValuePair("sname", loginName));
		nvps.add(new BasicNameValuePair("spwd", password));
		nvps.add(new BasicNameValuePair("scorpid", spCode));
		nvps.add(new BasicNameValuePair("sprdid", productId));
		nvps.add(new BasicNameValuePair("sdst", phoneNumber));
		nvps.add(new BasicNameValuePair("smsg", messages));

		if (model != null)
		{
			nvps.add(new BasicNameValuePair("key", model));
		}
		// 设置编码
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, UTF_8));
		return httpPost;
	}

	private Map<String, String> passer(HttpResponse response) throws Exception
	{
		Map<String, String> map = null;
		if (null != response && response.getStatusLine().getStatusCode() == 200)
		{
			// 如果是下载的文件，可以用response.getEntity().getContent返回InputStream
			// 获得返回的字符串
			String result = getMessage(response.getEntity(), Charset.forName(UTF_8));

			map = _convert(result);
			map.put(FLAG, "0".equals(map.get("result")) ? TRUE : FALSE);
		}
		else
		{
			map = new HashMap<String, String>();
			map.put(FLAG, FALSE);
		}
		return map;
	}

	/**
	 * 转换响应参数
	 * 
	 * @param str
	 * @return
	 * @throws BaseException
	 */
	private Map<String, String> _convert(String str) throws BaseException
	{
		Map<String, String> map = new HashMap<String, String>();
		try
		{
			if (str.indexOf("<State>") < 0 || str.indexOf("</State>") < 0)
			{
				map.put("result", "-1");
			}
			else
			{
				int start = str.indexOf("<State>") + 7;
				int end = str.indexOf("</State>");
				String result = str.substring(start, end);
				map.put("result", result);
			}

			/*
			 * String[] strs = str.split("<State>"); for (String s : strs) {
			 * String[] ss= s.split("="); if(ss.length == 2){ map.put(ss[0],
			 * ss[1]); } }
			 */
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new BaseException("转换响应参数异常");
		}
		return map;
	}

	public String getMessage(final HttpEntity entity, final Charset charset) throws IOException, ParseException
	{
		if (entity == null)
		{
			throw new IllegalArgumentException("HTTP entity may not be null");
		}
		InputStream instream = entity.getContent();
		if (instream == null)
		{
			return null;
		}
		try
		{
			if (entity.getContentLength() > Integer.MAX_VALUE)
			{
				throw new IllegalArgumentException("HTTP entity too large to be buffered in memory");
			}
			int i = (int) entity.getContentLength();
			if (i < 0)
			{
				i = 4096;
			}
			Reader reader = new InputStreamReader(instream, charset);
			CharArrayBuffer buffer = new CharArrayBuffer(i);
			char[] tmp = new char[1024];
			int l;
			while ((l = reader.read(tmp)) != -1)
			{
				buffer.append(tmp, 0, l);
			}
			return buffer.toString();
		}
		finally
		{
			instream.close();
		}
	}

	public static String generateCode(int length)
	{
		StringBuilder code = new StringBuilder();
		Random r = new Random();
		while (length-- > 0)
		{
			code.append(r.nextInt(10));
		}

		return code.toString();
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getLoginName()
	{
		return loginName;
	}

	public void setLoginName(String loginName)
	{
		this.loginName = loginName;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getProductId()
	{
		return productId;
	}

	public void setProductId(String productId)
	{
		this.productId = productId;
	}
	
	
}
