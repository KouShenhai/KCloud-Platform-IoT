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

package org.laokou.common.elasticsearch.config;

import co.elastic.clients.json.JsonpMapper;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest5_client.Rest5ClientOptions;
import co.elastic.clients.transport.rest5_client.Rest5ClientTransport;
import co.elastic.clients.transport.rest5_client.low_level.RequestOptions;
import co.elastic.clients.transport.rest5_client.low_level.Rest5Client;
import co.elastic.clients.transport.rest5_client.low_level.Rest5ClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.HttpHeaders;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.auth.StandardAuthScheme;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.impl.async.HttpAsyncClientBuilder;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManagerBuilder;
import org.apache.hc.client5.http.impl.routing.DefaultProxyRoutePlanner;
import org.apache.hc.client5.http.routing.HttpRoutePlanner;
import org.apache.hc.client5.http.ssl.DefaultClientTlsStrategy;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.message.BasicHeader;
import org.apache.hc.core5.reactor.IOReactorConfig;
import org.apache.hc.core5.reactor.ssl.SSLBufferMode;
import org.apache.hc.core5.util.Timeout;
import org.laokou.common.core.util.ArrayUtils;
import org.laokou.common.core.util.Base64Utils;
import org.laokou.common.i18n.common.constant.StringConstants;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.SslUtils;
import org.laokou.common.i18n.util.StringUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.boot.ssl.SslBundle;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.boot.ssl.SslOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.util.Assert;

import javax.net.ssl.SSLContext;
import java.net.URISyntaxException;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author spring project
 * @author laokou
 */
@EnableConfigurationProperties(SpringElasticsearchProperties.class)
class ElasticsearchRest5ClientConfig {

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnMissingBean(Rest5ClientBuilder.class)
	@RequiredArgsConstructor
	static class Rest5ClientBuilderConfig {

		private final SpringElasticsearchProperties springElasticsearchProperties;

		@Bean
		Rest5ClientBuilderCustomizer defaultRestClientBuilderCustomizer(ObjectProvider<SslBundles> objectProvider) {
			return new DefaultRest5ClientBuilderCustomizer(springElasticsearchProperties, objectProvider.getIfAvailable());
		}

		@Bean
		Rest5ClientBuilder elasticsearchRest5ClientBuilder(ObjectProvider<Rest5ClientBuilderCustomizer> builderCustomizers) {
			Rest5ClientBuilder builder = Rest5Client.builder(getHttpHosts());
			builder.setHttpClientConfigCallback((httpClientBuilder) -> builderCustomizers.orderedStream()
				.forEach((customizer) -> customizer.customize(httpClientBuilder)));
			builder.setConnectionManagerCallback((connectionManagerBuilder) -> builderCustomizers.orderedStream()
				.forEach((customizer) -> customizer.customize(connectionManagerBuilder)));
			builder.setConnectionConfigCallback((connectionConfigBuilder) -> builderCustomizers.orderedStream()
				.forEach((customizer) -> customizer.customize(connectionConfigBuilder)));
			builder.setRequestConfigCallback((requestConfigBuilder) -> builderCustomizers.orderedStream()
				.forEach((customizer) -> customizer.customize(requestConfigBuilder)));
			builderCustomizers.orderedStream().forEach((customizer) -> customizer.customize(builder));
			return builder;
		}

		private HttpHost[] getHttpHosts() {
			return springElasticsearchProperties.getNodes()
				.stream()
				.map(node -> new HttpHost(node.protocol().getScheme(), node.hostname(), node.port()))
				.toArray(HttpHost[]::new);
		}
	}

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnMissingBean(Rest5Client.class)
	static class Rest5ClientConfig {

		@Bean
		Rest5Client elasticsearchRestClient(Rest5ClientBuilder restClientBuilder) {
			return restClientBuilder.build();
		}

	}

	@RequiredArgsConstructor
	static class DefaultRest5ClientBuilderCustomizer implements Rest5ClientBuilderCustomizer, Ordered {

		private final SpringElasticsearchProperties springElasticsearchProperties;

		private final SslBundles sslBundles;

		@Override
		public void customize(Rest5ClientBuilder restClientBuilder) {
			String pathPrefix = springElasticsearchProperties.getPathPrefix();
			if (StringUtils.isNotEmpty(pathPrefix)) {
				restClientBuilder.setPathPrefix(pathPrefix);
			}
			Header[] headers = getHeaders();
			if (ArrayUtils.isNotEmpty(headers)) {
				restClientBuilder.setDefaultHeaders(headers);
			}
		}

		@Override
		public void customize(HttpAsyncClientBuilder httpClientBuilder) {
			// 默认情况下，HTTP 客户端使用抢占式身份验证：它在初始请求中包含凭据。
			// 您可能希望使用非抢占式身份验证，即发送不带凭据的请求，并在质询后使用标头重试401 Unauthorized。
			// 为此，请设置一个HttpClientConfigCallback禁用身份验证缓存的参数。
			httpClientBuilder.disableAuthCaching();
			String proxy = springElasticsearchProperties.getProxy();
			if (StringUtils.isNotEmpty(proxy)) {
				try {
					HttpRoutePlanner proxyRoutePlanner = new DefaultProxyRoutePlanner(HttpHost.create(proxy));
					httpClientBuilder.setRoutePlanner(proxyRoutePlanner);
				} catch (URISyntaxException ex) {
					throw new IllegalArgumentException("Could not create the proxy route planner", ex);
				}
			}
			PropertyMapper map = PropertyMapper.get();
			map.from(this.springElasticsearchProperties::isSocketKeepAlive).to((keepAlive) -> httpClientBuilder.setIOReactorConfig(IOReactorConfig.custom().setSoKeepAlive(keepAlive).build()));
		}

