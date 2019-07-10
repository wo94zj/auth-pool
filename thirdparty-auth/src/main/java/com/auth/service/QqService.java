package com.auth.service;

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
import com.auth.util.TimeUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class QqService {

private RestTemplate restTemplate;
	
	public QqService(RestTemplateBuilder builder) {
		restTemplate = builder.build();
	}
	
	public ThirdPartyUser qqUserInfo(String appname, String accessToken) {
		String openId = openId(appname, accessToken);
		if(Objects.isNull(openId)) {
			return null;
		}
		
		return userinfo(appname, accessToken, openId);
	}
	
	public String openId(String appname, String accessToken) {
		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("accessToken", accessToken);
		
		ResponseEntity<String> response = restTemplate.getForEntity(ThirdPartyConfig.qqOpenidURL, String.class, uriVariables);
		log.info("appname :{}; qq accessToken :{}; response: {}", appname, accessToken, response);
		
		if(response.getStatusCodeValue() == HttpStatus.OK.value()) {
			//callback( {"client_id":"111","openid":"sfagg123"} )
			String body = response.getBody();
			body = body.substring(body.indexOf("{"), body.indexOf("}") + 1);
			JSONObject json = JSON.parseObject(body);
			if(json.containsKey("openid")) {
				return json.getString("openid");
			}
		}
		
		return null;
	}
	
	public ThirdPartyUser userinfo(String appname, String accessToken, String openid) {
		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("accessToken", accessToken);
		uriVariables.put("openid", openid);
		uriVariables.put("appid", ThirdPartyConfig.getQqAppId(AppNameEnums.valueOf(appname)));
		
		ResponseEntity<String> response = restTemplate.getForEntity(ThirdPartyConfig.qqUserinfoURL, String.class, uriVariables);
		log.info("appname :{}; qq accessToken :{}; openid :{}; response: {}", appname, accessToken, openid, response);
		
		if(response.getStatusCodeValue() == HttpStatus.OK.value()) {
			JSONObject json = JSON.parseObject(response.getBody());
			if(json.containsKey("ret") && json.getIntValue("ret") == 0) {
				ThirdPartyUser user = new ThirdPartyUser();
				user.setAppname(appname);
				user.setOpenId(openid);
				user.setNickname(json.getString("nickname"));
				user.setSex(json.getString("gender").equals("男")?1:2);//普通用户性别，1为男性，2为女性
				user.setImg(json.getString("figureurl_qq_1"));
				user.setThirdType(ThirdTypeEnums.qq.name());
				long time = TimeUtil.currentMilli();
				user.setUpdateTime(time);
				user.setCreateTime(time);
				
				return user;
			}
		}
		
		return null;
	}
}
