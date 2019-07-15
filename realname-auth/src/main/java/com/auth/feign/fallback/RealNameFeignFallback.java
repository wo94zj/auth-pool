package com.auth.feign.fallback;

import com.alibaba.fastjson.JSON;
import com.auth.feign.RealNameFeignClient;
import com.auth.resp.ResultCode;
import com.auth.resp.ResultUtil;


public class RealNameFeignFallback implements RealNameFeignClient {

	@Override
	public String checkIDcardAndName(String idcard, String name) {
		return JSON.toJSONString(ResultUtil.result(ResultCode.BUSY));
	}

}
