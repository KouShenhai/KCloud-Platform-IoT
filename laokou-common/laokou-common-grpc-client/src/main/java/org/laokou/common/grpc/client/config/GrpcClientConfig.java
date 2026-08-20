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
import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.NonNull;
import org.laokou.common.core.util.RequestUtils;
import org.laokou.common.grpc.client.annotation.GrpcClientBeanPostProcessor;
import org.laokou.common.grpc.client.constant.GrpcClientConstants;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.grpc.client.autoconfigure.GrpcClientProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.ClientInterceptorsConfigurer;
import org.springframework.grpc.client.GlobalClientInterceptor;
import org.springframework.grpc.client.GrpcChannelBuilderCustomizer;
import org.springframework.grpc.client.GrpcClientFactory;
import org.springframework.grpc.client.ImportGrpcClients;
import org.springframework.grpc.client.interceptor.security.BearerTokenAuthenticationInterceptor;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author laokou
 */
@ImportGrpcClients
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(DiscoveryClient.class)
final class GrpcClientConfig {

	@Bean
	DiscoveryNameResolverProvider discoveryNameResolverProvider(DiscoveryClient discoveryClient,
			GrpcClientProperties grpcClientProperties) {
		return new DiscoveryNameResolverProvider(discoveryClient, grpcClientProperties);
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
	@GlobalClientInterceptor
	ClientInterceptor clientInterceptor(ObjectProvider<OAuth2AuthorizedToken> objectProvider) {
		return new BearerTokenAuthenticationInterceptor(() -> getAccessToken(objectProvider));
	}

	private String getAccessToken(ObjectProvider<OAuth2AuthorizedToken> objectProvider) {
		HttpServletRequest request = RequestUtils.getHttpServletRequest();
		String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (StringUtils.hasText(authorization) && authorization.startsWith(GrpcClientConstants.BEARER_PREFIX)) {
			return authorization.substring(7);
		}
		return objectProvider.getObject().getAccessToken();
	}

}
