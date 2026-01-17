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
 * OAuth2ClientCredentialsGrantAuthorization测试类.
 *
 * @author laokou
 */
class OAuth2ClientCredentialsGrantAuthorizationTest {

	@Test
	void test_clientCredentialsGrantAuthorization_creation() {
		// Given
		String id = "auth-1";
		String registeredClientId = "client-1";
		String principalName = "service-account";
		Set<String> authorizedScopes = new HashSet<>();
		authorizedScopes.add("read");
		authorizedScopes.add("write");

		Instant now = Instant.now();
		OAuth2AuthorizationGrantAuthorization.AccessToken accessToken = new OAuth2AuthorizationGrantAuthorization.AccessToken(
				"access_token", now, now.plusSeconds(3600), false, OAuth2AccessToken.TokenType.BEARER, authorizedScopes,
				OAuth2TokenFormat.SELF_CONTAINED,
				new OAuth2AuthorizationGrantAuthorization.ClaimsHolder(new HashMap<>()));

		// When
		OAuth2ClientCredentialsGrantAuthorization authorization = new OAuth2ClientCredentialsGrantAuthorization(id,
				registeredClientId, principalName, authorizedScopes, accessToken);

		// Then
		Assertions.assertThat(authorization).isNotNull();
		Assertions.assertThat(authorization.getId()).isEqualTo(id);
		Assertions.assertThat(authorization.getRegisteredClientId()).isEqualTo(registeredClientId);
		Assertions.assertThat(authorization.getPrincipalName()).isEqualTo(principalName);
		Assertions.assertThat(authorization.getAuthorizedScopes()).containsExactlyInAnyOrder("read", "write");
		Assertions.assertThat(authorization.getAccessToken()).isEqualTo(accessToken);
		Assertions.assertThat(authorization.getRefreshToken()).isNull();
	}

	@Test
	void test_clientCredentialsGrantAuthorization_has_no_refreshToken() {
		// Given
		String id = "auth-1";
		String registeredClientId = "client-1";
		String principalName = "service-account";
		Set<String> authorizedScopes = new HashSet<>();

		Instant now = Instant.now();
		OAuth2AuthorizationGrantAuthorization.AccessToken accessToken = new OAuth2AuthorizationGrantAuthorization.AccessToken(
				"access_token", now, now.plusSeconds(3600), false, OAuth2AccessToken.TokenType.BEARER, authorizedScopes,
				OAuth2TokenFormat.REFERENCE, new OAuth2AuthorizationGrantAuthorization.ClaimsHolder(new HashMap<>()));

		// When
		OAuth2ClientCredentialsGrantAuthorization authorization = new OAuth2ClientCredentialsGrantAuthorization(id,
				registeredClientId, principalName, authorizedScopes, accessToken);

		// Then
		Assertions.assertThat(authorization.getRefreshToken()).isNull();
	}

}
