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

package org.laokou.common.grpc.client.config;

import io.grpc.ClientInterceptor;
import io.grpc.netty.NettyChannelBuilder;
import org.jspecify.annotations.NonNull;
import org.laokou.common.grpc.client.annotation.GrpcClientBeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.ClientInterceptorsConfigurer;
import org.springframework.grpc.client.GlobalClientInterceptor;
import org.springframework.grpc.client.GrpcChannelBuilderCustomizer;
import org.springframework.grpc.client.GrpcClientFactory;
import org.springframework.grpc.client.ImportGrpcClients;
import org.springframework.grpc.client.interceptor.security.BearerTokenAuthenticationInterceptor;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author laokou
 */
@ImportGrpcClients
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(DiscoveryClient.class)
final class GrpcClientConfig {

	@Bean
	DiscoveryNameResolverProvider discoveryNameResolverProvider(DiscoveryClient discoveryClient) {
		return new DiscoveryNameResolverProvider(discoveryClient);
	}

	@Bean
	DiscoveryNameResolverRegister discoveryNameResolverRegister(
			DiscoveryNameResolverProvider discoveryNameResolverProvider) {
		return new DiscoveryNameResolverRegister(discoveryNameResolverProvider);
	}

	@Bean
	GrpcClientBeanPostProcessor grpcClientBeanPostProcessor(GrpcClientFactory grpcClientFactory) {
		return new GrpcClientBeanPostProcessor(grpcClientFactory);
	}

	@Bean
	DiscoveryGrpcChannelFactory discoveryGrpcChannelFactory(
			List<GrpcChannelBuilderCustomizer<@NonNull NettyChannelBuilder>> globalCustomizers,
			ClientInterceptorsConfigurer interceptorsConfigurer) {
		return new DiscoveryGrpcChannelFactory(globalCustomizers, interceptorsConfigurer);
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
		return new BearerTokenAuthenticationInterceptor(() -> {
			OAuth2AuthorizeRequest request = OAuth2AuthorizeRequest.withClientRegistrationId("default").principal("system").build();
			OAuth2AuthorizedClient client = authorizedClientManager.authorize(request);
			Assert.notNull(client, "authorized client is null");
			return client.getAccessToken().getTokenValue();
		});
	}

}
