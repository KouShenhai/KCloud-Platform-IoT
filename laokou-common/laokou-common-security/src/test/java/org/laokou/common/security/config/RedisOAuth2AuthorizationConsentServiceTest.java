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

package org.laokou.common.security.config;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.laokou.common.security.config.entity.OAuth2UserConsent;
import org.laokou.common.security.config.repository.OAuth2UserConsentRepository;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;

import java.util.HashSet;
import java.util.Set;

/**
 * RedisOAuth2AuthorizationConsentService测试类.
 *
 * @author laokou
 */
@ExtendWith(MockitoExtension.class)
class RedisOAuth2AuthorizationConsentServiceTest {

	@Mock
	private OAuth2UserConsentRepository userConsentRepository;

	private RedisOAuth2AuthorizationConsentService service;

	@BeforeEach
	void setUp() {
		service = new RedisOAuth2AuthorizationConsentService(userConsentRepository);
	}

	@Test
	void test_constructor_throws_exception_when_repository_is_null() {
		// Then
		Assertions.assertThatThrownBy(() -> new RedisOAuth2AuthorizationConsentService(null))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("UserConsentRepository cannot be null");
	}

	@Test
	void test_save_authorizationConsent() {
		// Given
		OAuth2AuthorizationConsent consent = OAuth2AuthorizationConsent.withId("client-1", "user-1")
			.authority(new SimpleGrantedAuthority("SCOPE_read"))
			.build();

		// When
		service.save(consent);

		// Then
		Mockito.verify(userConsentRepository).save(Mockito.any(OAuth2UserConsent.class));
	}

	@Test
	void test_remove_authorizationConsent() {
		// Given
		OAuth2AuthorizationConsent consent = OAuth2AuthorizationConsent.withId("client-1", "user-1")
			.authority(new SimpleGrantedAuthority("SCOPE_read"))
			.build();

		// When
		service.remove(consent);

		// Then
		Mockito.verify(userConsentRepository).deleteByRegisteredClientIdAndPrincipalName("client-1", "user-1");
	}

	@Test
	void test_findById_returns_null_when_not_found() {
		// Given
		Mockito.when(userConsentRepository.findByRegisteredClientIdAndPrincipalName("client-1", "user-1"))
			.thenReturn(null);

		// When
		OAuth2AuthorizationConsent result = service.findById("client-1", "user-1");

		// Then
		Assertions.assertThat(result).isNull();
	}

	@Test
	void test_findById_returns_consent_when_found() {
		// Given
		Set<org.springframework.security.core.GrantedAuthority> authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("SCOPE_read"));
		OAuth2UserConsent userConsent = new OAuth2UserConsent("client-1:user-1", "client-1", "user-1", authorities);
		Mockito.when(userConsentRepository.findByRegisteredClientIdAndPrincipalName("client-1", "user-1"))
			.thenReturn(userConsent);

		// When
		OAuth2AuthorizationConsent result = service.findById("client-1", "user-1");

		// Then
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result.getRegisteredClientId()).isEqualTo("client-1");
		Assertions.assertThat(result.getPrincipalName()).isEqualTo("user-1");
	}

	@Test
	void test_save_throws_exception_when_consent_is_null() {
		// Then
		Assertions.assertThatThrownBy(() -> service.save(null))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("AuthorizationConsent cannot be null");
	}

	@Test
	void test_findById_throws_exception_when_registeredClientId_is_empty() {
		// Then
		Assertions.assertThatThrownBy(() -> service.findById("", "user-1"))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("RegisteredClientId cannot be empty");
	}

	@Test
	void test_findById_throws_exception_when_principalName_is_empty() {
		// Then
		Assertions.assertThatThrownBy(() -> service.findById("client-1", ""))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("PrincipalName cannot be empty");
	}

}
