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

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.i18n.common.exception.ParamException;
import org.laokou.common.i18n.util.ParamValidator;

import java.util.Set;

/**
 * @author laokou
 */
class ParamValidatorTest {

	@Test
	void test_validate_withNoErrors() {
		// Test with all valid validations (should not throw exception)
		ParamValidator.Validate valid1 = ParamValidator.validate();
		ParamValidator.Validate valid2 = ParamValidator.validate();
		Assertions.assertThatCode(() -> ParamValidator.validate(valid1, valid2)).doesNotThrowAnyException();
	}

	@Test
	void test_validate_withSingleError() {
		// Test with single validation error (should throw ParamException)
		ParamValidator.Validate invalid = ParamValidator.invalidate("用户名不能为空");
		Assertions.assertThatThrownBy(() -> ParamValidator.validate(invalid))
			.isInstanceOf(ParamException.class)
			.hasMessageContaining("用户名不能为空");
	}

	@Test
	void test_validate_withMultipleErrors() {
		// Test with multiple validation errors
		ParamValidator.Validate invalid1 = ParamValidator.invalidate("用户名不能为空");
		ParamValidator.Validate invalid2 = ParamValidator.invalidate("密码不能为空");
		Assertions.assertThatThrownBy(() -> ParamValidator.validate(invalid1, invalid2))
			.isInstanceOf(ParamException.class);
	}

	@Test
	void test_validate_withMixedValidations() {
		// Test with mixed valid and invalid validations
		ParamValidator.Validate valid = ParamValidator.validate();
		ParamValidator.Validate invalid = ParamValidator.invalidate("邮箱格式不正确");
		Assertions.assertThatThrownBy(() -> ParamValidator.validate(valid, invalid))
			.isInstanceOf(ParamException.class)
			.hasMessageContaining("邮箱格式不正确");
	}

	@Test
	void test_validates_withNoErrors() {
		// Test validates method with no errors
		ParamValidator.Validate valid1 = ParamValidator.validate();
		ParamValidator.Validate valid2 = ParamValidator.validate();
		Set<String> errors = ParamValidator.validates(valid1, valid2);
		Assertions.assertThat(errors).isEmpty();
	}

	@Test
	void test_validates_withSingleError() {
		// Test validates method with single error
		ParamValidator.Validate invalid = ParamValidator.invalidate("用户名不能为空");
		Set<String> errors = ParamValidator.validates(invalid);
		Assertions.assertThat(errors).hasSize(1).contains("用户名不能为空");
	}

	@Test
	void test_validates_withMultipleErrors() {
		// Test validates method with multiple errors
		ParamValidator.Validate invalid1 = ParamValidator.invalidate("用户名不能为空");
		ParamValidator.Validate invalid2 = ParamValidator.invalidate("密码不能为空");
		ParamValidator.Validate invalid3 = ParamValidator.invalidate("邮箱格式不正确");
		Set<String> errors = ParamValidator.validates(invalid1, invalid2, invalid3);
		Assertions.assertThat(errors).hasSize(3).contains("用户名不能为空", "密码不能为空", "邮箱格式不正确");
	}

	@Test
	void test_validates_withDuplicateErrors() {
		// Test validates method with duplicate errors (Set should remove duplicates)
		ParamValidator.Validate invalid1 = ParamValidator.invalidate("用户名不能为空");
		ParamValidator.Validate invalid2 = ParamValidator.invalidate("用户名不能为空");
		Set<String> errors = ParamValidator.validates(invalid1, invalid2);
		Assertions.assertThat(errors).hasSize(1).contains("用户名不能为空");
	}

	@Test
	void test_validates_withMixedValidations() {
		// Test validates method with mixed valid and invalid validations
		ParamValidator.Validate valid1 = ParamValidator.validate();
		ParamValidator.Validate invalid1 = ParamValidator.invalidate("用户名不能为空");
		ParamValidator.Validate valid2 = ParamValidator.validate();
		ParamValidator.Validate invalid2 = ParamValidator.invalidate("密码不能为空");
		Set<String> errors = ParamValidator.validates(valid1, invalid1, valid2, invalid2);
		Assertions.assertThat(errors).hasSize(2).contains("用户名不能为空", "密码不能为空");
	}

	@Test
	void test_validate_factoryMethod() {
		// Test validate factory method creates empty validation
		ParamValidator.Validate valid = ParamValidator.validate();
		Assertions.assertThat(valid).isNotNull();
		Set<String> errors = ParamValidator.validates(valid);
		Assertions.assertThat(errors).isEmpty();
	}

