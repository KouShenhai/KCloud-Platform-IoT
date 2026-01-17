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

package org.laokou.common.security.config.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * OAuth2RegisteredClient测试类.
 *
 * @author laokou
 */
class OAuth2RegisteredClientTest {

	@Test
	void test_clientSettings_creation() {
		// When
		OAuth2RegisteredClient.ClientSettings clientSettings = new OAuth2RegisteredClient.ClientSettings(true, true,
				"https://jwkset.com", null, null);

		// Then
		Assertions.assertThat(clientSettings).isNotNull();
		Assertions.assertThat(clientSettings.requireProofKey()).isTrue();
		Assertions.assertThat(clientSettings.requireAuthorizationConsent()).isTrue();
		Assertions.assertThat(clientSettings.jwkSetUrl()).isEqualTo("https://jwkset.com");
	}

	@Test
	void test_tokenSettings_creation() {
		// Given
		Duration authCodeTTL = Duration.ofMinutes(5);
		Duration accessTokenTTL = Duration.ofHours(1);
		Duration deviceCodeTTL = Duration.ofMinutes(10);
		Duration refreshTokenTTL = Duration.ofDays(30);

		// When
		OAuth2RegisteredClient.TokenSettings tokenSettings = new OAuth2RegisteredClient.TokenSettings(authCodeTTL,
				accessTokenTTL, OAuth2TokenFormat.SELF_CONTAINED, deviceCodeTTL, true, refreshTokenTTL,
				SignatureAlgorithm.RS256, false);

		// Then
		Assertions.assertThat(tokenSettings).isNotNull();
		Assertions.assertThat(tokenSettings.authorizationCodeTimeToLive()).isEqualTo(authCodeTTL);
		Assertions.assertThat(tokenSettings.accessTokenTimeToLive()).isEqualTo(accessTokenTTL);
		Assertions.assertThat(tokenSettings.accessTokenFormat()).isEqualTo(OAuth2TokenFormat.SELF_CONTAINED);
		Assertions.assertThat(tokenSettings.deviceCodeTimeToLive()).isEqualTo(deviceCodeTTL);
		Assertions.assertThat(tokenSettings.reuseRefreshTokens()).isTrue();
		Assertions.assertThat(tokenSettings.refreshTokenTimeToLive()).isEqualTo(refreshTokenTTL);
		Assertions.assertThat(tokenSettings.idTokenSignatureAlgorithm()).isEqualTo(SignatureAlgorithm.RS256);
		Assertions.assertThat(tokenSettings.x509CertificateBoundAccessTokens()).isFalse();
	}

	@Test
	void test_registeredClient_creation() {
		// Given
		String id = "client-1";
		String clientId = "my-client";
		Instant clientIdIssuedAt = Instant.now();
		String clientSecret = "secret";
		String clientName = "My Client";
		Set<ClientAuthenticationMethod> authMethods = new HashSet<>();
		authMethods.add(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
		Set<AuthorizationGrantType> grantTypes = new HashSet<>();
		grantTypes.add(AuthorizationGrantType.AUTHORIZATION_CODE);
		grantTypes.add(AuthorizationGrantType.REFRESH_TOKEN);
		Set<String> redirectUris = new HashSet<>();
		redirectUris.add("https://example.com/callback");
		Set<String> postLogoutRedirectUris = new HashSet<>();
		Set<String> scopes = new HashSet<>();
		scopes.add("openid");
		scopes.add("profile");
		OAuth2RegisteredClient.ClientSettings clientSettings = new OAuth2RegisteredClient.ClientSettings(false, true,
				null, null, null);
		OAuth2RegisteredClient.TokenSettings tokenSettings = new OAuth2RegisteredClient.TokenSettings(
				Duration.ofMinutes(5), Duration.ofHours(1), OAuth2TokenFormat.SELF_CONTAINED, Duration.ofMinutes(10),
				true, Duration.ofDays(30), SignatureAlgorithm.RS256, false);

		// When
		OAuth2RegisteredClient client = new OAuth2RegisteredClient(id, clientId, clientIdIssuedAt, clientSecret, null,
				clientName, authMethods, grantTypes, redirectUris, postLogoutRedirectUris, scopes, clientSettings,
				tokenSettings);

		// Then
		Assertions.assertThat(client).isNotNull();
		Assertions.assertThat(client.getId()).isEqualTo(id);
		Assertions.assertThat(client.getClientId()).isEqualTo(clientId);
		Assertions.assertThat(client.getClientIdIssuedAt()).isEqualTo(clientIdIssuedAt);
		Assertions.assertThat(client.getClientSecret()).isEqualTo(clientSecret);
		Assertions.assertThat(client.getClientSecretExpiresAt()).isNull();
		Assertions.assertThat(client.getClientName()).isEqualTo(clientName);
		Assertions.assertThat(client.getClientAuthenticationMethods())
			.contains(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
		Assertions.assertThat(client.getAuthorizationGrantTypes())
			.contains(AuthorizationGrantType.AUTHORIZATION_CODE, AuthorizationGrantType.REFRESH_TOKEN);
		Assertions.assertThat(client.getRedirectUris()).contains("https://example.com/callback");
		Assertions.assertThat(client.getScopes()).containsExactlyInAnyOrder("openid", "profile");
		Assertions.assertThat(client.getClientSettings()).isEqualTo(clientSettings);
		Assertions.assertThat(client.getTokenSettings()).isEqualTo(tokenSettings);
	}

}
