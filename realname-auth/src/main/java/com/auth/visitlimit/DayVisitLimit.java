package com.auth.visitlimit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DayVisitLimit {

	/**
     * 分组
     */
    String value();

    /**
     * 提取参数表达式
     */
    String express();
    
    /**
     * 每天限制次数
     */
    int limit();
}
