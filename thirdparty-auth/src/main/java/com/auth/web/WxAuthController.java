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
import com.auth.service.ThirdPartyUserService;
import com.auth.service.TokenService;
import com.auth.service.WxService;

@RestController
@RequestMapping("/third/wx")
public class WxAuthController {

	@Autowired
	private WxService wxService;
	@Autowired
	private ThirdPartyUserService thirdPartyUserService;
	@Autowired
	private TokenService tokenService;
	
	/**
	 * 微信登陆
	 */
	@RequestMapping(value = "login", method = RequestMethod.POST, headers = {
			"Content-Type=application/x-www-form-urlencoded" })
	public BaseDto<Serializable> wxLogin(String appname, @RequestParam("code") String code) {

		ThirdPartyUser user = wxService.wxUserInfo(appname, code);
		if (Objects.isNull(user) || Objects.isNull(user.getOpenId())) {
			return ResultUtil.result(ResultCode.FAILED);
		}

		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("ThirdPartyUser", user);

		// 判断是否绑定手机号
		ThirdPartyUser binder = thirdPartyUserService.selectBindsByOpenId(appname, ThirdTypeEnums.wx.name(), user.getOpenId());
		if (Objects.nonNull(binder) && Objects.nonNull(binder.getUserId())) {
			// 已经绑定，需返回账户信息及token
			resultMap.put("userinfo", binder);
			resultMap.put("token", tokenService.buildToken(binder.getUserId()));
			
			user.setUserId(binder.getUserId());
		}
		
		thirdPartyUserService.saveThirdPartyUser(user);
		return ResultUtil.success(resultMap);
	}
	
	/**
	 * 微信绑定
	 */
	@RequestMapping(value = "bind", method = RequestMethod.POST, headers = {
			"Content-Type=application/x-www-form-urlencoded" })
	public BaseDto<Serializable> wxBind(String appname, @RequestParam Long userId, @RequestParam String token,
			@RequestParam("code") String code) {

		if(!tokenService.checkToken(userId, token)) {
			return ResultUtil.result(ResultCode.NOT_LOGIN);
		}
		
		ThirdPartyUser user = wxService.wxUserInfo(appname, code);
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
