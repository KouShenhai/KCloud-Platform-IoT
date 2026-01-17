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
import org.laokou.common.testcontainers.util.DockerImageNames;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * OAuth2AuthorizationRequest转换器集成测试类 - 使用Testcontainers.
 *
 * @author laokou
 */
@Testcontainers
class OAuth2AuthorizationRequestConverterIntegrationTest {

	@Container
	static RedisContainer redisContainer = new RedisContainer(DockerImageNames.redis()).withExposedPorts(6379)
		.withReuse(true);

	private RedisTemplate<String, byte[]> redisTemplate;

	private OAuth2AuthorizationRequestToBytesConverter toBytesConverter;

	private BytesToOAuth2AuthorizationRequestConverter fromBytesConverter;

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
		toBytesConverter = new OAuth2AuthorizationRequestToBytesConverter();
		fromBytesConverter = new BytesToOAuth2AuthorizationRequestConverter();
	}

	@Test
	@DisplayName("Test OAuth2AuthorizationRequest serialize and store to Redis then read back")
	void test_authorizationRequest_serialize_and_store_to_redis() {
		// Given
		String key = "test:oauth2:request";
		OAuth2AuthorizationRequest request = OAuth2AuthorizationRequest.authorizationCode()
			.authorizationUri("https://auth.example.com/oauth2/authorize")
			.clientId("client-123")
			.redirectUri("https://app.example.com/callback")
			.scope("openid", "profile", "email")
			.state("random-state-value")
			.build();

		// When - 序列化并存储
		byte[] bytes = toBytesConverter.convert(request);
		redisTemplate.opsForValue().set(key, bytes);

		// Then - 读取并反序列化
		byte[] storedBytes = redisTemplate.opsForValue().get(key);
		Assertions.assertThat(storedBytes).isNotNull();

		OAuth2AuthorizationRequest result = fromBytesConverter.convert(storedBytes);
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result.getAuthorizationUri()).isEqualTo("https://auth.example.com/oauth2/authorize");
		Assertions.assertThat(result.getClientId()).isEqualTo("client-123");
		Assertions.assertThat(result.getRedirectUri()).isEqualTo("https://app.example.com/callback");
		Assertions.assertThat(result.getScopes()).containsExactlyInAnyOrder("openid", "profile", "email");
		Assertions.assertThat(result.getState()).isEqualTo("random-state-value");
	}

	@Test
	@DisplayName("Test OAuth2AuthorizationRequest full serialization roundtrip")
	void test_authorizationRequest_full_roundtrip() {
		// Given
		OAuth2AuthorizationRequest original = OAuth2AuthorizationRequest.authorizationCode()
			.authorizationUri("https://example.com/authorize")
			.clientId("test-client")
			.redirectUri("https://example.com/callback")
			.scope("read", "write")
			.state("xyz123")
			.additionalParameters(params -> params.put("nonce", "nonce-value"))
			.build();

		// When
		byte[] bytes = toBytesConverter.convert(original);
		OAuth2AuthorizationRequest result = fromBytesConverter.convert(bytes);

		// Then
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result.getClientId()).isEqualTo("test-client");
		Assertions.assertThat(result.getScopes()).containsExactlyInAnyOrder("read", "write");
		Assertions.assertThat(result.getAdditionalParameters()).containsEntry("nonce", "nonce-value");
	}

	@Test
	@DisplayName("Test minimal OAuth2AuthorizationRequest serialization roundtrip")
	void test_minimal_authorizationRequest_roundtrip() {
		// Given
		OAuth2AuthorizationRequest original = OAuth2AuthorizationRequest.authorizationCode()
			.authorizationUri("https://example.com/authorize")
			.clientId("minimal-client")
			.build();

		// When
		byte[] bytes = toBytesConverter.convert(original);
		OAuth2AuthorizationRequest result = fromBytesConverter.convert(bytes);

		// Then
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result.getClientId()).isEqualTo("minimal-client");
		Assertions.assertThat(result.getAuthorizationUri()).isEqualTo("https://example.com/authorize");
	}

}
