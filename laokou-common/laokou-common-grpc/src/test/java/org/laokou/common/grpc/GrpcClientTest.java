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

package org.laokou.common.grpc;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.laokou.common.testcontainers.container.NacosContainer;
import org.laokou.common.testcontainers.util.DockerImageNames;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

/**
 * @author laokou
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = GrpcClientTest.GrpcTestConfig.class)
class GrpcClientTest {

	static final NacosContainer nacos = new NacosContainer(DockerImageNames.nacos("v3.1.0"), 28848, 29848);

	@BeforeAll
	static void beforeAll() {
		nacos.start();
	}

	@AfterAll
	static void afterAll() {
		nacos.stop();
	}

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.cloud.nacos.discovery.server-addr", nacos::getServerAddr);
		registry.add("spring.cloud.nacos.discovery.group", () -> "DEFAULT_GROUP");
		registry.add("spring.cloud.nacos.discovery.username", () -> "nacos");
		registry.add("spring.cloud.nacos.discovery.password", () -> "nacos");
		registry.add("spring.cloud.nacos.discovery.namespace", () -> "public");
	}

	@Test
	void test() {

	}

	@SpringBootConfiguration
	@EnableDiscoveryClient
	@ComponentScan(basePackages = "org.laokou")
	static class GrpcTestConfig {

	}

}
