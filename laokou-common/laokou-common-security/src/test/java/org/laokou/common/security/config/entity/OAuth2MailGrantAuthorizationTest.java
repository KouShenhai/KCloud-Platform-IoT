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
 * OAuth2MailGrantAuthorization测试类.
 *
 * @author laokou
 */
class OAuth2MailGrantAuthorizationTest {

	@Test
	void test_mailGrantAuthorization_creation() {
		// Given
		String id = "auth-1";
		String registeredClientId = "client-1";
		String principalName = "user@example.com";
		Set<String> authorizedScopes = new HashSet<>();
		authorizedScopes.add("read");

		Instant now = Instant.now();
		OAuth2AuthorizationGrantAuthorization.AccessToken accessToken = new OAuth2AuthorizationGrantAuthorization.AccessToken(
				"access_token", now, now.plusSeconds(3600), false, OAuth2AccessToken.TokenType.BEARER, authorizedScopes,
				OAuth2TokenFormat.SELF_CONTAINED,
				new OAuth2AuthorizationGrantAuthorization.ClaimsHolder(new HashMap<>()));

		OAuth2AuthorizationGrantAuthorization.RefreshToken refreshToken = new OAuth2AuthorizationGrantAuthorization.RefreshToken(
				"refresh_token", now, now.plusSeconds(86400), false);

		// When
		OAuth2MailGrantAuthorization authorization = new OAuth2MailGrantAuthorization(id, registeredClientId,
				principalName, authorizedScopes, accessToken, refreshToken, null);

		// Then
		Assertions.assertThat(authorization).isNotNull();
		Assertions.assertThat(authorization.getId()).isEqualTo(id);
		Assertions.assertThat(authorization.getRegisteredClientId()).isEqualTo(registeredClientId);
		Assertions.assertThat(authorization.getPrincipalName()).isEqualTo(principalName);
		Assertions.assertThat(authorization.getAccessToken()).isEqualTo(accessToken);
		Assertions.assertThat(authorization.getRefreshToken()).isEqualTo(refreshToken);
	}

}
