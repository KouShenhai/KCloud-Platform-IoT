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

package org.laokou.common.grpc.server;

import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.Test;
import org.laokou.common.security.annotation.EnableSecurity;
import org.laokou.common.testcontainers.util.DockerImageNames;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * @author laokou
 */
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = GrpcServerTest.GrpcTest.class)
class GrpcServerTest {

	@Container
	static RedisContainer redisContainer = new RedisContainer(DockerImageNames.redis()).withExposedPorts(6379)
		.withReuse(true);

	@DynamicPropertySource
	static void consulProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.data.redis.port", redisContainer::getFirstMappedPort);
	}

	@Test
	void test() {
	}

	@EnableSecurity
	@SpringBootApplication(scanBasePackages = { "org.laokou" })
	static class GrpcTest {

	}

	@TestConfiguration
	static class GrpcTestConfig {

		@Bean
		OAuth2AuthorizedClientManager authorizedClientManager(ClientRegistrationRepository registrations,
		                                                      OAuth2AuthorizedClientService service) {
			OAuth2AuthorizedClientProvider provider = OAuth2AuthorizedClientProviderBuilder.builder()
				.clientCredentials()
				.build();
			AuthorizedClientServiceOAuth2AuthorizedClientManager manager = new AuthorizedClientServiceOAuth2AuthorizedClientManager(
					registrations, service);
			manager.setAuthorizedClientProvider(provider);
			return manager;
		}

	}

}
