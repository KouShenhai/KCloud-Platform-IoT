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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.laokou.common.security.config.entity.OAuth2RegisteredClient;
import org.laokou.common.testcontainers.util.DockerImageNames;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.RedisKeyValueTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.mapping.RedisMappingContext;
import org.springframework.data.redis.repository.support.RedisRepositoryFactory;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * OAuth2RegisteredClientRepository integration test with Testcontainers.
 *
 * @author laokou
 */
@Testcontainers
class OAuth2RegisteredClientRepositoryIntegrationTest {

	@Container
	static RedisContainer redisContainer = new RedisContainer(DockerImageNames.redis()).withExposedPorts(6379)
		.withReuse(true);

	private OAuth2RegisteredClientRepository repository;

	@BeforeEach
	void setUp() {
		String redisHost = redisContainer.getHost();
		Integer redisPort = redisContainer.getMappedPort(6379);

		// Configure Redis connection
		RedisTemplate<String, Object> redisTemplate = OAuth2AuthorizationGrantAuthorizationRepositoryIntegrationTest
			.getStringObjectRedisTemplate(redisHost, redisPort, OAuth2RegisteredClientRepositoryIntegrationTest.class);

		// Create repository using RedisRepositoryFactory
		RedisMappingContext mappingContext = new RedisMappingContext();
		RedisKeyValueAdapter keyValueAdapter = new RedisKeyValueAdapter(redisTemplate, mappingContext);
		RedisKeyValueTemplate keyValueTemplate = new RedisKeyValueTemplate(keyValueAdapter, mappingContext);
		RedisRepositoryFactory factory = new RedisRepositoryFactory(keyValueTemplate);
		repository = factory.getRepository(OAuth2RegisteredClientRepository.class);

		// Clean up before each test
		Assertions.assertThat(redisTemplate.getConnectionFactory()).isNotNull();
		redisTemplate.getConnectionFactory().getConnection().serverCommands().flushAll();
	}

	@Test
	@DisplayName("Test save and findById registered client")
	void test_save_and_findById() {
		// Given
		OAuth2RegisteredClient client = createTestClient("client-1", "test-client-id");

		// When
		repository.save(client);
		var result = repository.findById("client-1");

		// Then
		Assertions.assertThat(result).isPresent();
		Assertions.assertThat(result.get().getClientId()).isEqualTo("test-client-id");
	}

	@Test
	@DisplayName("Test findByClientId")
	void test_findByClientId() {
		// Given
		OAuth2RegisteredClient client = createTestClient("client-2", "unique-client-id");
		repository.save(client);

		// When
		OAuth2RegisteredClient result = repository.findByClientId("unique-client-id");

		// Then
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result.getId()).isEqualTo("client-2");
	}

	@Test
	@DisplayName("Test findByClientId returns null when not found")
	void test_findByClientId_returns_null_when_not_found() {
		// When
		OAuth2RegisteredClient result = repository.findByClientId("non-existent-client");

		// Then
		Assertions.assertThat(result).isNull();
	}

	@Test
	@DisplayName("Test delete registered client")
	void test_delete_registeredClient() {
		// Given
		OAuth2RegisteredClient client = createTestClient("client-3", "delete-client-id");
		repository.save(client);

		// When
		repository.deleteById("client-3");
		var result = repository.findById("client-3");

		// Then
		Assertions.assertThat(result).isEmpty();
	}

	private OAuth2RegisteredClient createTestClient(String id, String clientId) {
		Set<ClientAuthenticationMethod> authMethods = new HashSet<>();
		authMethods.add(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);

		Set<AuthorizationGrantType> grantTypes = new HashSet<>();
		grantTypes.add(AuthorizationGrantType.AUTHORIZATION_CODE);
		grantTypes.add(AuthorizationGrantType.REFRESH_TOKEN);

		Set<String> redirectUris = new HashSet<>();
		redirectUris.add("https://example.com/callback");

		Set<String> scopes = new HashSet<>();
		scopes.add("openid");
		scopes.add("profile");

		return new OAuth2RegisteredClient(id, clientId, Instant.now(), "secret", Instant.now().plusSeconds(86400),
				"Test Client", authMethods, grantTypes, redirectUris, null, scopes,
				new OAuth2RegisteredClient.ClientSettings(false, false, null, null, null),
				new OAuth2RegisteredClient.TokenSettings(null, null, null, null, false, null, null, false));
	}

}
