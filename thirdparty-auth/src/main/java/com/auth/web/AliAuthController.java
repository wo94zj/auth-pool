package com.auth.web;

import java.io.Serializable;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auth.bean.ThirdPartyUser;
import com.auth.resp.BaseDto;
import com.auth.resp.ResultCode;
import com.auth.resp.ResultUtil;
import com.auth.service.AliService;
import com.auth.service.ThirdPartyUserService;
import com.auth.service.TokenService;


@RestController
@RequestMapping("/third/ali")
public class AliAuthController {

	@Autowired
	private ThirdPartyUserService thirdPartyUserService;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private AliService aliService;
	
	/**
	 * 支付宝绑定
	 */
	@RequestMapping(value = "bind", method = RequestMethod.POST, headers = {
			"Content-Type=application/x-www-form-urlencoded" })
	public BaseDto<Serializable> aliBind(String appname, @RequestParam Long userId, @RequestParam String token,
			@RequestParam("code") String code) {
		
		if (!tokenService.checkToken(userId, token)) {
			return ResultUtil.result(ResultCode.NOT_LOGIN);
		}

		// 获取支付宝账户信息
		ThirdPartyUser user = aliService.aliUserInfo(appname, code);
		if (Objects.isNull(user) || Objects.isNull(user.getOpenId())) {
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
	
	/**
	 * 支付宝签名接口
	 */
	@RequestMapping(value = "sign", method = RequestMethod.POST, headers = {
			"Content-Type=application/x-www-form-urlencoded" })
	public BaseDto<Serializable> aliSign(String appname, @RequestParam Integer userId, @RequestParam String token) {

		String sign = aliService.sign(appname);
		if (Objects.nonNull(sign)) {
			return ResultUtil.success(sign);
		}

		return ResultUtil.failed();
	}
}
