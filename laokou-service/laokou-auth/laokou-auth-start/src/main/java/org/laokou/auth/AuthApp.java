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

package org.laokou.auth;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.annotation.EnableTaskExecutor;
import org.laokou.common.core.annotation.EnableWarmUp;
import org.laokou.common.i18n.util.SslUtils;
import org.laokou.common.log4j2.annotation.EnableLog4j2ShutDown;
import org.laokou.common.nacos.annotation.EnableNacosShutDown;
import org.laokou.common.nacos.annotation.EnableRouter;
import org.laokou.common.redis.annotation.EnableRedisRepository;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.server.servlet.OAuth2AuthorizationServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.server.servlet.OAuth2AuthorizationServerJwtAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StopWatch;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * 认证服务启动类. exposeProxy=true => 使用Cglib代理，在切面中暴露代理对象，进行方法增强
 *
 * @author laokou
 */
@Slf4j
@EnableRouter
@EnableWarmUp
@EnableScheduling
@EnableTaskExecutor
@EnableLog4j2ShutDown
@EnableNacosShutDown
@EnableRedisRepository
@EnableDiscoveryClient
@EnableEncryptableProperties
@EnableConfigurationProperties
@EnableAspectJAutoProxy
@SpringBootApplication(scanBasePackages = "org.laokou", exclude = { OAuth2AuthorizationServerAutoConfiguration.class,
		OAuth2AuthorizationServerJwtAutoConfiguration.class })
public class AuthApp {

	// @formatter:off
    /// ```properties
    /// -Dcsp.sentinel.api.port=8722
    /// -Dserver.port=1111
    /// ```
    /// ```properties
    /// client_id => 95TxSsTPFA3tF12TBSMmUVK0da
    /// client_secret => FpHwIfw4wY92dO
    /// ```
	public static void main(String[] args) throws UnknownHostException, NoSuchAlgorithmException, KeyManagementException {
		// undertow虚拟线程 => EmbeddedWebServerFactoryCustomizerAutoConfiguration#virtualThreadsUndertowDeploymentInfoCustomizer
		StopWatch stopWatch = new StopWatch("Auth应用程序");
		stopWatch.start();
		// 配置关闭nacos日志，因为nacos的log4j2导致本项目的日志不输出的问题
		System.setProperty("nacos.logging.default.config.enabled", "false");
		System.setProperty("address", String.format("%s:%s", InetAddress.getLocalHost().getHostAddress(), System.getProperty("server.port", "1111")));
		// SpringSecurity 子线程读取父线程的上下文
		System.setProperty(SecurityContextHolder.SYSTEM_PROPERTY, SecurityContextHolder.TTL_MODE_INHERITABLETHREADLOCAL);
		// nacos认证 => HttpLoginProcessor，https://github.com/alibaba/nacos/pull/3654
		// 关闭sentinel健康检查 https://github.com/alibaba/Sentinel/issues/1494
		System.setProperty("management.health.sentinel.enabled", "false");
		// 忽略SSL认证
		SslUtils.ignoreSSLTrust();
		new SpringApplicationBuilder(AuthApp.class).web(WebApplicationType.SERVLET).run(args);
		stopWatch.stop();
		log.info("{}", stopWatch.prettyPrint());
	}
    // @formatter:on

}
