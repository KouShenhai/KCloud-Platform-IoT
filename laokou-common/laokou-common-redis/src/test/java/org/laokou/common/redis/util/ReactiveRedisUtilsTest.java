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
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.laokou.common.core.config.HttpMessageConverterConfig;
import org.laokou.common.testcontainers.util.DockerImageNames;
import org.redisson.Redisson;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.config.Config;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.Serializable;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Test cases for {@link ReactiveRedisUtils} using Testcontainers and JUnit 5.
 *
 * @author laokou
 */
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReactiveRedisUtilsTest {

	@Container
	static RedisContainer redisContainer = new RedisContainer(DockerImageNames.redis()).withExposedPorts(6379)
		.withReuse(true);

	private ReactiveRedisUtils reactiveRedisUtils;

	private ReactiveRedisTemplate<@NonNull String, @NonNull Object> reactiveRedisTemplate;

	@BeforeEach
	void setUp() {
		String redisHost = redisContainer.getHost();
		Integer redisPort = redisContainer.getMappedPort(6379);

		// Configure Lettuce Connection Factory
		LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(redisHost, redisPort);
		connectionFactory.afterPropertiesSet();

		// Configure ReactiveRedisTemplate
		StringRedisSerializer keySerializer = new StringRedisSerializer();
		GenericJacksonJsonRedisSerializer valueSerializer = new GenericJacksonJsonRedisSerializer(
				HttpMessageConverterConfig.getJsonMapper());

		RedisSerializationContext<@NonNull String, @NonNull Object> serializationContext = RedisSerializationContext
			.<String, Object>newSerializationContext()
			.key(keySerializer)
			.value(valueSerializer)
			.hashKey(keySerializer)
			.hashValue(valueSerializer)
			.build();

		reactiveRedisTemplate = new ReactiveRedisTemplate<>(connectionFactory, serializationContext);

		// Configure Redisson
		Config config = new Config();
		config.useSingleServer().setAddress("redis://" + redisHost + ":" + redisPort);
		RedissonReactiveClient redissonReactiveClient = Redisson.create(config).reactive();

		// Create ReactiveRedisUtils instance
		reactiveRedisUtils = new ReactiveRedisUtils(reactiveRedisTemplate, redissonReactiveClient);
	}

	@AfterEach
	void tearDown() {
		// Clean up all keys after each test
		reactiveRedisTemplate.execute(connection -> connection.serverCommands().flushAll())
			.blockLast(Duration.ofSeconds(5));
	}

	@Test
	@Order(1)
	@DisplayName("Test set and get operations")
	void testSetAndGet() {
		String key = "test:key";
		String value = "test-value";

		// Test set operation
		StepVerifier.create(reactiveRedisUtils.set(key, value, -1)).verifyComplete();

		// Test get operation
		StepVerifier.create(reactiveRedisUtils.get(key)).expectNext(value).verifyComplete();
	}

	@Test
	@Order(2)
	@DisplayName("Test set with expiration")
	void testSetWithExpiration() {
		String key = "test:expire:key";
		String value = "expire-value";
		long expireSeconds = 2;

		// Set with expiration
		StepVerifier.create(reactiveRedisUtils.set(key, value, expireSeconds)).verifyComplete();

		// Verify key exists
		StepVerifier.create(reactiveRedisUtils.hasKey(key)).expectNext(true).verifyComplete();

		// Wait for expiration
		StepVerifier.create(Mono.delay(Duration.ofSeconds(expireSeconds + 1)).then(reactiveRedisUtils.hasKey(key)))
			.expectNext(false)
			.verifyComplete();
	}

	@Test
	@Order(3)
	@DisplayName("Test hasKey operation")
	void testHasKey() {
		String existingKey = "test:existing";
		String nonExistingKey = "test:nonexisting";

		// Set a key
		StepVerifier.create(reactiveRedisUtils.set(existingKey, "value", -1)).verifyComplete();

		// Test existing key
		StepVerifier.create(reactiveRedisUtils.hasKey(existingKey)).expectNext(true).verifyComplete();

		// Test non-existing key
		StepVerifier.create(reactiveRedisUtils.hasKey(nonExistingKey)).expectNext(false).verifyComplete();
	}

	@Test
	@Order(4)
	@DisplayName("Test delete operation")
	void testDelete() {
		String key = "test:delete:key";
		String value = "delete-value";

		// Set a key
		StepVerifier.create(reactiveRedisUtils.set(key, value, -1)).verifyComplete();

		// Verify key exists
		StepVerifier.create(reactiveRedisUtils.hasKey(key)).expectNext(true).verifyComplete();

		// Delete the key
		StepVerifier.create(reactiveRedisUtils.delete(key)).expectNext(1L).verifyComplete();

		// Verify key no longer exists
		StepVerifier.create(reactiveRedisUtils.hasKey(key)).expectNext(false).verifyComplete();
	}

	@Test
	@Order(5)
	@DisplayName("Test hash operations - hGet")
	void testHashGet() {
		String key = "test:hash";
		String field = "field1";
		String value = "value1";

		// Put a value in hash
		StepVerifier.create(reactiveRedisUtils.getMap(key).put(field, value)).verifyComplete();

		// Get the value from hash
		StepVerifier.create(reactiveRedisUtils.hGet(key, field)).expectNext(value).verifyComplete();
	}

	@Test
	@Order(6)
	@DisplayName("Test hash operations - hasHashKey")
	void testHasHashKey() {
		String key = "test:hash:exists";
		String existingField = "existing-field";
		String nonExistingField = "non-existing-field";

		// Put a value in hash
		StepVerifier.create(reactiveRedisUtils.getMap(key).put(existingField, "value")).verifyComplete();

		// Test existing field
		StepVerifier.create(reactiveRedisUtils.hasHashKey(key, existingField)).expectNext(true).verifyComplete();

		// Test non-existing field
		StepVerifier.create(reactiveRedisUtils.hasHashKey(key, nonExistingField)).expectNext(false).verifyComplete();
	}

	@Test
	@Order(7)
	@DisplayName("Test hash operations - putAll")
	void testPutAll() {
		String key = "test:hash:putall";
		Map<String, Object> map = new HashMap<>();
		map.put("field1", "value1");
		map.put("field2", "value2");
		map.put("field3", "value3");

		// Put all values
		StepVerifier.create(reactiveRedisUtils.putAll(key, map)).verifyComplete();

		// Verify all fields exist
		StepVerifier.create(reactiveRedisUtils.hGet(key, "field1")).expectNext("value1").verifyComplete();

		StepVerifier.create(reactiveRedisUtils.hGet(key, "field2")).expectNext("value2").verifyComplete();

		StepVerifier.create(reactiveRedisUtils.hGet(key, "field3")).expectNext("value3").verifyComplete();
	}

	@Test
	@Order(8)
	@DisplayName("Test hash operations - values")
	void testValues() {
		String key = "test:hash:values";
		Map<String, Object> map = new HashMap<>();
		map.put("field1", "value1");
		map.put("field2", "value2");
		map.put("field3", "value3");

		// Put all values
		StepVerifier.create(reactiveRedisUtils.putAll(key, map)).verifyComplete();

		// Get all values
		Flux<@NonNull Object> values = reactiveRedisUtils.values(key);

		// Verify values count
		StepVerifier.create(values.collectList())
			.expectNextMatches(list -> list.size() == 3 && list.contains("value1") && list.contains("value2")
					&& list.contains("value3"))
			.verifyComplete();
	}

	@Test
	@Order(9)
	@DisplayName("Test hash operations - fastPutIfAbsent")
	void testFastPutIfAbsent() {
		String key = "test:hash:putifabsent";
		String field = "field1";
		String value1 = "value1";
		String value2 = "value2";

		// First put should succeed
		StepVerifier.create(reactiveRedisUtils.fastPutIfAbsent(key, field, value1)).expectNext(true).verifyComplete();

		// Verify the value
		StepVerifier.create(reactiveRedisUtils.hGet(key, field)).expectNext(value1).verifyComplete();

		// Second put should fail (field already exists)
		StepVerifier.create(reactiveRedisUtils.fastPutIfAbsent(key, field, value2)).expectNext(false).verifyComplete();

		// Verify the value is still the original
		StepVerifier.create(reactiveRedisUtils.hGet(key, field)).expectNext(value1).verifyComplete();
	}

	@Test
	@Order(10)
	@DisplayName("Test hash operations - putIfAbsent")
	void testPutIfAbsent() {
		String key = "test:hash:putifabsent2";
		String field = "field1";
		String value1 = "value1";
		String value2 = "value2";

		// First put should return null (no previous value)
		StepVerifier.create(reactiveRedisUtils.putIfAbsent(key, field, value1)).verifyComplete();

		// Verify the value
		StepVerifier.create(reactiveRedisUtils.hGet(key, field)).expectNext(value1).verifyComplete();

		// Second put should return the existing value
		StepVerifier.create(reactiveRedisUtils.putIfAbsent(key, field, value2)).expectNext(value1).verifyComplete();

		// Verify the value is still the original
		StepVerifier.create(reactiveRedisUtils.hGet(key, field)).expectNext(value1).verifyComplete();
	}

	@Test
	@Order(11)
	@DisplayName("Test getMap operation")
	void testGetMap() {
		String key = "test:map";
		String field1 = "field1";
		String value1 = "value1";
		String field2 = "field2";
		String value2 = "value2";

		// Get map and perform operations
		var map = reactiveRedisUtils.getMap(key);

		// Put values
		StepVerifier.create(map.put(field1, value1)).verifyComplete();

		StepVerifier.create(map.put(field2, value2)).verifyComplete();

		// Get values
		StepVerifier.create(map.get(field1)).expectNext(value1).verifyComplete();

		StepVerifier.create(map.get(field2)).expectNext(value2).verifyComplete();

		// Check size
		StepVerifier.create(map.size()).expectNext(2).verifyComplete();
	}

	@Test
	@Order(12)
	@DisplayName("Test complex object storage")
	void testComplexObjectStorage() {
		String key = "test:complex:object";
		TestObject testObject = new TestObject("test-id", "test-name", 100);

		// Set complex object
		StepVerifier.create(reactiveRedisUtils.set(key, testObject, -1)).verifyComplete();

		// Get complex object
		StepVerifier.create(reactiveRedisUtils.get(key)).expectNext(testObject).verifyComplete();
	}

	@Test
	@Order(13)
	@DisplayName("Test delete non-existing key")
	void testDeleteNonExistingKey() {
		String key = "test:nonexisting:delete";

		// Delete non-existing key should return 0
		StepVerifier.create(reactiveRedisUtils.delete(key)).expectNext(0L).verifyComplete();
	}

	@Test
	@Order(14)
	@DisplayName("Test concurrent operations")
	void testConcurrentOperations() {
		String key = "test:concurrent";

		// Perform multiple concurrent operations
		Flux<@NonNull Void> operations = Flux.range(1, 10)
			.flatMap(i -> reactiveRedisUtils.set(key + ":" + i, "value" + i, -1));

		StepVerifier.create(operations).verifyComplete();

		StepVerifier.create(reactiveRedisUtils.get(key + ":1")).expectNext("value1").verifyComplete();

		// Verify all keys exist
		Flux<@NonNull Boolean> checks = Flux.range(1, 10).flatMap(i -> reactiveRedisUtils.hasKey(key + ":" + i));

		StepVerifier.create(checks).expectNextCount(10).expectComplete().verify();
	}

	/**
	 * Test object for complex object storage testing.
	 */
	@Data
	static class TestObject implements Serializable {

		private String id;

		private String name;

		private Integer value;

		public TestObject(String id, String name, Integer value) {
			this.id = id;
			this.name = name;
			this.value = value;
		}

	}

}
