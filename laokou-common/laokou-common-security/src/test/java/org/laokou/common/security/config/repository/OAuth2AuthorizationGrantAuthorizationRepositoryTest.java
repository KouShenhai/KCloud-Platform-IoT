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

package org.laokou.common.security.config.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

/**
 * OAuth2AuthorizationGrantAuthorizationRepository test class.
 *
 * @author laokou
 */
class OAuth2AuthorizationGrantAuthorizationRepositoryTest {

	@Test
	void test_repository_has_findByState_method() throws NoSuchMethodException {
		// When
		Method method = OAuth2AuthorizationGrantAuthorizationRepository.class.getMethod("findByState", String.class);

		// Then
		Assertions.assertThat(method).isNotNull();
	}

	@Test
	void test_repository_has_findByAccessToken_TokenValue_method() throws NoSuchMethodException {
		// When
		Method method = OAuth2AuthorizationGrantAuthorizationRepository.class.getMethod("findByAccessToken_TokenValue",
				String.class);

		// Then
		Assertions.assertThat(method).isNotNull();
	}

	@Test
	void test_repository_has_findByRefreshToken_TokenValue_method() throws NoSuchMethodException {
		// When
		Method method = OAuth2AuthorizationGrantAuthorizationRepository.class.getMethod("findByRefreshToken_TokenValue",
				String.class);

		// Then
		Assertions.assertThat(method).isNotNull();
	}

	@Test
	void test_repository_has_findByIdToken_TokenValue_method() throws NoSuchMethodException {
		// When
		Method method = OAuth2AuthorizationGrantAuthorizationRepository.class.getMethod("findByIdToken_TokenValue",
				String.class);

		// Then
		Assertions.assertThat(method).isNotNull();
	}

	@Test
	void test_repository_has_findByDeviceState_method() throws NoSuchMethodException {
		// When
		Method method = OAuth2AuthorizationGrantAuthorizationRepository.class.getMethod("findByDeviceState",
				String.class);

		// Then
		Assertions.assertThat(method).isNotNull();
	}

	@Test
	void test_repository_has_findByDeviceCode_TokenValue_method() throws NoSuchMethodException {
		// When
		Method method = OAuth2AuthorizationGrantAuthorizationRepository.class.getMethod("findByDeviceCode_TokenValue",
				String.class);

		// Then
		Assertions.assertThat(method).isNotNull();
	}

	@Test
	void test_repository_has_findByUserCode_TokenValue_method() throws NoSuchMethodException {
		// When
		Method method = OAuth2AuthorizationGrantAuthorizationRepository.class.getMethod("findByUserCode_TokenValue",
				String.class);

		// Then
		Assertions.assertThat(method).isNotNull();
	}

	@Test
	void test_repository_has_findByAuthorizationCode_TokenValue_method() throws NoSuchMethodException {
		// When
		Method method = OAuth2AuthorizationGrantAuthorizationRepository.class
			.getMethod("findByAuthorizationCode_TokenValue", String.class);

		// Then
		Assertions.assertThat(method).isNotNull();
	}

}
