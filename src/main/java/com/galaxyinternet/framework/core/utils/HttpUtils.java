package com.galaxyinternet.framework.core.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpUtils {

    private static CloseableHttpClient httpClient = null;

    public static CloseableHttpClient httpClient(){
        if(httpClient!=null) return  HttpUtils.httpClient;
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        // Increase max total connection to 200
        cm.setMaxTotal(200);
        // Increase default max connection per route to 20
        cm.setDefaultMaxPerRoute(20);
        HttpUtils.httpClient = HttpClients.custom().setConnectionManager(cm).build();
        return httpClient;
    }

    public static String httpGet(String url){
    	return httpGet(url,null);
    }
    
    public static String httpGet(String url, Map<String, String> requestParams) {

        HttpGet httpGet = null;
        String result = "";
        try {
            // 参数设置
            StringBuilder builder = new StringBuilder(url);
            builder.append("?");
            if(requestParams!=null)
            for (Map.Entry<String, String> entry : requestParams.entrySet()) {
                builder.append((String) entry.getKey());
                builder.append("=");
                builder.append((String) entry.getValue());
                builder.append("&");
            }
            String tmpUrl = builder.toString();
            tmpUrl = tmpUrl.substring(0, tmpUrl.length() - 1);
            httpGet = new HttpGet(tmpUrl);
            HttpResponse response = httpClient().execute(httpGet);
            // 网页内容
            HttpEntity httpEntity = response.getEntity();
            result = EntityUtils.toString(httpEntity);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpGet != null) {
                httpGet.abort();
            }
        }
        return result;
    }

    public static String httpPost(String url, Map<String, String> requestParams, String urlEncode) {

        HttpPost httpPost = null;
        String result = "";
        try {
            // 参数设置
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            if(requestParams!=null)
            for (Map.Entry<String, String> entry : requestParams.entrySet()) {
                params.add(new BasicNameValuePair((String) entry.getKey(),
                        (String) entry.getValue()));
            }

            httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params, urlEncode));
            httpPost.setHeader("sessionId", requestParams.get("sessionId"));
            httpPost.setHeader("userId", requestParams.get("userId"));
            // reponse header
            HttpResponse response = httpClient().execute(httpPost);
            // 网页内容
            HttpEntity httpEntity = response.getEntity();
            result = EntityUtils.toString(httpEntity);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpPost != null) {
                httpPost.abort();
            }
        }
        return result;
    }
    public static void main(String[] args) {
    	System.out.println(HttpUtils.httpGet("https://p.youpin114.com/base/selectAll", null));
	}
}
