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
import org.laokou.common.context.util.User;
import org.laokou.common.context.util.UserExtDetails;
import org.laokou.common.context.util.UserUtils;
import org.laokou.common.crypto.util.AESUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

class UserUtilsTest {

	@Test
	void test() {
		Assertions.assertThat(UserUtils.userDetail()).isNotNull().isEqualTo(new UserExtDetails());
		SecurityContextHolder.getContext().setAuthentication(new TestAuthentication());
		Assertions.assertThat(UserUtils.userDetail()).isNotNull().isNotEqualTo(new UserExtDetails());
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
			return null;
		}

		@Override
		public Object getDetails() {
			return null;
		}

		@Override
		public Object getPrincipal() {
			User user = null;
			try {
				user = new User(1L, AESUtils.encrypt("admin"),
						"https://youke1.picui.cn/s1/2025/07/20/687ca202b2c53.jpg", true, 0,
						AESUtils.encrypt("2413176044@qq.com"), AESUtils.encrypt("13574411111"), 0L,
						Set.of("test:save"));
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
			return new UserExtDetails(user);
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
			return "laokou";
		}

	}

}
