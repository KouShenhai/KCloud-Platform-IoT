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

package org.laokou.mqtt.server;

import io.vertx.core.Vertx;
import lombok.RequiredArgsConstructor;
import org.laokou.common.network.mqtt.client.handler.ReactiveMessageHandler;
import org.laokou.mqtt.server.config.MqttServerProperties;
import org.laokou.mqtt.server.config.VertxMqttServer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import reactor.core.scheduler.Schedulers;

import java.util.List;

/**
 * @author laokou
 */
@RequiredArgsConstructor
@EnableConfigurationProperties
@SpringBootApplication(scanBasePackages = "org.laokou")
public class MqttServerApp implements CommandLineRunner {

	private final Vertx vertx;

	private final MqttServerProperties properties;

	private final List<ReactiveMessageHandler> reactiveMessageHandlers;

	public static void main(String[] args) {
		// 启用虚拟线程支持
		System.setProperty("reactor.schedulers.defaultBoundedElasticOnVirtualThreads", "true");
		new SpringApplicationBuilder(MqttServerApp.class).web(WebApplicationType.REACTIVE).run(args);
	}

	@Override
	public void run(String... args) {
		VertxMqttServer vertxMqttServer = new VertxMqttServer(vertx, properties, reactiveMessageHandlers);
		vertxMqttServer.start().subscribeOn(Schedulers.boundedElastic()).subscribe();
		vertxMqttServer.publish().subscribeOn(Schedulers.boundedElastic()).subscribe();
		Runtime.getRuntime()
			.addShutdownHook(
					new Thread(() -> vertxMqttServer.stop().subscribeOn(Schedulers.boundedElastic()).subscribe()));
	}

}
