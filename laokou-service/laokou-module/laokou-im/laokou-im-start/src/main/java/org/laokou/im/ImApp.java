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

package org.laokou.im;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.SneakyThrows;
import org.laokou.common.core.annotation.EnableTaskExecutor;
import org.laokou.common.nacos.annotation.EnableRouter;
import org.laokou.common.redis.annotation.EnableRedisRepository;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.actuate.autoconfigure.security.reactive.ReactiveManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.net.InetAddress;

/**
 * @author laokou
 */
@EnableRouter
@EnableTaskExecutor
@EnableRedisRepository
@SpringBootApplication(scanBasePackages = "org.laokou",
		exclude = { ReactiveSecurityAutoConfiguration.class, ReactiveManagementWebSecurityAutoConfiguration.class,
				ReactiveUserDetailsServiceAutoConfiguration.class })
@EnableEncryptableProperties
@EnableDiscoveryClient
public class ImApp {

	@SneakyThrows
	public static void main(final String[] args) {
		System.setProperty("ip", InetAddress.getLocalHost().getHostAddress());
		// 因为nacos的log4j2导致本项目的日志不输出的问题
		// 配置关闭nacos日志
		System.setProperty("nacos.logging.default.config.enabled", "false");
		// -Dtls.enable=true
		// -Dtls.client.authServer=true
		// -Dtls.client.trustCertPath=d:\\nacos.crt
		new SpringApplicationBuilder(ImApp.class).web(WebApplicationType.REACTIVE).run(args);
	}

}
