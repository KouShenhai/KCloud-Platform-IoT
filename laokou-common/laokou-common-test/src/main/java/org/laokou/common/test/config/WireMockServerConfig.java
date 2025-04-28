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

package org.laokou.common.test.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @author laokou
 */
public class WireMockServerConfig {

	@Bean(initMethod = "start", destroyMethod = "stop")
	public WireMockServer wireMockServer(WireMockServerProperties wireMockServerProperties) {
		return new WireMockServer(WireMockConfiguration.options()
			.port(wireMockServerProperties.getPort())
			.httpsPort(wireMockServerProperties.getHttpsPort()));
	}

}
