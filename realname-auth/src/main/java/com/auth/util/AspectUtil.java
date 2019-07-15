package com.auth.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;

public class AspectUtil {

	/**
	 * 切面中获取request请求参数，并组装成Map
	 */
	public static Map<String, Object> params(ProceedingJoinPoint joinPoint) {
		// joinPoint获取参数名
		String[] params = ((CodeSignature) joinPoint.getStaticPart().getSignature()).getParameterNames();
        // joinPoint获取参数值
        Object[] args = joinPoint.getArgs();
        
        Map<String, Object> map = new HashMap<>();
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                map.put(params[i], args[i]);
            }
        }
        
        return map;
	}
	
	/**
	 * 切面中获取方法上指定注解
	 */
	public static Annotation methodAnnotation(ProceedingJoinPoint joinPoint, Class<? extends Annotation> annotationType) {
		Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        Annotation annotation = method.getAnnotation(annotationType);
        return annotation;
	}
}
