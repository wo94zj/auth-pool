package com.auth.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.auth.bean.ThirdPartyUser;
import com.auth.config.ThirdPartyConfig;
import com.auth.enums.AppNameEnums;
import com.auth.enums.ThirdTypeEnums;
import com.auth.util.ExceptionUtil;
import com.auth.util.SecretUtil;
import com.auth.util.TimeUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AliService {

	private String charset = "UTF-8";

	public ThirdPartyUser aliUserInfo(String appname, String code) {
		String accessToken = accessToken(appname, code);
		if(Objects.isNull(accessToken)) {
			return null;
		}
		
		return userinfo(appname, accessToken);
	}
	
	public String accessToken(String appname, String code) {
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
				ThirdPartyConfig.getAliAppId(AppNameEnums.valueOf(appname)),
				ThirdPartyConfig.getAliAppprivatekey(AppNameEnums.valueOf(appname)), "json", charset,
				ThirdPartyConfig.getAliPaypublickey(AppNameEnums.valueOf(appname)), "RSA2");
		AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
		request.setCode(code);
		request.setGrantType("authorization_code");
		try {
			AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient.execute(request);
			log.info("appname :{}; ali code :{}; response: {}", appname, code, oauthTokenResponse.getBody());
			return oauthTokenResponse.getAccessToken();
		} catch (AlipayApiException e) {
			log.error("ali code ERROR :{}", ExceptionUtil.asString(e));
		}
		
		return null;
	}

	public ThirdPartyUser userinfo(String appname, String accessToken) {
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
				ThirdPartyConfig.getAliAppId(AppNameEnums.valueOf(appname)),
				ThirdPartyConfig.getAliAppprivatekey(AppNameEnums.valueOf(appname)), "json", charset,
				ThirdPartyConfig.getAliPaypublickey(AppNameEnums.valueOf(appname)), "RSA2");
		AlipayUserInfoShareRequest request = new AlipayUserInfoShareRequest();
		try {
			AlipayUserInfoShareResponse userinfoShareResponse = alipayClient.execute(request, accessToken);
			log.info("appname :{}; ali accessToken :{}; response: {}", appname, accessToken, userinfoShareResponse.getBody());
			
			ThirdPartyUser user = new ThirdPartyUser();
			user.setAppname(appname);
			user.setOpenId(userinfoShareResponse.getUserId());
			user.setNickname(userinfoShareResponse.getNickName());
			String gender = userinfoShareResponse.getGender();
			if(Objects.isNull(gender)) {
				user.setSex(0);
			}else {
				user.setSex(gender.equals("F")?2:1);//F：女性；M：男性
			}
			user.setImg(userinfoShareResponse.getAvatar());
			user.setThirdType(ThirdTypeEnums.ali.name());
			long time = TimeUtil.currentMilli();
			user.setUpdateTime(time);
			user.setCreateTime(time);
			
			return user;
		} catch (AlipayApiException e) {
			log.error("ali userinfo ERROR :{}", ExceptionUtil.asString(e));
		}
		
		return null;
	}
	
	/**
	 * 调起支付宝需要签名
	 */
	public String sign(String appname) {
		Map<String, String> sortedParams = new HashMap<>();
		sortedParams.put("apiname", "com.alipay.account.auth");
		sortedParams.put("method", "alipay.open.auth.sdk.code.get");
		sortedParams.put("app_id", ThirdPartyConfig.getAliAppId(AppNameEnums.valueOf(appname)));
		sortedParams.put("app_name", "mc");
		sortedParams.put("biz_type", "openservice");
		sortedParams.put("pid", ThirdPartyConfig.getAliPId(AppNameEnums.valueOf(appname)));
		sortedParams.put("product_id", "APP_FAST_LOGIN");
		sortedParams.put("scope", "kuaijie");
		sortedParams.put("target_id", SecretUtil.salt());
		sortedParams.put("auth_type", "AUTHACCOUNT");
		sortedParams.put("sign_type", "RSA2");
		
		String content = AlipaySignature.getSignContent(sortedParams);
		String signature = null;
		try {
			signature = AlipaySignature.rsa256Sign(content, ThirdPartyConfig.getAliAppprivatekey(AppNameEnums.valueOf(appname)), charset);
		} catch (AlipayApiException e) {
			log.error("ali signature ERROR :{}", ExceptionUtil.asString(e));
		}
		
		if(Objects.isNull(signature)) {
			return null;
		}
		
		try {
			return content + "&sign=" + URLEncoder.encode(signature, charset);
		} catch (UnsupportedEncodingException e) {
			log.error("ali URLEncoder ERROR :{}", ExceptionUtil.asString(e));
		}
		
		return null;
	}
}