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

package org.laokou.monitor.config;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import jakarta.servlet.DispatcherType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;

/**
 * @author laokou
 */
@Configuration
public class WebSecurityConfig {

	@Bean
	public ClientHttpConnector customHttpClient() throws SSLException {
		// http://docs.spring-boot-admin.com/current/security.html
		SslContext context = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
		HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(context));
		return new ReactorClientHttpConnector(httpClient);
	}

	@Bean
	@ConditionalOnMissingBean(SecurityFilterChain.class)
	SecurityFilterChain securityFilterChain(HttpSecurity http, AdminServerProperties adminServerProperties)
			throws Exception {
		SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
		successHandler.setTargetUrlParameter("redirectTo");
		successHandler.setDefaultTargetUrl(adminServerProperties.path("/"));
		return http
				.authorizeHttpRequests(request -> request
						.requestMatchers(adminServerProperties.path("/assets/**"),
								adminServerProperties.path("/variables.css"),
								adminServerProperties.path("/actuator/**"), adminServerProperties.path("/instances/**"),
								adminServerProperties.path("/login"))
						.permitAll().dispatcherTypeMatchers(DispatcherType.ASYNC).permitAll().anyRequest()
						.authenticated())
				.formLogin(
						login -> login.loginPage(adminServerProperties.path("/login")).successHandler(successHandler))
				.logout(logout -> logout.logoutUrl(adminServerProperties.path("/logout")))
				.csrf(AbstractHttpConfigurer::disable).build();
	}

}
