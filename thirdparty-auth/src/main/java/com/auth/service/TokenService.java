package com.auth.service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.auth.config.CommonConfig;
import com.auth.util.MD5Util;
import com.auth.util.TimeUtil;

@Service
public class TokenService {
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	private final static String CACHE_FLAG = "XWK_";
	private final static String TOKEN_SALT = "!qscvhu*";  //token加密salt

	private String tokenKey(Long userId) {
		return CACHE_FLAG + "token_" + userId;
	}
	
	public String buildToken(Long userId) {
		String key = tokenKey(userId);
		String token =  MD5Util.md5(TimeUtil.currentMilli() + TOKEN_SALT);
		
		stringRedisTemplate.opsForValue().set(key, token, CommonConfig.userExpireTime, TimeUnit.SECONDS);
		return token;
	}
	
	public boolean checkToken(Long userId, String token) {
		String key = tokenKey(userId);
		String presentToken = stringRedisTemplate.opsForValue().get(key);
		
		if(Objects.isNull(presentToken)) {
			return false;
		}
		
		if(presentToken.equals(token)) {
			return true;
		}
		
		return false;
	}
}
