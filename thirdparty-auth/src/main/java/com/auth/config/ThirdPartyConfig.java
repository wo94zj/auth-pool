package com.auth.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.auth.common.AppNameDynamicUtil;
import com.auth.enums.AppNameEnums;

/**
 * 支持数组配置加载
 *
 */
@Configuration
@PropertySource("classpath:thirdauth.properties") //不能加载yml文件
@ConfigurationProperties(prefix = "third.auth")
public class ThirdPartyConfig {

	/**
	 * 微信认证配置
	 */
	private static Map<String, String> wxAppid = new HashMap<>();
	private static Map<String, String> wxSecret = new HashMap<>();
	
	public static String wxAccesstokenURL;
	public static String wxUserinfoURL;
	
	/**
	 * QQ认证配置
	 */
	private static Map<String, String> qqAppid = new HashMap<>();
	private static Map<String, String> qqSecret = new HashMap<>();
	
	public static String qqOpenidURL;
	public static String qqUserinfoURL;
	
	/**
	 * 支付宝认证配置
	 */
	private static Map<String, String> aliAppid = new HashMap<>();
	private static Map<String, String> aliPid = new HashMap<>();
	
	private static Map<String, String> aliAppprivatekey = new HashMap<>();
	private static Map<String, String> aliPaypublickey = new HashMap<>();
	

	@PostConstruct
	private void initAppNameEnums() {
		Set<String> keys = wxAppid.keySet();
		for (String key : keys) {
			AppNameDynamicUtil.addEnum(AppNameEnums.class, key);
		}
		
		keys = aliAppid.keySet();
		for (String key : keys) {
			AppNameDynamicUtil.addEnum(AppNameEnums.class, key);
		}
	}

	public static String getAliAppprivatekey(AppNameEnums app) {
		return aliAppprivatekey.get(app.name());
	}
	public static String getAliPaypublickey(AppNameEnums app) {
		return aliPaypublickey.get(app.name());
	}
	public static String getAliAppId(AppNameEnums app) {
		return aliAppid.get(app.name());
	}
	public static String getAliPId(AppNameEnums app) {
		return aliPid.get(app.name());
	}
	
	public static String getWxAppId(AppNameEnums app) {
		return wxAppid.get(app.name());
	}
	public static String getWxSecret(AppNameEnums app) {
		return wxSecret.get(app.name());
	}
	
	public static String getQqAppId(AppNameEnums app) {
		return qqAppid.get(app.name());
	}
	public static String getQqSecret(AppNameEnums app) {
		return qqSecret.get(app.name());
	}
	
	
	public void setWxAppid(Map<String, String> wxAppid) {
		ThirdPartyConfig.wxAppid = wxAppid;
	}
	public void setWxSecret(Map<String, String> wxSecret) {
		ThirdPartyConfig.wxSecret = wxSecret;
	}
	public void setWxAccesstokenURL(String wxAccesstokenURL) {
		ThirdPartyConfig.wxAccesstokenURL = wxAccesstokenURL;
	}
	public  void setWxUserinfoURL(String wxUserinfoURL) {
		ThirdPartyConfig.wxUserinfoURL = wxUserinfoURL;
	}
	
	public void setQqAppid(Map<String, String> qqAppid) {
		ThirdPartyConfig.qqAppid = qqAppid;
	}
	public void setQqSecret(Map<String, String> qqSecret) {
		ThirdPartyConfig.qqSecret = qqSecret;
	}
	public void setQqOpenidURL(String qqOpenidURL) {
		ThirdPartyConfig.qqOpenidURL = qqOpenidURL;
	}
	public  void setQqUserinfoURL(String qqUserinfoURL) {
		ThirdPartyConfig.qqUserinfoURL = qqUserinfoURL;
	}
	
	public void setAliAppid(Map<String, String> aliAppid) {
		ThirdPartyConfig.aliAppid = aliAppid;
	}
	public void setAliPid(Map<String, String> aliPid) {
		ThirdPartyConfig.aliPid = aliPid;
	}
	public void setAliAppprivatekey(Map<String, String> aliAppprivatekey) {
		ThirdPartyConfig.aliAppprivatekey = aliAppprivatekey;
	}
	public void setAliPaypublickey(Map<String, String> aliPaypublickey) {
		ThirdPartyConfig.aliPaypublickey = aliPaypublickey;
	}
}
