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

package org.laokou.common.security.config.convertor;

import com.redis.testcontainers.RedisContainer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.laokou.common.security.config.entity.OAuth2AuthorizationGrantAuthorization;
import org.laokou.common.testcontainers.util.DockerImageNames;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashMap;
import java.util.Map;

/**
 * ClaimsHolder转换器集成测试类 - 使用Testcontainers.
 *
 * @author laokou
 */
@Testcontainers
class ClaimsHolderConverterIntegrationTest {

	@Container
	static RedisContainer redisContainer = new RedisContainer(DockerImageNames.redis()).withExposedPorts(6379)
		.withReuse(true);

	private RedisTemplate<String, byte[]> redisTemplate;

	private ClaimsHolderToBytesConverter toBytesConverter;

	private BytesToClaimsHolderConverter fromBytesConverter;

	@BeforeEach
	void setUp() {
		String redisHost = redisContainer.getHost();
		Integer redisPort = redisContainer.getMappedPort(6379);

		// 配置 Redis 连接
		RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(redisHost, redisPort);
		LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(redisConfig);
		connectionFactory.afterPropertiesSet();

		// 配置 RedisTemplate
		redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.afterPropertiesSet();

		// 初始化转换器
		toBytesConverter = new ClaimsHolderToBytesConverter();
		fromBytesConverter = new BytesToClaimsHolderConverter();
	}

	@Test
	@DisplayName("Test ClaimsHolder serialize and store to Redis then read back")
	void test_claimsHolder_serialize_and_store_to_redis() {
		// Given
		String key = "test:claims:holder";
		Map<String, Object> claims = new HashMap<>();
		claims.put("sub", "user123");
		claims.put("iss", "https://issuer.com");
		claims.put("aud", "client-app");
		OAuth2AuthorizationGrantAuthorization.ClaimsHolder claimsHolder = new OAuth2AuthorizationGrantAuthorization.ClaimsHolder(
				claims);

		// When - 序列化并存储
		byte[] bytes = toBytesConverter.convert(claimsHolder);
		redisTemplate.opsForValue().set(key, bytes);

		// Then - 读取并反序列化
		byte[] storedBytes = redisTemplate.opsForValue().get(key);
		Assertions.assertThat(storedBytes).isNotNull();

		OAuth2AuthorizationGrantAuthorization.ClaimsHolder result = fromBytesConverter.convert(storedBytes);
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result.claims().get("sub")).isEqualTo("user123");
		Assertions.assertThat(result.claims().get("iss")).isEqualTo("https://issuer.com");
		Assertions.assertThat(result.claims().get("aud")).isEqualTo("client-app");
	}

	@Test
	@DisplayName("Test ClaimsHolder full serialization roundtrip")
	void test_claimsHolder_full_roundtrip() {
		// Given
		Map<String, Object> claims = new HashMap<>();
		claims.put("test_key", "test_value");
		claims.put("number", 12345);
		OAuth2AuthorizationGrantAuthorization.ClaimsHolder original = new OAuth2AuthorizationGrantAuthorization.ClaimsHolder(
				claims);

		// When
		byte[] bytes = toBytesConverter.convert(original);
		OAuth2AuthorizationGrantAuthorization.ClaimsHolder result = fromBytesConverter.convert(bytes);

		// Then
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result.claims()).containsEntry("test_key", "test_value");
		Assertions.assertThat(result.claims()).containsEntry("number", 12345);
	}

	@Test
	@DisplayName("Test empty claims serialization roundtrip")
	void test_empty_claimsHolder_roundtrip() {
		// Given
		Map<String, Object> claims = new HashMap<>();
		OAuth2AuthorizationGrantAuthorization.ClaimsHolder original = new OAuth2AuthorizationGrantAuthorization.ClaimsHolder(
				claims);

		// When
		byte[] bytes = toBytesConverter.convert(original);
		OAuth2AuthorizationGrantAuthorization.ClaimsHolder result = fromBytesConverter.convert(bytes);

		// Then
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result.claims()).isEmpty();
	}

}
