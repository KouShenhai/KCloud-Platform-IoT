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
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

/**
 * OAuth2ModelMapper测试类.
 *
 * @author laokou
 */
class OAuth2ModelMapperTest {

	@Test
	void test_custom_grant_type_username_password() {
		// Then
		Assertions.assertThat(OAuth2ModelMapper.USERNAME_PASSWORD).isNotNull();
		Assertions.assertThat(OAuth2ModelMapper.USERNAME_PASSWORD.getValue()).isEqualTo("username_password");
	}

	@Test
	void test_custom_grant_type_test() {
		// Then
		Assertions.assertThat(OAuth2ModelMapper.TEST).isNotNull();
		Assertions.assertThat(OAuth2ModelMapper.TEST.getValue()).isEqualTo("test");
	}

	@Test
	void test_custom_grant_type_mail() {
		// Then
		Assertions.assertThat(OAuth2ModelMapper.MAIL).isNotNull();
		Assertions.assertThat(OAuth2ModelMapper.MAIL.getValue()).isEqualTo("mail");
	}

	@Test
	void test_custom_grant_type_mobile() {
		// Then
		Assertions.assertThat(OAuth2ModelMapper.MOBILE).isNotNull();
		Assertions.assertThat(OAuth2ModelMapper.MOBILE.getValue()).isEqualTo("mobile");
	}

	@Test
	void test_custom_grant_types_are_different() {
		// Then
		Assertions.assertThat(OAuth2ModelMapper.USERNAME_PASSWORD)
			.isNotEqualTo(OAuth2ModelMapper.TEST)
			.isNotEqualTo(OAuth2ModelMapper.MAIL)
			.isNotEqualTo(OAuth2ModelMapper.MOBILE);
	}

	@Test
	void test_custom_grant_types_not_equal_to_standard_types() {
		// Then
		Assertions.assertThat(OAuth2ModelMapper.USERNAME_PASSWORD)
			.isNotEqualTo(AuthorizationGrantType.AUTHORIZATION_CODE)
			.isNotEqualTo(AuthorizationGrantType.CLIENT_CREDENTIALS)
			.isNotEqualTo(AuthorizationGrantType.REFRESH_TOKEN);
	}

}
