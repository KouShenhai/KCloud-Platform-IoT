/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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
import org.laokou.common.core.utils.MapUtil;
import org.laokou.common.core.utils.SpringUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author laokou
 */
public class OAuth2ResourceConfig {

	// @formatter:off
	@NotNull
	public static Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> customizer(
		OAuth2ResourceServerProperties oAuth2ResourceServerProperties, SpringUtil springUtil) {
		Map<String, Set<String>> uriMap = MapUtil.toUriMap(oAuth2ResourceServerProperties.getRequestMatcher().getIgnorePatterns(), springUtil.getServiceId());
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

	@Bean
	SecurityFilterChain resourceClient(HttpSecurity http, SpringUtil springUtil,
			OAuth2ResourceServerProperties oAuth2ResourceServerProperties) throws Exception {
		return http.authorizeHttpRequests(customizer(oAuth2ResourceServerProperties, springUtil))
			.requestCache(AbstractHttpConfigurer::disable)
			.sessionManagement(AbstractHttpConfigurer::disable)
			.securityContext(AbstractHttpConfigurer::disable)
			.csrf(AbstractHttpConfigurer::disable)
			.cors(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			// 基于token，关闭session
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.build();
	}

}
