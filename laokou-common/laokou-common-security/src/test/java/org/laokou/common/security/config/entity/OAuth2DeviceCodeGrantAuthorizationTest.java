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
 * OAuth2DeviceCodeGrantAuthorization测试类.
 *
 * @author laokou
 */
class OAuth2DeviceCodeGrantAuthorizationTest {

	@Test
	void test_deviceCode_creation() {
		// Given
		String tokenValue = "device_code_value";
		Instant issuedAt = Instant.now();
		Instant expiresAt = issuedAt.plusSeconds(600);

		// When
		OAuth2DeviceCodeGrantAuthorization.DeviceCode deviceCode = new OAuth2DeviceCodeGrantAuthorization.DeviceCode(
				tokenValue, issuedAt, expiresAt, false);

		// Then
		Assertions.assertThat(deviceCode).isNotNull();
		Assertions.assertThat(deviceCode.getTokenValue()).isEqualTo(tokenValue);
		Assertions.assertThat(deviceCode.getIssuedAt()).isEqualTo(issuedAt);
		Assertions.assertThat(deviceCode.getExpiresAt()).isEqualTo(expiresAt);
		Assertions.assertThat(deviceCode.isInvalidated()).isFalse();
	}

	@Test
	void test_userCode_creation() {
		// Given
		String tokenValue = "user_code_value";
		Instant issuedAt = Instant.now();
		Instant expiresAt = issuedAt.plusSeconds(600);

		// When
		OAuth2DeviceCodeGrantAuthorization.UserCode userCode = new OAuth2DeviceCodeGrantAuthorization.UserCode(
				tokenValue, issuedAt, expiresAt, false);

		// Then
		Assertions.assertThat(userCode).isNotNull();
		Assertions.assertThat(userCode.getTokenValue()).isEqualTo(tokenValue);
		Assertions.assertThat(userCode.getIssuedAt()).isEqualTo(issuedAt);
		Assertions.assertThat(userCode.getExpiresAt()).isEqualTo(expiresAt);
		Assertions.assertThat(userCode.isInvalidated()).isFalse();
	}

	@Test
	void test_deviceCodeGrantAuthorization_creation() {
		// Given
		String id = "auth-1";
		String registeredClientId = "client-1";
		String principalName = "user123";
		String deviceState = "device-state";
		Set<String> authorizedScopes = new HashSet<>();
		authorizedScopes.add("openid");
		Set<String> requestedScopes = new HashSet<>();
		requestedScopes.add("openid");
		requestedScopes.add("profile");

		Instant now = Instant.now();
		OAuth2AuthorizationGrantAuthorization.AccessToken accessToken = new OAuth2AuthorizationGrantAuthorization.AccessToken(
				"access_token", now, now.plusSeconds(3600), false, OAuth2AccessToken.TokenType.BEARER, authorizedScopes,
				OAuth2TokenFormat.SELF_CONTAINED,
				new OAuth2AuthorizationGrantAuthorization.ClaimsHolder(new HashMap<>()));

		OAuth2AuthorizationGrantAuthorization.RefreshToken refreshToken = new OAuth2AuthorizationGrantAuthorization.RefreshToken(
				"refresh_token", now, now.plusSeconds(86400), false);

		OAuth2DeviceCodeGrantAuthorization.DeviceCode deviceCode = new OAuth2DeviceCodeGrantAuthorization.DeviceCode(
				"device_code", now, now.plusSeconds(600), false);

		OAuth2DeviceCodeGrantAuthorization.UserCode userCode = new OAuth2DeviceCodeGrantAuthorization.UserCode(
				"user_code", now, now.plusSeconds(600), false);

		// When
		OAuth2DeviceCodeGrantAuthorization authorization = new OAuth2DeviceCodeGrantAuthorization(id,
				registeredClientId, principalName, authorizedScopes, accessToken, refreshToken, null, deviceCode,
				userCode, requestedScopes, deviceState);

		// Then
		Assertions.assertThat(authorization).isNotNull();
		Assertions.assertThat(authorization.getId()).isEqualTo(id);
		Assertions.assertThat(authorization.getRegisteredClientId()).isEqualTo(registeredClientId);
		Assertions.assertThat(authorization.getPrincipalName()).isEqualTo(principalName);
		Assertions.assertThat(authorization.getDeviceCode()).isEqualTo(deviceCode);
		Assertions.assertThat(authorization.getUserCode()).isEqualTo(userCode);
		Assertions.assertThat(authorization.getRequestedScopes()).containsExactlyInAnyOrder("openid", "profile");
		Assertions.assertThat(authorization.getDeviceState()).isEqualTo(deviceState);
	}

	@Test
	void test_deviceCode_invalidated() {
		// Given
		Instant now = Instant.now();

		// When
		OAuth2DeviceCodeGrantAuthorization.DeviceCode deviceCode = new OAuth2DeviceCodeGrantAuthorization.DeviceCode(
				"device_code", now, now.plusSeconds(600), true);

		// Then
		Assertions.assertThat(deviceCode.isInvalidated()).isTrue();
	}

}
