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
 * OAuth2AuthorizationGrantAuthorization测试类.
 *
 * @author laokou
 */
class OAuth2AuthorizationGrantAuthorizationTest {

	@Test
	void test_claimsHolder_creation() {
		// Given
		Map<String, Object> claims = new HashMap<>();
		claims.put("sub", "user123");
		claims.put("iss", "https://issuer.com");

		// When
		OAuth2AuthorizationGrantAuthorization.ClaimsHolder claimsHolder = new OAuth2AuthorizationGrantAuthorization.ClaimsHolder(
				claims);

		// Then
		Assertions.assertThat(claimsHolder).isNotNull();
		Assertions.assertThat(claimsHolder.claims()).isEqualTo(claims);
		Assertions.assertThat(claimsHolder.claims().get("sub")).isEqualTo("user123");
	}

	@Test
	void test_accessToken_creation() {
		// Given
		String tokenValue = "access_token_value";
		Instant issuedAt = Instant.now();
		Instant expiresAt = issuedAt.plusSeconds(3600);
		Set<String> scopes = new HashSet<>();
		scopes.add("read");
		scopes.add("write");
		Map<String, Object> claimsMap = new HashMap<>();
		claimsMap.put("sub", "user123");
		OAuth2AuthorizationGrantAuthorization.ClaimsHolder claims = new OAuth2AuthorizationGrantAuthorization.ClaimsHolder(
				claimsMap);

		// When
		OAuth2AuthorizationGrantAuthorization.AccessToken accessToken = new OAuth2AuthorizationGrantAuthorization.AccessToken(
				tokenValue, issuedAt, expiresAt, false, OAuth2AccessToken.TokenType.BEARER, scopes,
				OAuth2TokenFormat.SELF_CONTAINED, claims);

		// Then
		Assertions.assertThat(accessToken).isNotNull();
		Assertions.assertThat(accessToken.getTokenValue()).isEqualTo(tokenValue);
		Assertions.assertThat(accessToken.getIssuedAt()).isEqualTo(issuedAt);
		Assertions.assertThat(accessToken.getExpiresAt()).isEqualTo(expiresAt);
		Assertions.assertThat(accessToken.isInvalidated()).isFalse();
		Assertions.assertThat(accessToken.getTokenType()).isEqualTo(OAuth2AccessToken.TokenType.BEARER);
		Assertions.assertThat(accessToken.getScopes()).containsExactlyInAnyOrder("read", "write");
		Assertions.assertThat(accessToken.getTokenFormat()).isEqualTo(OAuth2TokenFormat.SELF_CONTAINED);
		Assertions.assertThat(accessToken.getClaims()).isEqualTo(claims);
	}

	@Test
	void test_refreshToken_creation() {
		// Given
		String tokenValue = "refresh_token_value";
		Instant issuedAt = Instant.now();
		Instant expiresAt = issuedAt.plusSeconds(86400);

		// When
		OAuth2AuthorizationGrantAuthorization.RefreshToken refreshToken = new OAuth2AuthorizationGrantAuthorization.RefreshToken(
				tokenValue, issuedAt, expiresAt, false);

		// Then
		Assertions.assertThat(refreshToken).isNotNull();
		Assertions.assertThat(refreshToken.getTokenValue()).isEqualTo(tokenValue);
		Assertions.assertThat(refreshToken.getIssuedAt()).isEqualTo(issuedAt);
		Assertions.assertThat(refreshToken.getExpiresAt()).isEqualTo(expiresAt);
		Assertions.assertThat(refreshToken.isInvalidated()).isFalse();
	}

	@Test
	void test_refreshToken_invalidated() {
		// Given
		String tokenValue = "refresh_token_value";
		Instant issuedAt = Instant.now();
		Instant expiresAt = issuedAt.plusSeconds(86400);

		// When
		OAuth2AuthorizationGrantAuthorization.RefreshToken refreshToken = new OAuth2AuthorizationGrantAuthorization.RefreshToken(
				tokenValue, issuedAt, expiresAt, true);

		// Then
		Assertions.assertThat(refreshToken.isInvalidated()).isTrue();
	}

	@Test
	void test_claimsHolder_with_empty_claims() {
		// Given
		Map<String, Object> claims = new HashMap<>();

		// When
		OAuth2AuthorizationGrantAuthorization.ClaimsHolder claimsHolder = new OAuth2AuthorizationGrantAuthorization.ClaimsHolder(
				claims);

		// Then
		Assertions.assertThat(claimsHolder.claims()).isNotNull().isEmpty();
	}

}
