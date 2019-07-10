package com.auth.service;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth.bean.ThirdPartyUser;
import com.auth.config.ThirdPartyConfig;
import com.auth.enums.AppNameEnums;
import com.auth.enums.ThirdTypeEnums;
import com.auth.util.ExceptionUtil;
import com.auth.util.TimeUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WxService {

	private RestTemplate restTemplate;
	
	public WxService(RestTemplateBuilder builder) {
		restTemplate = builder.build();
	}
	
	/**
	 * 不成功返回null
	 */
	public ThirdPartyUser wxUserInfo(String appname, String code) {
		JSONObject accessTokenAndOpenId = accessTokenAndOpenId(appname, code);
		if(Objects.isNull(accessTokenAndOpenId)) {
			return null;
		}
		
		return userinfo(appname, accessTokenAndOpenId.getString("access_token"), accessTokenAndOpenId.getString("openid"));
	}
	
	/**
	 * 不成功返回null
	 */
	public JSONObject accessTokenAndOpenId(String appname, String code) {
		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("appid", ThirdPartyConfig.getWxAppId(AppNameEnums.valueOf(appname)));
		uriVariables.put("secret", ThirdPartyConfig.getWxSecret(AppNameEnums.valueOf(appname)));
		uriVariables.put("code", code);
		
		ResponseEntity<String> response = restTemplate.getForEntity(ThirdPartyConfig.wxAccesstokenURL, String.class, uriVariables);
		log.info("appname :{}; wx code :{}; response: {}", appname, code, response);
		
		if(response.getStatusCodeValue() == HttpStatus.OK.value()) {
			JSONObject json = JSON.parseObject(response.getBody());
			if(json.containsKey("errcode")) {
				return null;
			}
			
			return json;
		}
		
		return null;
	}
	
	/**
	 * 不成功返回null
	 */
	public ThirdPartyUser userinfo(String appname, String accessToken, String openid) {
		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("accessToken", accessToken);
		uriVariables.put("openid", openid);
		
		ResponseEntity<String> response = restTemplate.getForEntity(ThirdPartyConfig.wxUserinfoURL, String.class, uriVariables);
		log.info("appname :{}; wx accessToken :{}; openid :{}; response: {}", appname, accessToken, openid, response);
		
		if(response.getStatusCodeValue() == HttpStatus.OK.value()) {
			String body = null;
			try {
				body = new String(response.getBody().getBytes("ISO-8859-1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				log.error("wx response decode ERROR :{}", ExceptionUtil.asString(e));
				return null;
			}
			JSONObject json = JSON.parseObject(body);
			if(json.containsKey("errcode")) {
				return null;
			}
			
			ThirdPartyUser user = new ThirdPartyUser();
			user.setAppname(appname);
			user.setOpenId(json.getString("openid"));
			user.setNickname(json.getString("nickname"));
			user.setSex(json.getIntValue("sex"));//普通用户性别，1为男性，2为女性
			user.setImg(json.getString("headimgurl"));
			user.setThirdType(ThirdTypeEnums.wx.name());
			long time = TimeUtil.currentMilli();
			user.setUpdateTime(time);
			user.setCreateTime(time);
			
			return user;
		}
		
		return null;
	}
}
