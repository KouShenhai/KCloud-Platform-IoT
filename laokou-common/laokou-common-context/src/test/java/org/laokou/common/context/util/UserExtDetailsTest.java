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
import org.laokou.common.crypto.util.AESUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * UserExtDetails test class.
 *
 * @author laokou
 */
@DisplayName("UserExtDetails Unit Tests")
class UserExtDetailsTest {

	private UserExtDetails userExtDetails;

	@BeforeEach
	void setUp() {
		Set<String> permissions = new HashSet<>();
		permissions.add("sys:user:query");
		permissions.add("sys:user:add");

		userExtDetails = UserExtDetails.builder()
			.id(1L)
			.username("testuser")
			.password("password123")
			.avatar("https://example.com/avatar.png")
			.superAdmin(true)
			.status(0)
			.mail("test@example.com")
			.mobile("13800138000")
			.tenantId(100L)
			.deptId(10L)
			.permissions(permissions)
			.build();
	}

	@Test
	@DisplayName("Test UserExtDetails basic properties via getters")
	void test_userExtDetails_basic_properties() {
		// Then
		Assertions.assertThat(userExtDetails.getId()).isEqualTo(1L);
		Assertions.assertThat(userExtDetails.getUsername()).isEqualTo("testuser");
		Assertions.assertThat(userExtDetails.getPassword()).isEqualTo("password123");
		Assertions.assertThat(userExtDetails.getAvatar()).isEqualTo("https://example.com/avatar.png");
		Assertions.assertThat(userExtDetails.getSuperAdmin()).isTrue();
		Assertions.assertThat(userExtDetails.getStatus()).isZero();
		Assertions.assertThat(userExtDetails.getMail()).isEqualTo("test@example.com");
		Assertions.assertThat(userExtDetails.getMobile()).isEqualTo("13800138000");
		Assertions.assertThat(userExtDetails.getTenantId()).isEqualTo(100L);
		Assertions.assertThat(userExtDetails.getDeptId()).isEqualTo(10L);
		Assertions.assertThat(userExtDetails.getPermissions())
			.hasSize(2)
			.containsExactlyInAnyOrder("sys:user:query", "sys:user:add");
	}

	@Test
	@DisplayName("Test getAuthorities returns correct authorities")
	void test_getAuthorities_returns_correct_authorities() {
		// When
		Collection<? extends GrantedAuthority> authorities = userExtDetails.getAuthorities();

		// Then
		Assertions.assertThat(authorities)
			.isNotNull()
			.hasSize(2)
			.extracting(GrantedAuthority::getAuthority)
			.containsExactlyInAnyOrder("sys:user:query", "sys:user:add");
	}

	@Test
	@DisplayName("Test getAttributes returns empty map")
	void test_getAttributes_returns_empty_map() {
		// When
		Map<String, Object> attributes = userExtDetails.getAttributes();

		// Then
		Assertions.assertThat(attributes).isNotNull().isEmpty();
	}

	@Test
	@DisplayName("Test getName returns username")
	void test_getName_returns_username() {
		// When
		String name = userExtDetails.getName();

		// Then
		Assertions.assertThat(name).isEqualTo("testuser");
	}

	@Test
	@DisplayName("Test toBuilder creates modifiable copy")
	void test_toBuilder_creates_modifiable_copy() {
		// When
		UserExtDetails modified = userExtDetails.toBuilder().id(999L).username("newuser").build();

		// Then
		Assertions.assertThat(modified.getId()).isEqualTo(999L);
		Assertions.assertThat(modified.getUsername()).isEqualTo("newuser");
		// Original should be unchanged
		Assertions.assertThat(userExtDetails.getId()).isEqualTo(1L);
		Assertions.assertThat(userExtDetails.getUsername()).isEqualTo("testuser");
	}

	@Test
	@DisplayName("Test UserExtDetails implements UserDetails interface")
	void test_userExtDetails_implements_userDetails() {
		// Then
		Assertions.assertThat(userExtDetails).isInstanceOf(UserDetails.class);
	}

	@Test
	@DisplayName("Test UserExtDetails implements OAuth2AuthenticatedPrincipal interface")
	void test_userExtDetails_implements_oauth2AuthenticatedPrincipal() {
		// Then
		Assertions.assertThat(userExtDetails).isInstanceOf(OAuth2AuthenticatedPrincipal.class);
	}

	@Test
	@DisplayName("Test equals and hashCode based on id, username, tenantId, deptId")
	void test_equals_and_hashCode() {
		// Given
		UserExtDetails sameUser = UserExtDetails.builder()
			.id(1L)
			.username("testuser")
			.password("different_password")
			.avatar("different_avatar")
			.superAdmin(false)
			.status(1)
			.mail("different@example.com")
			.mobile("different_mobile")
			.tenantId(100L)
			.deptId(10L)
			.permissions(new HashSet<>())
			.build();

		UserExtDetails differentUser = UserExtDetails.builder()
			.id(2L)
			.username("otheruser")
			.tenantId(100L)
			.deptId(10L)
			.build();

		// Then
		Assertions.assertThat(userExtDetails).isEqualTo(sameUser);
		Assertions.assertThat(userExtDetails.hashCode()).isEqualTo(sameUser.hashCode());
		Assertions.assertThat(userExtDetails).isNotEqualTo(differentUser);
	}

