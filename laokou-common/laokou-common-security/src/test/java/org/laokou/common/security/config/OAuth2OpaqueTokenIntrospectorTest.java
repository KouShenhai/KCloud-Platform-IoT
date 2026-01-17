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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

/**
 * OAuth2OpaqueTokenIntrospector测试类.
 *
 * @author laokou
 */
@ExtendWith(MockitoExtension.class)
class OAuth2OpaqueTokenIntrospectorTest {

	@Mock
	private OAuth2AuthorizationService authorizationService;

	@Test
	void test_introspect_throws_exception_when_authorization_is_null() {
		// Given
		OAuth2OpaqueTokenIntrospector introspector = new OAuth2OpaqueTokenIntrospector(authorizationService);
		Mockito.when(authorizationService.findByToken("invalid-token", OAuth2TokenType.ACCESS_TOKEN)).thenReturn(null);

		// Then
		Assertions.assertThatThrownBy(() -> introspector.introspect("invalid-token")).isNotNull();
	}

	@Test
	void test_introspect_throws_exception_when_accessToken_is_null() {
		// Given
		OAuth2OpaqueTokenIntrospector introspector = new OAuth2OpaqueTokenIntrospector(authorizationService);
		RegisteredClient registeredClient = createRegisteredClient();
		OAuth2Authorization authorization = OAuth2Authorization.withRegisteredClient(registeredClient)
			.principalName("user")
			.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
			.build();
		Mockito.when(authorizationService.findByToken("token-without-access", OAuth2TokenType.ACCESS_TOKEN))
			.thenReturn(authorization);

		// Then
		Assertions.assertThatThrownBy(() -> introspector.introspect("token-without-access")).isNotNull();
	}

	@Test
	void test_record_constructor() {
		// When
		OAuth2OpaqueTokenIntrospector introspector = new OAuth2OpaqueTokenIntrospector(authorizationService);

		// Then
		Assertions.assertThat(introspector.authorizationService()).isEqualTo(authorizationService);
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
