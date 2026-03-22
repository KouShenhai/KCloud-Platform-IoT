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

package org.laokou.common.context.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * User record test class.
 *
 * @author laokou
 */
@DisplayName("User Record Unit Tests")
class UserExtDetailsTest {

	private UserExtDetails userExtDetails;

	@BeforeEach
	void setUp() {
		Set<String> permissions = Set.of("sys:user:query", "sys:user:add", "sys:role:query");

		userExtDetails = UserExtDetails.builder()
			.id(1L)
			.username("admin")
			.password("password123")
			.avatar("https://example.com/avatar.png")
			.superAdmin(true)
			.status(0)
			.mail("admin@example.com")
			.mobile("13800138000")
			.tenantId(100L)
			.deptId(10L)
			.deptIds(Set.of(1L))
			.creator(1L)
			.permissions(permissions)
			.build();
	}

	@Test
	@DisplayName("Test builder creates User with all properties")
	void test_builder_allProperties_createsUser() {
		// Then
		Assertions.assertThat(userExtDetails.id()).isEqualTo(1L);
		Assertions.assertThat(userExtDetails.username()).isEqualTo("admin");
		Assertions.assertThat(userExtDetails.password()).isEqualTo("password123");
		Assertions.assertThat(userExtDetails.avatar()).isEqualTo("https://example.com/avatar.png");
		Assertions.assertThat(userExtDetails.superAdmin()).isTrue();
		Assertions.assertThat(userExtDetails.status()).isZero();
		Assertions.assertThat(userExtDetails.mail()).isEqualTo("admin@example.com");
		Assertions.assertThat(userExtDetails.mobile()).isEqualTo("13800138000");
		Assertions.assertThat(userExtDetails.tenantId()).isEqualTo(100L);
		Assertions.assertThat(userExtDetails.deptId()).isEqualTo(10L);
		Assertions.assertThat(userExtDetails.deptIds()).isEqualTo(Set.of(1L));
		Assertions.assertThat(userExtDetails.creator()).isEqualTo(1L);
		Assertions.assertThat(userExtDetails.permissions())
			.hasSize(3)
			.containsExactlyInAnyOrder("sys:user:query", "sys:user:add", "sys:role:query");
	}

	@Test
	@DisplayName("Test getName with valid username returns username")
	void test_getName_withValidUsername_returnsUsername() {
		// When
		String name = userExtDetails.getName();

		// Then
		Assertions.assertThat(name).isEqualTo("admin");
	}

	@Test
	@DisplayName("Test getAuthorities with valid permissions returns correct authorities")
	void test_getAuthorities_withValidPermissions_returnsAuthorities() {
		// When
		Collection<GrantedAuthority> authorities = userExtDetails.getAuthorities();

		// Then
		Assertions.assertThat(authorities).isNotNull().hasSize(0);
	}

	@Test
	@DisplayName("Test getCredentials with valid user returns username")
	void test_getCredentials_withValidUser_returnsUsername() {
		// When
		Object credentials = userExtDetails.getCredentials();

		// Then
		Assertions.assertThat(credentials).isEqualTo("admin");
	}

	@Test
	@DisplayName("Test getDetails with valid user returns username")
	void test_getDetails_withValidUser_returnsUsername() {
		// When
		Object details = userExtDetails.getDetails();

		// Then
		Assertions.assertThat(details).isEqualTo("admin");
	}

	@Test
	@DisplayName("Test getPrincipal with valid user returns username")
	void test_getPrincipal_withValidUser_returnsUsername() {
		// When
		Object principal = userExtDetails.getPrincipal();

		// Then
		Assertions.assertThat(principal).isEqualTo("admin");
	}

	@Test
	@DisplayName("Test isAuthenticated always returns true")
	void test_isAuthenticated_always_returnsTrue() {
		// When
		boolean authenticated = userExtDetails.isAuthenticated();

		// Then
		Assertions.assertThat(authenticated).isTrue();
	}

	@Test
	@DisplayName("Test setAuthenticated always throws UnsupportedOperationException")
	void test_setAuthenticated_always_throwsUnsupportedOperationException() {
		// When & Then
		Assertions.assertThatThrownBy(() -> userExtDetails.setAuthenticated(false))
			.isInstanceOf(UnsupportedOperationException.class)
			.hasMessage("Cannot change authentication state");
	}

	@Test
	@DisplayName("Test builder with null values creates User with nulls")
	void test_builder_nullValues_createsUserWithNulls() {
		// Given
		UserExtDetails userExtDetailsWithNulls = UserExtDetails.builder()
			.id(2L)
			.username("testuser")
			.password(null)
			.avatar(null)
			.superAdmin(false)
			.status(1)
			.mail(null)
			.mobile(null)
			.tenantId(null)
			.deptId(null)
			.deptIds(null)
			.creator(null)
			.permissions(null)
			.build();

		// Then
		Assertions.assertThat(userExtDetailsWithNulls.id()).isEqualTo(2L);
		Assertions.assertThat(userExtDetailsWithNulls.username()).isEqualTo("testuser");
		Assertions.assertThat(userExtDetailsWithNulls.password()).isNull();
		Assertions.assertThat(userExtDetailsWithNulls.avatar()).isNull();
		Assertions.assertThat(userExtDetailsWithNulls.superAdmin()).isFalse();
		Assertions.assertThat(userExtDetailsWithNulls.status()).isEqualTo(1);
		Assertions.assertThat(userExtDetailsWithNulls.mail()).isNull();
		Assertions.assertThat(userExtDetailsWithNulls.mobile()).isNull();
		Assertions.assertThat(userExtDetailsWithNulls.tenantId()).isNull();
		Assertions.assertThat(userExtDetailsWithNulls.deptId()).isNull();
		Assertions.assertThat(userExtDetailsWithNulls.permissions()).isNull();
		Assertions.assertThat(userExtDetailsWithNulls.deptIds()).isNull();
		Assertions.assertThat(userExtDetailsWithNulls.creator()).isNull();
	}

	@Test
	@DisplayName("Test getAuthorities with empty permissions returns empty collection")
	void test_getAuthorities_emptyPermissions_returnsEmptyCollection() {
		// Given
		UserExtDetails userExtDetailsWithEmptyPerms = UserExtDetails.builder()
			.id(3L)
			.username("user3")
			.permissions(new HashSet<>())
			.build();

		// When
		Collection<GrantedAuthority> authorities = userExtDetailsWithEmptyPerms.getAuthorities();

		// Then
		Assertions.assertThat(authorities).isNotNull().isEmpty();
	}

	@Test
	@DisplayName("Test class type is Authentication implementation")
	void test_classType_always_implementsAuthentication() {
		// Then
		Assertions.assertThat(userExtDetails).isInstanceOf(org.springframework.security.core.Authentication.class);
	}

}
