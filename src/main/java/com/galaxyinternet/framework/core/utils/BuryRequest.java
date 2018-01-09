package com.galaxyinternet.framework.core.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.galaxyinternet.framework.core.model.BuryPointEntity;
import com.galaxyinternet.framework.core.model.BuryPointResult;
import com.galaxyinternet.framework.core.model.Result.Status;

public class BuryRequest {
	private static final Logger logger = LoggerFactory.getLogger(BuryRequest.class);
	private String burySaveUrl;
	private String burySoftWareVersion;
	private RestTemplate template;
	private ExecutorService buryReqPool; 
	public BuryRequest()
	{
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setConnectTimeout(2000);
		factory.setReadTimeout(5000);
		template = new RestTemplate(factory);
		
		buryReqPool = Executors.newSingleThreadExecutor(new ThreadFactory(){

			@Override
			public Thread newThread(Runnable r)
			{
				Thread t = new Thread(r);
				t.setName("Bury-Thread");
				return t;
			}
			
		});
	}
	public String getBurySaveUrl() {
		return burySaveUrl;
	}
	public void setBurySaveUrl(String burySaveUrl) {
		this.burySaveUrl = burySaveUrl;
	}
	
	public String burySave(final BuryPointEntity entity){
		buryReqPool.execute(new Runnable(){
			@Override
			public void run()
			{
				HttpHeaders headers = new HttpHeaders();
				entity.getList().get(0).setSoftVersion(burySoftWareVersion);
				headers.set("userId", entity.getUserId());
				headers.set("sessionId", entity.getSessionId());
				headers.setContentType(MediaType.APPLICATION_JSON);
				HttpEntity<BuryPointEntity> request = new HttpEntity<BuryPointEntity>(entity, headers);
				if(logger.isDebugEnabled())
				{
					logger.debug(String.format("Bury Request: %s, URL: %s, version:%s", entity, burySaveUrl+"/bury/save", burySoftWareVersion));
				}
				ResponseEntity<BuryPointResult> rtn = template.postForEntity(burySaveUrl+"/bury/save", request, BuryPointResult.class);
				if(logger.isDebugEnabled())
				{
					logger.debug(String.format("Bury Response: %s", rtn.getBody()));
				}
			}
		});
		return Status.OK.toString();

	}
	public String getBurySoftWareVersion() {
		return burySoftWareVersion;
	}
	public void setBurySoftWareVersion(String burySoftWareVersion) {
		this.burySoftWareVersion = burySoftWareVersion;
	}
	
	
	

}
