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

package org.laokou.udp.server;

import io.vertx.core.Vertx;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.udp.server.config.UdpServerProperties;
import org.laokou.udp.server.config.VertxUdpServerManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.util.StopWatch;
import reactor.core.publisher.Hooks;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;

/**
 * @author laokou
 */
@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties
@SpringBootApplication(scanBasePackages = "org.laokou")
public class UdpServerApp implements CommandLineRunner {

	private final Vertx vertx;

	private final UdpServerProperties properties;

	private final ExecutorService virtualThreadExecutor;

	public static void main(String[] args) throws UnknownHostException {
		StopWatch stopWatch = new StopWatch("UdpServer应用程序");
		stopWatch.start();
		String host = InetAddress.getLocalHost().getHostAddress();
		System.setProperty("address", String.format("%s:%s", host, System.getProperty("server.port", "9998")));
		System.setProperty("host", host);
		// 启用虚拟线程支持
		System.setProperty("reactor.schedulers.defaultBoundedElasticOnVirtualThreads", "true");
		// 开启reactor的上下文传递
		// https://spring.io/blog/2023/03/30/context-propagation-with-project-reactor-3-unified-bridging-between-reactive
		Hooks.enableAutomaticContextPropagation();
		new SpringApplicationBuilder(UdpServerApp.class).web(WebApplicationType.REACTIVE).run(args);
		stopWatch.stop();
		log.info("{}", stopWatch.prettyPrint());
	}

	@Override
	public void run(String... args) {
		virtualThreadExecutor.execute(this::listenMessage);
	}

	private void listenMessage() {
		VertxUdpServerManager.deploy(vertx, properties);
	}

}
