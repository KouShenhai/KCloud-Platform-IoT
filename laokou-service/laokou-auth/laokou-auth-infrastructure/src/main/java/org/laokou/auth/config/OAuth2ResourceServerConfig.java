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

package org.laokou.auth.config;

import lombok.Data;
import org.laokou.common.core.config.OAuth2ResourceServerProperties;
import org.laokou.common.core.utils.SpringUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

import static org.laokou.common.security.config.OAuth2ResourceServerConfig.customizer;

/**
 * 资源服务器配置.
 *
 * @author laokou
 */
// @formatter:off
@Data
@Configuration
@ConditionalOnProperty(havingValue = "true", matchIfMissing = true, prefix = "spring.security.oauth2.authorization-server", name = "enabled")
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
class OAuth2ResourceServerConfig {

	/**
	 * 不拦截拦截静态资源 如果您不想要警告消息并且需要性能优化，则可以为静态资源引入第二个过滤器链.
	 * <a href="https://github.com/spring-projects/spring-security/issues/10938">优化配置</a>
	 * @param http http配置
	 * @param oAuth2ResourceServerProperties OAuth2配置文件
	 * @param springUtil Spring工具类
	 * @return 认证过滤器
	 * @throws Exception 异常
	 */
	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http,
		SpringUtil springUtil,
		OAuth2ResourceServerProperties oAuth2ResourceServerProperties,
		SessionExpiredStrategy sessionExpiredStrategy,
        SessionInvalidStrategy sessionInvalidStrategy,
		SessionRegistry springSessionBackedSessionRegistry
	) throws Exception {
		return http
			// 只会在需要时创建 HttpSession【默认配置】
			//  .sessionManagement(config -> config
			// 	.sessionCreationPolicy(IF_REQUIRED)
			// 	.invalidSessionStrategy(sessionInvalidStrategy)
			// 	// 最大会话1
			// 	.maximumSessions(1)
			// 	.sessionRegistry(springSessionBackedSessionRegistry)
			// 	.expiredSessionStrategy(sessionExpiredStrategy))
			.headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer
				.httpStrictTransportSecurity(hsts -> hsts
					.includeSubDomains(true)
					.preload(true)
					.maxAgeInSeconds(31536000)))
			.authorizeHttpRequests(customizer(oAuth2ResourceServerProperties, springUtil))
			.cors(AbstractHttpConfigurer::disable)
			.csrf(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			// 自定义登录页面
			// https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/form.html
			// 登录页面 -> DefaultLoginPageGeneratingFilter
			.formLogin(Customizer.withDefaults())
			// 不记住
			.rememberMe(AbstractHttpConfigurer::disable)
			// 清除session
			.logout(logout -> logout.clearAuthentication(true).invalidateHttpSession(true))
			.build();
	}

	@Bean
	SessionRegistry springSessionBackedSessionRegistry(RedisIndexedSessionRepository redisIndexedSessionRepository) {
		return new SpringSessionBackedSessionRegistry<>(redisIndexedSessionRepository);
	}

}
