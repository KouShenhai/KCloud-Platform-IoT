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
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.laokou.common.i18n.common.exception.SystemException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchProperties;
import org.springframework.boot.autoconfigure.elasticsearch.RestClientBuilderCustomizer;
import org.springframework.boot.autoconfigure.service.connection.ConnectionDetails;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.boot.ssl.SslBundle;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.boot.ssl.SslOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;

import static org.laokou.common.i18n.utils.SslUtil.sslContext;
import static org.laokou.common.i18n.common.constant.StringConstant.EMPTY;
import static org.laokou.common.i18n.common.constant.StringConstant.RISK;

/**
 * @author laokou
 */
@Slf4j
@AutoConfiguration
@RequiredArgsConstructor
@EnableConfigurationProperties(ElasticsearchProperties.class)
class ElasticsearchAutoConfig {

	/**
	 * HTTP协议头.
	 */
	private static final String HTTP_SCHEME = "http://";

	/**
	 * HTTPS协议头.
	 */
	private static final String HTTPS_SCHEME = "https://";

	private final ElasticsearchProperties properties;

	@Bean
	@ConditionalOnMissingBean(ElasticsearchConnectionDetails.class)
	PropertiesElasticsearchConnectionDetails elasticsearchConnectionDetails() {
		return new PropertiesElasticsearchConnectionDetails(this.properties);
	}

	@Bean
	RestClientBuilderCustomizer defaultRestClientBuilderCustomizer(ElasticsearchConnectionDetails connectionDetails) {
		return new DefaultRestClientBuilderCustomizer(this.properties, connectionDetails);
	}

	@Bean
	RestClientBuilder elasticsearchRestClientBuilder(ElasticsearchConnectionDetails connectionDetails,
			ObjectProvider<RestClientBuilderCustomizer> builderCustomizers, ObjectProvider<SslBundles> sslBundles) {
		RestClientBuilder builder = RestClient.builder(connectionDetails.getNodes()
			.stream()
			.map((node) -> new HttpHost(node.hostname(), node.port(), node.protocol().getScheme()))
			.toArray(HttpHost[]::new));
		builder.setHttpClientConfigCallback((httpClientBuilder) -> {
			builderCustomizers.orderedStream().forEach((customizer) -> customizer.customize(httpClientBuilder));
			String sslBundleName = this.properties.getRestclient().getSsl().getBundle();
			if (StringUtils.hasText(sslBundleName)) {
				configureSsl(httpClientBuilder, sslBundles.getObject().getBundle(sslBundleName));
			}
			else {
				try {
					ignoreConfigureSsl(httpClientBuilder);
				}
				catch (NoSuchAlgorithmException | KeyManagementException e) {
					log.error("ignoreConfigureSsl error", e);
					throw new SystemException("S_Elasticsearch_IgnoreSslFailed", "忽略SSL验证失败", e);
				}
			}
			return httpClientBuilder;
		});
		builder.setRequestConfigCallback((requestConfigBuilder) -> {
			builderCustomizers.orderedStream().forEach((customizer) -> customizer.customize(requestConfigBuilder));
			return requestConfigBuilder;
		});
		String pathPrefix = connectionDetails.getPathPrefix();
		if (pathPrefix != null) {
			builder.setPathPrefix(pathPrefix);
		}
		builderCustomizers.orderedStream().forEach((customizer) -> customizer.customize(builder));
		return builder;
	}

	@Bean(name = "elasticsearchClient", destroyMethod = "shutdown")
	@ConditionalOnMissingBean(ElasticsearchClient.class)
	@ConditionalOnClass(RestClientBuilder.class)
	ElasticsearchClient elasticsearchClient(RestClientBuilder elasticsearchRestClientBuilder) {
		return new ElasticsearchClient(getTransport(elasticsearchRestClientBuilder));
	}

	@Bean(name = "elasticsearchAsyncClient", destroyMethod = "shutdown")
	@ConditionalOnMissingBean(ElasticsearchAsyncClient.class)
	@ConditionalOnClass(RestClientBuilder.class)
	ElasticsearchAsyncClient elasticsearchAsyncClient(RestClientBuilder elasticsearchRestClientBuilder) {
		return new ElasticsearchAsyncClient(getTransport(elasticsearchRestClientBuilder));
	}

