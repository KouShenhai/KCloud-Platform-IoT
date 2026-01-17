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

package org.laokou.common.idempotent.aspectj;

import com.redis.testcontainers.RedisContainer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.laokou.common.i18n.util.RedisKeyUtils;
import org.laokou.common.redis.util.RedisUtils;
import org.laokou.common.testcontainers.util.DockerImageNames;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import tools.jackson.databind.json.JsonMapper;

/**
 * IdempotentAspectj integration test with Redis Testcontainer.
 *
 * @author laokou
 */
@Testcontainers
class IdempotentAspectjIntegrationTest {

	@Container
	static RedisContainer redisContainer = new RedisContainer(DockerImageNames.redis()).withExposedPorts(6379)
		.withReuse(true);

	private RedisUtils redisUtils;

	@BeforeEach
	void setUp() {
		String redisHost = redisContainer.getHost();
		Integer redisPort = redisContainer.getMappedPort(6379);

		// Configure Redis connection
		RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(redisHost, redisPort);
		LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(redisConfig);
		connectionFactory.afterPropertiesSet();

		// Configure ObjectMapper
		JsonMapper objectMapper = JsonMapper.builder().build();

		// Configure RedisTemplate
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		GenericJacksonJsonRedisSerializer serializer = new GenericJacksonJsonRedisSerializer(objectMapper);
		redisTemplate.setValueSerializer(serializer);
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashValueSerializer(serializer);
		redisTemplate.afterPropertiesSet();

		// Configure RedissonClient
		Config config = new Config();
		config.useSingleServer().setAddress("redis://" + redisHost + ":" + redisPort);
		RedissonClient redissonClient = Redisson.create(config);

		// Create RedisUtils
		redisUtils = new RedisUtils(redisTemplate, redissonClient);
	}

	@Test
	@DisplayName("Test setIfAbsent returns true for new key")
	void test_setIfAbsent_returns_true_for_new_key() {
		// Given
		String requestId = "test-request-001";
		String idempotentKey = RedisKeyUtils.getApiIdempotentKey(requestId);

		// When
		boolean result = redisUtils.setIfAbsent(idempotentKey, 0, RedisUtils.FIVE_MINUTE_EXPIRE);

		// Then
		Assertions.assertThat(result).isTrue();
	}

	@Test
	@DisplayName("Test setIfAbsent returns false for existing key")
	void test_setIfAbsent_returns_false_for_existing_key() {
		// Given
		String requestId = "test-request-002";
		String idempotentKey = RedisKeyUtils.getApiIdempotentKey(requestId);
		redisUtils.setIfAbsent(idempotentKey, 0, RedisUtils.FIVE_MINUTE_EXPIRE);

		// When
		boolean result = redisUtils.setIfAbsent(idempotentKey, 0, RedisUtils.FIVE_MINUTE_EXPIRE);

		// Then
		Assertions.assertThat(result).isFalse();
	}

	@Test
	@DisplayName("Test different request IDs can execute in parallel")
	void test_different_request_ids_can_execute_in_parallel() {
		// Given
		String requestId1 = "test-request-003";
		String requestId2 = "test-request-004";
		String idempotentKey1 = RedisKeyUtils.getApiIdempotentKey(requestId1);
		String idempotentKey2 = RedisKeyUtils.getApiIdempotentKey(requestId2);

		// When
		boolean result1 = redisUtils.setIfAbsent(idempotentKey1, 0, RedisUtils.FIVE_MINUTE_EXPIRE);
		boolean result2 = redisUtils.setIfAbsent(idempotentKey2, 0, RedisUtils.FIVE_MINUTE_EXPIRE);

		// Then
		Assertions.assertThat(result1).isTrue();
		Assertions.assertThat(result2).isTrue();
	}

	@Test
	@DisplayName("Test idempotent key is correctly stored in Redis")
	void test_idempotent_key_is_stored_in_redis() {
		// Given
		String requestId = "test-request-005";
		String idempotentKey = RedisKeyUtils.getApiIdempotentKey(requestId);

		// When
		redisUtils.setIfAbsent(idempotentKey, 0, RedisUtils.FIVE_MINUTE_EXPIRE);

		// Then
		Assertions.assertThat(redisUtils.hasKey(idempotentKey)).isTrue();
		Assertions.assertThat(redisUtils.get(idempotentKey)).isEqualTo(0);
	}

	@Test
	@DisplayName("Test idempotent key has correct TTL")
	void test_idempotent_key_has_correct_ttl() {
		// Given
		String requestId = "test-request-006";
		String idempotentKey = RedisKeyUtils.getApiIdempotentKey(requestId);

		// When
		redisUtils.setIfAbsent(idempotentKey, 0, RedisUtils.FIVE_MINUTE_EXPIRE);
		Long ttl = redisUtils.getExpire(idempotentKey);

		// Then
		Assertions.assertThat(ttl).isNotNull();
		// TTL should be around 5 minutes (300 seconds), allow some tolerance
		Assertions.assertThat(ttl).isGreaterThan(290L);
		Assertions.assertThat(ttl).isLessThanOrEqualTo(300L);
	}

	@Test
	@DisplayName("Test RedisKeyUtils.getApiIdempotentKey generates correct key format")
	void test_redis_key_utils_generates_correct_key_format() {
		// Given
		String requestId = "my-unique-request-id";

		// When
		String key = RedisKeyUtils.getApiIdempotentKey(requestId);

		// Then
		Assertions.assertThat(key).isEqualTo("api:idempotent:my-unique-request-id");
	}

	@Test
	@DisplayName("Test concurrent requests with same ID - only first succeeds")
	void test_concurrent_requests_with_same_id() throws InterruptedException {
		// Given
		String requestId = "test-concurrent-request";
		String idempotentKey = RedisKeyUtils.getApiIdempotentKey(requestId);

		int threadCount = 10;
		java.util.concurrent.CountDownLatch startLatch = new java.util.concurrent.CountDownLatch(1);
		java.util.concurrent.CountDownLatch endLatch = new java.util.concurrent.CountDownLatch(threadCount);
		java.util.concurrent.atomic.AtomicInteger successCount = new java.util.concurrent.atomic.AtomicInteger(0);

		// When
		for (int i = 0; i < threadCount; i++) {
			new Thread(() -> {
				try {
					startLatch.await();
					if (redisUtils.setIfAbsent(idempotentKey, 0, RedisUtils.FIVE_MINUTE_EXPIRE)) {
						successCount.incrementAndGet();
					}
				}
				catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
				finally {
					endLatch.countDown();
				}
			}).start();
		}
		startLatch.countDown();
		endLatch.await();

		// Then - only one thread should succeed
		Assertions.assertThat(successCount.get()).isEqualTo(1);
	}

	@Test
	@DisplayName("Test FIVE_MINUTE_EXPIRE constant value")
	void test_five_minute_expire_constant() {
		// Then
		Assertions.assertThat(RedisUtils.FIVE_MINUTE_EXPIRE).isEqualTo(5 * 60);
	}

}
