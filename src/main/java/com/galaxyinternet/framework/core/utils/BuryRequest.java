package com.galaxyinternet.framework.core.utils;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.galaxyinternet.framework.core.model.BuryPointResult;

public class BuryRequest {
	private static final Logger logger = LoggerFactory.getLogger(BuryRequest.class);
	private String burySaveUrl;
	private RestTemplate template = new RestTemplate();
	
	public String getBurySaveUrl() {
		return burySaveUrl;
	}
	public void setBurySaveUrl(String burySaveUrl) {
		this.burySaveUrl = burySaveUrl;
	}
	
	public String burySave(Map<String, String> urlVariables){
		//Map<String, String> urlVariables = new HashMap<>();
		HttpHeaders headers = new HttpHeaders();
		headers.set("userId", urlVariables.get("userId"));
		headers.set("sessionId", urlVariables.get("sessionId"));
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Map<String, String>> request = new HttpEntity<>(urlVariables, headers);
		ResponseEntity<BuryPointResult> rtn = template.postForEntity(burySaveUrl+"/bury/save", request, BuryPointResult.class);
		return rtn.getBody().getErrorCode();

	}
	
	

}
