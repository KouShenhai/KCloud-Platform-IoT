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

package org.laokou.common.security.config.convertor;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laokou.common.security.config.entity.OAuth2AuthorizationGrantAuthorization;

import java.util.HashMap;
import java.util.Map;

/**
 * BytesToClaimsHolderConverter测试类.
 *
 * @author laokou
 */
class BytesToClaimsHolderConverterTest {

	private BytesToClaimsHolderConverter converter;

	private ClaimsHolderToBytesConverter toByteConverter;

	@BeforeEach
	void setUp() {
		converter = new BytesToClaimsHolderConverter();
		toByteConverter = new ClaimsHolderToBytesConverter();
	}

	@Test
	void test_convert_bytes_to_claimsHolder() {
		// Given
		Map<String, Object> claims = new HashMap<>();
		claims.put("sub", "user123");
		claims.put("iss", "https://issuer.com");
		OAuth2AuthorizationGrantAuthorization.ClaimsHolder originalClaimsHolder = new OAuth2AuthorizationGrantAuthorization.ClaimsHolder(
				claims);
		byte[] bytes = toByteConverter.convert(originalClaimsHolder);

		// When
		OAuth2AuthorizationGrantAuthorization.ClaimsHolder result = converter.convert(bytes);

		// Then
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result.claims()).isNotNull();
		Assertions.assertThat(result.claims().get("sub")).isEqualTo("user123");
		Assertions.assertThat(result.claims().get("iss")).isEqualTo("https://issuer.com");
	}

	@Test
	void test_convert_empty_claims_roundtrip() {
		// Given
		Map<String, Object> claims = new HashMap<>();
		OAuth2AuthorizationGrantAuthorization.ClaimsHolder originalClaimsHolder = new OAuth2AuthorizationGrantAuthorization.ClaimsHolder(
				claims);
		byte[] bytes = toByteConverter.convert(originalClaimsHolder);

		// When
		OAuth2AuthorizationGrantAuthorization.ClaimsHolder result = converter.convert(bytes);

		// Then
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result.claims()).isNotNull();
	}

}
