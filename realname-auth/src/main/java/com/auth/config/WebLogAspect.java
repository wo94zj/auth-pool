package com.auth.config;

import java.util.Arrays;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.extern.slf4j.Slf4j;

@Order(100)
@Aspect
@Slf4j
@Component
public class WebLogAspect {

	@Pointcut("within(com.auth.web.*)")
	public void webLog() {
	}

	@Before("webLog()")
	public void doBefore(JoinPoint joinPoint) {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();

		log.info("URL :{}; HTTP_METHOD :{}; IP :{}; CLASS :{}; METHOD :{}; ARGS :{}",
				request.getRequestURL().toString(), request.getMethod(), request.getRemoteAddr(),
				joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(),
				Arrays.toString(joinPoint.getArgs()));
	}

	@Around("webLog()")
	public Object paramsValid(ProceedingJoinPoint joinPoint) throws Throwable {
		Object[] args = joinPoint.getArgs();

		for (Object arg : args) {
			if (arg instanceof BindingResult) {
				BindingResult result = (BindingResult) arg;
				if (result.hasErrors()) {
					ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
							.getRequestAttributes();
					HttpServletRequest request = requestAttributes.getRequest();
					log.info("Bind ERROR URL :{}; ARGS :{}", request.getRequestURL().toString(),
							Arrays.toString(joinPoint.getArgs()));

					String collect = result.getAllErrors().stream().map(v1 -> {
						/*if (v1 instanceof FieldError) {
							return ((FieldError) v1).getField().concat(" ").concat(v1.getDefaultMessage());
						}
						return v1.getObjectName().concat(" ").concat(v1.getDefaultMessage());*/
						return v1.getDefaultMessage();
					}).collect(Collectors.joining(", "));

					throw new ValidationException(collect);
				}
			}
		}

		return joinPoint.proceed(args);
	}
}
