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

package org.laokou.common.grpc;

import io.grpc.StatusException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.grpc.annotation.GrpcClient;
import org.laokou.common.grpc.proto.HelloWorldProto;
import org.laokou.common.grpc.proto.SimpleGrpc;
import org.laokou.common.testcontainers.util.DockerImageNames;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.consul.ConsulContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * @author laokou
 */
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = GrpcClientTest.GrpcTest.class)
class GrpcClientTest {

	@Container
	static ConsulContainer consul = new ConsulContainer(DockerImageNames.consul());

	@DynamicPropertySource
	static void consulProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.cloud.consul.host", consul::getHost);
		registry.add("spring.cloud.consul.port", consul::getFirstMappedPort);
		registry.add("spring.cloud.consul.discovery.service-name", () -> "laokou-common-grpc");
		registry.add("spring.cloud.consul.discovery.instance-id", () -> "laokou-common-grpc-1");
		registry.add("spring.cloud.consul.discovery.metadata.grpc_port", () -> "9097");
		registry.add("spring.cloud.consul.discovery.prefer-ip-address", () -> true);
	}

	@GrpcClient(serviceId = "laokou-common-grpc")
	private SimpleGrpc.SimpleBlockingV2Stub simpleBlockingV2Stub;

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

	}

}