	@Test
	void test_invalidate_factoryMethod() {
		// Test invalidate factory method creates validation with error
		String errorMessage = "测试错误信息";
		ParamValidator.Validate invalid = ParamValidator.invalidate(errorMessage);
		Assertions.assertThat(invalid).isNotNull();
		Set<String> errors = ParamValidator.validates(invalid);
		Assertions.assertThat(errors).hasSize(1).contains(errorMessage);
	}

	@Test
	void test_validate_withEmptyArray() {
		// Test validate with empty array (should not throw exception)
		Assertions.assertThatCode(() -> ParamValidator.validate()).doesNotThrowAnyException();
	}

	@Test
	void test_validates_withEmptyArray() {
		// Test validates with empty array
		Set<String> errors = ParamValidator.validates();
		Assertions.assertThat(errors).isEmpty();
	}

	@Test
	void test_validate_withNullErrorMessage() {
		// Test with null error message (should be treated as empty)
		ParamValidator.Validate invalid = ParamValidator.invalidate(null);
		Assertions.assertThatCode(() -> ParamValidator.validate(invalid)).doesNotThrowAnyException();
	}

	@Test
	void test_validate_withEmptyErrorMessage() {
		// Test with empty error message (should be treated as valid)
		ParamValidator.Validate invalid = ParamValidator.invalidate("");
		Assertions.assertThatCode(() -> ParamValidator.validate(invalid)).doesNotThrowAnyException();
	}

	@Test
	void test_validates_withEmptyErrorMessage() {
		// Test validates with empty error message
		ParamValidator.Validate invalid = ParamValidator.invalidate("");
		Set<String> errors = ParamValidator.validates(invalid);
		Assertions.assertThat(errors).isEmpty();
	}

	@Test
	void test_validate_exceptionCode() {
		// Test that ParamException has correct error code
		ParamValidator.Validate invalid = ParamValidator.invalidate("测试错误");
		try {
			ParamValidator.validate(invalid);
			Assertions.fail("Should have thrown ParamException");
		}
		catch (ParamException ex) {
			Assertions.assertThat(ex.getCode()).isEqualTo("P_System_ValidateFailed");
		}
	}

	@Test
	void test_validate_complexScenario() {
		// Test complex scenario with multiple validations
		ParamValidator.Validate valid1 = ParamValidator.validate();
		ParamValidator.Validate invalid1 = ParamValidator.invalidate("用户名长度必须在3-20个字符之间");
		ParamValidator.Validate valid2 = ParamValidator.validate();
		ParamValidator.Validate invalid2 = ParamValidator.invalidate("密码强度不够");
		ParamValidator.Validate invalid3 = ParamValidator.invalidate("邮箱已被注册");

		Assertions.assertThatThrownBy(() -> ParamValidator.validate(valid1, invalid1, valid2, invalid2, invalid3))
			.isInstanceOf(ParamException.class);

		Set<String> errors = ParamValidator.validates(valid1, invalid1, valid2, invalid2, invalid3);
		Assertions.assertThat(errors).hasSize(3).contains("用户名长度必须在3-20个字符之间", "密码强度不够", "邮箱已被注册");
	}

	@Test
	void test_validate_withLongErrorMessage() {
		// Test with long error message
		String longMessage = "这是一个非常长的错误消息，用于测试系统是否能够正确处理较长的验证错误信息，包括各种特殊字符和标点符号！@#￥%……&*（）";
		ParamValidator.Validate invalid = ParamValidator.invalidate(longMessage);
		Assertions.assertThatThrownBy(() -> ParamValidator.validate(invalid))
			.isInstanceOf(ParamException.class)
			.hasMessageContaining(longMessage);
	}

	@Test
	void test_validate_withSpecialCharacters() {
		// Test with special characters in error message
		ParamValidator.Validate invalid = ParamValidator.invalidate("错误：<script>alert('test')</script>");
		Set<String> errors = ParamValidator.validates(invalid);
		Assertions.assertThat(errors).hasSize(1).contains("错误：<script>alert('test')</script>");
	}

	@Test
	void test_validateObject_constructor() {
		// Test Validate object constructor
		ParamValidator.Validate validate1 = new ParamValidator.Validate();
		ParamValidator.Validate validate2 = new ParamValidator.Validate("错误信息");

		Set<String> errors1 = ParamValidator.validates(validate1);
		Set<String> errors2 = ParamValidator.validates(validate2);

		Assertions.assertThat(errors1).isEmpty();
		Assertions.assertThat(errors2).hasSize(1).contains("错误信息");
	}

}
