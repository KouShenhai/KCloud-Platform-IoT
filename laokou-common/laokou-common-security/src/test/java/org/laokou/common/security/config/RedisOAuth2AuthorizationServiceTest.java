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
import org.laokou.common.security.config.repository.OAuth2AuthorizationGrantAuthorizationRepository;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.util.Optional;

/**
 * RedisOAuth2AuthorizationService测试类.
 *
 * @author laokou
 */
@ExtendWith(MockitoExtension.class)
class RedisOAuth2AuthorizationServiceTest {

	@Mock
	private RegisteredClientRepository registeredClientRepository;

	@Mock
	private OAuth2AuthorizationGrantAuthorizationRepository authorizationGrantAuthorizationRepository;

	private RedisOAuth2AuthorizationService service;

	@BeforeEach
	void setUp() {
		service = new RedisOAuth2AuthorizationService(registeredClientRepository,
				authorizationGrantAuthorizationRepository);
	}

	@Test
	void test_constructor_throws_exception_when_registeredClientRepository_is_null() {
		// Then
		Assertions
			.assertThatThrownBy(
					() -> new RedisOAuth2AuthorizationService(null, authorizationGrantAuthorizationRepository))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("RegisteredClientRepository cannot be null");
	}

	@Test
	void test_constructor_throws_exception_when_authorizationGrantAuthorizationRepository_is_null() {
		// Then
		Assertions.assertThatThrownBy(() -> new RedisOAuth2AuthorizationService(registeredClientRepository, null))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("AuthorizationGrantAuthorizationRepository cannot be null");
	}

	@Test
	void test_remove_authorization() {
		// Given
		RegisteredClient registeredClient = createRegisteredClient();
		OAuth2Authorization authorization = OAuth2Authorization.withRegisteredClient(registeredClient)
			.principalName("user")
			.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
			.build();

		// When
		service.remove(authorization);

		// Then
		Mockito.verify(authorizationGrantAuthorizationRepository).deleteById(authorization.getId());
	}

	@Test
	void test_findById_returns_null_when_not_found() {
		// Given
		Mockito.when(authorizationGrantAuthorizationRepository.findById("non-existent")).thenReturn(Optional.empty());

		// When
		OAuth2Authorization result = service.findById("non-existent");

		// Then
		Assertions.assertThat(result).isNull();
	}

	@Test
	void test_findByToken_returns_null_when_not_found() {
		// Given
		Mockito.when(authorizationGrantAuthorizationRepository.findByAccessToken_TokenValue("non-existent-token"))
			.thenReturn(null);

		// When
		OAuth2Authorization result = service.findByToken("non-existent-token", OAuth2TokenType.ACCESS_TOKEN);

		// Then
		Assertions.assertThat(result).isNull();
	}

	@Test
	void test_save_throws_exception_when_authorization_is_null() {
		// Then
		Assertions.assertThatThrownBy(() -> service.save(null))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("Authorization cannot be null");
	}

	@Test
	void test_remove_throws_exception_when_authorization_is_null() {
		// Then
		Assertions.assertThatThrownBy(() -> service.remove(null))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("authorization cannot be null");
	}

	@Test
	void test_findById_throws_exception_when_id_is_empty() {
		// Then
		Assertions.assertThatThrownBy(() -> service.findById(""))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("id cannot be empty");
	}

	@Test
	void test_findByToken_throws_exception_when_token_is_empty() {
		// Then
		Assertions.assertThatThrownBy(() -> service.findByToken("", OAuth2TokenType.ACCESS_TOKEN))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("Token cannot be empty");
	}

	private RegisteredClient createRegisteredClient() {
		return RegisteredClient.withId("client-1")
			.clientId("my-client")
			.clientSecret("secret")
			.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
			.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
			.redirectUri("https://example.com/callback")
			.scope("openid")
			.clientSettings(ClientSettings.builder().build())
			.tokenSettings(TokenSettings.builder().build())
			.build();
	}

}
