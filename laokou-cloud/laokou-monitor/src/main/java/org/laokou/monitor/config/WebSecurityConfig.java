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
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

/**
 * @author laokou
 */
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

	private final AdminServerProperties adminServerProperties;
	private final RestTemplateBuilder restTemplateBuilder;

	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = restTemplateBuilder.build();
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		disableSsl(httpClientBuilder);
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(
				closeableHttpClient);
		restTemplate.setRequestFactory(requestFactory);
		return restTemplate;
	}

	@Bean
	@ConditionalOnMissingBean(SecurityFilterChain.class)
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
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
				.httpBasic(Customizer.withDefaults())
				.csrf(AbstractHttpConfigurer::disable).build();
	}

	static class DisableValidationTrustManager implements X509TrustManager {

		DisableValidationTrustManager() {
		}

		public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
		}

		public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[0];
		}

	}

	static class TrustAllHostnames implements HostnameVerifier {

		TrustAllHostnames() {
		}

		public boolean verify(String s, SSLSession sslSession) {
			return true;
		}

	}

	@SneakyThrows
	public static void disableSsl(HttpClientBuilder builder) {
		X509TrustManager disabledTrustManager = new DisableValidationTrustManager();
		TrustManager[] trustManagers = new TrustManager[] { disabledTrustManager };
		SSLContext sslContext = SSLContext.getInstance("SSL");
		sslContext.init(null, trustManagers, new SecureRandom());
		SSLConnectionSocketFactory sslConnectionSocketFactory = SSLConnectionSocketFactoryBuilder.create()
				.setSslContext(sslContext).setHostnameVerifier(new TrustAllHostnames()).build();
		PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = PoolingHttpClientConnectionManagerBuilder
				.create().setSSLSocketFactory(sslConnectionSocketFactory).build();
		builder.setConnectionManager(poolingHttpClientConnectionManager);
	}

}
