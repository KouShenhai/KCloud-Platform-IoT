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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.laokou.common.i18n.util.SpringContextUtils;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

/**
 * DomainFactory test class.
 *
 * @author laokou
 */
@DisplayName("DomainFactory Unit Tests")
class DomainFactoryTest {

	private MockedStatic<SpringContextUtils> springContextUtilsMockedStatic;

	@AfterEach
	void tearDown() {
		if (springContextUtilsMockedStatic != null) {
			springContextUtilsMockedStatic.close();
		}
	}

	@Test
	@DisplayName("Test getUserDetails returns UserExtDetails from SpringContextUtils")
	void test_getUserDetails_returns_userExtDetails_from_springContextUtils() {
		// Given
		UserExtDetails expectedUserExtDetails = UserExtDetails.builder()
			.id(1L)
			.username("admin")
			.tenantId(100L)
			.build();

		springContextUtilsMockedStatic = Mockito.mockStatic(SpringContextUtils.class);
		springContextUtilsMockedStatic.when(() -> SpringContextUtils.getBeanProvider(UserExtDetails.class))
			.thenReturn(expectedUserExtDetails);

		// When
		UserExtDetails result = DomainFactory.createUserDetails();

		// Then
		Assertions.assertThat(result).isEqualTo(expectedUserExtDetails);
		Assertions.assertThat(result.getId()).isEqualTo(1L);
		Assertions.assertThat(result.getUsername()).isEqualTo("admin");
		Assertions.assertThat(result.getTenantId()).isEqualTo(100L);
	}

	@Test
	@DisplayName("Test getUserDetails returns null when no bean found")
	void test_getUserDetails_returns_null_when_no_bean_found() {
		// Given
		springContextUtilsMockedStatic = Mockito.mockStatic(SpringContextUtils.class);
		springContextUtilsMockedStatic.when(() -> SpringContextUtils.getBeanProvider(UserExtDetails.class))
			.thenReturn(null);

		// When
		UserExtDetails result = DomainFactory.createUserDetails();

		// Then
		Assertions.assertThat(result).isNull();
	}

	@Test
	@DisplayName("Test getUserDetails calls SpringContextUtils.getBeanProvider with correct class")
	void test_getUserDetails_calls_springContextUtils_with_correct_class() {
		// Given
		springContextUtilsMockedStatic = Mockito.mockStatic(SpringContextUtils.class);
		springContextUtilsMockedStatic.when(() -> SpringContextUtils.getBeanProvider(UserExtDetails.class))
			.thenReturn(UserExtDetails.builder().build());

		// When
		DomainFactory.createUserDetails();

		// Then
		springContextUtilsMockedStatic.verify(() -> SpringContextUtils.getBeanProvider(UserExtDetails.class),
				Mockito.times(1));
	}

}
