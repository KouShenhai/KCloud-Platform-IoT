/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.laokou.common.redis.utils;

import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.utils.StringUtil;
import org.redisson.api.*;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 *
 * @author laokou
 */
@RequiredArgsConstructor
public class RedisUtil {

	private final RedisTemplate<String, Object> redisTemplate;

	private final RedissonClient redissonClient;

	/** 默认过期时长为24小时，单位：秒 */
	public final static long DEFAULT_EXPIRE = 60 * 60 * 24;

	/** 过期时长为1小时，单位：秒 */
	public final static long HOUR_ONE_EXPIRE = 60 * 60;

	/** 过期时长为6小时，单位：秒 */
	public final static long HOUR_SIX_EXPIRE = 60 * 60 * 6;

	/** 不设置过期时长 */
	public final static long NOT_EXPIRE = -1L;

	public RLock getLock(String key) {
		return redissonClient.getLock(key);
	}

	public RLock getFairLock(String key) {
		return redissonClient.getFairLock(key);
	}

	public RLock getReadLock(String key) {
		return redissonClient.getReadWriteLock(key).readLock();
	}

	public RLock getWriteLock(String key) {
		return redissonClient.getReadWriteLock(key).writeLock();
	}

	public boolean tryLock(RLock lock, long expire, long timeout) throws InterruptedException {
		return lock.tryLock(timeout, expire, TimeUnit.MILLISECONDS);
	}

	public boolean tryLock(String key, long expire, long timeout) throws InterruptedException {
		return tryLock(getLock(key), expire, timeout);
	}

	public void unlock(String key) {
		unlock(getLock(key));
	}

	public void unlock(RLock lock) {
		lock.unlock();
	}

	public void lock(String key) {
		lock(getLock(key));
	}

	public void lock(RLock lock) {
		lock.lock();
	}

	public boolean isLocked(String key) {
		return isLocked(getLock(key));
	}

	public boolean isLocked(RLock lock) {
		return lock.isLocked();
	}

	public boolean isHeldByCurrentThread(String key) {
		return isHeldByCurrentThread(getLock(key));
	}

	public boolean isHeldByCurrentThread(RLock lock) {
		return lock.isHeldByCurrentThread();
	}

	public void set(String key, Object value) {
		set(key, value, DEFAULT_EXPIRE);
	}

	public void set(String key, Object value, long expire) {
		redissonClient.getBucket(key).set(value, Duration.ofSeconds(expire));
	}

	public void lSet(String key, List<Object> objList, long expire) {
		RList<Object> rList = redissonClient.getList(key);
		if (rList.addAll(objList)) {
			rList.expireIfNotSet(Duration.ofSeconds(expire));
		}
	}

	public List<Object> lGetAll(String key) {
		return redissonClient.getList(key).readAll();
	}

	public Object lGet(String key, int index) {
		return redissonClient.getList(key).get(index);
	}

	public Long getExpire(String key) {
		return redisTemplate.getExpire(key);
	}

	public void setIfAbsent(String key, Object value) {
		setIfAbsent(key, value, DEFAULT_EXPIRE);
	}

	public void setIfAbsent(String key, Object value, long expire) {
		redissonClient.getBucket(key).setIfAbsent(value, Duration.ofSeconds(expire));
	}

	public Object get(String key) {
		return redissonClient.getBucket(key).get();
	}

	public boolean delete(String key) {
		return redissonClient.getKeys().delete(key) > 0;
	}

	public boolean hasKey(String key) {
		return Boolean.TRUE.equals(redisTemplate.hasKey(key));
	}

	public boolean hasHashKey(String key, String field) {
		return redissonClient.getMap(key).containsKey(field);
	}

	public long incrementAndGet(String key) {
		RAtomicLong atomicLong = redissonClient.getAtomicLong(key);
		atomicLong.expireIfNotSet(Duration.ofSeconds(HOUR_ONE_EXPIRE));
		return atomicLong.incrementAndGet();
	}

	public long decrementAndGet(String key) {
		RAtomicLong atomicLong = redissonClient.getAtomicLong(key);
		atomicLong.expireIfNotSet(Duration.ofSeconds(HOUR_ONE_EXPIRE));
		return atomicLong.decrementAndGet();
	}

	public long addAndGet(String key, long value) {
		RAtomicLong atomicLong = redissonClient.getAtomicLong(key);
		long newValue = atomicLong.addAndGet(value);
		atomicLong.expireIfNotSet(Duration.ofSeconds(HOUR_ONE_EXPIRE));
		return newValue;
	}

	public Set<String> keys(String pattern) {
		return redisTemplate.keys(pattern);
	}

	public long getAtomicValue(String key) {
		return redissonClient.getAtomicLong(key).get();
	}

	public void hSet(String key, String field, Object value, long expire) {
		RMap<Object, Object> map = redissonClient.getMap(key);
		map.put(field, value);
		map.expireIfNotSet(Duration.ofSeconds(expire));
	}

	public void hSet(String key, String field, Object value) {
		hSet(key, field, value, NOT_EXPIRE);
	}

	public Object hGet(String key, String field) {
		return redissonClient.getMap(key).get(field);
	}

	public long getKeysSize() {
		final Object obj = redisTemplate.execute(RedisServerCommands::dbSize);
		return obj == null ? 0 : Long.parseLong(obj.toString());
	}

	public List<Map<String, String>> getCommandStatus() {
		Properties commandStats = (Properties) redisTemplate
			.execute((RedisCallback<Object>) connection -> connection.serverCommands().info("commandstats"));
		List<Map<String, String>> pieList = new ArrayList<>();
		assert commandStats != null;
		commandStats.stringPropertyNames().forEach(key -> {
			Map<String, String> data = new HashMap<>(2);
			String property = commandStats.getProperty(key);
			data.put("name", StringUtil.removeStart(key, "cmdstat_"));
			data.put("value", StringUtil.substringBetween(property, "calls=", ",usec"));
			pieList.add(data);
		});
		return pieList;
	}

	public Map<String, String> getInfo() {
		final Properties properties = redisTemplate.execute(RedisServerCommands::info,
				redisTemplate.isExposeConnection());
		assert properties != null;
		final Set<String> set = properties.stringPropertyNames();
		final Iterator<String> iterator = set.iterator();
		Map<String, String> dataMap = new HashMap<>(set.size());
		while (iterator.hasNext()) {
			final String key = iterator.next();
			final String value = properties.getProperty(key);
			dataMap.put(key, value);
		}
		return dataMap;
	}

	public <T> T execute(RedisScript<T> script, List<String> keys, Object... args) {
		return redisTemplate.execute(script, keys, args);
	}

}
