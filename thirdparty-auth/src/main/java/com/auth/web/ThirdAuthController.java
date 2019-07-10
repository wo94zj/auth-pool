package com.auth.web;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
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

@RestController
@RequestMapping("/third")
public class ThirdAuthController {

	@Autowired
	private ThirdPartyUserService thirdPartyUserService;
	@Autowired
	private TokenService tokenService;
	
	/**
	 * 解除绑定
	 */
	@RequestMapping(value = "bind", method = RequestMethod.DELETE)
	public BaseDto<Serializable> unbind(String appname, @RequestParam Long userId, @RequestParam String token,
			@RequestParam String thirdType) {

		if (!tokenService.checkToken(userId, token)) {
			return ResultUtil.result(ResultCode.NOT_LOGIN);
		}

		int unbind = thirdPartyUserService.unbind(appname, userId, thirdType);
		if (unbind > 0) {
			return ResultUtil.success();
		}

		return ResultUtil.failed();
	}

	/**
	 * 账户绑定信息列表
	 */
	@RequestMapping(value = "binds", method = RequestMethod.GET)
	public BaseDto<?> bindList(String appname, @RequestParam(value = "type", defaultValue = "all") String type,
			@RequestParam Integer userId, @RequestParam String token) {

		List<ThirdPartyUser> binds = thirdPartyUserService.selectBindsByUserId(appname, userId);
		//type=pay时，只提取支持支付的列表
		if ((!CollectionUtils.isEmpty(binds)) && "pay".equals(type)) {
			binds.removeIf(v -> v.getThirdType().equals(ThirdTypeEnums.qq.name()));
		}
		return ResultUtil.success(binds);
	}
}
