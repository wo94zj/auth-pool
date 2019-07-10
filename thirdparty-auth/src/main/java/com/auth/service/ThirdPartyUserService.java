package com.auth.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth.bean.ThirdPartyUser;
import com.auth.mapper.ThirdPartyUserMapper;

@Service
public class ThirdPartyUserService {

	@Autowired
	private ThirdPartyUserMapper thirdPartyUserMapper;
	
	/**
	 * 保存三方信息
	 */
	public int saveThirdPartyUser(ThirdPartyUser user) {
		return thirdPartyUserMapper.insertOrUpdate(user);
	}
	
	/**
	 * 通过userId查询绑定的信息
	 */
	public List<ThirdPartyUser> selectBindsByUserId(String appname, long userId) {
		return thirdPartyUserMapper.selectBindsByUserId(appname, userId);
	}
	public ThirdPartyUser selectBindsByOpenId(String appname, String thirdType, String openId) {
		return thirdPartyUserMapper.selectBindsByOpenId(appname, thirdType, openId);
	}
	
	/**
	 * 解除绑定
	 */
	public int unbind(String appname, long userId, String thirdType) {
		return thirdPartyUserMapper.updateStatus(appname, userId, thirdType);
	}
}
