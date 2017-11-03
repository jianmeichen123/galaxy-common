package com.galaxyinternet.framework.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.galaxyinternet.framework.core.model.BuryPointEntity;
import com.galaxyinternet.framework.core.model.BuryPointResult;

public class BuryRequest {
	private static final Logger logger = LoggerFactory.getLogger(BuryRequest.class);
	private String burySaveUrl;
	private String burySoftWareVersion;
	private RestTemplate template = new RestTemplate();
	
	public String getBurySaveUrl() {
		return burySaveUrl;
	}
	public void setBurySaveUrl(String burySaveUrl) {
		this.burySaveUrl = burySaveUrl;
	}
	
	public String burySave(BuryPointEntity entity){
		HttpHeaders headers = new HttpHeaders();
		entity.getList().get(0).setSoftVersion(burySoftWareVersion);
		headers.set("userId", entity.getUserId());
		headers.set("sessionId", entity.getSessionId());
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<BuryPointEntity> request = new HttpEntity<BuryPointEntity>(entity, headers);
		ResponseEntity<BuryPointResult> rtn = template.postForEntity(burySaveUrl+"/bury/save", request, BuryPointResult.class);
		return rtn.getBody().getErrorCode();

	}
	public String getBurySoftWareVersion() {
		return burySoftWareVersion;
	}
	public void setBurySoftWareVersion(String burySoftWareVersion) {
		this.burySoftWareVersion = burySoftWareVersion;
	}
	
	
	

}
