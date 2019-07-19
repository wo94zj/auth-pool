package com.auth.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.auth.feign.fallback.RealNameFeignFallbackFactory;

/**
 * 走zuul网关的话，需要value写网关serviceId
 * <br>
 * 请求路径写对应请求服务serviceId + "/" + 请求路径
 *
 */
@Component
@FeignClient(value = "springcloud-zuul-server", fallbackFactory = RealNameFeignFallbackFactory.class)
public interface RealNameFeignClient {

	//OpenFeign @QueryMap注释为POJO提供了对用作GET参数映射的支持。不幸的是，默认的OpenFeign QueryMap注释与Spring不兼容，因为它缺少value属性。
	//Spring Cloud OpenFeign提供等效@SpringQueryMap注释，用于将POJO或Map参数注释为查询参数映射
	@RequestMapping(value = "/auth-realname-api/auth-realname-api/realname/idcardname", method = RequestMethod.POST)
	String checkIDcardAndName(@RequestParam("idcard") String idcard, @RequestParam("name") String name);
}
