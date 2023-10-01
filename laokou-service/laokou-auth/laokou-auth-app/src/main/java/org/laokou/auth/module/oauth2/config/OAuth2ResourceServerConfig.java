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

package org.laokou.auth.module.oauth2.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * @author laokou
 */
@Data
@Configuration
@RefreshScope
@ConditionalOnProperty(havingValue = "true", matchIfMissing = true, prefix = OAuth2AuthorizationServerProperties.PREFIX,
		name = "enabled")
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class OAuth2ResourceServerConfig {

	/**
	 * 不拦截拦截静态资源 如果您不想要警告消息并且需要性能优化，则可以为静态资源引入第二个过滤器链
	 * <a href="https://github.com/spring-projects/spring-security/issues/10938">...</a>
	 * @param http http
	 * @return defaultSecurityFilterChain
	 * @throws Exception Exception
	 */
	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http, OAuth2AuthorizationServerProperties properties)
			throws Exception {
		Set<String> patterns = Optional.ofNullable(properties.getRequestMatcher().getPatterns())
			.orElseGet(HashSet::new);
		return http
			.authorizeHttpRequests(request -> request
				.requestMatchers(
						patterns.stream().map(AntPathRequestMatcher::new).toArray(AntPathRequestMatcher[]::new))
				.permitAll()
				.anyRequest()
				.authenticated())
			.cors(AbstractHttpConfigurer::disable)
			// 自定义登录页面
			// https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/form.html
			// 登录页面 -> DefaultLoginPageGeneratingFilter
			.formLogin(Customizer.withDefaults())
			// 清除session
			.logout(logout -> logout.clearAuthentication(true).invalidateHttpSession(true))
			.build();
	}

}
