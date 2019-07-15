package com.auth.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class ExpressUtil {

	/**
	 * spel表达式提取
	 * @param map
	 * @param express
	 * @return
	 */
	public static String extract(Map<String, Object> map, String express) {
		EvaluationContext ctx = new StandardEvaluationContext();
		for (Entry<String, Object> kv : map.entrySet()) {
			ctx.setVariable(kv.getKey(), kv.getValue());
		}
		
		ExpressionParser parser = new SpelExpressionParser();
		Expression val = parser.parseExpression(express);
		return val.getValue(ctx, String.class);
	}
	
	public static void main(String[] args) {
		Map<String, Object> map = new HashMap<>();
		map.put("userId", 11);
		map.put("token", "x11");
		map.put("signature", "x11x");
		
		String express = "#userId + '-' + #token";
		System.out.println(ExpressUtil.extract(map, express));
	}
}
