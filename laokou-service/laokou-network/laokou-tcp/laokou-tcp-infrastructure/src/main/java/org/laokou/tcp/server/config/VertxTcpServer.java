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

package org.laokou.tcp.server.config;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 * @author laokou
 */
@Slf4j
final class VertxTcpServer extends AbstractVerticle {

	private final SpringTcpServerProperties properties;

	private volatile Flux<NetServer> netServer;

	private boolean isClosed = false;

	VertxTcpServer(Vertx vertx, SpringTcpServerProperties properties) {
		this.vertx = vertx;
		this.properties = properties;
	}

	@Override
	public synchronized void start() {
		netServer = getTcpServerOptions().map(vertx::createNetServer)
			.doOnNext(server -> server
				.connectHandler(socket -> socket
					.handler(buffer -> log.info("【Vertx-TCP-Server】 => 服务端接收数据：{}", buffer.toString()))
					.closeHandler(close -> log.info("【Vertx-TCP-Server】 => 客户端连接关闭")))
				.listen()
				.onComplete(result -> {
					if (isClosed) {
						return;
					}
					if (result.succeeded()) {
						log.info("【Vertx-TCP-Server】 => TCP服务启动成功，端口：{}", result.result().actualPort());
					}
					else {
						Throwable ex = result.cause();
						log.error("【Vertx-TCP-Server】 => TCP服务启动失败，错误信息：{}", ex.getMessage(), ex);
					}
				}));
		netServer.subscribeOn(Schedulers.boundedElastic()).subscribe();
	}

	@Override
	public synchronized void stop() {
		isClosed = true;
		netServer.doOnNext(server -> server.close().onComplete(result -> {
			if (result.succeeded()) {
				log.info("【Vertx-TCP-Server】 => HTTP服务停止成功，端口：{}", server.actualPort());
			}
			else {
				Throwable ex = result.cause();
				log.error("【Vertx-TCP-Server】 => HTTP服务停止失败，错误信息：{}", ex.getMessage(), ex);
			}
		})).subscribeOn(Schedulers.boundedElastic()).subscribe();
	}

	public void deploy() {
		// 部署服务
		vertx.deployVerticle(this);
		// 停止服务
		Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
	}

	private Flux<NetServerOptions> getTcpServerOptions() {
		return Flux.fromIterable(properties.getPorts()).map(this::getTcpServerOption);
	}

	private NetServerOptions getTcpServerOption(int port) {
		NetServerOptions options = new NetServerOptions();
		options.setHost(properties.getHost());
		options.setPort(port);
		options.setClientAuth(properties.getClientAuth());
		options.setSni(properties.isSni());
		options.setUseProxyProtocol(properties.isUseProxyProtocol());
		options.setProxyProtocolTimeout(properties.getProxyProtocolTimeout());
		options.setProxyProtocolTimeoutUnit(properties.getProxyProtocolTimeoutUnit());
		options.setRegisterWriteHandler(properties.isRegisterWriteHandler());
		options.setAcceptBacklog(properties.getAcceptBacklog());
		return options;
	}

}
