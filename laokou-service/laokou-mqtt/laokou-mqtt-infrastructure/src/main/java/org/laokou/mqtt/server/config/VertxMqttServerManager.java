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

package org.laokou.mqtt.server.config;

import io.vertx.core.Vertx;
import org.laokou.common.network.mqtt.client.handler.ReactiveMessageHandler;
import reactor.core.Disposable;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author laokou
 */
public final class VertxMqttServerManager {

	private volatile static VertxMqttServer vertxMqttServer;

	private static final List<Disposable> DISPOSABLES = new CopyOnWriteArrayList<>();

	private VertxMqttServerManager() {
	}

	public static void start(final Vertx vertx, final MqttServerProperties properties,
			final List<ReactiveMessageHandler> reactiveMessageHandlers) {
		vertxMqttServer = new VertxMqttServer(vertx, properties, reactiveMessageHandlers);
		// 启动服务
		DISPOSABLES.add(vertxMqttServer.start().subscribeOn(Schedulers.boundedElastic()).subscribe());
		// 推送数据
		DISPOSABLES.add(vertxMqttServer.publish().subscribeOn(Schedulers.boundedElastic()).subscribe());
	}

	public static void stop() {
		// 停止服务
		DISPOSABLES.add(vertxMqttServer.stop().subscribeOn(Schedulers.boundedElastic()).subscribe());
		try {
			Thread.sleep(3000);
			// 取消订阅
			disposes();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException(e);
		}
	}

	private static void disposes() {
		for (Disposable disposable : DISPOSABLES) {
			if (!Objects.isNull(disposable) && !disposable.isDisposed()) {
				disposable.dispose();
			}
		}
	}

}