	@Test
	@DisplayName("Test UserExtDetails with null values")
	void test_userExtDetails_with_null_values() {
		// Given
		UserExtDetails nullUser = UserExtDetails.builder()
			.id(null)
			.username(null)
			.password(null)
			.avatar(null)
			.superAdmin(null)
			.status(null)
			.mail(null)
			.mobile(null)
			.tenantId(null)
			.deptId(null)
			.permissions(null)
			.build();

		// Then
		Assertions.assertThat(nullUser.getId()).isNull();
		Assertions.assertThat(nullUser.getUsername()).isNull();
		Assertions.assertThat(nullUser.getPassword()).isNull();
		Assertions.assertThat(nullUser.getAvatar()).isNull();
		Assertions.assertThat(nullUser.getSuperAdmin()).isNull();
		Assertions.assertThat(nullUser.getStatus()).isNull();
		Assertions.assertThat(nullUser.getMail()).isNull();
		Assertions.assertThat(nullUser.getMobile()).isNull();
		Assertions.assertThat(nullUser.getTenantId()).isNull();
		Assertions.assertThat(nullUser.getDeptId()).isNull();
		Assertions.assertThat(nullUser.getPermissions()).isNull();
	}

	@Test
	@DisplayName("Test decryptUsername with encrypted username")
	void test_decryptUsername_with_encrypted_username() throws Exception {
		// Given
		String originalUsername = "admin";
		String encryptedUsername = AESUtils.encrypt(originalUsername);

		UserExtDetails userWithEncryptedUsername = UserExtDetails.builder().id(1L).username(encryptedUsername).build();

		// When
		UserExtDetails decrypted = userWithEncryptedUsername.decryptUsername();

		// Then
		Assertions.assertThat(decrypted.getUsername()).isEqualTo(originalUsername);
	}

	@Test
	@DisplayName("Test decryptMail with encrypted mail")
	void test_decryptMail_with_encrypted_mail() throws Exception {
		// Given
		String originalMail = "test@example.com";
		String encryptedMail = AESUtils.encrypt(originalMail);

		UserExtDetails userWithEncryptedMail = UserExtDetails.builder().id(1L).mail(encryptedMail).build();

		// When
		UserExtDetails decrypted = userWithEncryptedMail.decryptMail();

		// Then
		Assertions.assertThat(decrypted.getMail()).isEqualTo(originalMail);
	}

	@Test
	@DisplayName("Test decryptMobile with encrypted mobile")
	void test_decryptMobile_with_encrypted_mobile() throws Exception {
		// Given
		String originalMobile = "13800138000";
		String encryptedMobile = AESUtils.encrypt(originalMobile);

		UserExtDetails userWithEncryptedMobile = UserExtDetails.builder().id(1L).mobile(encryptedMobile).build();

		// When
		UserExtDetails decrypted = userWithEncryptedMobile.decryptMobile();

		// Then
		Assertions.assertThat(decrypted.getMobile()).isEqualTo(originalMobile);
	}

	@Test
	@DisplayName("Test decryptUsername with null username returns this")
	void test_decryptUsername_with_null_username() {
		// Given
		UserExtDetails userWithNullUsername = UserExtDetails.builder().id(1L).username(null).build();

		// When
		UserExtDetails result = userWithNullUsername.decryptUsername();

		// Then
		Assertions.assertThat(result).isSameAs(userWithNullUsername);
		Assertions.assertThat(result.getUsername()).isNull();
	}

	@Test
	@DisplayName("Test decryptUsername with empty username returns this")
	void test_decryptUsername_with_empty_username() {
		// Given
		UserExtDetails userWithEmptyUsername = UserExtDetails.builder().id(1L).username("").build();

		// When
		UserExtDetails result = userWithEmptyUsername.decryptUsername();

		// Then
		Assertions.assertThat(result).isSameAs(userWithEmptyUsername);
	}

	@Test
	@DisplayName("Test chained decrypt methods")
	void test_chained_decrypt_methods() throws Exception {
		// Given
		String originalUsername = "admin";
		String originalMail = "admin@example.com";
		String originalMobile = "13800138000";

		UserExtDetails user = UserExtDetails.builder()
			.id(1L)
			.username(AESUtils.encrypt(originalUsername))
			.mail(AESUtils.encrypt(originalMail))
			.mobile(AESUtils.encrypt(originalMobile))
			.build();

		// When
		UserExtDetails decrypted = user.decryptUsername().decryptMail().decryptMobile();

		// Then
		Assertions.assertThat(decrypted.getUsername()).isEqualTo(originalUsername);
		Assertions.assertThat(decrypted.getMail()).isEqualTo(originalMail);
		Assertions.assertThat(decrypted.getMobile()).isEqualTo(originalMobile);
	}

}
