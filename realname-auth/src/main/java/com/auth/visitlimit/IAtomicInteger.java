package com.auth.visitlimit;

public interface IAtomicInteger {

	int incr(String key, long timeout);
	
	int decr(String key, long timeout);
}
