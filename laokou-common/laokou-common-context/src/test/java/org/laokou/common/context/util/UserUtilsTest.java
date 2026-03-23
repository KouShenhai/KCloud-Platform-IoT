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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

/**
 * UserUtils test class.
 *
 * @author laokou
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserUtils Unit Tests")
class UserUtilsTest {

	private OAuth2AuthenticatedExtPrincipal principal;


	@BeforeEach
	void setUp() {
		principal = OAuth2AuthenticatedExtPrincipal.builder()
			.id(1L)
			.username("admin")
			.avatar("https://example.com/avatar.png")
			.superAdmin(true)
			.status(0)
			.mail("admin@example.com")
			.mobile("13800138000")
			.tenantId(100L)
			.deptId(10L)
			.permissions(Set.of("sys:user:query"))
			.build();
	}

}
