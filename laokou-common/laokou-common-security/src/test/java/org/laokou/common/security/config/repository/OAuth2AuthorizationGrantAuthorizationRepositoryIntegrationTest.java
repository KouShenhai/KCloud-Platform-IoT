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

package org.laokou.common.security.config.repository;

import com.redis.testcontainers.RedisContainer;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.laokou.common.security.config.entity.OAuth2AuthorizationGrantAuthorization;
import org.laokou.common.security.config.entity.OAuth2UsernamePasswordGrantAuthorization;
import org.laokou.common.testcontainers.util.DockerImageNames;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.RedisKeyValueTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.mapping.RedisMappingContext;
import org.springframework.data.redis.repository.support.RedisRepositoryFactory;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.jackson.SecurityJacksonModules;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import tools.jackson.databind.json.JsonMapper;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * OAuth2AuthorizationGrantAuthorizationRepository integration test with Testcontainers.
 *
 * @author laokou
 */
@Testcontainers
class OAuth2AuthorizationGrantAuthorizationRepositoryIntegrationTest {

	@Container
	static RedisContainer redisContainer = new RedisContainer(DockerImageNames.redis()).withExposedPorts(6379)
		.withReuse(true);

	private OAuth2AuthorizationGrantAuthorizationRepository repository;

	@BeforeEach
	void setUp() {
		String redisHost = redisContainer.getHost();
		Integer redisPort = redisContainer.getMappedPort(6379);

		// Configure Redis connection
		RedisTemplate<String, Object> redisTemplate = OAuth2AuthorizationGrantAuthorizationRepositoryIntegrationTest
			.getStringObjectRedisTemplate(redisHost, redisPort,
					OAuth2AuthorizationGrantAuthorizationRepositoryIntegrationTest.class);

		// Create repository using RedisRepositoryFactory
		RedisMappingContext mappingContext = new RedisMappingContext();
		RedisKeyValueAdapter keyValueAdapter = new RedisKeyValueAdapter(redisTemplate, mappingContext);
		RedisKeyValueTemplate keyValueTemplate = new RedisKeyValueTemplate(keyValueAdapter, mappingContext);
		RedisRepositoryFactory factory = new RedisRepositoryFactory(keyValueTemplate);
		repository = factory.getRepository(OAuth2AuthorizationGrantAuthorizationRepository.class);

		// Clean up before each test
		Assertions.assertThat(redisTemplate.getConnectionFactory()).isNotNull();
		redisTemplate.getConnectionFactory().getConnection().serverCommands().flushAll();
	}

	@Test
	@DisplayName("Test save and findById authorization")
	void test_save_and_findById() {
		// Given
		OAuth2UsernamePasswordGrantAuthorization authorization = createTestAuthorization("auth-1", "client-1", "user-1",
				"test-access-token");

		// When
		repository.save(authorization);
		var result = repository.findById("auth-1");

		// Then
		Assertions.assertThat(result).isPresent();
		Assertions.assertThat(result.get().getPrincipalName()).isEqualTo("user-1");
	}

	@Test
	@DisplayName("Test findByAccessToken_TokenValue")
	void test_findByAccessToken_TokenValue() {
		// Given
		OAuth2UsernamePasswordGrantAuthorization authorization = createTestAuthorization("auth-2", "client-2", "user-2",
				"unique-access-token");
		repository.save(authorization);

		// When
		var result = repository.findByAccessToken_TokenValue("unique-access-token");

		// Then
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result.getId()).isEqualTo("auth-2");
	}

	@Test
	@DisplayName("Test findByRefreshToken_TokenValue")
	void test_findByRefreshToken_TokenValue() {
		// Given
		OAuth2UsernamePasswordGrantAuthorization authorization = createTestAuthorization("auth-3", "client-3", "user-3",
				"access-token-3");
		repository.save(authorization);

		// When
		var result = repository.findByRefreshToken_TokenValue("refresh-token-auth-3");

		// Then
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result.getId()).isEqualTo("auth-3");
	}

	@Test
	@DisplayName("Test deleteById authorization")
	void test_deleteById() {
		// Given
		OAuth2UsernamePasswordGrantAuthorization authorization = createTestAuthorization("auth-4", "client-4", "user-4",
				"access-token-4");
		repository.save(authorization);

		// When
		repository.deleteById("auth-4");
		var result = repository.findById("auth-4");

		// Then
		Assertions.assertThat(result).isEmpty();
	}

	private OAuth2UsernamePasswordGrantAuthorization createTestAuthorization(String id, String clientId,
			String principalName, String accessTokenValue) {
		Set<String> scopes = new HashSet<>();
		scopes.add("read");
		scopes.add("write");

		Instant now = Instant.now();
		OAuth2AuthorizationGrantAuthorization.AccessToken accessToken = new OAuth2AuthorizationGrantAuthorization.AccessToken(
				accessTokenValue, now, now.plusSeconds(3600), false, OAuth2AccessToken.TokenType.BEARER, scopes,
				OAuth2TokenFormat.SELF_CONTAINED,
				new OAuth2AuthorizationGrantAuthorization.ClaimsHolder(new HashMap<>()));

		OAuth2AuthorizationGrantAuthorization.RefreshToken refreshToken = new OAuth2AuthorizationGrantAuthorization.RefreshToken(
				"refresh-token-" + id, now, now.plusSeconds(86400), false);

		return new OAuth2UsernamePasswordGrantAuthorization(id, clientId, principalName, scopes, accessToken,
				refreshToken, null);
	}

	@NotNull
	public static RedisTemplate<String, Object> getStringObjectRedisTemplate(String redisHost, Integer redisPort,
			Class<?> clazz) {
		RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(redisHost, redisPort);
		LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(redisConfig);
		connectionFactory.afterPropertiesSet();

		// Configure ObjectMapper with SecurityJacksonModules for SimpleGrantedAuthority
		// support
		JsonMapper objectMapper = JsonMapper.builder()
			.addModules(SecurityJacksonModules.getModules(clazz.getClassLoader()))
			.build();

		// Configure RedisTemplate
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		GenericJacksonJsonRedisSerializer serializer = new GenericJacksonJsonRedisSerializer(objectMapper);
		redisTemplate.setValueSerializer(serializer);
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashValueSerializer(serializer);
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}

}
