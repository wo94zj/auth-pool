package com.auth.visitlimit;

import java.util.Date;
import java.util.Map;

import javax.validation.ValidationException;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.auth.exception.CommonException;
import com.auth.resp.ResultCode;
import com.auth.util.AspectUtil;
import com.auth.util.ExpressUtil;
import com.auth.util.StringFormatUtil;
import com.auth.util.TimeUtil;

/**
 * 访问次数限制，放在参数校验后面，幂等性校验前面执行
 */
@Order(200)
@Aspect
@Component
public class DayVisitLimitAspect {

	@Autowired
    private IAtomicInteger atomicInteger;

    /**
     * 定义切入点为 带有 VisitLimit 注解的
     */
    @Pointcut("@annotation(com.auth.visitlimit.DayVisitLimit)")
    public void dayVisitLimit() {
    }

    @Around("dayVisitLimit()")
    public Object dayVisitLimitGuaranteed(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        
        Map<String, Object> params = AspectUtil.params(joinPoint);
        DayVisitLimit visitLimit = (DayVisitLimit) AspectUtil.methodAnnotation(joinPoint, DayVisitLimit.class);
        
        String key = ExpressUtil.extract(params, visitLimit.express());
        if (StringUtils.isEmpty(visitLimit.value()) || StringUtils.isEmpty(key)) {
            new ValidationException("DayVisitLimit FAILED!");
        }

        Date date = new Date();
        long timeout = TimeUtil.secondInterval(date, TimeUtil.dateLastTime(date));
        key = StringFormatUtil.joinFormat(":", visitLimit.value(), key);
        int count = atomicInteger.incr(key, timeout);
        if(count <= visitLimit.limit()){
            result = joinPoint.proceed(joinPoint.getArgs());
        }

        if(result == null){
            throw new CommonException(ResultCode.VISIT_LIMIT.getCode(), ResultCode.VISIT_LIMIT.getMsg());
        }
        return result;
    }
}
