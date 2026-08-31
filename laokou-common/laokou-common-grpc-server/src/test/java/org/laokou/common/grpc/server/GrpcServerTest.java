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
import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall;
import io.grpc.MethodDescriptor;
import io.grpc.StatusException;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laokou.common.grpc.proto.HelloWorldProto;
import org.laokou.common.grpc.proto.SimpleGrpc;
import org.laokou.common.security.annotation.EnableSecurity;
import org.laokou.common.security.constant.Constants;
import org.laokou.common.testcontainers.container.OAuth2Container;
import org.laokou.common.testcontainers.util.DockerImageNames;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.grpc.client.BlockingV2StubFactory;
import org.springframework.grpc.client.GlobalClientInterceptor;
import org.springframework.grpc.client.ImportGrpcClients;
import org.springframework.grpc.internal.GrpcHeaders;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestConstructor;
import org.springframework.util.Assert;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * @author laokou
 */
@Testcontainers
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		classes = { GrpcServerTest.GrpcTest.class, GrpcServerTest.GrpcTestConfig.class })
class GrpcServerTest {

	@Container
	static RedisContainer redisContainer = new RedisContainer(DockerImageNames.redis()).withExposedPorts(6379)
		.withReuse(true);

	@Container
	static OAuth2Container oauth2Container = new OAuth2Container(DockerImageNames.oauth2()).withReuse(true);

	@DynamicPropertySource
	static void consulProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.data.redis.port", redisContainer::getFirstMappedPort);
		registry.add("spring.security.oauth2.client.provider.local.token-uri",
				() -> "http://localhost:" + oauth2Container.getFirstMappedPort() + "/oauth2/token");
		registry.add("test.oauth2.jwk-set-uri",
				() -> "http://localhost:" + oauth2Container.getFirstMappedPort() + "/oauth2/jwks");
	}

	private final SimpleGrpc.SimpleBlockingV2Stub simpleBlockingV2Stub;

	private final RegisteredClientRepository registeredClientRepository;

	@BeforeEach
	void registerClient() {
		RegisteredClient registeredClient = RegisteredClient.withId(Constants.GRPC)
			.clientId("client-id")
			.clientSecret("{noop}client-secret")
			.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
			.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
			.scope("read")
			.build();
		registeredClientRepository.save(registeredClient);
	}

	@Test
	void test() throws StatusException {
		HelloWorldProto.HelloRequest request = HelloWorldProto.HelloRequest.newBuilder().setName("test").build();
		HelloWorldProto.HelloReply helloReply = simpleBlockingV2Stub.sayHello(request);
		Assertions.assertThat(helloReply).isNotNull();
		Assertions.assertThat(helloReply.getMessage()).isEqualTo("Hello ==> test");
	}

	@EnableSecurity
	@ImportGrpcClients(target = "test-grpc", types = SimpleGrpc.SimpleBlockingV2Stub.class,
			factory = BlockingV2StubFactory.class)
	@SpringBootApplication(scanBasePackages = { "org.laokou" })
	static class GrpcTest {

	}

	@TestConfiguration
	static class GrpcTestConfig {

		@Bean
		@Primary
		JwtDecoder testJwtDecoder(@Value("${test.oauth2.jwk-set-uri}") String jwkSetUri) {
			return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
		}

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

		@Bean
		@GlobalClientInterceptor
		ClientInterceptor clientInterceptor(OAuth2AuthorizedClientManager authorizedClientManager) {
			return new ClientInterceptor() {
				@Override
				public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method,
						CallOptions callOptions, Channel next) {
					return new ForwardingClientCall.SimpleForwardingClientCall<>(next.newCall(method, callOptions)) {
						public void start(ClientCall.Listener<RespT> responseListener, io.grpc.Metadata headers) {
							headers.put(GrpcHeaders.AUTHORIZATION_KEY, getAccessToken(authorizedClientManager));
							super.start(responseListener, headers);
						}
					};
				}
			};
		}

		private String getAccessToken(OAuth2AuthorizedClientManager authorizedClientManager) {
			OAuth2AuthorizeRequest request = OAuth2AuthorizeRequest.withClientRegistrationId(Constants.GRPC)
				.principal(Constants.GRPC)
				.build();
			OAuth2AuthorizedClient client = authorizedClientManager.authorize(request);
			Assert.notNull(client, "authorized client is null");
			return "Bearer " + client.getAccessToken().getTokenValue();
		}

	}

}
