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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User record test class.
 *
 * @author laokou
 */
@DisplayName("User Record Unit Tests")
class UserTest {

	private User user;

	@BeforeEach
	void setUp() {
		List<String> permissions = new ArrayList<>();
		permissions.add("sys:user:query");
		permissions.add("sys:user:add");
		permissions.add("sys:role:query");

		user = User.builder()
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
			.permissions(permissions)
			.build();
	}

	@Test
	@DisplayName("Test builder creates User with all properties")
	void test_builder_allProperties_createsUser() {
		// Then
		Assertions.assertThat(user.id()).isEqualTo(1L);
		Assertions.assertThat(user.username()).isEqualTo("admin");
		Assertions.assertThat(user.password()).isEqualTo("password123");
		Assertions.assertThat(user.avatar()).isEqualTo("https://example.com/avatar.png");
		Assertions.assertThat(user.superAdmin()).isTrue();
		Assertions.assertThat(user.status()).isZero();
		Assertions.assertThat(user.mail()).isEqualTo("admin@example.com");
		Assertions.assertThat(user.mobile()).isEqualTo("13800138000");
		Assertions.assertThat(user.tenantId()).isEqualTo(100L);
		Assertions.assertThat(user.deptId()).isEqualTo(10L);
		Assertions.assertThat(user.permissions())
			.hasSize(3)
			.containsExactlyInAnyOrder("sys:user:query", "sys:user:add", "sys:role:query");
	}

	@Test
	@DisplayName("Test getName with valid username returns username")
	void test_getName_withValidUsername_returnsUsername() {
		// When
		String name = user.getName();

		// Then
		Assertions.assertThat(name).isEqualTo("admin");
	}

	@Test
	@DisplayName("Test getAuthorities with valid permissions returns correct authorities")
	void test_getAuthorities_withValidPermissions_returnsAuthorities() {
		// When
		Collection<GrantedAuthority> authorities = user.getAuthorities();

		// Then
		Assertions.assertThat(authorities)
			.isNotNull()
			.hasSize(3)
			.extracting(GrantedAuthority::getAuthority)
			.containsExactlyInAnyOrder("sys:user:query", "sys:user:add", "sys:role:query");
	}

	@Test
	@DisplayName("Test getCredentials with valid user returns username")
	void test_getCredentials_withValidUser_returnsUsername() {
		// When
		Object credentials = user.getCredentials();

		// Then
		Assertions.assertThat(credentials).isEqualTo("admin");
	}

	@Test
	@DisplayName("Test getDetails with valid user returns username")
	void test_getDetails_withValidUser_returnsUsername() {
		// When
		Object details = user.getDetails();

		// Then
		Assertions.assertThat(details).isEqualTo("admin");
	}

	@Test
	@DisplayName("Test getPrincipal with valid user returns username")
	void test_getPrincipal_withValidUser_returnsUsername() {
		// When
		Object principal = user.getPrincipal();

		// Then
		Assertions.assertThat(principal).isEqualTo("admin");
	}

	@Test
	@DisplayName("Test isAuthenticated always returns true")
	void test_isAuthenticated_always_returnsTrue() {
		// When
		boolean authenticated = user.isAuthenticated();

		// Then
		Assertions.assertThat(authenticated).isTrue();
	}

	@Test
	@DisplayName("Test setAuthenticated always throws UnsupportedOperationException")
	void test_setAuthenticated_always_throwsUnsupportedOperationException() {
		// When & Then
		Assertions.assertThatThrownBy(() -> user.setAuthenticated(false))
			.isInstanceOf(UnsupportedOperationException.class)
			.hasMessage("Cannot change authentication state");
	}

	@Test
	@DisplayName("Test builder with null values creates User with nulls")
	void test_builder_nullValues_createsUserWithNulls() {
		// Given
		User userWithNulls = User.builder()
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
			.permissions(null)
			.build();

		// Then
		Assertions.assertThat(userWithNulls.id()).isEqualTo(2L);
		Assertions.assertThat(userWithNulls.username()).isEqualTo("testuser");
		Assertions.assertThat(userWithNulls.password()).isNull();
		Assertions.assertThat(userWithNulls.avatar()).isNull();
		Assertions.assertThat(userWithNulls.superAdmin()).isFalse();
		Assertions.assertThat(userWithNulls.status()).isEqualTo(1);
		Assertions.assertThat(userWithNulls.mail()).isNull();
		Assertions.assertThat(userWithNulls.mobile()).isNull();
		Assertions.assertThat(userWithNulls.tenantId()).isNull();
		Assertions.assertThat(userWithNulls.deptId()).isNull();
		Assertions.assertThat(userWithNulls.permissions()).isNull();
	}

	@Test
	@DisplayName("Test getAuthorities with empty permissions returns empty collection")
	void test_getAuthorities_emptyPermissions_returnsEmptyCollection() {
		// Given
		User userWithEmptyPerms = User.builder().id(3L).username("user3").permissions(new ArrayList<>()).build();

		// When
		Collection<GrantedAuthority> authorities = userWithEmptyPerms.getAuthorities();

		// Then
		Assertions.assertThat(authorities).isNotNull().isEmpty();
	}

	@Test
	@DisplayName("Test class type is Authentication implementation")
	void test_classType_always_implementsAuthentication() {
		// Then
		Assertions.assertThat(user).isInstanceOf(org.springframework.security.core.Authentication.class);
	}

}
