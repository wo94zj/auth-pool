package com.auth.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.auth.feign.fallback.RealNameFeignFallback;

@Component
@FeignClient(value = "auth-realname-api", fallback = RealNameFeignFallback.class)
public interface RealNameFeignClient {

	@RequestMapping(value = "/auth-realname-api/realname/idcardname", method = RequestMethod.POST)
	String checkIDcardAndName(@RequestParam("idcard") String idcard, @RequestParam("name") String name);
}
