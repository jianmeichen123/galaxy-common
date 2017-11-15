package com.galaxyinternet.framework.core.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.galaxyinternet.framework.core.model.User;
import com.galaxyinternet.framework.core.model.UserResult;

public class AuthRequestUtil {
	private static final Logger logger = LoggerFactory.getLogger(AuthRequestUtil.class);
	private String authSaveUrl;
	private RestTemplate template = new RestTemplate();
	public String getAuthSaveUrl() {
		return authSaveUrl;
	}
	public void setAuthSaveUrl(String authSaveUrl) {
		this.authSaveUrl = authSaveUrl;
	}
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>>getUserList()
	{
		String uri = authSaveUrl + "/user/getUserList";
		Map<String, Integer> urlVariables = new HashMap<String, Integer>();
		if(logger.isDebugEnabled())
		{
			logger.debug(String.format("Request URI:%s, Params:%s", uri, urlVariables));
		}
		HttpHeaders headers = new HttpHeaders();
		urlVariables.put("pageNo", 0);
		urlVariables.put("pageSize", 500);
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Map<String, Integer>> request = new HttpEntity<>(urlVariables, headers);
		ParameterizedTypeReference<UserResult> ref = new ParameterizedTypeReference<UserResult>() {};  
		ResponseEntity<UserResult> rtn = template.exchange(uri, HttpMethod.POST,request, ref);
		Map<String,Object> dataMap=(Map<String,Object>)rtn.getBody().getValue();
		return ( List<Map<String,Object>>)dataMap.get("rows");
	}
	
	

}
