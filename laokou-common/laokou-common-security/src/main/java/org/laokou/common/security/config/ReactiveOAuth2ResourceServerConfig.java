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

import jakarta.validation.constraints.NotNull;
import org.laokou.common.core.config.OAuth2ResourceServerProperties;
import org.laokou.common.core.util.MapUtils;
import org.laokou.common.core.util.SpringUtils;
import org.laokou.common.security.handler.OAuth2ExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
	SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http,
												ReactiveOAuth2OpaqueTokenIntrospector reactiveOAuth2OpaqueTokenIntrospector,
												OAuth2ResourceServerProperties oAuth2ResourceServerProperties,
												SpringUtils springUtils) {
		return http
			.requestCache(ServerHttpSecurity.RequestCacheSpec::disable)
			.httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
			.csrf(ServerHttpSecurity.CsrfSpec::disable)
			.requestCache(ServerHttpSecurity.RequestCacheSpec::disable)
			.authorizeExchange(customizer(oAuth2ResourceServerProperties, springUtils))
			.oauth2ResourceServer(oauth2 -> oauth2.opaqueToken(spec -> spec.introspector(reactiveOAuth2OpaqueTokenIntrospector))
				.accessDeniedHandler(OAuth2ExceptionHandler::handleAccessDenied)
				.authenticationEntryPoint((OAuth2ExceptionHandler::handleAuthentication))
			)
			.securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
			.build();
	}

	// @formatter:off
	@NotNull
	private Customizer<ServerHttpSecurity.AuthorizeExchangeSpec> customizer(OAuth2ResourceServerProperties oAuth2ResourceServerProperties, SpringUtils springUtils) {
		Map<String, Set<String>> uriMap = MapUtils.toUriMap(oAuth2ResourceServerProperties.getRequestMatcher().getIgnorePatterns(), springUtils.getServiceId());
		return request -> request.pathMatchers(HttpMethod.GET, Optional.ofNullable(uriMap.get(HttpMethod.GET.name())).orElseGet(HashSet::new).toArray(String[]::new))
			.permitAll()
			.pathMatchers(HttpMethod.POST, Optional.ofNullable(uriMap.get(HttpMethod.POST.name())).orElseGet(HashSet::new).toArray(String[]::new))
			.permitAll()
			.pathMatchers(HttpMethod.PUT, Optional.ofNullable(uriMap.get(HttpMethod.PUT.name())).orElseGet(HashSet::new).toArray(String[]::new))
			.permitAll()
			.pathMatchers(HttpMethod.DELETE, Optional.ofNullable(uriMap.get(HttpMethod.DELETE.name())).orElseGet(HashSet::new).toArray(String[]::new))
			.permitAll()
			.pathMatchers(HttpMethod.HEAD, Optional.ofNullable(uriMap.get(HttpMethod.HEAD.name())).orElseGet(HashSet::new).toArray(String[]::new))
			.permitAll()
			.pathMatchers(HttpMethod.PATCH, Optional.ofNullable(uriMap.get(HttpMethod.PATCH.name())).orElseGet(HashSet::new).toArray(String[]::new))
			.permitAll()
			.anyExchange()
			.authenticated();
	}
	// @formatter:on

}
