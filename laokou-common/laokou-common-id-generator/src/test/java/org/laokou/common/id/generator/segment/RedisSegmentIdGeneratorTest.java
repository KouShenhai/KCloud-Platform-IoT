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

package org.laokou.common.id.generator.segment;

import com.redis.testcontainers.RedisContainer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.laokou.common.i18n.common.enums.BizType;
import org.laokou.common.redis.config.JacksonCodec;
import org.laokou.common.redis.util.RedisUtils;
import org.laokou.common.testcontainers.util.DockerImageNames;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Integration tests for {@link RedisSegmentIdGenerator} using Testcontainers.
 *
 * @author laokou
 */
@Testcontainers
class RedisSegmentIdGeneratorTest {

	@Container
	static final RedisContainer redisContainer = new RedisContainer(DockerImageNames.redis()).withExposedPorts(6379)
		.withReuse(true);

	private RedisSegmentIdGenerator generator;

	private RedisTemplate<String, Object> redisTemplate;

	private RedissonClient redissonClient;

	@BeforeEach
	void setUp() {
		String redisHost = redisContainer.getHost();
		Integer redisPort = redisContainer.getMappedPort(6379);

		// Configure Lettuce Connection Factory
		RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(redisHost, redisPort);
		LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(redisConfig);
		connectionFactory.afterPropertiesSet();

		// Configure RedisTemplate - use StringRedisSerializer for both key and value
		// to ensure Lua script arguments are serialized as plain strings
		redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactory);
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		redisTemplate.setKeySerializer(stringRedisSerializer);
		redisTemplate.setValueSerializer(JacksonCodec.OBJECT_REDIS_SERIALIZER);
		redisTemplate.setHashKeySerializer(stringRedisSerializer);
		redisTemplate.setHashValueSerializer(JacksonCodec.OBJECT_REDIS_SERIALIZER);
		redisTemplate.afterPropertiesSet();

		// Configure Redisson
		Config config = new Config();
		config.useSingleServer().setAddress("redis://" + redisHost + ":" + redisPort);
		config.setCodec(JacksonCodec.INSTANCE);
		redissonClient = Redisson.create(config);

		// Create RedisUtils instance
		RedisUtils redisUtils = new RedisUtils(redisTemplate, redissonClient);

		// Configure SpringSegmentProperties with small step to facilitate segment
		// switching tests
		SpringSegmentProperties springSegmentProperties = getSpringSegmentProperties();

