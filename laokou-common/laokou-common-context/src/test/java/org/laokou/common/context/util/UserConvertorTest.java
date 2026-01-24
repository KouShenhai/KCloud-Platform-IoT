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
import org.laokou.common.crypto.util.AESUtils;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Set;

/**
 * UserConvertor test class.
 *
 * @author laokou
 */
@DisplayName("UserConvertor Unit Tests")
class UserConvertorTest {

	private MockedStatic<DomainFactory> domainFactoryMockedStatic;

	@BeforeEach
	void setUp() {
		// Create a template UserExtDetails that will be used by DomainFactory
		UserExtDetails templateUserExtDetails = UserExtDetails.builder().build();

		domainFactoryMockedStatic = Mockito.mockStatic(DomainFactory.class);
		domainFactoryMockedStatic.when(DomainFactory::createUserDetails).thenReturn(templateUserExtDetails);
	}

	@AfterEach
	void tearDown() {
		if (domainFactoryMockedStatic != null) {
			domainFactoryMockedStatic.close();
		}
	}

	@Test
	@DisplayName("Test toUserDetails converts User to UserExtDetails with all properties")
	void test_toUserDetails_converts_all_properties() throws Exception {
		// Given
		Set<String> permissions = new HashSet<>();
		permissions.add("sys:user:query");
		permissions.add("sys:user:add");

		String encryptedUsername = AESUtils.encrypt("admin");
		String encryptedMail = AESUtils.encrypt("admin@example.com");
		String encryptedMobile = AESUtils.encrypt("13800138000");

		User user = User.builder()
			.id(1L)
			.username(encryptedUsername)
			.password("password123")
			.avatar("https://example.com/avatar.png")
			.superAdmin(true)
			.status(0)
			.mail(encryptedMail)
			.mobile(encryptedMobile)
			.tenantId(100L)
			.deptId(10L)
			.permissions(permissions)
			.build();

		// When
		UserExtDetails result = UserConvertor.toUserDetails(user);

		// Then
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result.getId()).isEqualTo(1L);
		Assertions.assertThat(result.getUsername()).isEqualTo("admin");
		Assertions.assertThat(result.getPassword()).isEqualTo("password123");
		Assertions.assertThat(result.getAvatar()).isEqualTo("https://example.com/avatar.png");
		Assertions.assertThat(result.getSuperAdmin()).isTrue();
		Assertions.assertThat(result.getStatus()).isZero();
		Assertions.assertThat(result.getMail()).isEqualTo("admin@example.com");
		Assertions.assertThat(result.getMobile()).isEqualTo("13800138000");
		Assertions.assertThat(result.getTenantId()).isEqualTo(100L);
		Assertions.assertThat(result.getDeptId()).isEqualTo(10L);
		Assertions.assertThat(result.getPermissions())
			.hasSize(2)
			.containsExactlyInAnyOrder("sys:user:query", "sys:user:add");
	}

	@Test
	@DisplayName("Test toUserDetails decrypts username, mail, and mobile")
	void test_toUserDetails_decrypts_sensitive_fields() throws Exception {
		// Given
		String originalUsername = "testuser";
		String originalMail = "test@example.com";
		String originalMobile = "13900139000";

		String encryptedUsername = AESUtils.encrypt(originalUsername);
		String encryptedMail = AESUtils.encrypt(originalMail);
		String encryptedMobile = AESUtils.encrypt(originalMobile);

		User user = User.builder()
			.id(2L)
			.username(encryptedUsername)
			.mail(encryptedMail)
			.mobile(encryptedMobile)
			.build();

		// When
		UserExtDetails result = UserConvertor.toUserDetails(user);

		// Then
		Assertions.assertThat(result.getUsername()).isEqualTo(originalUsername);
		Assertions.assertThat(result.getMail()).isEqualTo(originalMail);
		Assertions.assertThat(result.getMobile()).isEqualTo(originalMobile);
	}

	@Test
	@DisplayName("Test toUserDetails with null optional fields")
	void test_toUserDetails_with_null_optional_fields() {
		// Given
		User user = User.builder()
			.id(3L)
			.username(null)
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

		// When
		UserExtDetails result = UserConvertor.toUserDetails(user);

		// Then
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result.getId()).isEqualTo(3L);
		Assertions.assertThat(result.getUsername()).isNull();
		Assertions.assertThat(result.getPassword()).isNull();
		Assertions.assertThat(result.getAvatar()).isNull();
		Assertions.assertThat(result.getSuperAdmin()).isFalse();
		Assertions.assertThat(result.getStatus()).isEqualTo(1);
		Assertions.assertThat(result.getMail()).isNull();
		Assertions.assertThat(result.getMobile()).isNull();
		Assertions.assertThat(result.getTenantId()).isNull();
		Assertions.assertThat(result.getDeptId()).isNull();
		Assertions.assertThat(result.getPermissions()).isNull();
	}

	@Test
	@DisplayName("Test toUserDetails with empty permissions set")
	void test_toUserDetails_with_empty_permissions() throws Exception {
		// Given
		User user = User.builder().id(4L).username(AESUtils.encrypt("user4")).permissions(new HashSet<>()).build();

		// When
		UserExtDetails result = UserConvertor.toUserDetails(user);

		// Then
		Assertions.assertThat(result.getPermissions()).isNotNull().isEmpty();
	}

	@Test
	@DisplayName("Test toUserDetails uses DomainFactory template")
	void test_toUserDetails_uses_domainFactory_template() throws Exception {
		// Given
		User user = User.builder().id(5L).username(AESUtils.encrypt("user5")).build();

		// When
		UserConvertor.toUserDetails(user);

		// Then
		domainFactoryMockedStatic.verify(DomainFactory::createUserDetails, Mockito.times(1));
	}

}
