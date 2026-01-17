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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;

/**
 * OAuth2UserConsent测试类.
 *
 * @author laokou
 */
class OAuth2UserConsentTest {

	@Test
	void test_userConsent_creation() {
		// Given
		String id = "client-1-user-1";
		String registeredClientId = "client-1";
		String principalName = "user-1";
		Set<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("SCOPE_read"));
		authorities.add(new SimpleGrantedAuthority("SCOPE_write"));

		// When
		OAuth2UserConsent userConsent = new OAuth2UserConsent(id, registeredClientId, principalName, authorities);

		// Then
		Assertions.assertThat(userConsent).isNotNull();
		Assertions.assertThat(userConsent.getId()).isEqualTo(id);
		Assertions.assertThat(userConsent.getRegisteredClientId()).isEqualTo(registeredClientId);
		Assertions.assertThat(userConsent.getPrincipalName()).isEqualTo(principalName);
		Assertions.assertThat(userConsent.getAuthorities()).hasSize(2);
	}

	@Test
	void test_userConsent_with_empty_authorities() {
		// Given
		String id = "client-1-user-1";
		String registeredClientId = "client-1";
		String principalName = "user-1";
		Set<GrantedAuthority> authorities = new HashSet<>();

		// When
		OAuth2UserConsent userConsent = new OAuth2UserConsent(id, registeredClientId, principalName, authorities);

		// Then
		Assertions.assertThat(userConsent.getAuthorities()).isNotNull().isEmpty();
	}

	@Test
	void test_userConsent_authorities_contains_expected_values() {
		// Given
		String id = "client-1-user-1";
		String registeredClientId = "client-1";
		String principalName = "user-1";
		Set<GrantedAuthority> authorities = new HashSet<>();
		SimpleGrantedAuthority readAuthority = new SimpleGrantedAuthority("SCOPE_read");
		authorities.add(readAuthority);

		// When
		OAuth2UserConsent userConsent = new OAuth2UserConsent(id, registeredClientId, principalName, authorities);

		// Then
		Assertions.assertThat(userConsent.getAuthorities()).contains(readAuthority);
	}

}
