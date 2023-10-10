/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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
package org.laokou.common.security.config.auto;

import lombok.Data;
import org.laokou.common.security.config.GlobalOpaqueTokenIntrospector;
import org.laokou.common.security.config.OAuth2ResourceServerProperties;
import org.laokou.common.security.exception.handler.ForbiddenExceptionHandler;
import org.laokou.common.security.exception.handler.InvalidAuthenticationEntryPoint;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.laokou.common.i18n.common.Constant.TRUE;

/**
 * 关闭oauth2,请在yml配置spring.oauth2.resource-server.enabled=false
 * 关闭security，请排除SecurityAutoConfiguration、ManagementWebSecurityAutoConfiguration
 *
 * @author laokou
 */
@EnableWebSecurity
@Configuration
@EnableMethodSecurity
@AutoConfigureAfter({ OAuth2AuthorizationAutoConfig.class })
@Data
@ConditionalOnProperty(havingValue = TRUE, matchIfMissing = true, prefix = OAuth2ResourceServerProperties.PREFIX,
		name = OAuth2ResourceServerProperties.ENABLED)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class OAuth2ResourceServerAutoConfig {

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE + 1000)
	@ConditionalOnMissingBean(SecurityFilterChain.class)
	SecurityFilterChain resourceFilterChain(GlobalOpaqueTokenIntrospector globalOpaqueTokenIntrospector,
			InvalidAuthenticationEntryPoint invalidAuthenticationEntryPoint,
			ForbiddenExceptionHandler forbiddenExceptionHandler, OAuth2ResourceServerProperties properties,
			HttpSecurity http) throws Exception {
		OAuth2ResourceServerProperties.RequestMatcher requestMatcher = Optional
			.ofNullable(properties.getRequestMatcher())
			.orElseGet(OAuth2ResourceServerProperties.RequestMatcher::new);
		Set<String> patterns = Optional.ofNullable(requestMatcher.getPatterns()).orElseGet(HashSet::new);
		return http.csrf(AbstractHttpConfigurer::disable)
			.cors(AbstractHttpConfigurer::disable)
			// 基于token，关闭session
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(request -> request.requestMatchers(patterns.toArray(String[]::new))
				.permitAll()
				.anyRequest()
				.authenticated())
			// https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/opaque-token.html
			// 提供自定义OpaqueTokenIntrospector，否则回退到NimbusOpaqueTokenIntrospector
			.oauth2ResourceServer(
					oauth2 -> oauth2.opaqueToken(token -> token.introspector(globalOpaqueTokenIntrospector))
						.accessDeniedHandler(forbiddenExceptionHandler)
						.authenticationEntryPoint(invalidAuthenticationEntryPoint))
			.build();
	}

}
