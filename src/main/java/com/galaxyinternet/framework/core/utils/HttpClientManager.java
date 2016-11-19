package com.galaxyinternet.framework.core.utils;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLException;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientManager
{
	private static final Logger logger = LoggerFactory.getLogger(HttpClientManager.class);
	private PoolingHttpClientConnectionManager cm = null;
	private static HttpClientManager instance = null;
	private HttpClientManager()
	{
		cm = new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(50);
		cm.setDefaultMaxPerRoute(50);
	}
	public static HttpClientManager getInstance()
	{
		if(instance == null)
		{
			instance = new HttpClientManager();
		}
		return instance;
	}
	HttpRequestRetryHandler myRetryHandler = new HttpRequestRetryHandler() {

		@Override
		public boolean retryRequest(IOException exception, int executionCount, HttpContext context)
		{
			if (executionCount >= 3) {  
                logger.error("重试超过三次，",exception);
                return false;  
            }  
			logger.error("链接错误",exception);
            if (exception instanceof InterruptedIOException) {  
                // Timeout  
                return false;  
            }  
            if (exception instanceof UnknownHostException) {  
                // Unknown host  
                return false;  
            }  
            if (exception instanceof ConnectTimeoutException) {  
                // Connection refused  
                return false;             
            }  
            if (exception instanceof SSLException) {  
                // SSL handshake exception  
                return false;  
            }  
            HttpClientContext clientContext = HttpClientContext.adapt(context);  
            HttpRequest request = clientContext.getRequest();  
            boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);  
			if (idempotent) {  
                // Retry if the request is considered idempotent  
                return true;  
            }  
            return false; 
		}
	};

	public CloseableHttpClient getHttpClient(){       
        // 创建全局的requestConfig  
        RequestConfig requestConfig = RequestConfig.custom()  
                .setConnectTimeout(2000)  
                .setSocketTimeout(50000)  
                .setCookieSpec(CookieSpecs.DEFAULT).build();  
        // 声明重定向策略对象  
        LaxRedirectStrategy redirectStrategy = new LaxRedirectStrategy();  
          
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm)  
                                                    .setDefaultRequestConfig(requestConfig)  
                                                    .setRedirectStrategy(redirectStrategy)  
                                                    .setRetryHandler(myRetryHandler)  
                                                    .build();     
        return httpClient;   
    } 
}
