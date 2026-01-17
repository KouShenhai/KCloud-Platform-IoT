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

/**
 * OAuth2UserConsentRepository test class.
 *
 * @author laokou
 */
class OAuth2UserConsentRepositoryTest {

	@Test
	void test_repository_has_findByRegisteredClientIdAndPrincipalName_method() throws NoSuchMethodException {
		// When
		var method = OAuth2UserConsentRepository.class.getMethod("findByRegisteredClientIdAndPrincipalName",
				String.class, String.class);

		// Then
		Assertions.assertThat(method).isNotNull();
		Assertions.assertThat(method.getReturnType().getSimpleName()).isEqualTo("OAuth2UserConsent");
	}

	@Test
	void test_repository_has_deleteByRegisteredClientIdAndPrincipalName_method() throws NoSuchMethodException {
		// When
		var method = OAuth2UserConsentRepository.class.getMethod("deleteByRegisteredClientIdAndPrincipalName",
				String.class, String.class);

		// Then
		Assertions.assertThat(method).isNotNull();
	}

}
