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

package org.laokou.common.monitor.config.auto;

import de.codecentric.boot.admin.client.config.ClientProperties;
import de.codecentric.boot.admin.client.config.InstanceProperties;
import de.codecentric.boot.admin.client.config.SpringBootAdminClientEnabledCondition;
import de.codecentric.boot.admin.client.registration.*;
import de.codecentric.boot.admin.client.registration.metadata.CompositeMetadataContributor;
import de.codecentric.boot.admin.client.registration.metadata.MetadataContributor;
import de.codecentric.boot.admin.client.registration.metadata.StartupDateMetadataContributor;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import jakarta.servlet.ServletContext;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.laokou.common.core.utils.HttpUtil;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementServerProperties;
import org.springframework.boot.actuate.endpoint.web.PathMappedEndpoints;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxProperties;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletPath;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import java.util.Collections;
import java.util.List;

import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;

/**
 * @author laokou
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication
@AutoConfiguration
@Conditional(SpringBootAdminClientEnabledCondition.class)
@AutoConfigureAfter({ WebEndpointAutoConfiguration.class, RestTemplateAutoConfiguration.class,
		WebClientAutoConfiguration.class })
@EnableConfigurationProperties({ ClientProperties.class, InstanceProperties.class, ServerProperties.class,
		ManagementServerProperties.class })
public class SpringBootAdminClientAutoConfig {

	@Bean
	@ConditionalOnMissingBean
	public ApplicationRegistrator registrator(RegistrationClient registrationClient, ClientProperties client,
			ApplicationFactory applicationFactory) {
		return new DefaultApplicationRegistrator(applicationFactory, registrationClient, client.getAdminUrl(),
				client.isRegisterOnce());
	}

	@Bean
	@ConditionalOnMissingBean
	public RegistrationApplicationListener registrationListener(ClientProperties client,
			ApplicationRegistrator registrator, Environment environment) {
		RegistrationApplicationListener listener = new RegistrationApplicationListener(registrator);
		listener.setAutoRegister(client.isAutoRegistration());
		listener.setAutoDeregister(client.isAutoDeregistration(environment));
		listener.setRegisterPeriod(client.getPeriod());
		return listener;
	}

	@Bean
	@ConditionalOnMissingBean
	public StartupDateMetadataContributor startupDateMetadataContributor() {
		return new StartupDateMetadataContributor();
	}

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
	@AutoConfigureAfter(DispatcherServletAutoConfiguration.class)
	public static class ServletConfiguration {

		@Bean
		@Lazy(false)
		@ConditionalOnMissingBean
		public ApplicationFactory applicationFactory(InstanceProperties instance, ManagementServerProperties management,
				ServerProperties server, ServletContext servletContext, PathMappedEndpoints pathMappedEndpoints,
				WebEndpointProperties webEndpoint, ObjectProvider<List<MetadataContributor>> metadataContributors,
				DispatcherServletPath dispatcherServletPath) {
			return new ServletApplicationFactory(instance, management, server, servletContext, pathMappedEndpoints,
					webEndpoint,
					new CompositeMetadataContributor(metadataContributors.getIfAvailable(Collections::emptyList)),
					dispatcherServletPath);
		}

	}

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
	public static class ReactiveConfiguration {

		@Bean
		@Lazy(false)
		@ConditionalOnMissingBean
		public ApplicationFactory applicationFactory(InstanceProperties instance, ManagementServerProperties management,
				ServerProperties server, PathMappedEndpoints pathMappedEndpoints, WebEndpointProperties webEndpoint,
				ObjectProvider<List<MetadataContributor>> metadataContributors, WebFluxProperties webFluxProperties) {
			return new ReactiveApplicationFactory(instance, management, server, pathMappedEndpoints, webEndpoint,
					new CompositeMetadataContributor(metadataContributors.getIfAvailable(Collections::emptyList)),
					webFluxProperties);
		}

	}

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnBean(RestTemplateBuilder.class)
	public static class BlockingRegistrationClientConfig {

		@Bean
		@ConditionalOnMissingBean
		public RegistrationClient registrationClient(ClientProperties client) {
			RestTemplateBuilder builder = new RestTemplateBuilder().setConnectTimeout(client.getConnectTimeout());
			builder.setReadTimeout(client.getReadTimeout());
			if (client.getUsername() != null && client.getPassword() != null) {
				builder = builder.basicAuthentication(client.getUsername(), client.getPassword());
			}
			RestTemplate build = builder.build();
			HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
			HttpUtil.disableSsl(httpClientBuilder);
			CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
			HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(
					closeableHttpClient);
			build.setRequestFactory(requestFactory);
			return new BlockingRegistrationClient(build);
		}

	}

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnBean(WebClient.Builder.class)
	@ConditionalOnMissingBean(RestTemplateBuilder.class)
	public static class ReactiveRegistrationClientConfig {

		@Bean
		@ConditionalOnMissingBean
		public RegistrationClient registrationClient(ClientProperties client, WebClient.Builder webClient)
				throws SSLException {
			if (client.getUsername() != null && client.getPassword() != null) {
				webClient = webClient.filter(basicAuthentication(client.getUsername(), client.getPassword()));
			}
			SslContext context = SslContextBuilder.forClient()
				.trustManager(InsecureTrustManagerFactory.INSTANCE)
				.build();
			HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(context));
			webClient.clientConnector(new ReactorClientHttpConnector(httpClient));
			return new ReactiveRegistrationClient(webClient.build(), client.getReadTimeout());
		}

	}

}
