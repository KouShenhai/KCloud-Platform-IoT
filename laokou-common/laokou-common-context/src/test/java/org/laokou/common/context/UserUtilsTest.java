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

package org.laokou.common.context;

import org.assertj.core.api.Assertions;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;
import org.laokou.common.context.util.DomainFactory;
import org.laokou.common.context.util.User;
import org.laokou.common.context.util.UserConvertor;
import org.laokou.common.context.util.UserExtDetails;
import org.laokou.common.context.util.UserUtils;
import org.laokou.common.crypto.util.AESUtils;
import org.laokou.common.i18n.util.SpringContextUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = { SpringContextUtils.class, UserExtDetails.class })
class UserUtilsTest {

	@Test
	void test() {
		Assertions.assertThat(UserUtils.userDetail()).isNotNull().isEqualTo(DomainFactory.getUserDetails());
		Assertions.assertThat(UserUtils.getUserId()).isNull();
		Assertions.assertThat(UserUtils.getUserName()).isBlank();
		Assertions.assertThat(UserUtils.getTenantId()).isNull();
		Assertions.assertThat(UserUtils.isSuperAdmin()).isNull();
		Assertions.assertThat(UserUtils.userDetail().getPermissions()).isNull();
		SecurityContextHolder.getContext().setAuthentication(new TestAuthentication());
		Assertions.assertThat(UserUtils.userDetail()).isNotNull().isNotEqualTo(DomainFactory.getUserDetails());
		Assertions.assertThat(UserUtils.getUserId()).isNotNull().isEqualTo(1L);
		Assertions.assertThat(UserUtils.getUserName()).isNotNull().isEqualTo("admin");
		Assertions.assertThat(UserUtils.getTenantId()).isNotNull().isEqualTo(0L);
		Assertions.assertThat(UserUtils.isSuperAdmin()).isNotNull().isTrue();
		Assertions.assertThat(UserUtils.userDetail().getPermissions()).isNotNull().isEqualTo(Set.of("test:save"));
	}

	static class TestAuthentication implements Authentication {

		@Override
		@NullMarked
		public Collection<? extends GrantedAuthority> getAuthorities() {
			return Collections.emptySet();
		}

		@Override
		public Object getCredentials() {
			try {
				return AESUtils.encrypt("admin");
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public Object getDetails() {
			try {
				return AESUtils.encrypt("admin");
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public Object getPrincipal() {
			try {
				User user = User.builder()
					.id(1L)
					.username("admin")
					.password("admin123")
					.avatar("https://youke1.picui.cn/s1/2025/07/20/687ca202b2c53.jpg")
					.superAdmin(true)
					.tenantId(0L)
					.deptId(0L)
					.permissions(Set.of("test:save"))
					.status(0)
					.mail(AESUtils.encrypt("2413176044@qq.com"))
					.mobile(AESUtils.encrypt("13574411111"))
					.build();
				return UserConvertor.toUserDetails(user);
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public boolean isAuthenticated() {
			return true;
		}

		@Override
		public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
			throw new UnsupportedOperationException();
		}

		@Override
		public String getName() {
			try {
				return AESUtils.encrypt("admin");
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

	}

}
