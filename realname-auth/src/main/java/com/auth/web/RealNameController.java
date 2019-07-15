package com.auth.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auth.feign.RealNameFeignClient;
import com.auth.visitlimit.DayVisitLimit;

@RestController
@RequestMapping(value = "/realname")
public class RealNameController {

	@Autowired
	private RealNameFeignClient realNameFeignClient;

	@DayVisitLimit(value = "realname", express = "#userId", limit = 3)
	@RequestMapping(value = "", method = RequestMethod.POST)
	public String postRealnameAuth(@RequestHeader("userId") Integer userId, @RequestHeader String token,
			@RequestParam String idcard, @RequestParam String name) {
		return realNameFeignClient.checkIDcardAndName(idcard, name);
	}
}
