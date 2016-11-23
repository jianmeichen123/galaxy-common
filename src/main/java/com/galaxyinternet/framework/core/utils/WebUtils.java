package com.galaxyinternet.framework.core.utils;



import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;



/**
 * 网络工具类。
 * 
 * doPost  doGet 请求
 * URLDecoder方法   
 * 
 * 通过url获取对应参数
 */
public abstract class WebUtils {

	private static final String DEFAULT_CHARSET = "utf-8";
	public static final String METHOD_POST = "POST";
	public static final String METHOD_GET = "GET";
	private static class DefaultTrustManager implements X509TrustManager {
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}
	}

	private WebUtils() {
	}


	public static String doPost(String method,String url, String params, Map<String, String> headerMap) throws IOException {
		String ctype = "application/json; charset=" + DEFAULT_CHARSET;
		byte[] content = null;
		if(StringUtils.isNotBlank(params)){
			content = params.getBytes(DEFAULT_CHARSET);
		}
		return _doPost(method,url, ctype, content, 2000, 2000, headerMap);
	}
	public static void main(String[] args) throws Exception {
//		String url = "http://yun_m.gi.com/api/datasources/proxy/2/query?epoch=ms&q=SELECT+mean(%22value%22)+%2F10000000+FROM+%22virt_cpu_time%22+WHERE+%22hostname%22+%3D~+%2Fnode-76$%2F+AND+%22instance_id%22+%3D~+%2F013f8de7-f17b-4ac1-b7f4-a6d211f397fd$%2F+AND+time+%3E+now()+-+1h+GROUP+BY+time(1m)+fill(none)%0ASELECT+mean(%22value%22)+%2F10000000+FROM+%22virt_vcpu_time%22+WHERE+%22hostname%22+%3D~+%2Fnode-76$%2F+AND+%22instance_id%22+%3D~+%2F013f8de7-f17b-4ac1-b7f4-a6d211f397fd$%2F+AND+time+%3E+now()+-+1h+GROUP+BY+time(1m),+%22vcpu_number%22+fill(none)";
//		String result = sendGet(url, null);
//		System.out.println(result);
//		JSONObject resultJson = JSONObject.parseObject(result);
//		JSONArray resultArray =  resultJson.getJSONArray("results");
//		List<String> xaisList = new ArrayList<String>();
//		List<String> cpuList = new ArrayList<String>();
//		List<String> cpu0List = new ArrayList<String>();
//		List<String> cpu1List = new ArrayList<String>();
//		List<String> cpu2List = new ArrayList<String>();
//		List<String> cpu3List = new ArrayList<String>();
//		System.out.println(System.currentTimeMillis());
//		String tag = null;
//		for(int i =0; i < resultArray.size();i++){
//			JSONObject seJson = resultArray.getJSONObject(i).getJSONArray("series").getJSONObject(0);
//			if(seJson.containsKey("tag")){
//				tag = seJson.getJSONObject("tags").getString("vcpu_number");
//			}
//			String name = seJson.getString("name");
//			JSONArray valueJa = seJson.getJSONArray("values");
//			System.out.println(valueJa.size());
//			for(int j = 0; j< valueJa.size() ; j++){
//				String   xais = valueJa.getJSONArray(j).get(0).toString();
//				String   value = valueJa.getJSONArray(j).get(1).toString();
//				if(tag == null){
//					cpuList.add(value);
//				}else{
//					if(StringUtils.equals("0", tag)){
//						cpu0List.add(value);
//					}else if(StringUtils.equals("2", tag)){
//						cpu1List.add(value);
//					}else if(StringUtils.equals("3", tag)){
//						cpu2List.add(value);
//					}else{
//						cpu3List.add(value);
//					}
//				}
//				System.out.println(name + ":" + NumberUtils.formatData(Double.valueOf(value)));
//			}
//			//Tue Oct 25 13:09:56 CST 2016 Sun Oct 23 12:09:56 CST 2016
//		}
//		System.out.println(new Date(1477372773000l));
//		String query_host_name_url = "http://yun_m.gi.com/api/datasources/proxy/2/query?epoch=ms&q=show+tag+values+from+openstack_nova_running_instances+with+key+%3D+hostname+where+environment_label+%3D+%27env-13%27";
//		String result = sendGet(query_host_name_url,null);
//		JSONObject resultJson = JSONObject.parseObject(result);
//		JSONArray resultArray =  resultJson.getJSONArray("results");
//		String instanceId = "f72feff4-8757-44db-9121-bea149b3768b";
//		for(int i =0; i < resultArray.size();i++){
//			JSONObject seJson = resultArray.getJSONObject(i).getJSONArray("series").getJSONObject(0);
//			JSONArray valueJa = seJson.getJSONArray("values");
//			for(int j = 0; j< valueJa.size() ; j++){
//				String hostName = valueJa.getJSONArray(j).get(0).toString();
//				String query_instance_url= "http://yun_m.gi.com/api/datasources/proxy/2/query?epoch=ms&q=show+tag+values+from+virt_cpu_time+with+key+%3D+instance_id+where+hostname+%3D+%27"+hostName+"%27";
//				result = sendGet(query_instance_url, null);
//				if(result.contains(instanceId)){
//					System.out.println("hostName = " + hostName);
//				}
//			}
//		}
		double ss =   1475135031000l ; //1477385031s 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		System.out.println(sdf.format(new Date(System.currentTimeMillis())));
		//2016-09-29 2016-10-25
		System.out.println(sdf.parse("2016-09-29 15:43:51").getTime()/1000);
	}

	/**
	 * 执行HTTP POST请求。
	 * 
	 * @param url 请求地址
	 * @param ctype 请求类型
	 * @param content 请求字节数组
	 * @return 响应字符串
	 * @throws IOException
	 */
	@Deprecated
	public static String doPost(String method,String url, String ctype, byte[] content, int connectTimeout, int readTimeout)
			throws IOException {
		return _doPost(method,url, ctype, content, connectTimeout, readTimeout, null);
	}
	private static String _doPost(String method,String url, String ctype, byte[] content, int connectTimeout, int readTimeout,
			Map<String, String> headerMap) throws IOException {
		HttpURLConnection conn = null;
		OutputStream out = null;
		String rsp = null;
		try {
			try {
				conn = getConnection(new URL(url), method, ctype, headerMap);
				conn.setConnectTimeout(connectTimeout);
				conn.setReadTimeout(readTimeout);
			} catch (IOException e) {
				throw e;
			}
			try {
				out = conn.getOutputStream();
//				out.write(content);
				rsp = getResponseAsString(conn);
			} catch (IOException e) {
				throw e;
			}

		} finally {
			if (out != null) {
				out.close();
			}
			if (conn != null) {
				conn.disconnect();
			}
		}

		return rsp;
	}



	private static HttpURLConnection getConnection(URL url, String method, String ctype, Map<String, String> headerMap) throws IOException {
		HttpURLConnection conn = null;
		if ("https".equals(url.getProtocol())) {
			SSLContext ctx = null;
			try {
				ctx = SSLContext.getInstance("TLS");
				ctx.init(new KeyManager[0], new TrustManager[] { new DefaultTrustManager() }, new SecureRandom());
			} catch (Exception e) {
				throw new IOException(e);
			}
			HttpsURLConnection connHttps = (HttpsURLConnection) url.openConnection();
			connHttps.setSSLSocketFactory(ctx.getSocketFactory());
			connHttps.setHostnameVerifier(new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;// 默认都认证通过
				}
			});
			conn = connHttps;
		} else {
			conn = (HttpURLConnection) url.openConnection();
		}

		conn.setRequestMethod(method);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87 Safari/537.36");
		conn.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");
		conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
		conn.setRequestProperty("Upgrade-Insecure-Requests", "1");
		conn.setRequestProperty("Connection", "keep-alive");
		conn.setRequestProperty("Cookie", "grafana_sess=800dace5085f852d; grafana_user=lma; grafana_remember=0e13a3646a6711015e554ffb385c2009bf45d6b6");
		if (headerMap != null) {
			for (Map.Entry<String, String> entry : headerMap.entrySet()) {
				conn.setRequestProperty(entry.getKey(), entry.getValue());
			}
		}
		return conn;
	}

	protected static String getResponseAsString(HttpURLConnection conn) throws IOException {
		String charset = getResponseCharset(conn.getContentType());
		InputStream es = conn.getErrorStream();
		if (es == null) {
			return getStreamAsString(conn.getInputStream(), charset);
		} else {
			String msg = getStreamAsString(es, charset);
			if (StringUtils.isEmpty(msg)) {
				throw new IOException(conn.getResponseCode() + ":" + conn.getResponseMessage());
			} else {
				throw new IOException(msg);
			}
		}
	}

	private static String getStreamAsString(InputStream stream, String charset) throws IOException {
		try {
			Reader reader = new InputStreamReader(stream, charset);
			StringBuilder response = new StringBuilder();

			final char[] buff = new char[1024];
			int read = 0;
			while ((read = reader.read(buff)) > 0) {
				response.append(buff, 0, read);
			}

			return response.toString();
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
	}

	private static String getResponseCharset(String ctype) {
		String charset = DEFAULT_CHARSET;
		if (!StringUtils.isEmpty(ctype)) {
			String[] params = ctype.split(";");
			for (String param : params) {
				param = param.trim();
				if (param.startsWith("charset")) {
					String[] pair = param.split("=", 2);
					if (pair.length == 2) {
						if (!StringUtils.isEmpty(pair[1])) {
							charset = pair[1].trim();
						}
					}
					break;
				}
			}
		}
		return charset;
	}

	/**
	 * 使用默认的UTF-8字符集反编码请求参数值。
	 * 
	 * @param value 参数值
	 * @return 反编码后的参数值
	 */
	public static String decode(String value) {
		return decode(value, DEFAULT_CHARSET);
	}

	/**
	 * 使用默认的UTF-8字符集编码请求参数值。
	 * 
	 * @param value 参数值
	 * @return 编码后的参数值
	 */
	public static String encode(String value) {
		return encode(value, DEFAULT_CHARSET);
	}

	/**
	 * 使用指定的字符集反编码请求参数值。
	 * 
	 * @param value 参数值
	 * @param charset 字符集
	 * @return 反编码后的参数值
	 */
	public static String decode(String value, String charset) {
		String result = null;
		if (!StringUtils.isEmpty(value)) {
			try {
				result = URLDecoder.decode(value, charset);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return result;
	}

	/**
	 * 使用指定的字符集编码请求参数值。
	 * 
	 * @param value 参数值
	 * @param charset 字符集
	 * @return 编码后的参数值
	 */
	public static String encode(String value, String charset) {
		String result = null;
		if (!StringUtils.isEmpty(value)) {
			try {
				result = URLEncoder.encode(value, charset);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return result;
	}

	public static Map<String, String> getParamsFromUrl(String url) {
		Map<String, String> map = null;
		if (url != null && url.indexOf('?') != -1) {
			map = splitUrlQuery(url.substring(url.indexOf('?') + 1));
		}
		if (map == null) {
			map = new HashMap<String, String>();
		}
		return map;
	}

	/**
	 * 从URL中提取所有的参数。
	 * 
	 * @param query URL地址
	 * @return 参数映射
	 */
	public static Map<String, String> splitUrlQuery(String query) {
		Map<String, String> result = new HashMap<String, String>();

		String[] pairs = query.split("&");
		if (pairs != null && pairs.length > 0) {
			for (String pair : pairs) {
				String[] param = pair.split("=", 2);
				if (param != null && param.length == 2) {
					result.put(param[0], param[1]);
				}
			}
		}

		return result;
	}
	
	public static String getIpAddress(HttpServletRequest request) { 
	    String ip = request.getHeader("x-forwarded-for"); 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getHeader("Proxy-Client-IP"); 
	    } 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getHeader("WL-Proxy-Client-IP"); 
	    } 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getRemoteAddr(); 
	    }
	    if((!StringUtils.isEmpty(ip)) && ip.indexOf(",")>1){
	    	ip = ip.split(",")[0];
	    }
	    return ip; 
	  } 
	/**
	 * unicode 转换成 中文
	 * 
	 * @author fanhui 2007-3-15
	 * @param theString
	 * @return
	 */
	public static String decodeUnicode(String theString) {
		char aChar;
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);
		for (int x = 0; x < len;) {
			aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);
				if (aChar == 'u') {
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException(
									"Malformed      encoding.");
						}
					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't') {
						aChar = '\t';
					} else if (aChar == 'r') {
						aChar = '\r';
					} else if (aChar == 'n') {
						aChar = '\n';
					} else if (aChar == 'f') {
						aChar = '\f';
					}
					outBuffer.append(aChar);
				}
			} else {
				outBuffer.append(aChar);
			}
		}
		return outBuffer.toString();
	}
	
	
	/**
	 * @param urlStr
	 *            请求的地址
	 * @param content
	 *            请求的参数 格式为：name=xxx&pwd=xxx
	 * @param encoding
	 *            服务器端请求编码。如GBK,UTF-8等
	 * @return
	 */
	private static String getResult(String urlStr, String content, String encoding) {
		URL url = null;
		HttpURLConnection connection = null;
		try {
			url = new URL(urlStr);
			connection = (HttpURLConnection) url.openConnection();// 新建连接实例
			connection.setConnectTimeout(2000);// 设置连接超时时间，单位毫秒
			connection.setReadTimeout(2000);// 设置读取数据超时时间，单位毫秒
			connection.setDoOutput(true);// 是否打开输出流 true|false
			connection.setDoInput(true);// 是否打开输入流true|false
			connection.setRequestMethod("POST");// 提交方法POST|GET
			connection.setUseCaches(false);// 是否缓存true|false
			connection.connect();// 打开连接端口
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());// 打开输出流往对端服务器写数据
			out.writeBytes(content);// 写数据,也就是提交你的表单 name=xxx&pwd=xxx
			out.flush();// 刷新
			out.close();// 关闭输出流
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding));// 往对端写完数据对端服务器返回数据
			// ,以BufferedReader流来读取
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			reader.close();
			return buffer.toString();
		} catch (IOException e) {
			//e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();// 关闭连接
			}
		}
		return null;
	}
	 public static String sendGet(String url, String param) {
	        String result = "";
	        BufferedReader in = null;
	        try {
	            String urlNameString = url;
	            URL realUrl = new URL(urlNameString);
	            // 打开和URL之间的连接
	            URLConnection connection = realUrl.openConnection();
	            // 设置通用的请求属性
	            connection.setRequestProperty("accept", "*/*");
	            connection.setRequestProperty("connection", "Keep-Alive");
	            connection.setRequestProperty("user-agent",
	                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	            connection.setRequestProperty("accept", "*/*");
	            connection.setRequestProperty("Authorization", " Bearer eyJrIjoienNtS3EwNDZLR3p4aWU5VnMzYkU5eHk0dVl2R01VTGwiLCJuIjoidGVzdCIsImlkIjoxfQ==");
	            // 建立实际的连接
	            connection.connect();
	            // 获取所有响应头字段
	            Map<String, List<String>> map = connection.getHeaderFields();
	            // 遍历所有的响应头字段
	            for (String key : map.keySet()) {
	                System.out.println(key + "--->" + map.get(key));
	            }
	            // 定义 BufferedReader输入流来读取URL的响应
	            in = new BufferedReader(new InputStreamReader(
	                    connection.getInputStream()));
	            String line;
	            while ((line = in.readLine()) != null) {
	                result += line;
	            }
	        } catch (Exception e) {
	            System.out.println("发送GET请求出现异常！" + e);
	            e.printStackTrace();
	        }
	        // 使用finally块来关闭输入流
	        finally {
	            try {
	                if (in != null) {
	                    in.close();
	                }
	            } catch (Exception e2) {
	                e2.printStackTrace();
	            }
	        }
	        return result;
	    }
	 
	 public static boolean isAjax(HttpServletRequest request)
	 {
		 String xReq = request.getHeader("X-Requested-With");
		 if(xReq != null && "XMLHttpRequest".equals(xReq))
		 {
			 return true;
		 }
		 return false;
	 }
	 
	public static Cookie getCookie(HttpServletRequest request, String name)
	 {
		if(name == null) return null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0)
		{
			for (Cookie cookie : cookies)
			{
				String cookieName = cookie.getName();
				if (name.equals(name))
				{
					return cookie;
				}
			}
		}
		return null;
	 }
}
