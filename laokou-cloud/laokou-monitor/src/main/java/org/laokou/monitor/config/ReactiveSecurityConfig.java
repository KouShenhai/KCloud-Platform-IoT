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

package org.laokou.monitor.config;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.authentication.password.ReactiveCompromisedPasswordChecker;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiReactivePasswordChecker;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import java.net.URI;

// @formatter:off
/**
 * 响应式认证配置.
 * <a href="https://github.com/codecentric/spring-boot-admin/blob/master/spring-boot-admin-samples/spring-boot-admin-sample-reactive/src/main/java/de/codecentric/boot/admin/SpringBootAdminReactiveApplication.java">...</a>
 *
 * @author laokou
 */
// @formatter:on

@Configuration(proxyBeanMethods = false)
public class ReactiveSecurityConfig {

	@Bean
	public ReactiveCompromisedPasswordChecker reactiveCompromisedPasswordChecker() {
		return new HaveIBeenPwnedRestApiReactivePasswordChecker();
	}

	/**
	 * 自定义请求客户端（关闭ssl校验）.
	 * @return 请求客户端
	 * @throws SSLException ssl异常
	 */
	@Bean
	public ClientHttpConnector customHttpClient() throws SSLException {
		// http://docs.spring-boot-admin.com/current/security.html
		SslContext context = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
		HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(context));
		return new ReactorClientHttpConnector(httpClient);
	}

	/**
	 * 响应式认证配置.
	 * @param http http对象
	 * @param adminServerProperties admin配置
	 * @return 认证配置
	 */
	@Bean
	@ConditionalOnMissingBean(SecurityFilterChain.class)
	SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
			AdminServerProperties adminServerProperties) {
		return http
			.authorizeExchange(exchange -> exchange
				.pathMatchers(adminServerProperties.path("/assets/**"), adminServerProperties.path("/variables.css"),
						adminServerProperties.path("/actuator/**"), adminServerProperties.path("/instances/**"),
						adminServerProperties.path("/login"))
				.permitAll()
				.anyExchange()
				.authenticated())
			.formLogin(login -> login.loginPage(adminServerProperties.path("/login"))
				.authenticationSuccessHandler(loginSuccessHandler(adminServerProperties.path("/"))))
			.logout(logout -> logout.logoutUrl(adminServerProperties.path("/logout"))
				.logoutSuccessHandler(logoutSuccessHandler(adminServerProperties.path("/login?logout"))))
			.httpBasic(Customizer.withDefaults())
			.csrf(ServerHttpSecurity.CsrfSpec::disable)
			.build();
	}

	/**
	 * 构建服务注销成功处理器.
	 * @param uri uri地址
	 * @return 服务注销成功处理器
	 */
	private ServerLogoutSuccessHandler logoutSuccessHandler(String uri) {
		RedirectServerLogoutSuccessHandler successHandler = new RedirectServerLogoutSuccessHandler();
		successHandler.setLogoutSuccessUrl(URI.create(uri));
		return successHandler;
	}

	/**
	 * 构建服务认证成功处理器.
	 * @param uri uri地址
	 * @return 服务认证成功处理器
	 */
	private ServerAuthenticationSuccessHandler loginSuccessHandler(String uri) {
		RedirectServerAuthenticationSuccessHandler successHandler = new RedirectServerAuthenticationSuccessHandler();
		successHandler.setLocation(URI.create(uri));
		return successHandler;
	}

}
