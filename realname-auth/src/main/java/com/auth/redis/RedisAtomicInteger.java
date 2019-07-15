package com.auth.redis;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.auth.visitlimit.IAtomicInteger;

@Component
public class RedisAtomicInteger implements IAtomicInteger {

	@Resource
    private RedisTemplate<String, Integer> redisTemplate;
	
	@Override
	public int incr(String key, long timeout) {
		RedisAtomicIntegerExtend atomic = new RedisAtomicIntegerExtend(key, redisTemplate.getConnectionFactory(), timeout, TimeUnit.SECONDS);
		return atomic.incrementAndGet();
	}

	@Override
	public int decr(String key, long timeout) {
		RedisAtomicIntegerExtend atomic = new RedisAtomicIntegerExtend(key, redisTemplate.getConnectionFactory(), timeout, TimeUnit.SECONDS);
		return atomic.decrementAndGet();
	}

}
