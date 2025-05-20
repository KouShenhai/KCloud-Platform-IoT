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

package org.laokou.udp.server.config;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.datagram.DatagramSocket;
import io.vertx.core.datagram.DatagramSocketOptions;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 * @author laokou
 */
@Slf4j
public final class VertxUdpServer extends AbstractVerticle {

	private volatile Flux<DatagramSocket> datagramSocket;

	private final UdpServerProperties udpServerProperties;

	private boolean isClosed = false;

	VertxUdpServer(Vertx vertx, UdpServerProperties udpServerProperties) {
		this.udpServerProperties = udpServerProperties;
		this.vertx = vertx;
	}

	@Override
	public synchronized void start() {
		datagramSocket = Flux.fromIterable(udpServerProperties.getPorts()).map(port -> {
			DatagramSocket datagramSocket = vertx.createDatagramSocket(getDatagramSocketOption())
				.handler(packet -> log.info("【Vertx-UDP-Server】 => 收到数据包：{}", packet.data()));
			datagramSocket.listen(port, udpServerProperties.getHost()).onComplete(result -> {
				if (isClosed) {
					return;
				}
				if (result.succeeded()) {
					log.info("【Vertx-UDP-Server】 => UDP服务启动成功，端口：{}", port);
				}
				else {
					Throwable ex = result.cause();
					log.error("【Vertx-UDP-Server】 => UDP服务启动失败，错误信息：{}", ex.getMessage(), ex);
				}
			});
			return datagramSocket;
		});
		datagramSocket.subscribeOn(Schedulers.boundedElastic()).subscribe();
	}

	@Override
	public synchronized void stop() {
		isClosed = true;
		datagramSocket.doOnNext(socket -> socket.close().onComplete(result -> {
			if (result.succeeded()) {
				log.info("【Vertx-UDP-Server】 => UDP服务停止成功");
			}
			else {
				Throwable ex = result.cause();
				log.error("【Vertx-UDP-Server】 => UDP服务停止失败，错误信息：{}", ex.getMessage(), ex);
			}
		})).subscribeOn(Schedulers.boundedElastic()).subscribe();
	}

	public void deploy() {
		// 部署服务
		vertx.deployVerticle(this);
		// 停止服务
		Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
	}

	private DatagramSocketOptions getDatagramSocketOption() {
		DatagramSocketOptions datagramSocketOptions = new DatagramSocketOptions();
		datagramSocketOptions.setBroadcast(udpServerProperties.isBroadcast());
		datagramSocketOptions.setLoopbackModeDisabled(udpServerProperties.isLoopbackModeDisabled());
		datagramSocketOptions.setMulticastNetworkInterface(udpServerProperties.getMulticastNetworkInterface());
		datagramSocketOptions.setIpV6(udpServerProperties.isIpV6());
		return datagramSocketOptions;
	}

}
