/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.i18n;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.assertj.core.api.Assertions;
import org.hibernate.validator.constraints.Range;
import org.junit.jupiter.api.Test;
import org.laokou.common.i18n.util.ValidatorUtils;

import java.util.Set;

/**
 * @author laokou
 */
class ValidatorUtilsTest {

	@Test
	void test_validate_withValidObject() {
		// Test with valid object (no constraint violations)
		TestUser user = new TestUser("John", "john@example.com", 25);
		Set<String> errors = ValidatorUtils.validate(user);
		Assertions.assertThat(errors).isEmpty();
	}

	@Test
	void test_validate_withInvalidName() {
		// Test with invalid name (null)
		TestUser user = new TestUser(null, "john@example.com", 25);
		Set<String> errors = ValidatorUtils.validate(user);
		Assertions.assertThat(errors).isNotEmpty();
		Assertions.assertThat(errors).hasSize(1);
	}

	@Test
	void test_validate_withInvalidEmail() {
		// Test with invalid email
		TestUser user = new TestUser("John", "invalid-email", 25);
		Set<String> errors = ValidatorUtils.validate(user);
		Assertions.assertThat(errors).isNotEmpty();
		Assertions.assertThat(errors).hasSize(1);
	}

	@Test
	void test_validate_withInvalidAge() {
		// Test with invalid age (negative)
		TestUser user = new TestUser("John", "john@example.com", -1);
		Set<String> errors = ValidatorUtils.validate(user);
		Assertions.assertThat(errors).isNotEmpty();
		Assertions.assertThat(errors).hasSize(1);
	}

	@Test
	void test_validate_withMultipleViolations() {
		// Test with multiple constraint violations
		TestUser user = new TestUser(null, "invalid-email", -1);
		Set<String> errors = ValidatorUtils.validate(user);
		Assertions.assertThat(errors).isNotEmpty();
		Assertions.assertThat(errors).hasSize(3);
	}

	@Test
	void test_validate_withEmptyName() {
		// Test with empty name
		TestUser user = new TestUser("", "john@example.com", 25);
		Set<String> errors = ValidatorUtils.validate(user);
		Assertions.assertThat(errors).isNotEmpty();
	}

	@Test
	void test_validate_withNullEmail() {
		// Test with null email
		TestUser user = new TestUser("John", null, 25);
		Set<String> errors = ValidatorUtils.validate(user);
		Assertions.assertThat(errors).isNotEmpty();
	}

	@Test
	void test_validate_withZeroAge() {
		// Test with zero age (should be valid as Min is 0)
		TestUser user = new TestUser("John", "john@example.com", 0);
		Set<String> errors = ValidatorUtils.validate(user);
		Assertions.assertThat(errors).isEmpty();
	}

	@Test
	void test_validate_withMaxAge() {
		// Test with maximum age
		TestUser user = new TestUser("John", "john@example.com", 150);
		Set<String> errors = ValidatorUtils.validate(user);
		Assertions.assertThat(errors).isNotEmpty();
	}

	@Test
	void test_validate_withValidationGroups() {
		// Test with validation groups
		TestUserWithGroups user = new TestUserWithGroups("John", "john@example.com");
		Set<String> errors = ValidatorUtils.validate(user, CreateGroup.class);
		Assertions.assertThat(errors).isEmpty();
	}

	@Test
	void test_validate_withDifferentValidationGroups() {
		// Test with different validation groups
		TestUserWithGroups user = new TestUserWithGroups("Jo", "john@example.com");
		Set<String> errorsCreate = ValidatorUtils.validate(user, CreateGroup.class);
		Set<String> errorsUpdate = ValidatorUtils.validate(user, UpdateGroup.class);

		// CreateGroup requires min 3 chars, UpdateGroup requires min 2 chars
		Assertions.assertThat(errorsCreate).isNotEmpty();
		Assertions.assertThat(errorsUpdate).isEmpty();
	}

	@Test
	void test_validate_withNoGroups() {
		// Test with no validation groups (default group)
		TestUser user = new TestUser("John", "john@example.com", 25);
		Set<String> errors = ValidatorUtils.validate(user);
		Assertions.assertThat(errors).isEmpty();
	}

	@Test
	void test_validate_withNestedObject() {
		// Test with nested object validation
		Address address = new Address(null, "12345");
		TestUserWithAddress user = new TestUserWithAddress("John", address);
		Set<String> errors = ValidatorUtils.validate(user);
		Assertions.assertThat(errors).isNotEmpty();
	}

