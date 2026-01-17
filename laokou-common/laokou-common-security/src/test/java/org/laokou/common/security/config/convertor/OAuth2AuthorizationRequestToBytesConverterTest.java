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
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

/**
 * OAuth2AuthorizationRequestToBytesConverter测试类.
 *
 * @author laokou
 */
class OAuth2AuthorizationRequestToBytesConverterTest {

	private OAuth2AuthorizationRequestToBytesConverter converter;

	@BeforeEach
	void setUp() {
		converter = new OAuth2AuthorizationRequestToBytesConverter();
	}

	@Test
	void test_convert_authorizationRequest_to_bytes() {
		// Given
		OAuth2AuthorizationRequest authorizationRequest = OAuth2AuthorizationRequest.authorizationCode()
			.authorizationUri("https://example.com/oauth2/authorize")
			.clientId("test-client")
			.redirectUri("https://example.com/callback")
			.scope("openid", "profile")
			.state("random-state")
			.build();

		// When
		byte[] result = converter.convert(authorizationRequest);

		// Then
		Assertions.assertThat(result).isNotNull().isNotEmpty();
	}

	@Test
	void test_convert_minimal_authorizationRequest_to_bytes() {
		// Given
		OAuth2AuthorizationRequest authorizationRequest = OAuth2AuthorizationRequest.authorizationCode()
			.authorizationUri("https://example.com/oauth2/authorize")
			.clientId("test-client")
			.build();

		// When
		byte[] result = converter.convert(authorizationRequest);

		// Then
		Assertions.assertThat(result).isNotNull().isNotEmpty();
	}

}
