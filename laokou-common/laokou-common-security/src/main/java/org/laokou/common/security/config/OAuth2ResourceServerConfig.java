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
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * 关闭OAuth2,请在yml配置spring.oauth2.resource-server.enabled=false
 * 关闭security，请排除SecurityAutoConfiguration、ManagementWebSecurityAutoConfiguration.
 *
 * @author laokou
 */
@EnableWebSecurity
@EnableMethodSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnProperty(havingValue = "true", matchIfMissing = true, prefix = "spring.security.oauth2.resource-server",
		name = "enabled")
public class OAuth2ResourceServerConfig {

	// @formatter:off
	@NotNull
	public static Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> customizer(
		OAuth2ResourceServerProperties oAuth2ResourceServerProperties, SpringUtils springUtils) {
		Map<String, Set<String>> uriMap = MapUtils.toUriMap(oAuth2ResourceServerProperties.getRequestMatcher().getIgnorePatterns(), springUtils.getServiceId());
		return request -> request
			.requestMatchers(HttpMethod.GET, Optional.ofNullable(uriMap.get(HttpMethod.GET.name())).orElseGet(HashSet::new).toArray(String[]::new))
			.permitAll()
			.requestMatchers(HttpMethod.POST, Optional.ofNullable(uriMap.get(HttpMethod.POST.name())).orElseGet(HashSet::new).toArray(String[]::new))
			.permitAll()
			.requestMatchers(HttpMethod.PUT, Optional.ofNullable(uriMap.get(HttpMethod.PUT.name())).orElseGet(HashSet::new).toArray(String[]::new))
			.permitAll()
			.requestMatchers(HttpMethod.DELETE, Optional.ofNullable(uriMap.get(HttpMethod.DELETE.name())).orElseGet(HashSet::new).toArray(String[]::new))
			.permitAll()
			.requestMatchers(HttpMethod.HEAD, Optional.ofNullable(uriMap.get(HttpMethod.HEAD.name())).orElseGet(HashSet::new).toArray(String[]::new))
			.permitAll()
			.requestMatchers(HttpMethod.PATCH, Optional.ofNullable(uriMap.get(HttpMethod.PATCH.name())).orElseGet(HashSet::new).toArray(String[]::new))
			.permitAll()
			.anyRequest()
			.authenticated();
	}
	// @formatter:on

	// @formatter:off
	@Bean
	@Order(HIGHEST_PRECEDENCE + 1000)
	@ConditionalOnMissingBean(SecurityFilterChain.class)
	SecurityFilterChain resourceFilterChain(GlobalOpaqueTokenIntrospector globalOpaqueTokenIntrospector,
			SpringUtils springUtils, OAuth2ResourceServerProperties oAuth2ResourceServerProperties, HttpSecurity http)
			throws Exception {
		return http
			.headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer
				.httpStrictTransportSecurity(hsts -> hsts
					.includeSubDomains(true)
					.preload(true)
					.maxAgeInSeconds(31536000)))
			.requestCache(AbstractHttpConfigurer::disable)
			.sessionManagement(AbstractHttpConfigurer::disable)
			.securityContext(AbstractHttpConfigurer::disable)
			.csrf(AbstractHttpConfigurer::disable)
			.cors(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			// 基于token，关闭session
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(customizer(oAuth2ResourceServerProperties, springUtils))
			// https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/opaque-token.html
			// 提供自定义OpaqueTokenIntrospector，否则回退到NimbusOpaqueTokenIntrospector
			.oauth2ResourceServer(resource -> resource
						.opaqueToken(token -> token.introspector(globalOpaqueTokenIntrospector))
						.accessDeniedHandler((request, response, ex) -> OAuth2ExceptionHandler.handleAccessDenied(response, ex))
						.authenticationEntryPoint((request, response, ex) -> OAuth2ExceptionHandler.handleAuthentication(response, ex)))
			.build();
	}
	// @formatter:on

}
