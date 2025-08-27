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

package org.laokou.common.security.config;

import org.laokou.common.security.handler.OAuth2ExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * @author laokou
 */
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@ConditionalOnProperty(havingValue = "true", matchIfMissing = true, prefix = "spring.security.oauth2.resource-server",
	name = "enabled")
public class ReactiveOAuth2ResourceServerConfig {

	@Bean
	@Order(HIGHEST_PRECEDENCE + 1000)
	@ConditionalOnMissingBean(SecurityWebFilterChain.class)
	SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http, ReactiveGlobalOpaqueTokenIntrospector reactiveGlobalOpaqueTokenIntrospector) {
		return http
			.requestCache(ServerHttpSecurity.RequestCacheSpec::disable)
			.httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
			.csrf(ServerHttpSecurity.CsrfSpec::disable)
			.authorizeExchange(exchange -> exchange.anyExchange().authenticated())
			.oauth2ResourceServer(oauth2 -> oauth2.opaqueToken(spec -> spec.introspector(reactiveGlobalOpaqueTokenIntrospector))
				.accessDeniedHandler(OAuth2ExceptionHandler::handleAccessDenied)
				.authenticationEntryPoint((OAuth2ExceptionHandler::handleAuthentication))
			)
			.securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
			.build();
	}

}
