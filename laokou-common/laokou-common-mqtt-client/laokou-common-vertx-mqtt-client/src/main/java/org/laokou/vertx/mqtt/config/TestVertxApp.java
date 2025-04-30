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

package org.laokou.vertx.mqtt.config;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import reactor.core.publisher.Sinks;

/**
 * @author laokou
 */
@Slf4j
@RequiredArgsConstructor
@SpringBootApplication(scanBasePackages = "org.laokou")
public class TestVertxApp implements CommandLineRunner {

	public static void main(String[] args) {
		new SpringApplicationBuilder(TestVertxApp.class).web(WebApplicationType.SERVLET).run(args);
	}

	@Override
	public void run(String... args) {
		Vertx vertx = Vertx.vertx();
		Sinks.Many<Buffer> messageSink = Sinks.many().multicast().onBackpressureBuffer(Integer.MAX_VALUE, false);
		MqttClientOptions options = new MqttClientOptions();
		options.setUsername("emqx")
			.setPassword("laokou123")
			.setClientId("test-client")
			.setCleanSession(false)
			.setKeepAliveInterval(60)
			.setAutoKeepAlive(true)
			.setWillQoS(1);
		MqttClient client = MqttClient.create(vertx, options);
		client.connect(1883, "127.0.0.1", connectResult -> {
			if (connectResult.succeeded()) {
				log.info("MQTT连接成功");
				client.subscribe("/test/#", 1, subscribeResult -> {
					if (subscribeResult.succeeded()) {
						log.info("MQTT订阅成功");
					}
					else {
						log.error("MQTT订阅失败");
					}
				}).publishHandler(message -> messageSink.tryEmitNext(message.payload()));
			}
			else {
				log.error("MQTT连接失败", connectResult.cause());
			}
		});
		// // 异常处理器
		// client.exceptionHandler(e -> log.error("Exception occurred: {}",
		// e.getMessage()));
		// messageSink.asFlux()
		// .doOnNext(buffer -> log.info("收到消息：{}", buffer.toString()))
		// .subscribeOn(Schedulers.boundedElastic())
		// .subscribe();
		// client.disconnect(res -> {
		// if (res.succeeded()) {
		// log.info("MQTT断开连接成功");
		// }
		// else {
		// log.error("MQTT断开连接失败");
		// }
		// }).closeHandler(v -> log.info("MQTT连接关闭"));
	}

}
