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

package org.laokou.common.security.handler;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

/**
 * OAuth2ExceptionHandler测试类.
 *
 * @author laokou
 */
class OAuth2ExceptionHandlerTest {

	@Test
	void test_error_url_constant() {
		// Then
		Assertions.assertThat(OAuth2ExceptionHandler.ERROR_URL)
			.isEqualTo("https://datatracker.ietf.org/doc/html/rfc6749#section-5.2");
	}

	@Test
	void test_getOAuth2AuthenticationException() {
		// Given
		String code = "invalid_token";
		String message = "Token expired";
		String uri = "https://example.com/error";

		// When
		OAuth2AuthenticationException exception = OAuth2ExceptionHandler.getOAuth2AuthenticationException(code, message,
				uri);

		// Then
		Assertions.assertThat(exception).isNotNull();
		Assertions.assertThat(exception.getError()).isNotNull();
		Assertions.assertThat(exception.getError().getErrorCode()).isEqualTo(code);
		Assertions.assertThat(exception.getError().getDescription()).isEqualTo(message);
		Assertions.assertThat(exception.getError().getUri()).isEqualTo(uri);
	}

	@Test
	void test_getOAuth2AuthenticationException_with_null_message() {
		// Given
		String code = "invalid_token";
		String uri = "https://example.com/error";

		// When
		OAuth2AuthenticationException exception = OAuth2ExceptionHandler.getOAuth2AuthenticationException(code, null,
				uri);

		// Then
		Assertions.assertThat(exception).isNotNull();
		Assertions.assertThat(exception.getError().getErrorCode()).isEqualTo(code);
		Assertions.assertThat(exception.getError().getDescription()).isNull();
	}

	@Test
	void test_getOAuth2AuthenticationException_with_different_uri() {
		// Given
		String code = "access_denied";
		String message = "Access denied";
		String customUri = "https://custom.com/error";

		// When
		OAuth2AuthenticationException exception = OAuth2ExceptionHandler.getOAuth2AuthenticationException(code, message,
				customUri);

		// Then
		Assertions.assertThat(exception.getError().getUri()).isEqualTo(customUri);
	}

}
