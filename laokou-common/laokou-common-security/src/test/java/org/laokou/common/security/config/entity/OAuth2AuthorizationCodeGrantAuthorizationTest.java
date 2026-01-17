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
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * OAuth2AuthorizationCodeGrantAuthorization测试类.
 *
 * @author laokou
 */
class OAuth2AuthorizationCodeGrantAuthorizationTest {

	@Test
	void test_authorizationCode_creation() {
		// Given
		String tokenValue = "auth_code_value";
		Instant issuedAt = Instant.now();
		Instant expiresAt = issuedAt.plusSeconds(300);

		// When
		OAuth2AuthorizationCodeGrantAuthorization.AuthorizationCode authorizationCode = new OAuth2AuthorizationCodeGrantAuthorization.AuthorizationCode(
				tokenValue, issuedAt, expiresAt, false);

		// Then
		Assertions.assertThat(authorizationCode).isNotNull();
		Assertions.assertThat(authorizationCode.getTokenValue()).isEqualTo(tokenValue);
		Assertions.assertThat(authorizationCode.getIssuedAt()).isEqualTo(issuedAt);
		Assertions.assertThat(authorizationCode.getExpiresAt()).isEqualTo(expiresAt);
		Assertions.assertThat(authorizationCode.isInvalidated()).isFalse();
	}

	@Test
	void test_authorizationCode_invalidated() {
		// Given
		String tokenValue = "auth_code_value";
		Instant issuedAt = Instant.now();
		Instant expiresAt = issuedAt.plusSeconds(300);

		// When
		OAuth2AuthorizationCodeGrantAuthorization.AuthorizationCode authorizationCode = new OAuth2AuthorizationCodeGrantAuthorization.AuthorizationCode(
				tokenValue, issuedAt, expiresAt, true);

		// Then
		Assertions.assertThat(authorizationCode.isInvalidated()).isTrue();
	}

	@Test
	void test_authorizationCodeGrantAuthorization_creation() {
		// Given
		String id = "auth-1";
		String registeredClientId = "client-1";
		String principalName = "user123";
		String state = "random-state";
		Set<String> authorizedScopes = new HashSet<>();
		authorizedScopes.add("openid");
		authorizedScopes.add("profile");

		Instant now = Instant.now();
		OAuth2AuthorizationGrantAuthorization.AccessToken accessToken = new OAuth2AuthorizationGrantAuthorization.AccessToken(
				"access_token", now, now.plusSeconds(3600), false, OAuth2AccessToken.TokenType.BEARER, authorizedScopes,
				OAuth2TokenFormat.SELF_CONTAINED,
				new OAuth2AuthorizationGrantAuthorization.ClaimsHolder(new HashMap<>()));

		OAuth2AuthorizationGrantAuthorization.RefreshToken refreshToken = new OAuth2AuthorizationGrantAuthorization.RefreshToken(
				"refresh_token", now, now.plusSeconds(86400), false);

		OAuth2AuthorizationCodeGrantAuthorization.AuthorizationCode authorizationCode = new OAuth2AuthorizationCodeGrantAuthorization.AuthorizationCode(
				"auth_code", now, now.plusSeconds(300), false);

		// When
		OAuth2AuthorizationCodeGrantAuthorization authorization = new OAuth2AuthorizationCodeGrantAuthorization(id,
				registeredClientId, principalName, authorizedScopes, accessToken, refreshToken, null, null,
				authorizationCode, state);

		// Then
		Assertions.assertThat(authorization).isNotNull();
		Assertions.assertThat(authorization.getId()).isEqualTo(id);
		Assertions.assertThat(authorization.getRegisteredClientId()).isEqualTo(registeredClientId);
		Assertions.assertThat(authorization.getPrincipalName()).isEqualTo(principalName);
		Assertions.assertThat(authorization.getAuthorizedScopes()).containsExactlyInAnyOrder("openid", "profile");
		Assertions.assertThat(authorization.getAccessToken()).isEqualTo(accessToken);
		Assertions.assertThat(authorization.getRefreshToken()).isEqualTo(refreshToken);
		Assertions.assertThat(authorization.getAuthorizationCode()).isEqualTo(authorizationCode);
		Assertions.assertThat(authorization.getState()).isEqualTo(state);
	}

}