		generator = new RedisSegmentIdGenerator(redisUtils, springSegmentProperties);
		generator.init();
	}

	@AfterEach
	void tearDown() {
		if (generator != null) {
			generator.close();
		}
		// Clean up Redis test data
		if (redisTemplate.getConnectionFactory() != null) {
			redisTemplate.getConnectionFactory().getConnection().serverCommands().flushAll();
		}
		if (redissonClient != null) {
			redissonClient.shutdown();
		}
	}

	@Test
	@DisplayName("Test init success - load segment")
	void test_init_success() {
		// init already called in setUp, verify generator can generate IDs
		Assertions.assertThatNoException().isThrownBy(() -> generator.nextId(BizType.AUTH));
	}

	@Test
	@DisplayName("Test nextId generates unique positive and increasing IDs")
	void test_nextId_generateUniqueId() {
		long id1 = generator.nextId(BizType.AUTH);
		long id2 = generator.nextId(BizType.AUTH);
		Assertions.assertThat(id1).isPositive();
		Assertions.assertThat(id2).isPositive();
		Assertions.assertThat(id2).isGreaterThan(id1);
	}

	@Test
	@DisplayName("Test nextId generates unique IDs consecutively")
	void test_nextId_uniqueness() {
		Set<Long> ids = new HashSet<>();
		for (int i = 0; i < 500; i++) {
			long id = generator.nextId(BizType.AUTH);
			Assertions.assertThat(ids.add(id)).isTrue();
		}
		Assertions.assertThat(ids).hasSize(500);
	}

	@Test
	@DisplayName("Test nextIds generates a batch of unique IDs")
	void test_nextIds_batch() {
		int batchSize = 50;
		List<Long> ids = generator.nextIds(BizType.AUTH, batchSize);
		Assertions.assertThat(ids).hasSize(batchSize);
		// All IDs should be positive
		Assertions.assertThat(ids).allMatch(id -> id > 0);
		// All IDs should be unique
		Set<Long> uniqueIds = new HashSet<>(ids);
		Assertions.assertThat(uniqueIds).hasSize(batchSize);
	}

	@Test
	@DisplayName("Test nextId throws IllegalStateException when not initialized")
	void test_nextId_notInitialized_throwsException() {
		generator.close();
		Assertions.assertThatThrownBy(() -> generator.nextId(BizType.AUTH))
			.isInstanceOf(IllegalStateException.class)
			.hasMessageContaining("not initialized");
	}

	@Test
	@DisplayName("Test nextId continues generating IDs after segment switch (step=100, 200+ IDs trigger switch)")
	void test_nextId_crossSegmentSwitch() {
		Set<Long> ids = new HashSet<>();
		// step=100, generating 250 IDs will definitely trigger segment switching
		for (int i = 0; i < 250; i++) {
			long id = generator.nextId(BizType.AUTH);
			Assertions.assertThat(id).isPositive();
			Assertions.assertThat(ids.add(id)).isTrue();
		}
		Assertions.assertThat(ids).hasSize(250);
	}

	@Test
	@DisplayName("Test getInstant throws UnsupportedOperationException")
	void test_getInstant_throwsUnsupported() {
		Assertions.assertThatThrownBy(() -> generator.getInstant(1L)).isInstanceOf(UnsupportedOperationException.class);
	}

	@Test
	@DisplayName("Test getDatacenterId throws UnsupportedOperationException")
	void test_getDatacenterId_throwsUnsupported() {
		Assertions.assertThatThrownBy(() -> generator.getDatacenterId(1L))
			.isInstanceOf(UnsupportedOperationException.class);
	}

	@Test
	@DisplayName("Test getWorkerId throws UnsupportedOperationException")
	void test_getWorkerId_throwsUnsupported() {
		Assertions.assertThatThrownBy(() -> generator.getWorkerId(1L))
			.isInstanceOf(UnsupportedOperationException.class);
	}

	@Test
	@DisplayName("Test getSequence throws UnsupportedOperationException")
	void test_getSequence_throwsUnsupported() {
		Assertions.assertThatThrownBy(() -> generator.getSequence(1L))
			.isInstanceOf(UnsupportedOperationException.class);
	}

	@Test
	@DisplayName("Test close flushes cursor and rejects new requests")
	void test_close() {
		// Generate some IDs
		long id = generator.nextId(BizType.AUTH);
		Assertions.assertThat(id).isPositive();

		// Close
		generator.close();

		// Should throw exception after close
		Assertions.assertThatThrownBy(() -> generator.nextId(BizType.AUTH)).isInstanceOf(IllegalStateException.class);
	}

	private static SpringSegmentProperties getSpringSegmentProperties() {
		SpringSegmentProperties springSegmentProperties = new SpringSegmentProperties();
		Map<String, SpringSegmentProperties.SegmentConfig> configs = new HashMap<>();

		SpringSegmentProperties.SegmentConfig authConfig = new SpringSegmentProperties.SegmentConfig();
		authConfig.setStep(100);
		authConfig.setKey("id-generator:segment:test:auth");
		authConfig.setCursorKey("id-generator:segment:cursor:test:auth:1");
		authConfig.setLoadFactor(0.8);
		configs.put(BizType.AUTH.getCode(), authConfig);

		springSegmentProperties.setConfigs(configs);
		return springSegmentProperties;
	}

}
