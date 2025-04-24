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

package org.laokou.common.openfeign;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.util.SslUtils;
import org.laokou.common.nacos.annotation.EnableServiceShutDown;
import org.laokou.common.redis.annotation.EnableRedisRepository;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * @author laokou
 */
@EnableServiceShutDown
@EnableConfigurationProperties
@EnableDiscoveryClient
@EnableRedisRepository
@EnableEncryptableProperties
@RequiredArgsConstructor
@SpringBootApplication(scanBasePackages = { "org.laokou" })
class AppTest {

	public static void main(String[] args)
			throws NoSuchAlgorithmException, KeyManagementException, UnknownHostException {
		// 忽略SSL认证
		SslUtils.ignoreSSLTrust();
		System.setProperty("jdk.internal.httpclient.disableHostnameVerification", "true");
		System.setProperty("address", String.format("%s:%s", InetAddress.getLocalHost().getHostAddress(),
				System.getProperty("server.port", "8092")));
		new SpringApplicationBuilder(AppTest.class).web(WebApplicationType.SERVLET).run(args);
	}

}
