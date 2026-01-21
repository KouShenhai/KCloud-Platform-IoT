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

package org.laokou.common.coap.config;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * SpringCoapServerProperties test class.
 *
 * @author laokou
 */
@DisplayName("SpringCoapServerProperties test")
class SpringCoapServerPropertiesTest {

	private SpringCoapServerProperties properties;

	@BeforeEach
	void setUp() {
		properties = new SpringCoapServerProperties();
	}

	@Test
	@DisplayName("Test getPort returns null by default")
	void test_getPort_defaultValue_isNull() {
		// When
		int[] port = properties.getPort();

		// Then
		Assertions.assertThat(port).isNull();
	}

	@Test
	@DisplayName("Test setPort with single port")
	void test_setPort_singlePort_success() {
		// Given
		int[] ports = new int[] { 5683 };

		// When
		properties.setPort(ports);

		// Then
		Assertions.assertThat(properties.getPort()).isNotNull();
		Assertions.assertThat(properties.getPort()).hasSize(1);
		Assertions.assertThat(properties.getPort()[0]).isEqualTo(5683);
	}

	@Test
	@DisplayName("Test setPort with multiple ports")
	void test_setPort_multiplePorts_success() {
		// Given
		int[] ports = new int[] { 5683, 5684, 5685 };

		// When
		properties.setPort(ports);

		// Then
		Assertions.assertThat(properties.getPort()).isNotNull();
		Assertions.assertThat(properties.getPort()).hasSize(3);
		Assertions.assertThat(properties.getPort()).containsExactly(5683, 5684, 5685);
	}

	@Test
	@DisplayName("Test setPort with empty array")
	void test_setPort_emptyArray_success() {
		// Given
		int[] ports = new int[] {};

		// When
		properties.setPort(ports);

		// Then
		Assertions.assertThat(properties.getPort()).isNotNull();
		Assertions.assertThat(properties.getPort()).isEmpty();
	}

	@Test
	@DisplayName("Test setPort replaces existing value")
	void test_setPort_replaceExisting_success() {
		// Given
		properties.setPort(new int[] { 5683 });

		// When
		properties.setPort(new int[] { 8080, 9090 });

		// Then
		Assertions.assertThat(properties.getPort()).hasSize(2);
		Assertions.assertThat(properties.getPort()).containsExactly(8080, 9090);
	}

	@Test
	@DisplayName("Test setPort with standard CoAP port")
	void test_setPort_standardCoapPort_success() {
		// Given - 5683 is the standard CoAP port
		int[] ports = new int[] { 5683 };

		// When
		properties.setPort(ports);

		// Then
		Assertions.assertThat(properties.getPort()[0]).isEqualTo(5683);
	}

	@Test
	@DisplayName("Test setPort with CoAP secure port")
	void test_setPort_coapSecurePort_success() {
		// Given - 5684 is the standard CoAPS (secure) port
		int[] ports = new int[] { 5684 };

		// When
		properties.setPort(ports);

		// Then
		Assertions.assertThat(properties.getPort()[0]).isEqualTo(5684);
	}

	@Test
	@DisplayName("Test port array is modifiable after retrieval")
	void test_getPort_modifyArray_affectsOriginal() {
		// Given
		properties.setPort(new int[] { 5683 });
		int[] retrievedPorts = properties.getPort();

		// When
		retrievedPorts[0] = 9999;

		// Then - arrays are passed by reference, modification affects original
		Assertions.assertThat(properties.getPort()[0]).isEqualTo(9999);
	}

}
