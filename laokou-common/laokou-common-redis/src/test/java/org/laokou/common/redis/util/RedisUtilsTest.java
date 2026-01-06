/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.redis.util;

import com.redis.testcontainers.RedisContainer;
import lombok.Data;
import org.assertj.core.api.Assertions;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.laokou.common.redis.config.JacksonCodec;
import org.laokou.common.testcontainers.util.DockerImageNames;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.redisson.api.options.LocalCachedMapOptions;
import org.redisson.api.options.MapOptions;
import org.redisson.config.Config;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import tools.jackson.databind.json.JsonMapper;

import java.io.Serializable;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Comprehensive test cases for {@link RedisUtils} using Testcontainers and JUnit 5. This
 * test suite covers all methods in RedisUtils with various scenarios.
 *
 * @author laokou
 */
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RedisUtilsTest {

	@Container
	static RedisContainer redisContainer = new RedisContainer(DockerImageNames.redis()).withExposedPorts(6379)
		.withReuse(true);

	private RedisUtils redisUtils;

	private RedisTemplate<String, Object> redisTemplate;

	@BeforeEach
	void setUp() {
		String redisHost = redisContainer.getHost();
		Integer redisPort = redisContainer.getMappedPort(6379);

		// Configure Lettuce Connection Factory
		RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(redisHost, redisPort);
		LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(redisConfig);
		connectionFactory.afterPropertiesSet();

		// Configure RedisTemplate
		redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		GenericJacksonJsonRedisSerializer genericJacksonJsonRedisSerializer = new GenericJacksonJsonRedisSerializer(
				new JsonMapper());
		redisTemplate.setValueSerializer(genericJacksonJsonRedisSerializer);
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashValueSerializer(genericJacksonJsonRedisSerializer);
		redisTemplate.afterPropertiesSet();

		// Configure Redisson
		Config config = new Config();
		config.useSingleServer().setAddress("redis://" + redisHost + ":" + redisPort);
		config.setCodec(JacksonCodec.INSTANCE);
		RedissonClient redissonClient = Redisson.create(config);

		// Create RedisUtils instance
		redisUtils = new RedisUtils(redisTemplate, redissonClient);
	}

	@AfterEach
	void tearDown() {
		// Clean up all keys after each test
		if (redisTemplate.getConnectionFactory() != null) {
			redisTemplate.getConnectionFactory().getConnection().serverCommands().flushAll();
		}
	}

	// ==================== Basic String Operations ====================

	@Test
	@Order(1)
	@DisplayName("Test set and get operations with default expiration")
	void testSetAndGetWithDefaultExpiration() {
		String key = "test:key:default";
		String value = "test-value";

		redisUtils.set(key, value);

		Object result = redisUtils.get(key);
		Assertions.assertThat(result).isEqualTo(value);

		Long expire = redisUtils.getExpire(key);
		Assertions.assertThat(expire).isGreaterThan(0).isLessThanOrEqualTo(RedisUtils.DEFAULT_EXPIRE);
	}

	@Test
	@Order(2)
	@DisplayName("Test set and get operations with custom expiration")
	void testSetAndGetWithCustomExpiration() {
		String key = "test:key:custom";
		String value = "custom-value";
		long expireSeconds = 10;

		redisUtils.set(key, value, expireSeconds);

		Object result = redisUtils.get(key);
		Assertions.assertThat(result).isEqualTo(value);

		Long expire = redisUtils.getExpire(key);
		Assertions.assertThat(expire).isGreaterThan(0).isLessThanOrEqualTo(expireSeconds);
	}

	@Test
	@Order(3)
	@DisplayName("Test set with no expiration (NOT_EXPIRE)")
	void testSetWithNoExpiration() {
		String key = "test:key:noexpire";
		String value = "no-expire-value";

		redisUtils.set(key, value, RedisUtils.NOT_EXPIRE);

		Object result = redisUtils.get(key);
		Assertions.assertThat(result).isEqualTo(value);

		Long expire = redisUtils.getExpire(key);
		Assertions.assertThat(expire).isEqualTo(-1L);
	}

	@Test
	@Order(4)
	@DisplayName("Test setIfAbsent with default expiration")
	void testSetIfAbsentWithDefaultExpiration() {
		String key = "test:key:ifabsent";
		String value1 = "value1";
		String value2 = "value2";

		boolean firstSet = redisUtils.setIfAbsent(key, value1);
		Assertions.assertThat(firstSet).isTrue();
		Assertions.assertThat(redisUtils.get(key)).isEqualTo(value1);

		boolean secondSet = redisUtils.setIfAbsent(key, value2);
		Assertions.assertThat(secondSet).isFalse();
		Assertions.assertThat(redisUtils.get(key)).isEqualTo(value1);
	}

	@Test
	@Order(5)
	@DisplayName("Test setIfAbsent with custom expiration")
	void testSetIfAbsentWithCustomExpiration() {
		String key = "test:key:ifabsent:custom";
		String value = "value";
		long expireSeconds = 5;

		boolean result = redisUtils.setIfAbsent(key, value, expireSeconds);
		Assertions.assertThat(result).isTrue();

		Long expire = redisUtils.getExpire(key);
		Assertions.assertThat(expire).isGreaterThan(0).isLessThanOrEqualTo(expireSeconds);
	}

	@Test
	@Order(6)
	@DisplayName("Test get non-existing key returns null")
	void testGetNonExistingKey() {
		String key = "test:key:nonexisting";
		Object result = redisUtils.get(key);
		Assertions.assertThat(result).isNull();
	}

	// ==================== Key Operations ====================

	@Test
	@Order(7)
	@DisplayName("Test hasKey operation")
	void testHasKey() {
		String existingKey = "test:key:exists";
		String nonExistingKey = "test:key:notexists";

		redisUtils.set(existingKey, "value");

		Assertions.assertThat(redisUtils.hasKey(existingKey)).isTrue();
		Assertions.assertThat(redisUtils.hasKey(nonExistingKey)).isFalse();
	}

	@Test
	@Order(8)
	@DisplayName("Test delete single key")
	void testDeleteSingleKey() {
		String key = "test:key:delete";
		redisUtils.set(key, "value");

		Assertions.assertThat(redisUtils.hasKey(key)).isTrue();

		boolean deleted = redisUtils.del(key);
		Assertions.assertThat(deleted).isTrue();
		Assertions.assertThat(redisUtils.hasKey(key)).isFalse();
	}

	@Test
	@Order(9)
	@DisplayName("Test delete multiple keys")
	void testDeleteMultipleKeys() {
		String key1 = "test:key:delete1";
		String key2 = "test:key:delete2";
		String key3 = "test:key:delete3";

		redisUtils.set(key1, "value1");
		redisUtils.set(key2, "value2");
		redisUtils.set(key3, "value3");

		boolean deleted = redisUtils.del(key1, key2, key3);
		Assertions.assertThat(deleted).isTrue();
		Assertions.assertThat(redisUtils.hasKey(key1)).isFalse();
		Assertions.assertThat(redisUtils.hasKey(key2)).isFalse();
		Assertions.assertThat(redisUtils.hasKey(key3)).isFalse();
	}

	@Test
	@Order(10)
	@DisplayName("Test delete non-existing key")
	void testDeleteNonExistingKey() {
		String key = "test:key:notexists";
		boolean deleted = redisUtils.del(key);
		Assertions.assertThat(deleted).isFalse();
	}

	@Test
	@Order(11)
	@DisplayName("Test keys with pattern")
	void testKeysWithPattern() {
		redisUtils.set("user:1", "value1");
		redisUtils.set("user:2", "value2");
		redisUtils.set("order:1", "value3");

		Set<String> userKeys = redisUtils.keys("user:*");
		Assertions.assertThat(userKeys).hasSize(2).contains("user:1", "user:2");

		Set<String> orderKeys = redisUtils.keys("order:*");
		Assertions.assertThat(orderKeys).hasSize(1).contains("order:1");
	}

	@Test
	@Order(12)
	@DisplayName("Test keys without pattern (all keys)")
	void testKeysWithoutPattern() {
		redisUtils.set("key1", "value1");
		redisUtils.set("key2", "value2");
		redisUtils.set("key3", "value3");

		Set<String> allKeys = redisUtils.keys();
		Assertions.assertThat(allKeys).hasSizeGreaterThanOrEqualTo(3).contains("key1", "key2", "key3");
	}

	@Test
	@Order(13)
	@DisplayName("Test getExpire for key")
	void testGetExpire() {
		String key = "test:key:expire";
		long expireSeconds = 100;

		redisUtils.set(key, "value", expireSeconds);

		Long expire = redisUtils.getExpire(key);
		Assertions.assertThat(expire).isGreaterThan(0).isLessThanOrEqualTo(expireSeconds);
	}

	@Test
	@Order(14)
	@DisplayName("Test getKeysSize")
	void testGetKeysSize() {
		// Clear all keys first
		if (redisTemplate.getConnectionFactory() != null) {
			redisTemplate.getConnectionFactory().getConnection().serverCommands().flushAll();
		}

		redisUtils.set("key1", "value1");
		redisUtils.set("key2", "value2");
		redisUtils.set("key3", "value3");

		long size = redisUtils.getKeysSize();
		Assertions.assertThat(size).isEqualTo(3);
	}

	// ==================== Hash Operations ====================

	@Test
	@Order(15)
	@DisplayName("Test hSet and hGet single field")
	void testHSetAndHGet() {
		String key = "test:hash:single";
		String field = "field1";
		String value = "value1";

		redisUtils.hSet(key, field, value);

		Object result = redisUtils.hGet(key, field);
		Assertions.assertThat(result).isEqualTo(value);
	}

	@Test
	@Order(16)
	@DisplayName("Test hSet with expiration")
	void testHSetWithExpiration() {
		String key = "test:hash:expire";
		String field = "field1";
		String value = "value1";
		long expireSeconds = 10;

		redisUtils.hSet(key, field, value, expireSeconds);

		Object result = redisUtils.hGet(key, field);
		Assertions.assertThat(result).isEqualTo(value);

		Long expire = redisUtils.getExpire(key);
		Assertions.assertThat(expire).isLessThanOrEqualTo(expireSeconds);
	}

	@Test
	@Order(17)
	@DisplayName("Test hSet with map")
	void testHSetWithMap() {
		String key = "test:hash:map";
		Map<String, Object> map = new HashMap<>();
		map.put("field1", "value1");
		map.put("field2", "value2");
		map.put("field3", "value3");
		long expireSeconds = 10;

		redisUtils.hSet(key, map, expireSeconds);

		Assertions.assertThat(redisUtils.hGet(key, "field1")).isEqualTo("value1");
		Assertions.assertThat(redisUtils.hGet(key, "field2")).isEqualTo("value2");
		Assertions.assertThat(redisUtils.hGet(key, "field3")).isEqualTo("value3");
	}

	@Test
	@Order(18)
	@DisplayName("Test hSetFast operation")
	void testHSetFast() {
		String key = "test:hash:fast";
		String field = "field1";
		String value = "value1";
		long expireSeconds = 10;

		redisUtils.hSetFast(key, field, value, expireSeconds);

		Object result = redisUtils.hGet(key, field);
		Assertions.assertThat(result).isEqualTo(value);
	}

	@Test
	@Order(19)
	@DisplayName("Test hSetFastAsync operation")
	void testHSetFastAsync() throws InterruptedException {
		String key = "test:hash:fastasync";
		String field = "field1";
		String value = "value1";
		long expireSeconds = 10;

		redisUtils.hSetFastAsync(key, field, value, expireSeconds);

		// Wait a bit for async operation to complete
		Thread.sleep(100);

		Object result = redisUtils.hGet(key, field);
		Assertions.assertThat(result).isEqualTo(value);
	}

	@Test
	@Order(20)
	@DisplayName("Test hSetIfAbsent operation")
	void testHSetIfAbsent() {
		String key = "test:hash:ifabsent";
		String field = "field1";
		String value1 = "value1";
		String value2 = "value2";
		long expireSeconds = 10;

		redisUtils.hSetIfAbsent(key, field, value1, expireSeconds);
		Assertions.assertThat(redisUtils.hGet(key, field)).isEqualTo(value1);

		redisUtils.hSetIfAbsent(key, field, value2, expireSeconds);
		Assertions.assertThat(redisUtils.hGet(key, field)).isEqualTo(value1);
	}

	@Test
	@Order(21)
	@DisplayName("Test hSetIfAbsentAsync operation")
	void testHSetIfAbsentAsync() throws InterruptedException {
		String key = "test:hash:ifabsentasync";
		String field = "field1";
		String value = "value1";
		long expireSeconds = 10;

		redisUtils.hSetIfAbsentAsync(key, field, value, expireSeconds);

		// Wait a bit for async operation to complete
		Thread.sleep(100);

		Object result = redisUtils.hGet(key, field);
		Assertions.assertThat(result).isEqualTo(value);
	}

	@Test
	@Order(22)
	@DisplayName("Test hSetIfAbsentNative operation")
	void testHSetIfAbsentNative() {
		String key = "test:hash:ifabsentnative";
		String field = "field1";
		String value1 = "value1";
		String value2 = "value2";

		redisUtils.hSetIfAbsentNative(key, field, value1);
		Assertions.assertThat(redisUtils.hGetNative(key, field)).isEqualTo(value1);

		redisUtils.hSetIfAbsentNative(key, field, value2);
		Assertions.assertThat(redisUtils.hGetNative(key, field)).isEqualTo(value1);
	}

	@Test
	@Order(23)
	@DisplayName("Test hGetNative operation")
	void testHGetNative() {
		String key = "test:hash:native";
		String field = "field1";
		String value = "value1";

		redisTemplate.opsForHash().put(key, field, value);

		Object result = redisUtils.hGetNative(key, field);
		Assertions.assertThat(result).isEqualTo(value);
	}

	@Test
	@Order(24)
	@DisplayName("Test hasHashKey operation")
	void testHasHashKey() {
		String key = "test:hash:haskey";
		String existingField = "field1";
		String nonExistingField = "field2";

		redisUtils.hSet(key, existingField, "value1");

		Assertions.assertThat(redisUtils.hasHashKey(key, existingField)).isTrue();
		Assertions.assertThat(redisUtils.hasHashKey(key, nonExistingField)).isFalse();
	}

	@Test
	@Order(25)
	@DisplayName("Test hDel single field")
	void testHDelSingleField() {
		String key = "test:hash:delfield";
		String field = "field1";

		redisUtils.hSet(key, field, "value1");
		Assertions.assertThat(redisUtils.hasHashKey(key, field)).isTrue();

		redisUtils.hDel(key, field);
		Assertions.assertThat(redisUtils.hasHashKey(key, field)).isFalse();
	}

	@Test
	@Order(26)
	@DisplayName("Test hDelFast multiple fields")
	void testHDelFast() {
		String key = "test:hash:delfast";

		redisUtils.hSet(key, "field1", "value1");
		redisUtils.hSet(key, "field2", "value2");
		redisUtils.hSet(key, "field3", "value3");

		redisUtils.hDelFast(key, "field1", "field2");

		Assertions.assertThat(redisUtils.hasHashKey(key, "field1")).isFalse();
		Assertions.assertThat(redisUtils.hasHashKey(key, "field2")).isFalse();
		Assertions.assertThat(redisUtils.hasHashKey(key, "field3")).isTrue();
	}

	@Test
	@Order(27)
	@DisplayName("Test hDel entire hash")
	void testHDelEntireHash() {
		String key = "test:hash:delall";

		redisUtils.hSet(key, "field1", "value1");
		redisUtils.hSet(key, "field2", "value2");

		Assertions.assertThat(redisUtils.hasKey(key)).isTrue();

		redisUtils.hDel(key);

		Assertions.assertThat(redisUtils.hasKey(key)).isFalse();
	}

	// ==================== List Operations ====================

	@Test
	@Order(28)
	@DisplayName("Test lSet single object")
	void testLSetSingleObject() {
		String key = "test:list:single";
		String value = "value1";
		long expireSeconds = 10;

		redisUtils.lSet(key, value, expireSeconds);

		List<Object> result = redisUtils.lGetAll(key);
		Assertions.assertThat(result).hasSize(1).contains(value);
	}

	@Test
	@Order(29)
	@DisplayName("Test lSet list of objects")
	void testLSetListOfObjects() {
		String key = "test:list:multiple";
		List<Object> values = Arrays.asList("value1", "value2", "value3");
		long expireSeconds = 10;

		redisUtils.lSet(key, values, expireSeconds);

		List<Object> result = redisUtils.lGetAll(key);
		Assertions.assertThat(result).hasSize(3).containsExactly("value1", "value2", "value3");
	}

	@Test
	@Order(30)
	@DisplayName("Test lGet by index")
	void testLGetByIndex() {
		String key = "test:list:index";
		List<Object> values = Arrays.asList("value1", "value2", "value3");

		redisUtils.lSet(key, values, 10);

		Assertions.assertThat(redisUtils.lGet(key, 0)).isEqualTo("value1");
		Assertions.assertThat(redisUtils.lGet(key, 1)).isEqualTo("value2");
		Assertions.assertThat(redisUtils.lGet(key, 2)).isEqualTo("value3");
	}

	@Test
	@Order(31)
	@DisplayName("Test lSize operation")
	void testLSize() {
		String key = "test:list:size";
		List<Object> values = Arrays.asList("value1", "value2", "value3", "value4");

		redisUtils.lSet(key, values, 10);

		int size = redisUtils.lSize(key);
		Assertions.assertThat(size).isEqualTo(4);
	}

	@Test
	@Order(32)
	@DisplayName("Test lTrim operation")
	void testLTrim() {
		String key = "test:list:trim";
		List<Object> values = Arrays.asList("value1", "value2", "value3", "value4", "value5");

		redisUtils.lSet(key, values, 10);

		redisUtils.lTrim(key, 1, 3);

		List<Object> result = redisUtils.lGetAll(key);
		Assertions.assertThat(result).hasSize(3).containsExactly("value2", "value3", "value4");
	}

	@Test
	@Order(33)
	@DisplayName("Test lGetAll operation")
	void testLGetAll() {
		String key = "test:list:getall";
		List<Object> values = Arrays.asList("value1", "value2", "value3");

		redisUtils.lSet(key, values, 10);

		List<Object> result = redisUtils.lGetAll(key);
		Assertions.assertThat(result).isEqualTo(values);
	}

	// ==================== Atomic Operations ====================

	@Test
	@Order(34)
	@DisplayName("Test incrementAndGet operation")
	void testIncrementAndGet() {
		String key = "test:atomic:increment";

		long result1 = redisUtils.incrementAndGet(key);
		Assertions.assertThat(result1).isEqualTo(1);

		long result2 = redisUtils.incrementAndGet(key);
		Assertions.assertThat(result2).isEqualTo(2);

		long result3 = redisUtils.incrementAndGet(key);
		Assertions.assertThat(result3).isEqualTo(3);
	}

	@Test
	@Order(35)
	@DisplayName("Test decrementAndGet operation")
	void testDecrementAndGet() {
		String key = "test:atomic:decrement";

		long result1 = redisUtils.decrementAndGet(key);
		Assertions.assertThat(result1).isEqualTo(-1);

		long result2 = redisUtils.decrementAndGet(key);
		Assertions.assertThat(result2).isEqualTo(-2);

		long result3 = redisUtils.decrementAndGet(key);
		Assertions.assertThat(result3).isEqualTo(-3);
	}

	@Test
	@Order(36)
	@DisplayName("Test addAndGet operation")
	void testAddAndGet() {
		String key = "test:atomic:add";

		long result1 = redisUtils.addAndGet(key, 10);
		Assertions.assertThat(result1).isEqualTo(10);

		long result2 = redisUtils.addAndGet(key, 5);
		Assertions.assertThat(result2).isEqualTo(15);

		long result3 = redisUtils.addAndGet(key, -3);
		Assertions.assertThat(result3).isEqualTo(12);
	}

	@Test
	@Order(37)
	@DisplayName("Test getAtomicValue operation")
	void testGetAtomicValue() {
		String key = "test:atomic:value";

		redisUtils.addAndGet(key, 100);

		long value = redisUtils.getAtomicValue(key);
		Assertions.assertThat(value).isEqualTo(100);
	}

	// ==================== Lock Operations ====================

	@Test
	@Order(38)
	@DisplayName("Test getLock and lock/unlock operations")
	void testGetLockAndLockUnlock() {
		String key = "test:lock:basic";

		RLock lock = redisUtils.getLock(key);
		Assertions.assertThat(lock).isNotNull();

		redisUtils.lock(lock);
		Assertions.assertThat(redisUtils.isLocked(lock)).isTrue();
		Assertions.assertThat(redisUtils.isHeldByCurrentThread(lock)).isTrue();

		redisUtils.unlock(lock);
		Assertions.assertThat(redisUtils.isLocked(lock)).isFalse();
	}

	@Test
	@Order(39)
	@DisplayName("Test lock and unlock with key")
	void testLockAndUnlockWithKey() {
		String key = "test:lock:key";

		redisUtils.lock(key);
		Assertions.assertThat(redisUtils.isLocked(key)).isTrue();

		redisUtils.unlock(key);
		Assertions.assertThat(redisUtils.isLocked(key)).isFalse();
	}

	@Test
	@Order(40)
	@DisplayName("Test tryLock operation")
	void testTryLock() throws InterruptedException {
		String key = "test:lock:try";

		RLock lock = redisUtils.getLock(key);

		boolean acquired = redisUtils.tryLock(lock, 1000);
		Assertions.assertThat(acquired).isTrue();
		Assertions.assertThat(redisUtils.isLocked(lock)).isTrue();

		redisUtils.unlock(lock);
	}

	@Test
	@Order(41)
	@DisplayName("Test getFencedLock operation")
	void testGetFencedLock() {
		String key = "test:lock:fenced";

		RLock lock = redisUtils.getFencedLock(key);
		Assertions.assertThat(lock).isNotNull();

		redisUtils.lock(lock);
		Assertions.assertThat(redisUtils.isLocked(lock)).isTrue();

		redisUtils.unlock(lock);
	}

	@Test
	@Order(42)
	@DisplayName("Test getFairLock operation")
	void testGetFairLock() {
		String key = "test:lock:fair";

		RLock lock = redisUtils.getFairLock(key);
		Assertions.assertThat(lock).isNotNull();

		redisUtils.lock(lock);
		Assertions.assertThat(redisUtils.isLocked(lock)).isTrue();

		redisUtils.unlock(lock);
	}

	@Test
	@Order(43)
	@DisplayName("Test getReadLock operation")
	void testGetReadLock() {
		String key = "test:lock:read";

		RLock readLock = redisUtils.getReadLock(key);
		Assertions.assertThat(readLock).isNotNull();

		redisUtils.lock(readLock);
		Assertions.assertThat(redisUtils.isLocked(readLock)).isTrue();

		redisUtils.unlock(readLock);
	}

	@Test
	@Order(44)
	@DisplayName("Test getWriteLock operation")
	void testGetWriteLock() {
		String key = "test:lock:write";

		RLock writeLock = redisUtils.getWriteLock(key);
		Assertions.assertThat(writeLock).isNotNull();

		redisUtils.lock(writeLock);
		Assertions.assertThat(redisUtils.isLocked(writeLock)).isTrue();

		redisUtils.unlock(writeLock);
	}

	@Test
	@Order(45)
	@DisplayName("Test concurrent lock acquisition")
	void testConcurrentLockAcquisition() {
		String key = "test:lock:concurrent";
		int threadCount = 5;
		AtomicInteger successCount = new AtomicInteger(0);
		for (int i = 0; i < threadCount; i++) {
			CompletableFuture.runAsync(() -> {
				try {
					RLock lock = redisUtils.getLock(key);
					if (redisUtils.tryLock(lock, 100)) {
						try {
							successCount.incrementAndGet();
							Thread.sleep(50);
						}
						finally {
							redisUtils.unlock(lock);
						}
					}
				}
				catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}).join();
		}
		Assertions.assertThat(successCount.get()).isGreaterThan(0);
	}

	// ==================== Rate Limiter Operations ====================

	@Test
	@Order(46)
	@DisplayName("Test rateLimiter operation")
	void testRateLimiter() {
		String key = "test:ratelimiter";
		long replenishRate = 5;
		Duration rateInterval = Duration.ofSeconds(1);
		Duration ttlInterval = Duration.ofSeconds(10);

		// First few requests should succeed
		for (int i = 0; i < replenishRate; i++) {
			boolean allowed = redisUtils.rateLimiter(key, RateType.OVERALL, replenishRate, rateInterval, ttlInterval);
			Assertions.assertThat(allowed).isTrue();
		}

		// Next request should fail (rate limit exceeded)
		boolean allowed = redisUtils.rateLimiter(key, RateType.OVERALL, replenishRate, rateInterval, ttlInterval);
		Assertions.assertThat(allowed).isFalse();
	}

	// ==================== Map Cache Operations ====================

	@Test
	@Order(47)
	@DisplayName("Test getMapCacheNative operation")
	void testGetMapCacheNative() {
		MapOptions<String, String> options = MapOptions.name("test:mapcache");

		var mapCache = redisUtils.getMapCacheNative(options);
		Assertions.assertThat(mapCache).isNotNull();

		mapCache.put("key1", "value1");
		Assertions.assertThat(mapCache.get("key1")).isEqualTo("value1");
	}

	@Test
	@Order(48)
	@DisplayName("Test getLocalCachedMap operation")
	void testGetLocalCachedMap() {
		LocalCachedMapOptions<String, String> options = LocalCachedMapOptions.name("test:localcache");

		var localCachedMap = redisUtils.getLocalCachedMap(options);
		Assertions.assertThat(localCachedMap).isNotNull();

		localCachedMap.put("key1", "value1");
		Assertions.assertThat(localCachedMap.get("key1")).isEqualTo("value1");
	}

	// ==================== Redis Info Operations ====================

	@Test
	@Order(49)
	@DisplayName("Test getInfo operation")
	void testGetInfo() {
		Map<String, String> info = redisUtils.getInfo();
		Assertions.assertThat(info).isNotNull().isNotEmpty();
		Assertions.assertThat(info).containsKey("redis_version");
	}

	@Test
	@Order(50)
	@DisplayName("Test getCommandStatus operation")
	void testGetCommandStatus() {
		// Execute some commands first
		redisUtils.set("test:key", "value");
		redisUtils.get("test:key");

		List<Map<String, String>> commandStatus = redisUtils.getCommandStatus();
		Assertions.assertThat(commandStatus).isNotNull().isNotEmpty();
	}

	// ==================== Script Execution ====================

	@Test
	@Order(51)
	@DisplayName("Test execute Redis script with string length")
	void testExecuteScript() {
		String key = "test:script:key";
		String value = "test-value";

		// Set a value first
		redisUtils.set(key, value);

		// Lua script to get string length (returns a number, not "OK")
		String scriptText = "return redis.call('strlen', KEYS[1])";
		DefaultRedisScript<@NonNull Long> script = new DefaultRedisScript<>(scriptText, Long.class);

		Long result = redisUtils.execute(script, Collections.singletonList(key));
		Assertions.assertThat(result).isGreaterThan(0L);
	}

	@Test
	@Order(52)
	@DisplayName("Test execute Redis script with numeric result")
	void testExecuteScriptWithNumericResult() {
		String key = "test:script:counter";

		// Initialize counter
		redisUtils.set(key, 0);

		// Lua script to increment
		String scriptText = "return redis.call('incr', KEYS[1])";
		DefaultRedisScript<@NonNull Long> script = new DefaultRedisScript<>(scriptText, Long.class);

		Long result = redisUtils.execute(script, Collections.singletonList(key));
		Assertions.assertThat(result).isEqualTo(1L);
	}

	// ==================== Complex Object Storage ====================

	@Test
	@Order(53)
	@DisplayName("Test complex object storage and retrieval")
	void testComplexObjectStorage() {
		String key = "test:object:complex";
		TestUser user = new TestUser("user123", "John Doe", 30, "john@example.com");

		redisUtils.set(key, user);

		Object result = redisUtils.get(key);
		// Note: Without type information in Jackson config, objects are deserialized as
		// LinkedHashMap
		Assertions.assertThat(result).isInstanceOf(Map.class);

		@SuppressWarnings("unchecked")
		Map<String, Object> retrievedUser = (Map<String, Object>) result;
		Assertions.assertThat(retrievedUser.get("id")).isEqualTo(user.getId());
		Assertions.assertThat(retrievedUser.get("name")).isEqualTo(user.getName());
		Assertions.assertThat(retrievedUser.get("age")).isEqualTo(user.getAge());
		Assertions.assertThat(retrievedUser.get("email")).isEqualTo(user.getEmail());
	}

	@Test
	@Order(54)
	@DisplayName("Test complex object in hash")
	void testComplexObjectInHash() {
		String key = "test:hash:object";
		String field = "user1";
		TestUser user = new TestUser("user456", "Jane Smith", 25, "jane@example.com");

		redisUtils.hSet(key, field, user);

		Object result = redisUtils.hGet(key, field);
		// Note: Without type information in Jackson config, objects are deserialized as
		// LinkedHashMap
		Assertions.assertThat(result).isInstanceOf(Map.class);

		@SuppressWarnings("unchecked")
		Map<String, Object> retrievedUser = (Map<String, Object>) result;
		Assertions.assertThat(retrievedUser.get("id")).isEqualTo(user.getId());
		Assertions.assertThat(retrievedUser.get("name")).isEqualTo(user.getName());
	}

	@Test
	@Order(55)
	@DisplayName("Test complex object in list")
	void testComplexObjectInList() {
		String key = "test:list:object";
		TestUser user1 = new TestUser("user1", "Alice", 28, "alice@example.com");
		TestUser user2 = new TestUser("user2", "Bob", 32, "bob@example.com");

		List<Object> users = Arrays.asList(user1, user2);
		redisUtils.lSet(key, users, 10);

		List<Object> result = redisUtils.lGetAll(key);
		Assertions.assertThat(result).hasSize(2);
		// Note: Without type information in Jackson config, objects are deserialized as
		// LinkedHashMap
		Assertions.assertThat(result.get(0)).isInstanceOf(Map.class);
		Assertions.assertThat(result.get(1)).isInstanceOf(Map.class);
	}

	// ==================== Edge Cases and Error Scenarios ====================

	@Test
	@Order(56)
	@DisplayName("Test null value handling")
	void testNullValueHandling() {
		String key = "test:null:value";

		redisUtils.set(key, null);

		Object result = redisUtils.get(key);
		Assertions.assertThat(result).isNull();
	}

	@Test
	@Order(57)
	@DisplayName("Test empty string handling")
	void testEmptyStringHandling() {
		String key = "test:empty:string";
		String value = "";

		redisUtils.set(key, value);

		Object result = redisUtils.get(key);
		Assertions.assertThat(result).isEqualTo(value);
	}

	@Test
	@Order(58)
	@DisplayName("Test large value storage")
	void testLargeValueStorage() {
		String key = "test:large:value";
		StringBuilder largeValue = new StringBuilder();
		largeValue.append("This is a test string. ".repeat(10000));
		redisUtils.set(key, largeValue.toString());
		Object result = redisUtils.get(key);
		Assertions.assertThat(result).isEqualTo(largeValue.toString());
	}

	@Test
	@Order(59)
	@DisplayName("Test special characters in key")
	void testSpecialCharactersInKey() {
		String key = "test:special:key:with:colons:and-dashes_and_underscores";
		String value = "special-value";

		redisUtils.set(key, value);

		Object result = redisUtils.get(key);
		Assertions.assertThat(result).isEqualTo(value);
	}

	@Test
	@Order(60)
	@DisplayName("Test expiration constants")
	void testExpirationConstants() {
		Assertions.assertThat(RedisUtils.DEFAULT_EXPIRE).isEqualTo(60 * 60 * 24);
		Assertions.assertThat(RedisUtils.ONE_HOUR_EXPIRE).isEqualTo(60 * 60);
		Assertions.assertThat(RedisUtils.FIVE_MINUTE_EXPIRE).isEqualTo(5 * 60);
		Assertions.assertThat(RedisUtils.NOT_EXPIRE).isEqualTo(-1L);
	}

	// ==================== Test Helper Classes ====================

	/**
	 * Test user object for complex object storage testing.
	 */
	@Data
	static class TestUser implements Serializable {

		private String id;

		private String name;

		private Integer age;

		private String email;

		public TestUser(String id, String name, Integer age, String email) {
			this.id = id;
			this.name = name;
			this.age = age;
			this.email = email;
		}

	}

}
