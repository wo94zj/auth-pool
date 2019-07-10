package com.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:common.properties")
@ConfigurationProperties(prefix = "project.server")
public class CommonConfig {

	public static Integer userExpireTime;
	
	public void setUserExpireTime(Integer userExpireTime) {
		CommonConfig.userExpireTime = userExpireTime;
	}
}
