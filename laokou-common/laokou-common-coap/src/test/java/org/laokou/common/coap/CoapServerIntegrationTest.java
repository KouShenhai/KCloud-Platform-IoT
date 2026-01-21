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

package org.laokou.common.coap;

import org.assertj.core.api.Assertions;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.config.CoapConfig;
import org.eclipse.californium.elements.config.Configuration;
import org.eclipse.californium.elements.config.UdpConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * CoAP Server Integration test class.
 *
 * @author laokou
 */
@DisplayName("CoapServer Integration test")
class CoapServerIntegrationTest {

	private static final int TEST_PORT = 15683;

	private CoapServer coapServer;

	@BeforeAll
	static void initConfiguration() {
		// Register Californium configuration modules (required since Californium 3.0+)
		CoapConfig.register();
		UdpConfig.register();
	}

	@BeforeEach
	void setUp() {
		// Create CoAP server with default configuration
		coapServer = new CoapServer(TEST_PORT);
	}

	@AfterEach
	void tearDown() {
		if (coapServer != null) {
			coapServer.stop();
			coapServer.destroy();
		}
	}

	@Test
	@DisplayName("Test CoapServer creation with port")
	void test_coapServer_create_withPort_success() {
		// Then
		Assertions.assertThat(coapServer).isNotNull();
	}

	@Test
	@DisplayName("Test CoapServer starts successfully")
	void test_coapServer_start_success() {
		// When
		coapServer.start();

		// Then - server should be running (no exception)
		Assertions.assertThat(coapServer).isNotNull();
	}

	@Test
	@DisplayName("Test CoapServer stops successfully")
	void test_coapServer_stop_success() {
		// Given
		coapServer.start();

		// When
		coapServer.stop();

		// Then - no exception should be thrown
		Assertions.assertThat(coapServer).isNotNull();
	}

	@Test
	@DisplayName("Test CoapServer can be restarted")
	void test_coapServer_restart_success() {
		// Given
		coapServer.start();
		coapServer.stop();

		// When
		coapServer.start();

		// Then
		Assertions.assertThat(coapServer).isNotNull();
	}

	@Test
	@DisplayName("Test CoapServer with multiple ports")
	void test_coapServer_multiplePorts_success() {
		// Given
		CoapServer multiPortServer = new CoapServer(15684, 15685);

		try {
			// When
			multiPortServer.start();

			// Then
			Assertions.assertThat(multiPortServer).isNotNull();
		}
		finally {
			multiPortServer.stop();
			multiPortServer.destroy();
		}
	}

	@Test
	@DisplayName("Test CoapServer with empty ports uses default configuration")
	void test_coapServer_emptyPorts_success() {
		// Given - CoapServer without explicit ports uses default configuration
		CoapServer defaultServer = new CoapServer();

		try {
			// When
			defaultServer.start();

			// Then
			Assertions.assertThat(defaultServer).isNotNull();
		}
		finally {
			defaultServer.stop();
			defaultServer.destroy();
		}
	}

	@Test
	@DisplayName("Test CoapServer destroy")
	void test_coapServer_destroy_success() {
		// Given
		coapServer.start();
		coapServer.stop();

		// When
		coapServer.destroy();

		// Then - no exception should be thrown
		Assertions.assertThat(coapServer).isNotNull();
	}

	@Test
	@DisplayName("Test CoapServer with custom configuration")
	void test_coapServer_customConfiguration_success() {
		// Given
		Configuration config = Configuration.getStandard();
		CoapServer configuredServer = new CoapServer(config, 25683);

		try {
			// When
			configuredServer.start();

			// Then
			Assertions.assertThat(configuredServer).isNotNull();
		}
		finally {
			configuredServer.stop();
			configuredServer.destroy();
		}
	}

	@Test
	@DisplayName("Test multiple CoapServer instances can coexist on different ports")
	void test_coapServer_multipleInstances_differentPorts_success() {
		// Given
		CoapServer server1 = new CoapServer(16683);
		CoapServer server2 = new CoapServer(16684);

		try {
			// When
			server1.start();
			server2.start();

			// Then
			Assertions.assertThat(server1).isNotNull();
			Assertions.assertThat(server2).isNotNull();
		}
		finally {
			server1.stop();
			server1.destroy();
			server2.stop();
			server2.destroy();
		}
	}

}
