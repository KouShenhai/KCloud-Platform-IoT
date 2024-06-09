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

import com.alibaba.nacos.common.tls.TlsSystemConfig;
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
import org.springframework.util.ResourceUtils;

import java.net.InetAddress;
import static org.laokou.common.i18n.common.constants.StringConstant.TRUE;

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
		System.setProperty(TlsSystemConfig.TLS_ENABLE, TRUE);
		System.setProperty(TlsSystemConfig.CLIENT_AUTH, TRUE);
		System.setProperty(TlsSystemConfig.CLIENT_TRUST_CERT, System.getProperty(TlsSystemConfig.CLIENT_TRUST_CERT, ResourceUtils.getFile("classpath:nacos.crt").getCanonicalPath()));
		new SpringApplicationBuilder(ImApp.class).web(WebApplicationType.REACTIVE).run(args);
	}

}
