package com.auth.feign.fallback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.auth.feign.RealNameFeignClient;
import com.auth.util.ExceptionUtil;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * FallbackFactory比fallback多了异常信息
 *
 */
@Slf4j
@Component
public class RealNameFeignFallbackFactory implements FallbackFactory<RealNameFeignClient> {

	@Autowired
	private RealNameFeignFallback realNameFeignFallback;
	
	@Override
	public RealNameFeignClient create(Throwable cause) {
		log.error("RealName Request ERROR :{}", ExceptionUtil.asString(cause));
		return realNameFeignFallback;
	}

}
