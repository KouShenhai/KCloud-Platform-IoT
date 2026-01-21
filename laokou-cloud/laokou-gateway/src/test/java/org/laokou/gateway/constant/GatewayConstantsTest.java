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

package org.laokou.gateway.constant;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * GatewayConstants 常量类测试.
 *
 * @author laokou
 */
@DisplayName("GatewayConstants Tests")
class GatewayConstantsTest {

	@Test
	@DisplayName("Test ROUTER_NOT_EXIST constant value")
	void test_ROUTER_NOT_EXIST_value_equalsExpected() {
		// Then
		Assertions.assertThat(GatewayConstants.ROUTER_NOT_EXIST).isEqualTo("S_Gateway_RouterNotExist");
	}

	@Test
	@DisplayName("Test ROUTER_NOT_EXIST constant is not empty")
	void test_ROUTER_NOT_EXIST_notEmpty_isTrue() {
		// Then
		Assertions.assertThat(GatewayConstants.ROUTER_NOT_EXIST).isNotNull();
		Assertions.assertThat(GatewayConstants.ROUTER_NOT_EXIST).isNotEmpty();
	}

	@Test
	@DisplayName("Test ROUTER_NOT_EXIST constant starts with S_Gateway_ prefix")
	void test_ROUTER_NOT_EXIST_prefix_startsWithSGateway() {
		// Then
		Assertions.assertThat(GatewayConstants.ROUTER_NOT_EXIST).startsWith("S_Gateway_");
	}

}
