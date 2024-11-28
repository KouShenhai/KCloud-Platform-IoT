/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.core.config;

import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.ThreadUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import java.net.http.HttpClient;
import java.util.concurrent.ExecutorService;

import static org.laokou.common.core.utils.HttpUtil.getHttpClient;
import static org.laokou.common.i18n.utils.SslUtil.sslContext;

/**
 * @author laokou
 */
@Slf4j
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class RestClientConfig {

	@Bean(bootstrap = Bean.Bootstrap.BACKGROUND)
	public RestClient jdkRestClient() {
		log.info("{} => Initializing JDK RestClient", Thread.currentThread().getName());
		// 虚拟线程
		ExecutorService executor = ThreadUtil.newVirtualTaskExecutor();
		HttpClient httpClient = HttpClient.newBuilder().sslContext(sslContext()).build();
		JdkClientHttpRequestFactory factory = new JdkClientHttpRequestFactory(httpClient, executor);
		return RestClient.builder().requestFactory(factory).build();
	}

	@Bean(bootstrap = Bean.Bootstrap.BACKGROUND)
	public RestClient restClient() {
		log.info("{} => Initializing Default RestClient", Thread.currentThread().getName());
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setHttpClient(getHttpClient(true));
		return RestClient.builder().requestFactory(factory).build();
	}

}
