package com.galaxyinternet.framework.core.utils;

import com.galaxyinternet.framework.core.exception.BaseException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.CharArrayBuffer;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 短信接口
 * Created by zhaoying on 2016/11/9.
 */
public class SmsUtil {
    public final Logger _logger = Logger.getLogger(SmsUtil.class);

    public static String TRUE = "true";
    public static String FALSE = "false";
    public static String FLAG = "flag";
    private static String UTF_8 = "utf-8";
    private static HttpClient client = null;

    //短信平台地址
    private static final String url = "http://cf.51welink.com/submitdata/Service.asmx/g_Submit";
    //企业代码
    private static String spCode = "";
    //账号
    private static String loginName = "DL-fudl";
    //密码
    private static String password = "1uA6RvGL";
    //产品编号
    private static String productId = "1012818";


    /**
     * 发送普通短信
     * @param messages
     * @param mobile
     * @return
     * @throws BaseException
     */
    public static boolean send(String messages,String mobile) throws BaseException {
        try {
            client = new DefaultHttpClient();
            //建立HttpPost对象
            HttpPost httpPost = createPost(messages, mobile);
            //发送Post,并返回一个HttpResponse对象
            HttpResponse response=client.execute(httpPost);
            //如果状态码是200，则正常返回
            Map<String, String> map = passer(response);
            return TRUE.equals(map.get(FLAG));
        }catch (Exception e) {
            throw new BaseException("发送短信异常",e);
        }finally{
            if(null != client){
                client.getConnectionManager().shutdown();
            }
        }
    }


    private static HttpPost createPost(String messages,String mobile)
            throws UnsupportedEncodingException {
        HttpPost httpPost=new HttpPost(url);
        //建立一个NameValuePair数组，用于存储欲传递的参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        //添加参数
        nvps.add(new BasicNameValuePair("sname", loginName));
        nvps.add(new BasicNameValuePair("spwd", password));
        nvps.add(new BasicNameValuePair("scorpid", spCode));
        nvps.add(new BasicNameValuePair("sprdid", productId));
        nvps.add(new BasicNameValuePair("sdst", mobile));
        nvps.add(new BasicNameValuePair("smsg",messages));

        //设置编码
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, UTF_8));
        return httpPost;
    }

    private static Map<String,String> passer(HttpResponse response)
            throws Exception {
        Map<String, String> map = null;
        if (null != response && response.getStatusLine().getStatusCode()==200) {
            //如果是下载的文件，可以用response.getEntity().getContent返回InputStream
            //获得返回的字符串
            String result=getMessage(response.getEntity(), Charset.forName(UTF_8));

            map = _convert(result);
            map.put(FLAG, "0".equals(map.get("result")) ? TRUE : FALSE);
        } else {
            map = new HashMap<String, String>();
            map.put(FLAG, FALSE);
        }
        return map;
    }

    /**
     * 转换响应参数
     * @param str
     * @return
     * @throws BaseException
     */
    private static Map<String,String> _convert(String str)throws BaseException{
        Map<String,String> map = new HashMap<String,String>();
        try {
        	if (str.indexOf("<State>")<0||str.indexOf("</State>")<0) {
        		map.put("result", "-1");
        	} else {
        		int start = str.indexOf("<State>")+7;
            	int end = str.indexOf("</State>");
            	String result = str.substring(start, end);
            	map.put("result", result);
        	}
        	
        	/* String[] strs = str.split("<State>");
            for (String s : strs) {
                String[] ss= s.split("=");
                if(ss.length == 2){
                    map.put(ss[0], ss[1]);
                }
            }*/
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException("转换响应参数异常");
        }
        return map;
    }

    public static String getMessage(
            final HttpEntity entity, final Charset charset) throws IOException, ParseException {
        if (entity == null) {
            throw new IllegalArgumentException("HTTP entity may not be null");
        }
        InputStream instream = entity.getContent();
        if (instream == null) {
            return null;
        }
        try {
            if (entity.getContentLength() > Integer.MAX_VALUE) {
                throw new IllegalArgumentException("HTTP entity too large to be buffered in memory");
            }
            int i = (int)entity.getContentLength();
            if (i < 0) {
                i = 4096;
            }
            Reader reader = new InputStreamReader(instream, charset);
            CharArrayBuffer buffer = new CharArrayBuffer(i);
            char[] tmp = new char[1024];
            int l;
            while((l = reader.read(tmp)) != -1) {
                buffer.append(tmp, 0, l);
            }
            return buffer.toString();
        } finally {
            instream.close();
        }
    }


    public static void main(String[] args) {

       boolean flag = SmsUtil.send("ok","18311082369");
       System.out.println(flag);

    }
}
