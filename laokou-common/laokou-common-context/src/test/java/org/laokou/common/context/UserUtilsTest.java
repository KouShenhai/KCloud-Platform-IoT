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
import org.junit.jupiter.api.Test;
import org.laokou.common.context.util.UserExtDetails;
import org.laokou.common.context.util.UserUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

class UserUtilsTest {

	@Test
	void test() {
		Assertions.assertThat(UserUtils.user()).isNotNull().isEqualTo(new UserExtDetails());
		Assertions.assertThat(UserUtils.getUserId()).isNull();
		Assertions.assertThat(UserUtils.getUserName()).isNull();
		Assertions.assertThat(UserUtils.getTenantId()).isNull();
		Assertions.assertThat(UserUtils.isSuperAdmin()).isNull();
		Assertions.assertThat(UserUtils.user().getPermissions()).isNull();
		SecurityContextHolder.getContext().setAuthentication(new TestAuthentication());
		Assertions.assertThat(UserUtils.user()).isNotNull().isNotEqualTo(new UserExtDetails());
		Assertions.assertThat(UserUtils.getUserId()).isNotNull().isEqualTo(1L);
		Assertions.assertThat(UserUtils.getUserName()).isNotNull().isEqualTo("admin");
		Assertions.assertThat(UserUtils.getTenantId()).isNotNull().isEqualTo(0L);
		Assertions.assertThat(UserUtils.isSuperAdmin()).isNotNull().isTrue();
		Assertions.assertThat(UserUtils.user().getPermissions()).isNotNull().isEqualTo(Set.of("test:save"));
	}

	static class TestAuthentication implements Authentication {

		@Override
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
			UserExtDetails userExtDetails = new UserExtDetails();
			userExtDetails.setId(1L);
			userExtDetails.setUsername("admin");
			userExtDetails.setAvatar("https://youke1.picui.cn/s1/2025/07/20/687ca202b2c53.jpg");
			userExtDetails.setSuperAdmin(true);
			userExtDetails.setStatus(0);
			userExtDetails.setMail("2413176044@qq.com");
			userExtDetails.setMobile("13574411111");
			userExtDetails.setPermissions(Set.of("test:save"));
			userExtDetails.setTenantId(0L);
			return userExtDetails;
		}

		@Override
		public boolean isAuthenticated() {
			return true;
		}

		@Override
		public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		}

		@Override
		public String getName() {
			return "laokou";
		}

	}

}
