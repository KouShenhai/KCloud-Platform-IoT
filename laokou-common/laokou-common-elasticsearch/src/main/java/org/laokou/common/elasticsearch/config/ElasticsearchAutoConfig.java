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
import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.JsonpMapper;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest5_client.Rest5ClientOptions;
import co.elastic.clients.transport.rest5_client.Rest5ClientTransport;
import co.elastic.clients.transport.rest5_client.low_level.RequestOptions;
import co.elastic.clients.transport.rest5_client.low_level.Rest5Client;
import co.elastic.clients.transport.rest5_client.low_level.Rest5ClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.net.HttpHeaders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.auth.StandardAuthScheme;
import org.apache.hc.client5.http.impl.routing.DefaultProxyRoutePlanner;
import org.apache.hc.client5.http.routing.HttpRoutePlanner;
import org.apache.hc.client5.http.ssl.DefaultClientTlsStrategy;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.message.BasicHeader;
import org.apache.hc.core5.http.nio.ssl.BasicClientTlsStrategy;
import org.apache.hc.core5.util.Timeout;
import org.laokou.common.core.util.ArrayUtils;
import org.laokou.common.core.util.Base64Utils;
import org.laokou.common.elasticsearch.template.ElasticsearchTemplate;
import org.laokou.common.i18n.common.constant.StringConstants;
import org.laokou.common.i18n.util.SslUtils;
import org.laokou.common.i18n.util.StringUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.util.Assert;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * @author spring-data-elasticsearch
 * @author laokou
 */
@Slf4j
@AutoConfiguration
@RequiredArgsConstructor
@EnableConfigurationProperties(SpringElasticsearchProperties.class)
public class ElasticsearchAutoConfig {

	private final SpringElasticsearchProperties springElasticsearchProperties;

	/**
	 * Provides the {@link ElasticsearchClient} to be used.
	 *
	 * @param transport the {@link ElasticsearchTransport} to use
	 * @return ElasticsearchClient instance
	 */
	@Bean(name = "elasticsearchClient", destroyMethod = "close")
	@ConditionalOnMissingBean(ElasticsearchClient.class)
	ElasticsearchClient elasticsearchClient(ElasticsearchTransport transport) {
		Assert.notNull(transport, "transport must not be null");
		return new ElasticsearchClient(transport);
	}

	@Bean(name = "elasticsearchAsyncClient", destroyMethod = "close")
	@ConditionalOnMissingBean(ElasticsearchAsyncClient.class)
	ElasticsearchAsyncClient elasticsearchAsyncClient(ElasticsearchTransport transport) {
		return new ElasticsearchAsyncClient(transport);
	}

	@Bean(name = "elasticsearchTemplate")
	@ConditionalOnMissingBean(ElasticsearchTemplate.class)
	@ConditionalOnClass({ElasticsearchAsyncClient.class, ElasticsearchClient.class})
	ElasticsearchTemplate elasticsearchTemplate(ElasticsearchClient elasticsearchClient, ElasticsearchAsyncClient elasticsearchAsyncClient) {
		return new ElasticsearchTemplate(elasticsearchClient, elasticsearchAsyncClient);
	}

	/**
	 * Provides the Elasticsearch transport to be used. The default implementation uses the {@link Rest5Client} bean and
	 * the {@link JsonpMapper} bean provided in this class.
	 *
	 * @return the {@link ElasticsearchTransport}
	 */
	@Bean
	ElasticsearchTransport elasticsearchTransport(Rest5Client rest5Client, JsonpMapper jsonpMapper) {
		Assert.notNull(rest5Client, "restClient must not be null");
		Assert.notNull(jsonpMapper, "jsonpMapper must not be null");
		return new Rest5ClientTransport(rest5Client, jsonpMapper, getRest5ClientOptions());
	}

