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

import co.elastic.clients.transport.rest5_client.low_level.Rest5Client;
import co.elastic.clients.transport.rest5_client.low_level.Rest5ClientBuilder;
import com.google.common.net.HttpHeaders;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.auth.StandardAuthScheme;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.message.BasicHeader;
import org.laokou.common.core.util.ArrayUtils;
import org.laokou.common.core.util.Base64Utils;
import org.laokou.common.i18n.common.constant.StringConstants;
import org.laokou.common.i18n.util.StringUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author laokou
 */
class ElasticsearchRest5ClientConfig {

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnMissingBean(Rest5ClientBuilder.class)
	@RequiredArgsConstructor
	static class RestClientBuilderConfig {

		private final SpringElasticsearchProperties springElasticsearchProperties;

		@Bean
		Rest5ClientBuilder elasticsearchRest5ClientBuilder(ObjectProvider<Rest5ClientBuilderCustomizer> builderCustomizers) {
			Rest5ClientBuilder builder = Rest5Client.builder(getHttpHosts());
			String pathPrefix = springElasticsearchProperties.getPathPrefix();
			if (StringUtils.isNotEmpty(pathPrefix)) {
				builder.setPathPrefix(pathPrefix);
			}
			Header[] headers = getHeaders();
			if (ArrayUtils.isNotEmpty(headers)) {
				builder.setDefaultHeaders(headers);
			}
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
				throw new IllegalArgumentException(String.format("用户名或密码包含无法编码的字符：%s" , StandardCharsets.UTF_8.displayName()));
			}
		}

	}
}
