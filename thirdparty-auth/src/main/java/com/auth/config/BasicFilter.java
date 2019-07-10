package com.auth.config;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.slf4j.MDC;

import com.auth.enums.AppNameEnums;
import com.auth.exception.CommonException;
import com.auth.resp.ResultCode;
import com.auth.util.AddressUtil;
import com.auth.util.StringUtil;

public class BasicFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		MDC.put("ip", AddressUtil.getIpAddr(httpRequest));
		
		String appname = httpRequest.getHeader(AppNameEnums.APPNAME);
		if(StringUtil.isBlank(appname)) {
			appname = AppNameEnums.DEFAULT_APPNAME;
		}
		
		//如果appname不在支持范围，直接交由BasicExceptionHandler处理
		if (!AppNameEnums.isValid(appname)) {
			throw new CommonException(ResultCode.NOT_AUTH);
		}
		
		final String appnameFinal = appname;
		HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(httpRequest) {
			@Override
			public String[] getParameterValues(String name) {
				if(AppNameEnums.APPNAME.equals(name)) {
					return new String[] {appnameFinal};
				}
				
				return super.getParameterValues(name);
			}
			
			@Override
			public Enumeration<String> getParameterNames() {
				Set<String> paramNames = new LinkedHashSet<>();
				paramNames.add(AppNameEnums.APPNAME);
				
				Enumeration<String> parameterNames = super.getParameterNames();
				while (parameterNames.hasMoreElements()) {
					paramNames.add(parameterNames.nextElement());
				}
				
				return Collections.enumeration(paramNames);
			}
		};
		
		chain.doFilter(requestWrapper, response);
	}

}
