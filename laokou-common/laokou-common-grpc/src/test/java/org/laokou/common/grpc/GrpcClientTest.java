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

import io.grpc.StatusException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.laokou.common.grpc.annotation.GrpcClient;
import org.laokou.common.grpc.proto.HelloWorldProto;
import org.laokou.common.grpc.proto.SimpleGrpc;
import org.laokou.common.testcontainers.container.NacosContainer;
import org.laokou.common.testcontainers.util.DockerImageNames;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

/**
 * @author laokou
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = GrpcClientTest.GrpcTest.class)
class GrpcClientTest {

	@GrpcClient(serviceId = "laokou-common-grpc")
	private SimpleGrpc.SimpleBlockingV2Stub simpleBlockingV2Stub;

	static final NacosContainer nacos = new NacosContainer(DockerImageNames.nacos("v3.1.0"));

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
	void test() throws StatusException {
		HelloWorldProto.HelloRequest request = HelloWorldProto.HelloRequest.newBuilder().setName("test").build();
		HelloWorldProto.HelloReply helloReply = simpleBlockingV2Stub.sayHello(request);
		Assertions.assertThat(helloReply).isNotNull();
		Assertions.assertThat(helloReply.getMessage()).isEqualTo("Hello ==> test");
	}

	@EnableDiscoveryClient
	@SpringBootApplication(scanBasePackages = { "org.laokou" })
	static class GrpcTest {

		static void main(String[] args) {
			new SpringApplicationBuilder(GrpcTest.class).web(WebApplicationType.SERVLET).run(args);
		}

	}

}