	private ElasticsearchTransport getTransport(RestClientBuilder elasticsearchRestClientBuilder) {
		return new RestClientTransport(elasticsearchRestClientBuilder.build(), new JacksonJsonpMapper());
	}

	private void configureSsl(HttpAsyncClientBuilder httpClientBuilder, SslBundle sslBundle) {
		SSLContext sslcontext = sslBundle.createSslContext();
		SslOptions sslOptions = sslBundle.getOptions();
		httpClientBuilder.setSSLStrategy(new SSLIOSessionStrategy(sslcontext, sslOptions.getEnabledProtocols(),
				sslOptions.getCiphers(), (HostnameVerifier) null));
	}

	private void ignoreConfigureSsl(HttpAsyncClientBuilder httpClientBuilder)
			throws NoSuchAlgorithmException, KeyManagementException {
		httpClientBuilder.setSSLContext(sslContext()).setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE);
	}

	interface ElasticsearchConnectionDetails extends ConnectionDetails {

		/**
		 * List of the Elasticsearch nodes to use.
		 * @return list of the Elasticsearch nodes to use
		 */
		List<Node> getNodes();

		/**
		 * Username for authentication with Elasticsearch.
		 * @return username for authentication with Elasticsearch or {@code null}
		 */
		default String getUsername() {
			return null;
		}

		/**
		 * Password for authentication with Elasticsearch.
		 * @return password for authentication with Elasticsearch or {@code null}
		 */
		default String getPassword() {
			return null;
		}

		/**
		 * Prefix added to the path of every request sent to Elasticsearch.
		 * @return prefix added to the path of every request sent to Elasticsearch or
		 * {@code null}
		 */
		default String getPathPrefix() {
			return null;
		}

		/**
		 * An Elasticsearch node.
		 *
		 * @param hostname the hostname
		 * @param port the port
		 * @param protocol the protocol
		 * @param username the username or {@code null}
		 * @param password the password or {@code null}
		 */
		record Node(String hostname, int port, Protocol protocol, String username, String password) {

			URI toUri() {
				try {
					return new URI(this.protocol.getScheme(), userInfo(), this.hostname, this.port, null, null, null);
				}
				catch (URISyntaxException ex) {
					throw new IllegalStateException("Can't construct URI", ex);
				}
			}

			private String userInfo() {
				if (this.username == null) {
					return null;
				}
				return (this.password != null) ? (this.username + RISK + this.password) : this.username;
			}

			/**
			 * Connection protocol.
			 */
			public enum Protocol {

				/**
				 * HTTP.
				 */
				HTTP("http"),

				/**
				 * HTTPS.
				 */
				HTTPS("https");

				private final String scheme;

				Protocol(String scheme) {
					this.scheme = scheme;
				}

				static Protocol forScheme(String scheme) {
					for (Protocol protocol : values()) {
						if (protocol.scheme.equals(scheme)) {
							return protocol;
						}
					}
					throw new IllegalArgumentException("Unknown scheme '" + scheme + "'");
				}

				String getScheme() {
					return this.scheme;
				}

			}
		}

	}

	static class DefaultRestClientBuilderCustomizer implements RestClientBuilderCustomizer {

		private static final PropertyMapper PROPERTY_MAPPER = PropertyMapper.get();

		private final ElasticsearchProperties properties;

		private final ElasticsearchConnectionDetails connectionDetails;

		DefaultRestClientBuilderCustomizer(ElasticsearchProperties properties,
				ElasticsearchConnectionDetails connectionDetails) {
			this.properties = properties;
			this.connectionDetails = connectionDetails;
		}

		@Override
		public void customize(RestClientBuilder builder) {
		}

		@Override
		public void customize(HttpAsyncClientBuilder builder) {
			builder.setDefaultCredentialsProvider(new ConnectionDetailsCredentialsProvider(this.connectionDetails));
			PROPERTY_MAPPER.from(this.properties::isSocketKeepAlive)
				.to((keepAlive) -> builder
					.setDefaultIOReactorConfig(IOReactorConfig.custom().setSoKeepAlive(keepAlive).build()));
		}

		@Override
		public void customize(RequestConfig.Builder builder) {
			PROPERTY_MAPPER.from(this.properties::getConnectionTimeout)
				.whenNonNull()
				.asInt(Duration::toMillis)
				.to(builder::setConnectTimeout);
			PROPERTY_MAPPER.from(this.properties::getSocketTimeout)
				.whenNonNull()
				.asInt(Duration::toMillis)
				.to(builder::setSocketTimeout);
		}

	}

	private static class ConnectionDetailsCredentialsProvider extends BasicCredentialsProvider {

		ConnectionDetailsCredentialsProvider(ElasticsearchConnectionDetails connectionDetails) {
			String username = connectionDetails.getUsername();
			if (StringUtils.hasText(username)) {
				Credentials credentials = new UsernamePasswordCredentials(username, connectionDetails.getPassword());
				setCredentials(AuthScope.ANY, credentials);
			}
			Stream<URI> uris = getUris(connectionDetails);
			uris.filter(this::hasUserInfo).forEach(this::addUserInfoCredentials);
		}

		private Stream<URI> getUris(ElasticsearchConnectionDetails connectionDetails) {
			return connectionDetails.getNodes().stream().map(ElasticsearchConnectionDetails.Node::toUri);
		}

		private boolean hasUserInfo(URI uri) {
			return uri != null && StringUtils.hasLength(uri.getUserInfo());
		}

		private void addUserInfoCredentials(URI uri) {
			AuthScope authScope = new AuthScope(uri.getHost(), uri.getPort());
			Credentials credentials = createUserInfoCredentials(uri.getUserInfo());
			setCredentials(authScope, credentials);
		}

		private Credentials createUserInfoCredentials(String userInfo) {
			int delimiter = userInfo.indexOf(':');
			if (delimiter == -1) {
				return new UsernamePasswordCredentials(userInfo, null);
			}
			String username = userInfo.substring(0, delimiter);
			String password = userInfo.substring(delimiter + 1);
			return new UsernamePasswordCredentials(username, password);
		}

	}

	/**
	 * Adapts {@link ElasticsearchProperties} to {@link ElasticsearchConnectionDetails}.
	 */
	static class PropertiesElasticsearchConnectionDetails implements ElasticsearchConnectionDetails {

		private final ElasticsearchProperties properties;

		PropertiesElasticsearchConnectionDetails(ElasticsearchProperties properties) {
			this.properties = properties;
		}

		@Override
		public List<Node> getNodes() {
			return this.properties.getUris().stream().map(this::createNode).toList();
		}

		@Override
		public String getUsername() {
			return this.properties.getUsername();
		}

		@Override
		public String getPassword() {
			return this.properties.getPassword();
		}

		@Override
		public String getPathPrefix() {
			return this.properties.getPathPrefix();
		}

		private Node createNode(String uri) {
			if (!(uri.startsWith(HTTP_SCHEME) || uri.startsWith(HTTPS_SCHEME))) {
				uri = HTTP_SCHEME + uri;
			}
			return createNode(URI.create(uri));
		}

		private Node createNode(URI uri) {
			String userInfo = uri.getUserInfo();
			Node.Protocol protocol = Node.Protocol.forScheme(uri.getScheme());
			if (!StringUtils.hasLength(userInfo)) {
				return new Node(uri.getHost(), uri.getPort(), protocol, null, null);
			}
			int separatorIndex = userInfo.indexOf(RISK);
			if (separatorIndex == -1) {
				return new Node(uri.getHost(), uri.getPort(), protocol, userInfo, null);
			}
			String[] components = userInfo.split(RISK);
			return new Node(uri.getHost(), uri.getPort(), protocol, components[0],
					(components.length > 1) ? components[1] : EMPTY);
		}

	}

}
