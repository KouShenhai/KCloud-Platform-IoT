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

package org.laokou.iot;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.laokou.common.core.annotation.EnableWarmUp;
import org.laokou.common.i18n.util.SslUtils;
import org.laokou.common.nacos.annotation.EnableServiceShutDown;
import org.laokou.common.nacos.annotation.EnableRouter;
import org.laokou.common.redis.annotation.EnableRedisRepository;
import org.laokou.common.security.annotation.EnableSecurity;
import org.laokou.common.websocket.annotation.EnableWebSocketServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.pulsar.annotation.EnablePulsar;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StopWatch;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * @author laokou
 */
@Slf4j
@EnablePulsar
@EnableDubbo
@EnableWarmUp
@EnableRouter
@EnableSecurity
@EnableScheduling
@EnableWebSocketServer
@EnableServiceShutDown
@EnableRedisRepository
@EnableDiscoveryClient
@EnableAspectJAutoProxy
@EnableConfigurationProperties
@MapperScan(basePackages = "org.laokou.iot.**.gatewayimpl.database")
@SpringBootApplication(exclude = { SecurityFilterAutoConfiguration.class }, scanBasePackages = "org.laokou")
public class IotApp {

	// @formatter:off
	public static void main(String[] args) throws UnknownHostException, NoSuchAlgorithmException, KeyManagementException {
		// undertow虚拟线程 => EmbeddedWebServerFactoryCustomizerAutoConfiguration#virtualThreadsUndertowDeploymentInfoCustomizer
		StopWatch stopWatch = new StopWatch("IoT应用程序");
		stopWatch.start();
		System.setProperty("address", String.format("%s:%s", InetAddress.getLocalHost().getHostAddress(), System.getProperty("server.port", "10005")));
		// SpringSecurity 子线程读取父线程的上下文
		System.setProperty(SecurityContextHolder.SYSTEM_PROPERTY, SecurityContextHolder.TTL_MODE_INHERITABLETHREADLOCAL);
		// 配置关闭nacos日志，因为nacos的log4j2导致本项目的日志不输出的问题
		System.setProperty("nacos.logging.default.config.enabled", "false");
		// 忽略SSL认证
		SslUtils.ignoreSSLTrust();
		// 启用虚拟线程支持
		System.setProperty("reactor.schedulers.defaultBoundedElasticOnVirtualThreads", "true");
		new SpringApplicationBuilder(IotApp.class).web(WebApplicationType.SERVLET).run(args);
		stopWatch.stop();
		log.info("{}", stopWatch.prettyPrint());
	}

}
