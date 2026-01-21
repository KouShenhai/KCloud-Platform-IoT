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
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.config.CoapConfig;
import org.eclipse.californium.elements.config.UdpConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * CoapServerConfig test class.
 *
 * @author laokou
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CoapServerConfig test")
class CoapServerConfigTest {

	@Mock
	private SpringCoapServerProperties mockProperties;

	private CoapServerConfig coapServerConfig;

	private CoapServer coapServer;

	@BeforeAll
	static void initConfiguration() {
		// Register Californium configuration modules (required since Californium 3.0+)
		CoapConfig.register();
		UdpConfig.register();
	}

	@BeforeEach
	void setUp() {
		coapServerConfig = new CoapServerConfig();
	}

	@AfterEach
	void tearDown() {
		if (coapServer != null) {
			coapServer.stop();
			coapServer.destroy();
			coapServer = null;
		}
	}

	@Test
	@DisplayName("Test coapServer bean creation with single port")
	void test_coapServer_withSinglePort_createsBeanSuccessfully() {
		// Given
		int[] ports = new int[] { 5683 };
		Mockito.when(mockProperties.getPort()).thenReturn(ports);

		// When
		coapServer = coapServerConfig.coapServer(mockProperties);

		// Then
		Assertions.assertThat(coapServer).isNotNull();
		Assertions.assertThat(coapServer).isInstanceOf(CoapServer.class);
		Mockito.verify(mockProperties).getPort();
	}

	@Test
	@DisplayName("Test coapServer bean creation with multiple ports")
	void test_coapServer_withMultiplePorts_createsBeanSuccessfully() {
		// Given
		int[] ports = new int[] { 5683, 5684 };
		Mockito.when(mockProperties.getPort()).thenReturn(ports);

		// When
		coapServer = coapServerConfig.coapServer(mockProperties);

		// Then
		Assertions.assertThat(coapServer).isNotNull();
		Mockito.verify(mockProperties).getPort();
	}

	@Test
	@DisplayName("Test coapServer bean creation with empty ports")
	void test_coapServer_withEmptyPorts_createsBeanSuccessfully() {
		// Given
		int[] ports = new int[] {};
		Mockito.when(mockProperties.getPort()).thenReturn(ports);

		// When
		coapServer = coapServerConfig.coapServer(mockProperties);

		// Then
		Assertions.assertThat(coapServer).isNotNull();
	}

	@Test
	@DisplayName("Test coapServer uses configured ports from properties")
	void test_coapServer_usesConfiguredPorts() {
		// Given
		int[] expectedPorts = new int[] { 5683 };
		Mockito.when(mockProperties.getPort()).thenReturn(expectedPorts);

		// When
		coapServer = coapServerConfig.coapServer(mockProperties);

		// Then
		Assertions.assertThat(coapServer).isNotNull();
		// Verify that getPort was called to retrieve configuration
		Mockito.verify(mockProperties, Mockito.times(1)).getPort();
	}

	@Test
	@DisplayName("Test CoapServerConfig is instantiable")
	void test_coapServerConfig_instantiation_success() {
		// When
		CoapServerConfig config = new CoapServerConfig();

		// Then
		Assertions.assertThat(config).isNotNull();
	}

	@Test
	@DisplayName("Test coapServer bean with standard CoAP port 5683")
	void test_coapServer_standardPort5683_success() {
		// Given - Standard CoAP port
		int[] ports = new int[] { 5683 };
		Mockito.when(mockProperties.getPort()).thenReturn(ports);

		// When
		coapServer = coapServerConfig.coapServer(mockProperties);

		// Then
		Assertions.assertThat(coapServer).isNotNull();
	}

}
