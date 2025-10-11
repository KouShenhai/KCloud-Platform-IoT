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

package org.laokou.common.core;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laokou.common.core.util.HttpUtils;
import org.laokou.common.core.util.MapUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * @author laokou
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class HttpUtilsTest {

	private WireMockServer wireMockServer;

	@BeforeEach
	void setUp() {
		wireMockServer = new WireMockServer(WireMockConfiguration.options().port(8884).httpsPort(8885));
		wireMockServer.start();
	}

	@Test
	void test_http() throws NoSuchAlgorithmException, KeyManagementException {
		wireMockServer.stubFor(WireMock.post("/test").willReturn(WireMock.ok("hello wiremock")));
		String resultJson = HttpUtils.doFormDataPost("http://localhost:" + wireMockServer.port() + "/test",
				MapUtils.newHashMap(0), MapUtils.newHashMap(0));
		Assertions.assertThat(resultJson).isEqualTo("hello wiremock");
		Assertions.assertThat(HttpUtils.getHttpClient()).isNotNull();
	}

	@AfterEach
	void tearDown() {
		if (wireMockServer != null && wireMockServer.isRunning()) {
			wireMockServer.stop();
		}
	}

}