	/**
	 * Provides the JsonpMapper bean that is used in the {@link #elasticsearchTransport(Rest5Client, JsonpMapper)} method.
	 *
	 * @return the {@link JsonpMapper} to use
	 * @since 5.2
	 */
	@Bean
	JsonpMapper jsonpMapper() {
		ObjectMapper objectMapper = new ObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, false);
		return new JacksonJsonpMapper(objectMapper);
	}

	/**
	 * Provides the underlying low level Elasticsearch RestClient.
	 *
	 * @return RestClient
	 */
	@Bean
	Rest5Client elasticsearchRest5Client(ObjectProvider<SslBundles> sslBundles) {
		return getRest5ClientBuilder(sslBundles.getObject()).build();
	}

	private Rest5ClientBuilder getRest5ClientBuilder(SslBundles sslBundles) {
		Rest5ClientBuilder builder = Rest5Client.builder(getHttpHosts());
		String pathPrefix = springElasticsearchProperties.getPathPrefix();
		if (StringUtils.isNotEmpty(pathPrefix)) {
			builder.setPathPrefix(pathPrefix);
		}
		Header[] headers = getHeaders();
		if (ArrayUtils.isNotEmpty(headers)) {
			builder.setDefaultHeaders(headers);
		}
		Duration connectionTimeout = springElasticsearchProperties.getConnectionTimeout();
		Duration socketTimeout = springElasticsearchProperties.getSocketTimeout();

		builder.setHttpClientConfigCallback(httpAsyncClientBuilder -> {
			// 默认情况下，HTTP 客户端使用抢占式身份验证：它在初始请求中包含凭据。
			// 您可能希望使用非抢占式身份验证，即发送不带凭据的请求，并在质询后使用标头重试401 Unauthorized。
			// 为此，请设置一个HttpClientConfigCallback禁用身份验证缓存的参数。
			httpAsyncClientBuilder.disableAuthCaching();
			String proxy = springElasticsearchProperties.getProxy();
			if (StringUtils.isNotEmpty(proxy)) {
				try {
					HttpRoutePlanner proxyRoutePlanner = new DefaultProxyRoutePlanner(HttpHost.create(proxy));
					httpAsyncClientBuilder.setRoutePlanner(proxyRoutePlanner);
				} catch (URISyntaxException ex) {
					throw new IllegalArgumentException(ex);
				}
			}
		});

		builder.setConnectionConfigCallback(connectionConfigBuilder -> {
			if (!connectionTimeout.isNegative()) {
				connectionConfigBuilder.setConnectTimeout(
					Timeout.of(Math.toIntExact(connectionTimeout.toMillis()), TimeUnit.MILLISECONDS));
			}
			if (!socketTimeout.isNegative()) {
				Timeout soTimeout = Timeout.of(Math.toIntExact(socketTimeout.toMillis()), TimeUnit.MILLISECONDS);
				connectionConfigBuilder.setSocketTimeout(soTimeout);
			} else {
				connectionConfigBuilder.setSocketTimeout(Timeout.of(Rest5ClientBuilder.DEFAULT_SOCKET_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS));
			}
		});

		builder.setConnectionManagerCallback(poolingAsyncClientConnectionManagerBuilder -> {
			String sslBundle = springElasticsearchProperties.getRestclient().getSsl().getBundle();
			try {
				if (StringUtils.isNotEmpty(sslBundle)) {
					poolingAsyncClientConnectionManagerBuilder.setTlsStrategy(new BasicClientTlsStrategy(sslBundles.getBundle(sslBundle).createSslContext()));
				} else {
					poolingAsyncClientConnectionManagerBuilder.setTlsStrategy(new DefaultClientTlsStrategy(SslUtils.sslContext(), NoopHostnameVerifier.INSTANCE));
				}
			} catch (KeyManagementException | NoSuchAlgorithmException e) {
				throw new IllegalStateException("could not create the default ssl context", e);
			}
		});

		builder.setRequestConfigCallback(requestConfigBuilder -> {
			if (!socketTimeout.isNegative()) {
				Timeout soTimeout = Timeout.of(Math.toIntExact(socketTimeout.toMillis()), TimeUnit.MILLISECONDS);
				requestConfigBuilder.setConnectionRequestTimeout(soTimeout);
			} else {
				requestConfigBuilder.setConnectionRequestTimeout(Timeout.of(Rest5ClientBuilder.DEFAULT_RESPONSE_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS));
			}
		});
		return builder;
	}

	private HttpHost [] getHttpHosts() {
		List<InetSocketAddress> hosts = springElasticsearchProperties.getEndpoints().stream().map(this::parse).toList();
		boolean useSsl = springElasticsearchProperties.isUseSsl();
		return hosts.stream()
			.map(it -> (useSsl ? "https" : "http") + "://" + it.getHostString() + ':' + it.getPort())
			.map(URI::create)
			.map(HttpHost::create)
			.toArray(HttpHost[]::new);
	}

	private Header[] getHeaders() {
		List<Header> headers = new ArrayList<>();
		String username = springElasticsearchProperties.getUsername();
		String password = springElasticsearchProperties.getPassword();
		if (StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(password)) {
			headers.add(new BasicHeader(HttpHeaders.AUTHORIZATION, StandardAuthScheme.BASIC + StringConstants.SPACE + encodeBasicAuth(username, password)));
		}
		return headers.toArray(new Header[0]);
	}

	private InetSocketAddress parse(String endpoint) {
		String[] hostAndPort = endpoint.split(StringConstants.RISK);
		return InetSocketAddress.createUnresolved(hostAndPort[0], Integer.parseInt(hostAndPort[1]));
	}

	private String encodeBasicAuth(String username, String password) {
		Assert.notNull(username, "Username must not be null");
		Assert.notNull(password, "Password must not be null");
		CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
		if (encoder.canEncode(username) && encoder.canEncode(password)) {
			return Base64Utils.encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
		}
		else {
			throw new IllegalArgumentException("Username or password contains characters that cannot be encoded to " + StandardCharsets.UTF_8.displayName());
		}
	}

	private Rest5ClientOptions getRest5ClientOptions() {
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