		@Override
		public void customize(ConnectionConfig.Builder connectionConfigBuilder) {
			PropertyMapper map = PropertyMapper.get();
			map.from(this.springElasticsearchProperties::getConnectionTimeout).as(Timeout::of).to(connectionConfigBuilder::setConnectTimeout);
			map.from(this.springElasticsearchProperties::getSocketTimeout).as(Timeout::of).to(connectionConfigBuilder::setSocketTimeout);
		}

		@Override
		public void customize(PoolingAsyncClientConnectionManagerBuilder connectionManagerBuilder) {
			SslBundle sslBundle = getSslBundle();
			if (ObjectUtils.isNotNull(sslBundle)) {
				SSLContext sslContext = sslBundle.createSslContext();
				SslOptions sslOptions = sslBundle.getOptions();
				DefaultClientTlsStrategy tlsStrategy = new DefaultClientTlsStrategy(sslContext,
					sslOptions.getEnabledProtocols(), sslOptions.getCiphers(), SSLBufferMode.STATIC,
					NoopHostnameVerifier.INSTANCE);
				connectionManagerBuilder.setTlsStrategy(tlsStrategy);
			} else {
				try {
					connectionManagerBuilder.setTlsStrategy(new DefaultClientTlsStrategy(SslUtils.sslContext(), NoopHostnameVerifier.INSTANCE));
				} catch (NoSuchAlgorithmException | KeyManagementException ex) {
					throw new IllegalArgumentException("Could not create the default ssl context", ex);
				}
			}
		}

		@Override
		public int getOrder() {
			return 0;
		}

		private SslBundle getSslBundle() {
			ElasticsearchProperties.Restclient.Ssl ssl = springElasticsearchProperties.getRestclient().getSsl();
			if (StringUtils.hasLength(ssl.getBundle())) {
				Assert.notNull(this.sslBundles, "SSL bundle name has been set but no SSL bundles found in context");
				return this.sslBundles.getBundle(ssl.getBundle());
			}
			return null;
		}

		private Header[] getHeaders() {
			List<Header> headers = new ArrayList<>(1);
			String username = springElasticsearchProperties.getUsername();
			String password = springElasticsearchProperties.getPassword();
			if (StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(password)) {
				headers.add(new BasicHeader(HttpHeaders.AUTHORIZATION, StandardAuthScheme.BASIC + StringConstants.SPACE + encodeBasicAuth(username, password)));
			}
			return headers.toArray(Header[]::new);
		}

		private String encodeBasicAuth(String username, String password) {
			Assert.notNull(username, "Username must not be null");
			Assert.notNull(password, "Password must not be null");
			CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
			if (encoder.canEncode(username) && encoder.canEncode(password)) {
				return Base64Utils.encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
			}
			else {
				throw new IllegalArgumentException(String.format("The username or password contains characters that cannot be encoded：%s" , StandardCharsets.UTF_8.displayName()));
			}
		}
	}

	@ConditionalOnMissingBean(ElasticsearchTransport.class)
	@Configuration(proxyBeanMethods = false)
	static class ElasticsearchTransportConfig {

		@Bean
		Rest5ClientTransport restClientTransport(Rest5Client restClient, JsonpMapper jsonMapper,
												 ObjectProvider<Rest5ClientOptions> restClientOptions) {
			return new Rest5ClientTransport(restClient, jsonMapper, restClientOptions.getIfAvailable());
		}

	}

	@ConditionalOnMissingBean(JsonpMapper.class)
	@ConditionalOnClass(ObjectMapper.class)
	@Configuration(proxyBeanMethods = false)
	static class JacksonJsonpMapperConfig {

		@Bean
		JacksonJsonpMapper jacksonJsonpMapper() {
			return new JacksonJsonpMapper();
		}

	}

	@RequiredArgsConstructor
	@Configuration(proxyBeanMethods = false)
	@ConditionalOnMissingBean(ElasticsearchTransport.class)
	static class Rest5ClientOptionsConfig {

		private final SpringElasticsearchProperties springElasticsearchProperties;

		@Bean
		Rest5ClientOptions rest5ClientOptions() {
			Rest5ClientOptions.Builder rest5ClientOptionsBuilder = new Rest5ClientOptions(RequestOptions.DEFAULT, false).toBuilder();
			rest5ClientOptionsBuilder.addHeader("Elasticsearch", getVersion());
			return rest5ClientOptionsBuilder.build();
		}

		private String getVersion() {
			String version = springElasticsearchProperties.getVersion();
			String clientVersion = springElasticsearchProperties.getClientVersion();
			return String.format("elasticsearch %s / elasticsearch client %s / imperative", version, clientVersion);
		}

	}

}
