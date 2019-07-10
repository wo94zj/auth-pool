package com.auth.web;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auth.bean.ThirdPartyUser;
import com.auth.enums.ThirdTypeEnums;
import com.auth.resp.BaseDto;
import com.auth.resp.ResultCode;
import com.auth.resp.ResultUtil;
import com.auth.service.QqService;
import com.auth.service.ThirdPartyUserService;
import com.auth.service.TokenService;

@RestController
@RequestMapping("/third/qq")
public class QqAuthController {

	@Autowired
	private ThirdPartyUserService thirdPartyUserService;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private QqService qqService;
	
	
	/**
	 * QQ登陆
	 */
	@RequestMapping(value = "login", method = RequestMethod.POST, headers = {
			"Content-Type=application/x-www-form-urlencoded" })
	public BaseDto<Serializable> qqLogin(String appname, @RequestParam("accessToken") String accessToken) {

		ThirdPartyUser user = qqService.qqUserInfo(appname, accessToken);
		if (Objects.isNull(user) || Objects.isNull(user.getOpenId())) {
			return ResultUtil.result(ResultCode.FAILED);
		}

		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("ThirdPartyUser", user);

		// 判断是否绑定手机号
		ThirdPartyUser binder = thirdPartyUserService.selectBindsByOpenId(appname, ThirdTypeEnums.qq.name(), user.getOpenId());
		if (Objects.nonNull(binder) && Objects.nonNull(binder.getUserId())) {
			// 已经绑定，需返回账户信息及token
			resultMap.put("userinfo", binder);
			resultMap.put("token", tokenService.buildToken(binder.getUserId()));
			
			user.setUserId(binder.getUserId());
		}
		
		// 如果未绑定，只返回QQ账户相关信息
		thirdPartyUserService.saveThirdPartyUser(user);
		return ResultUtil.success(resultMap);
	}
	
	/**
	 * QQ绑定
	 */
	@RequestMapping(value = "bind", method = RequestMethod.POST, headers = {
			"Content-Type=application/x-www-form-urlencoded" })
	public BaseDto<Serializable> qqBind(String appname, @RequestParam Long userId, @RequestParam String token,
			@RequestParam("accessToken") String accessToken) {

		if (!tokenService.checkToken(userId, token)) {
			return ResultUtil.result(ResultCode.NOT_LOGIN);
		}

		ThirdPartyUser user = qqService.qqUserInfo(appname, accessToken);
		if (Objects.isNull(user)) {
			return ResultUtil.result(ResultCode.FAILED);
		}

		// 绑定账户
		user.setUserId(userId);
		int result = thirdPartyUserService.saveThirdPartyUser(user);
		if (result > 0) {
			return ResultUtil.success();
		}

		return ResultUtil.failed();
	}
}
