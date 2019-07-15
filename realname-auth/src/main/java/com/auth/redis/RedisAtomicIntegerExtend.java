package com.auth.redis;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.BoundKeyOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.lang.Nullable;

public class RedisAtomicIntegerExtend extends Number implements Serializable, BoundKeyOperations<String> {

	private static final long serialVersionUID = 4889544357293007002L;
	
	private volatile String key;

	private final ValueOperations<String, Integer> operations;
	private final RedisOperations<String, Integer> generalOps;
	
	public RedisAtomicIntegerExtend(String redisCounter, RedisConnectionFactory factory) {
		this(redisCounter, factory, null, 0, null);
	}
	
	public RedisAtomicIntegerExtend(String redisCounter, RedisConnectionFactory factory, long timeout, TimeUnit unit) {
		this(redisCounter, factory, null, timeout, unit);
	}
	
	public RedisAtomicIntegerExtend(String redisCounter, RedisConnectionFactory factory, int initialValue, long timeout, TimeUnit unit) {
		this(redisCounter, factory, Integer.valueOf(initialValue), timeout, unit);
	}

	private RedisAtomicIntegerExtend(String redisCounter, RedisConnectionFactory factory, @Nullable Integer initialValue, long timeout, @Nullable TimeUnit unit) {
		RedisTemplate<String, Integer> redisTemplate = new RedisTemplate<>();
		redisTemplate.setKeySerializer(RedisSerializer.string());
		redisTemplate.setValueSerializer(new GenericToStringSerializer<>(Integer.class));
		redisTemplate.setExposeConnection(true);
		redisTemplate.setConnectionFactory(factory);
		redisTemplate.afterPropertiesSet();

		this.key = redisCounter;
		this.generalOps = redisTemplate;
		this.operations = generalOps.opsForValue();

		if (initialValue == null) {
			if(timeout > 0 && unit != null) {
				operations.setIfAbsent(key, 0, timeout, unit);
			}else {
				operations.setIfAbsent(key, 0);
			}
		} else {
			if(timeout > 0 && unit != null) {
				operations.setIfAbsent(key, initialValue, timeout, unit);
			}else {
				operations.setIfAbsent(key, initialValue);
			}
		}
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public DataType getType() {
		return DataType.STRING;
	}

	@Override
	public Long getExpire() {
		return generalOps.getExpire(key);
	}

	@Override
	public Boolean expire(long timeout, TimeUnit unit) {
		return generalOps.expire(key, timeout, unit);
	}

	@Override
	public Boolean expireAt(Date date) {
		return generalOps.expireAt(key, date);
	}

	@Override
	public Boolean persist() {
		return generalOps.persist(key);
	}

	@Override
	public void rename(String newKey) {
		generalOps.rename(key, newKey);
		key = newKey;
	}

	@Override
	public int intValue() {
		return get();
	}

	@Override
	public long longValue() {
		return get();
	}

	@Override
	public float floatValue() {
		return get();
	}

	@Override
	public double doubleValue() {
		return get();
	}

	public int get() {
		Integer value = operations.get(key);
		if (value != null) {
			return value;
		}

		throw new DataRetrievalFailureException(String.format("The key '%s' seems to no longer exist.", key));
	}

	public void set(int newValue) {
		operations.set(key, newValue);
	}

	/**
	 * Set to the given value and return the old value.
	 *
	 * @param newValue the new value.
	 * @return the previous value.
	 */
	public int getAndSet(int newValue) {

		Integer value = operations.getAndSet(key, newValue);

		return value != null ? value : 0;
	}

	/**
	 * Atomically set the value to the given updated value if the current value {@code ==} the expected value.
	 *
	 * @param expect the expected value.
	 * @param update the new value.
	 * @return {@literal true} if successful. {@literal false} indicates that the actual value was not equal to the
	 *         expected value.
	 */
	/*public boolean compareAndSet(int expect, int update) {
		return generalOps.execute(new CompareAndSet<>(this::get, this::set, key, expect, update));
	}*/

	/**
	 * Atomically increment by one the current value.
	 *
	 * @return the previous value.
	 */
	public int getAndIncrement() {
		return incrementAndGet() - 1;
	}

	/**
	 * Atomically decrement by one the current value.
	 *
	 * @return the previous value.
	 */
	public int getAndDecrement() {
		return decrementAndGet() + 1;
	}

	/**
	 * Atomically add the given value to current value.
	 *
	 * @param delta the value to add.
	 * @return the previous value.
	 */
	public int getAndAdd(int delta) {
		return addAndGet(delta) - delta;
	}

	/**
	 * Atomically increment by one the current value.
	 *
	 * @return the updated value.
	 */
	public int incrementAndGet() {
		return operations.increment(key).intValue();
	}

	/**
	 * Atomically decrement by one the current value.
	 *
	 * @return the updated value.
	 */
	public int decrementAndGet() {
		return operations.decrement(key).intValue();
	}

	/**
	 * Atomically add the given value to current value.
	 *
	 * @param delta the value to add.
	 * @return the updated value.
	 */
	public int addAndGet(int delta) {
		return operations.increment(key, delta).intValue();
	}
}
