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
package org.laokou.gateway.config;

import io.netty.handler.logging.LogLevel;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * @author laokou
 */
@Configuration
public class HttpConfig {

	/**
	 * 消息转换器配置
	 */
	@Bean
	@ConditionalOnMissingBean(HttpMessageConverters.class)
	public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
		return new HttpMessageConverters(converters.orderedStream().toList());
	}

	@Bean
	public WebClient webClient() {
		/*
		 * Setting maxIdleTime as 10s, because servers usually have a keepAliveTimeout of
		 * 60s, after which the connection gets closed. If the connection pool has any
		 * connection which has been idle for over 10s, it will be evicted from the pool.
		 * Refer
		 * https://github.com/reactor/reactor-netty/issues/1318#issuecomment-702668918
		 */
		ConnectionProvider connectionProvider = ConnectionProvider.builder("connectionProvider")
			.maxIdleTime(Duration.ofSeconds(10))
			.build();
		HttpClient httpClient = HttpClient.create(connectionProvider)
			.wiretap("reactor.netty.http.client.HttpClient", LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL)
			.responseTimeout(Duration.of(5, ChronoUnit.SECONDS));
		return WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient)).build();
	}

}