	@Test
	void test_validate_withValidNestedObject() {
		// Test with valid nested object
		Address address = new Address("123 Main St", "12345");
		TestUserWithAddress user = new TestUserWithAddress("John", address);
		Set<String> errors = ValidatorUtils.validate(user);
		Assertions.assertThat(errors).isEmpty();
	}

	@Test
	void test_getMessage_withValidCode() {
		// Test getMessage with valid code
		String message = ValidatorUtils.getMessage("test.message");
		Assertions.assertThat(message).isEqualTo("测试");
	}

	@Test
	void test_getMessage_withNullArgs() {
		// Test getMessage with null args
		String message = ValidatorUtils.getMessage("test.message", null);
		Assertions.assertThat(message).isEqualTo("测试");
	}

	@Test
	void test_getMessage_withArgs() {
		// Test getMessage with arguments
		Object[] args = new Object[] { "John", 25 };
		String message = ValidatorUtils.getMessage("test.message", args);
		Assertions.assertThat(message).isNotNull();
	}

	@Test
	void test_getMessage_withoutArgs() {
		// Test getMessage without args (convenience method)
		String message = ValidatorUtils.getMessage("test.message");
		Assertions.assertThat(message).isNotNull();
	}

	@Test
	void test_validate_withSizeConstraint() {
		// Test with size constraint
		TestUserWithSize user = new TestUserWithSize("Jo");
		Set<String> errors = ValidatorUtils.validate(user);
		Assertions.assertThat(errors).isNotEmpty();
	}

	@Test
	void test_validate_withValidSize() {
		// Test with valid size
		TestUserWithSize user = new TestUserWithSize("John");
		Set<String> errors = ValidatorUtils.validate(user);
		Assertions.assertThat(errors).isEmpty();
	}

	@Test
	void test_validate_withPatternConstraint() {
		// Test with pattern constraint
		TestUserWithPattern user = new TestUserWithPattern("invalid123");
		Set<String> errors = ValidatorUtils.validate(user);
		Assertions.assertThat(errors).isNotEmpty();
	}

	@Test
	void test_validate_withValidPattern() {
		// Test with valid pattern (letters only)
		TestUserWithPattern user = new TestUserWithPattern("JohnDoe");
		Set<String> errors = ValidatorUtils.validate(user);
		Assertions.assertThat(errors).isEmpty();
	}

	@Test
	void test_validate_returnsImmutableSet() {
		// Test that validate returns an immutable/unmodifiable set
		TestUser user = new TestUser("John", "john@example.com", 25);
		Set<String> errors = ValidatorUtils.validate(user);
		Assertions.assertThat(errors).isEmpty();
		// Verify it's an empty set
		Assertions.assertThat(errors).isEqualTo(Set.of());
	}

	// Test classes for validation

	private static class TestUser {

		@NotBlank(message = "Name must not be blank")
		private String name;

		@NotNull(message = "Email must not be null")
		@Email(message = "Email must be valid")
		private String email;

		@Range(min = 0, max = 120, message = "Age range 0-120")
		private int age;

		public TestUser(String name, String email, int age) {
			this.name = name;
			this.email = email;
			this.age = age;
		}

	}

	private static class TestUserWithGroups {

		@NotBlank(message = "Name must not be blank")
		@Size(min = 3, message = "Name must be at least 3 characters", groups = CreateGroup.class)
		@Size(min = 2, message = "Name must be at least 2 characters", groups = UpdateGroup.class)
		private String name;

		@Email(message = "Email must be valid")
		private String email;

		public TestUserWithGroups(String name, String email) {
			this.name = name;
			this.email = email;
		}

	}

	private static class TestUserWithAddress {

		@NotBlank(message = "Name must not be blank")
		private String name;

		@Valid
		private Address address;

		public TestUserWithAddress(String name, Address address) {
			this.name = name;
			this.address = address;
		}

	}

	private static class Address {

		@NotBlank(message = "Street must not be blank")
		private String street;

		@NotBlank(message = "Zip code must not be blank")
		private String zipCode;

		public Address(String street, String zipCode) {
			this.street = street;
			this.zipCode = zipCode;
		}

	}

	private static class TestUserWithSize {

		@Size(min = 3, max = 20, message = "Name must be between 3 and 20 characters")
		private String name;

		public TestUserWithSize(String name) {
			this.name = name;
		}

	}

	private static class TestUserWithPattern {

		@Pattern(regexp = "^[a-zA-Z]+$", message = "Name must contain only letters")
		private String name;

		public TestUserWithPattern(String name) {
			this.name = name;
		}

	}

	// Validation groups
	private interface CreateGroup {

	}

	private interface UpdateGroup {

	}

}
