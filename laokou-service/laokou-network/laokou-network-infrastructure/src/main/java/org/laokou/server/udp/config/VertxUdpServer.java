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

package org.laokou.server.udp.config;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.datagram.DatagramSocket;
import io.vertx.core.datagram.DatagramSocketOptions;
import lombok.extern.slf4j.Slf4j;
import org.laokou.server.AbstractVertxServer;

/**
 * @author laokou
 */
@Slf4j
final class VertxUdpServer extends AbstractVertxServer<DatagramSocket> {

	private final UdpServerProperties udpServerProperties;

	private final DatagramSocketOptions datagramSocketOptions;

	VertxUdpServer(Vertx vertx, UdpServerProperties udpServerProperties) {
		super(vertx);
		this.udpServerProperties = udpServerProperties;
		this.datagramSocketOptions = getDatagramSocketOptions(udpServerProperties);
	}

	@Override
	public Future<String> deploy0() {
		return vertx.deployVerticle(this).onComplete(res -> {
			if (res.succeeded()) {
				log.info("【Vertx-UDP-Server】 => UDP服务部署成功，端口：{}", udpServerProperties.getPort());
			}
			else {
				Throwable ex = res.cause();
				log.error("【Vertx-UDP-Server】 => UDP服务部署失败，错误信息：{}", ex.getMessage(), ex);
			}
		});
	}

	@Override
	public Future<String> undeploy0() {
		return deploymentIdFuture.onSuccess(deploymentId -> vertx.undeploy(deploymentId)).onComplete(res -> {
			if (res.succeeded()) {
				log.info("【Vertx-UDP-Server】 => UDP服务卸载成功，端口：{}", udpServerProperties.getPort());
			}
			else {
				log.error("【Vertx-UDP-Server】 => UDP服务卸载失败，错误信息：{}", res.cause().getMessage(), res.cause());
			}
		});
	}

	@Override
	public Future<DatagramSocket> start0() {
		return vertx.createDatagramSocket(datagramSocketOptions)
			.handler(packet -> log.debug("【Vertx-UDP-Server】 => 收到数据包：{}", packet.data()))
			.listen(udpServerProperties.getPort(), udpServerProperties.getHost())
			.onComplete(result -> {
				if (result.succeeded()) {
					log.info("【Vertx-UDP-Server】 => UDP服务启动成功，端口：{}", udpServerProperties.getPort());
				}
				else {
					Throwable ex = result.cause();
					log.error("【Vertx-UDP-Server】 => UDP服务启动失败，错误信息：{}", ex.getMessage(), ex);
				}
			});
	}

	@Override
	public Future<DatagramSocket> stop0() {
		return serverFuture.onSuccess(DatagramSocket::close).onComplete(result -> {
			if (result.succeeded()) {
				log.info("【Vertx-UDP-Server】 => UDP服务停止成功");
			}
			else {
				Throwable ex = result.cause();
				log.error("【Vertx-UDP-Server】 => UDP服务停止失败，错误信息：{}", ex.getMessage(), ex);
			}
		});
	}

	private DatagramSocketOptions getDatagramSocketOptions(UdpServerProperties udpServerProperties) {
		DatagramSocketOptions datagramSocketOptions = new DatagramSocketOptions();
		datagramSocketOptions.setBroadcast(udpServerProperties.isBroadcast());
		datagramSocketOptions.setLoopbackModeDisabled(udpServerProperties.isLoopbackModeDisabled());
		datagramSocketOptions.setMulticastNetworkInterface(udpServerProperties.getMulticastNetworkInterface());
		datagramSocketOptions.setIpV6(udpServerProperties.isIpV6());
		return datagramSocketOptions;
	}

}
