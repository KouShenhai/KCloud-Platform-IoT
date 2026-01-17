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

package org.laokou.common.security.config;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.laokou.common.security.config.entity.OAuth2RegisteredClient;
import org.laokou.common.security.config.repository.OAuth2RegisteredClientRepository;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.util.Optional;

/**
 * RedisRegisteredClientRepository测试类.
 *
 * @author laokou
 */
@ExtendWith(MockitoExtension.class)
class RedisRegisteredClientRepositoryTest {

	@Mock
	private OAuth2RegisteredClientRepository registeredClientRepository;

	private RedisRegisteredClientRepository repository;

	@BeforeEach
	void setUp() {
		repository = new RedisRegisteredClientRepository(registeredClientRepository);
	}

	@Test
	void test_constructor_throws_exception_when_repository_is_null() {
		// Then
		Assertions.assertThatThrownBy(() -> new RedisRegisteredClientRepository(null))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("RegisteredClientRepository cannot be null");
	}

	@Test
	void test_save_registeredClient() {
		// Given
		RegisteredClient registeredClient = RegisteredClient.withId("client-1")
			.clientId("my-client")
			.clientSecret("secret")
			.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
			.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
			.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
			.redirectUri("https://example.com/callback")
			.scope("openid")
			.clientSettings(ClientSettings.builder().build())
			.tokenSettings(TokenSettings.builder().build())
			.build();

		// When
		repository.save(registeredClient);

		// Then
		Mockito.verify(registeredClientRepository).save(Mockito.any(OAuth2RegisteredClient.class));
	}

	@Test
	void test_findById_returns_null_when_not_found() {
		// Given
		Mockito.when(registeredClientRepository.findById("non-existent")).thenReturn(Optional.empty());

		// When
		RegisteredClient result = repository.findById("non-existent");

		// Then
		Assertions.assertThat(result).isNull();
	}

	@Test
	void test_findByClientId_returns_null_when_not_found() {
		// Given
		Mockito.when(registeredClientRepository.findByClientId("non-existent")).thenReturn(null);

		// When
		RegisteredClient result = repository.findByClientId("non-existent");

		// Then
		Assertions.assertThat(result).isNull();
	}

	@Test
	void test_save_throws_exception_when_client_is_null() {
		// Then
		Assertions.assertThatThrownBy(() -> repository.save(null))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("RegisteredClient cannot be null");
	}

	@Test
	void test_findById_throws_exception_when_id_is_empty() {
		// Then
		Assertions.assertThatThrownBy(() -> repository.findById(""))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("Id cannot be empty");
	}

	@Test
	void test_findByClientId_throws_exception_when_clientId_is_empty() {
		// Then
		Assertions.assertThatThrownBy(() -> repository.findByClientId(""))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("ClientId cannot be empty");
	}

}
