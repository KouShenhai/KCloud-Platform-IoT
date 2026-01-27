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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * UserUtils test class.
 *
 * @author laokou
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserUtils Unit Tests")
class UserUtilsTest {

	private UserExtDetails mockUserExtDetails;

	private MockedStatic<DomainFactory> domainFactoryMockedStatic;

	@BeforeEach
	void setUp() {
		List<String> permissions = new ArrayList<>();
		permissions.add("sys:user:query");

		mockUserExtDetails = UserExtDetails.builder()
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

		// Mock DomainFactory
		domainFactoryMockedStatic = Mockito.mockStatic(DomainFactory.class);
		domainFactoryMockedStatic.when(DomainFactory::createUserDetails).thenReturn(mockUserExtDetails);
	}

	@AfterEach
	void tearDown() {
		SecurityContextHolder.clearContext();
		if (domainFactoryMockedStatic != null) {
			domainFactoryMockedStatic.close();
		}
	}

	@Test
	@DisplayName("Test userDetail returns UserExtDetails when principal is UserExtDetails")
	void test_userDetail_returns_userExtDetails_when_principal_is_userExtDetails() {
		// Given
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		Authentication auth = new UsernamePasswordAuthenticationToken(mockUserExtDetails, null);
		context.setAuthentication(auth);
		SecurityContextHolder.setContext(context);

		// When
		UserExtDetails result = UserUtils.userDetail();

		// Then
		Assertions.assertThat(result).isEqualTo(mockUserExtDetails);
		Assertions.assertThat(result.getId()).isEqualTo(1L);
		Assertions.assertThat(result.getUsername()).isEqualTo("admin");
	}

	@Test
	@DisplayName("Test userDetail returns DomainFactory user when principal is not UserExtDetails")
	void test_userDetail_returns_domainFactory_user_when_principal_is_not_userExtDetails() {
		// Given
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		Authentication auth = new UsernamePasswordAuthenticationToken("simpleUser", "password");
		context.setAuthentication(auth);
		SecurityContextHolder.setContext(context);

		// When
		UserExtDetails result = UserUtils.userDetail();

		// Then
		Assertions.assertThat(result).isEqualTo(mockUserExtDetails);
	}

	@Test
	@DisplayName("Test userDetail returns DomainFactory user when authentication is null")
	void test_userDetail_returns_domainFactory_user_when_authentication_is_null() {
		// Given
		SecurityContextHolder.clearContext();

		// When
		UserExtDetails result = UserUtils.userDetail();

		// Then
		Assertions.assertThat(result).isEqualTo(mockUserExtDetails);
	}

	@Test
	@DisplayName("Test getUserId returns correct user id")
	void test_getUserId_returns_correct_id() {
		// Given
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		Authentication auth = new UsernamePasswordAuthenticationToken(mockUserExtDetails, null);
		context.setAuthentication(auth);
		SecurityContextHolder.setContext(context);

		// When
		Long userId = UserUtils.getUserId();

		// Then
		Assertions.assertThat(userId).isEqualTo(1L);
	}

	@Test
	@DisplayName("Test getUserName returns correct username")
	void test_getUserName_returns_correct_username() {
		// Given
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		Authentication auth = new UsernamePasswordAuthenticationToken(mockUserExtDetails, null);
		context.setAuthentication(auth);
		SecurityContextHolder.setContext(context);

		// When
		String userName = UserUtils.getUserName();

		// Then
		Assertions.assertThat(userName).isEqualTo("admin");
	}

	@Test
	@DisplayName("Test getTenantId returns correct tenant id")
	void test_getTenantId_returns_correct_tenantId() {
		// Given
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		Authentication auth = new UsernamePasswordAuthenticationToken(mockUserExtDetails, null);
		context.setAuthentication(auth);
		SecurityContextHolder.setContext(context);

		// When
		Long tenantId = UserUtils.getTenantId();

		// Then
		Assertions.assertThat(tenantId).isEqualTo(100L);
	}

	@Test
	@DisplayName("Test getDeptId returns correct department id")
	void test_getDeptId_returns_correct_deptId() {
		// Given
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		Authentication auth = new UsernamePasswordAuthenticationToken(mockUserExtDetails, null);
		context.setAuthentication(auth);
		SecurityContextHolder.setContext(context);

		// When
		Long deptId = UserUtils.getDeptId();

		// Then
		Assertions.assertThat(deptId).isEqualTo(10L);
	}

	@Test
	@DisplayName("Test isSuperAdmin returns correct super admin status")
	void test_isSuperAdmin_returns_correct_status() {
		// Given
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		Authentication auth = new UsernamePasswordAuthenticationToken(mockUserExtDetails, null);
		context.setAuthentication(auth);
		SecurityContextHolder.setContext(context);

		// When
		Boolean isSuperAdmin = UserUtils.isSuperAdmin();

		// Then
		Assertions.assertThat(isSuperAdmin).isTrue();
	}

	@Test
	@DisplayName("Test isSuperAdmin returns false for non-super admin user")
	void test_isSuperAdmin_returns_false_for_non_super_admin() {
		// Given
		UserExtDetails nonSuperAdmin = UserExtDetails.builder()
			.id(2L)
			.username("normalUser")
			.superAdmin(false)
			.tenantId(200L)
			.deptId(20L)
			.build();

		domainFactoryMockedStatic.when(DomainFactory::createUserDetails).thenReturn(nonSuperAdmin);

		SecurityContext context = SecurityContextHolder.createEmptyContext();
		Authentication auth = new UsernamePasswordAuthenticationToken(nonSuperAdmin, null);
		context.setAuthentication(auth);
		SecurityContextHolder.setContext(context);

		// When
		Boolean isSuperAdmin = UserUtils.isSuperAdmin();

		// Then
		Assertions.assertThat(isSuperAdmin).isFalse();
	}

}
