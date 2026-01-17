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
import java.util.Map;
import java.util.Set;

/**
 * OidcAuthorizationCodeGrantAuthorization测试类.
 *
 * @author laokou
 */
class OidcAuthorizationCodeGrantAuthorizationTest {

	@Test
	void test_idToken_creation() {
		// Given
		String tokenValue = "id_token_value";
		Instant issuedAt = Instant.now();
		Instant expiresAt = issuedAt.plusSeconds(3600);
		Map<String, Object> claims = new HashMap<>();
		claims.put("sub", "user123");
		claims.put("iss", "https://issuer.com");
		OAuth2AuthorizationGrantAuthorization.ClaimsHolder claimsHolder = new OAuth2AuthorizationGrantAuthorization.ClaimsHolder(
				claims);

		// When
		OidcAuthorizationCodeGrantAuthorization.IdToken idToken = new OidcAuthorizationCodeGrantAuthorization.IdToken(
				tokenValue, issuedAt, expiresAt, false, claimsHolder);

		// Then
		Assertions.assertThat(idToken).isNotNull();
		Assertions.assertThat(idToken.getTokenValue()).isEqualTo(tokenValue);
		Assertions.assertThat(idToken.getIssuedAt()).isEqualTo(issuedAt);
		Assertions.assertThat(idToken.getExpiresAt()).isEqualTo(expiresAt);
		Assertions.assertThat(idToken.isInvalidated()).isFalse();
		Assertions.assertThat(idToken.getClaims()).isEqualTo(claimsHolder);
	}

	@Test
	void test_oidcAuthorizationCodeGrantAuthorization_creation() {
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

		Map<String, Object> idTokenClaims = new HashMap<>();
		idTokenClaims.put("sub", "user123");
		OidcAuthorizationCodeGrantAuthorization.IdToken idToken = new OidcAuthorizationCodeGrantAuthorization.IdToken(
				"id_token", now, now.plusSeconds(3600), false,
				new OAuth2AuthorizationGrantAuthorization.ClaimsHolder(idTokenClaims));

		// When
		OidcAuthorizationCodeGrantAuthorization authorization = new OidcAuthorizationCodeGrantAuthorization(id,
				registeredClientId, principalName, authorizedScopes, accessToken, refreshToken, null, null,
				authorizationCode, state, idToken);

		// Then
		Assertions.assertThat(authorization).isNotNull();
		Assertions.assertThat(authorization.getId()).isEqualTo(id);
		Assertions.assertThat(authorization.getRegisteredClientId()).isEqualTo(registeredClientId);
		Assertions.assertThat(authorization.getPrincipalName()).isEqualTo(principalName);
		Assertions.assertThat(authorization.getAuthorizedScopes()).containsExactlyInAnyOrder("openid", "profile");
		Assertions.assertThat(authorization.getIdToken()).isEqualTo(idToken);
		Assertions.assertThat(authorization.getState()).isEqualTo(state);
	}

	@Test
	void test_idToken_invalidated() {
		// Given
		Instant now = Instant.now();
		Map<String, Object> claims = new HashMap<>();
		OAuth2AuthorizationGrantAuthorization.ClaimsHolder claimsHolder = new OAuth2AuthorizationGrantAuthorization.ClaimsHolder(
				claims);

		// When
		OidcAuthorizationCodeGrantAuthorization.IdToken idToken = new OidcAuthorizationCodeGrantAuthorization.IdToken(
				"id_token", now, now.plusSeconds(3600), true, claimsHolder);

		// Then
		Assertions.assertThat(idToken.isInvalidated()).isTrue();
	}

	@Test
	void test_oidcAuthorizationCodeGrantAuthorization_with_null_idToken() {
		// Given
		String id = "auth-1";
		String registeredClientId = "client-1";
		String principalName = "user123";
		Set<String> authorizedScopes = new HashSet<>();
		authorizedScopes.add("openid");

		Instant now = Instant.now();
		OAuth2AuthorizationGrantAuthorization.AccessToken accessToken = new OAuth2AuthorizationGrantAuthorization.AccessToken(
				"access_token", now, now.plusSeconds(3600), false, OAuth2AccessToken.TokenType.BEARER, authorizedScopes,
				OAuth2TokenFormat.SELF_CONTAINED,
				new OAuth2AuthorizationGrantAuthorization.ClaimsHolder(new HashMap<>()));

		OAuth2AuthorizationCodeGrantAuthorization.AuthorizationCode authorizationCode = new OAuth2AuthorizationCodeGrantAuthorization.AuthorizationCode(
				"auth_code", now, now.plusSeconds(300), false);

		// When
		OidcAuthorizationCodeGrantAuthorization authorization = new OidcAuthorizationCodeGrantAuthorization(id,
				registeredClientId, principalName, authorizedScopes, accessToken, null, null, null, authorizationCode,
				null, null);

		// Then
		Assertions.assertThat(authorization.getIdToken()).isNull();
		Assertions.assertThat(authorization.getState()).isNull();
	}

}
