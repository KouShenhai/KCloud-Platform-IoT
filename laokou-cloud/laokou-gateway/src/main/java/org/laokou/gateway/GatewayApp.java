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

package org.laokou.gateway;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.util.SslUtils;
import org.laokou.gateway.repository.NacosRouteDefinitionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.util.StopWatch;
import reactor.core.publisher.Hooks;
import reactor.core.scheduler.Schedulers;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;

/**
 * 网关服务启动类. exposeProxy=true => 使用Cglib代理，在切面中暴露代理对象，进行方法增强
 *
 * @author laokou
 */
@Slf4j
@EnableDiscoveryClient
@EnableEncryptableProperties
@EnableConfigurationProperties
@RequiredArgsConstructor
@EnableAspectJAutoProxy
@SpringBootApplication(scanBasePackages = "org.laokou",
		exclude = { RedisReactiveAutoConfiguration.class, ReactiveUserDetailsServiceAutoConfiguration.class })
public class GatewayApp implements CommandLineRunner {

	private final NacosRouteDefinitionRepository nacosRouteDefinitionRepository;

	private final ExecutorService virtualThreadExecutor;

	// @formatter:off
	public static void main(String[] args) throws UnknownHostException, NoSuchAlgorithmException, KeyManagementException {
		StopWatch stopWatch = new StopWatch("Gateway应用程序");
		stopWatch.start();
		System.setProperty("address", String.format("%s:%s", InetAddress.getLocalHost().getHostAddress(), System.getProperty("server.port", "5555")));
		// 配置关闭nacos日志，因为nacos的log4j2导致本项目的日志不输出的问题
		System.setProperty("nacos.logging.default.config.enabled", "false");
		// 忽略SSL认证
		SslUtils.ignoreSSLTrust();
		// 开启reactor的上下文传递
		// https://spring.io/blog/2023/03/30/context-propagation-with-project-reactor-3-unified-bridging-between-reactive
		Hooks.enableAutomaticContextPropagation();
		// 关闭sentinel健康检查 https://github.com/alibaba/Sentinel/issues/1494
		System.setProperty("management.health.sentinel.enabled", "false");
		// 启用虚拟线程支持
		System.setProperty("reactor.schedulers.defaultBoundedElasticOnVirtualThreads", "true");
		new SpringApplicationBuilder(GatewayApp.class).web(WebApplicationType.REACTIVE).run(args);
		stopWatch.stop();
		log.info("{}", stopWatch.prettyPrint());
	}

	@Override
	public void run(String... args) {
		// 执行同步路由任务
		virtualThreadExecutor.execute(() -> nacosRouteDefinitionRepository.syncRouter()
			.subscribeOn(Schedulers.boundedElastic())
			.subscribe());
	}
	// @formatter:on

}
