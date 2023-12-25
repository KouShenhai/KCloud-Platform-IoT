/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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
package org.laokou.common.elasticsearch.config.auto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.RestHighLevelClientBuilder;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchProperties;
import org.springframework.boot.autoconfigure.elasticsearch.RestClientBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.annotation.Bean;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.Objects;

import static org.laokou.common.i18n.common.Constant.RISK;

/**
 * es配置文件
 *
 * @author laokou
 */
@AutoConfiguration
@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties(ElasticsearchProperties.class)
public class ElasticsearchAutoConfig {

	@Bean("defaultRestClientBuilderCustomizer")
	@ConditionalOnMissingBean(RestClientBuilderCustomizer.class)
	RestClientBuilderCustomizer defaultRestClientBuilderCustomizer(ElasticsearchProperties properties) {
		return new DefaultRestClientBuilderCustomizer(properties);
	}

	@Bean("elasticsearchRestClientBuilder")
	@ConditionalOnMissingBean(RestClientBuilder.class)
	RestClientBuilder elasticsearchRestClientBuilder(ElasticsearchProperties properties,
			ObjectProvider<RestClientBuilderCustomizer> builderCustomizers) {
		HttpHost[] hosts = properties.getUris().stream().map(this::createHttpHost).toArray(HttpHost[]::new);
		RestClientBuilder builder = RestClient.builder(hosts);
		builder.setHttpClientConfigCallback((httpClientBuilder) -> {
			builderCustomizers.orderedStream().forEach((customizer) -> customizer.customize(httpClientBuilder));
			return httpClientBuilder;
		});
		builder.setRequestConfigCallback((requestConfigBuilder) -> {
			builderCustomizers.orderedStream().forEach((customizer) -> customizer.customize(requestConfigBuilder));
			return requestConfigBuilder;
		});
		if (ObjectUtil.isNotNull(properties.getPathPrefix())) {
			builder.setPathPrefix(properties.getPathPrefix());
		}
		builderCustomizers.orderedStream().forEach((customizer) -> customizer.customize(builder));
		return builder;
	}

	private HttpHost createHttpHost(String uri) {
		try {
			return createHttpHost(URI.create(uri));
		}
		catch (IllegalArgumentException ex) {
			return HttpHost.create(uri);
		}
	}

	private HttpHost createHttpHost(URI uri) {
		if (StringUtil.isEmpty(uri.getUserInfo())) {
			return HttpHost.create(uri.toString());
		}
		try {
			return HttpHost.create(new URI(uri.getScheme(), null, uri.getHost(), uri.getPort(), uri.getPath(),
					uri.getQuery(), uri.getFragment())
				.toString());
		}
		catch (URISyntaxException ex) {
			throw new IllegalStateException(ex);
		}
	}

	static class DefaultRestClientBuilderCustomizer implements RestClientBuilderCustomizer {

		private static final PropertyMapper MAP = PropertyMapper.get();

		private final ElasticsearchProperties properties;

		DefaultRestClientBuilderCustomizer(ElasticsearchProperties properties) {
			this.properties = properties;
		}

		@Override
		public void customize(RestClientBuilder builder) {
		}

		@Override
		public void customize(HttpAsyncClientBuilder builder) {
			builder.setDefaultCredentialsProvider(new PropertiesCredentialsProvider(this.properties));
			MAP.from(this.properties::isSocketKeepAlive)
				.to((keepAlive) -> builder
					.setDefaultIOReactorConfig(IOReactorConfig.custom().setSoKeepAlive(keepAlive).build()));
		}

		@Override
		public void customize(RequestConfig.Builder builder) {
			MAP.from(this.properties::getConnectionTimeout)
				.whenNonNull()
				.asInt(Duration::toMillis)
				.to(builder::setConnectTimeout);
			MAP.from(this.properties::getSocketTimeout)
				.whenNonNull()
				.asInt(Duration::toMillis)
				.to(builder::setSocketTimeout);
		}

	}

	private static class PropertiesCredentialsProvider extends BasicCredentialsProvider {

		PropertiesCredentialsProvider(ElasticsearchProperties properties) {
			if (StringUtil.isNotEmpty(properties.getUsername())) {
				Credentials credentials = new UsernamePasswordCredentials(properties.getUsername(),
						properties.getPassword());
				setCredentials(AuthScope.ANY, credentials);
			}
			properties.getUris()
				.stream()
				.map(this::toUri)
				.filter(this::hasUserInfo)
				.filter(Objects::nonNull)
				.forEach(this::addUserInfoCredentials);
		}

		private URI toUri(String uri) {
			try {
				return URI.create(uri);
			}
			catch (IllegalArgumentException ex) {
				return null;
			}
		}

		private boolean hasUserInfo(URI uri) {
			return ObjectUtil.isNotNull(uri) && StringUtil.isNotEmpty(uri.getUserInfo());
		}

		private void addUserInfoCredentials(URI uri) {
			AuthScope authScope = new AuthScope(uri.getHost(), uri.getPort());
			Credentials credentials = createUserInfoCredentials(uri.getUserInfo());
			setCredentials(authScope, credentials);
		}

		private Credentials createUserInfoCredentials(String info) {
			int delimiter = info.indexOf(RISK);
			if (delimiter == -1) {
				return new UsernamePasswordCredentials(info, null);
			}
			String username = info.substring(0, delimiter);
			String password = info.substring(delimiter + 1);
			return new UsernamePasswordCredentials(username, password);
		}

	}

	// @Bean(name = "elasticsearchClient")
	// @ConditionalOnMissingBean(ElasticsearchClient.class)
	// @ConditionalOnClass(RestClientBuilder.class)
	// public ElasticsearchClient elasticsearchClient(RestClientBuilder
	// elasticsearchRestClientBuilder) {
	// ElasticsearchTransport transport = new
	// RestClientTransport(elasticsearchRestClientBuilder.build(),
	// new JacksonJsonpMapper());
	// return new ElasticsearchClient(transport);
	// }

	@Bean(name = "restHighLevelClient", destroyMethod = "close")
	@ConditionalOnMissingBean(RestHighLevelClient.class)
	@ConditionalOnClass(RestClientBuilder.class)
	@Deprecated
	public RestHighLevelClient restHighLevelClient(RestClientBuilder elasticsearchRestClientBuilder) {
		return new RestHighLevelClientBuilder(elasticsearchRestClientBuilder.build()).setApiCompatibilityMode(true)
			.build();
	}

}
