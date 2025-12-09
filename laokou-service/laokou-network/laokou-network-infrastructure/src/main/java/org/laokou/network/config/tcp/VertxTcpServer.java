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

package org.laokou.network.config.tcp;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;
import lombok.extern.slf4j.Slf4j;
import org.laokou.network.config.AbstractVertxService;

/**
 * @author laokou
 */
@Slf4j
final class VertxTcpServer extends AbstractVertxService<NetServer> {

	private final NetServerOptions netServerOptions;

	VertxTcpServer(Vertx vertx, TcpServerProperties tcpServerProperties) {
		super(vertx);
		this.netServerOptions = getTcpServerOptions(tcpServerProperties);
	}

	@Override
	public Future<String> doDeploy() {
		return vertx.deployVerticle(this).onComplete(res -> {
			if (res.succeeded()) {
				log.info("【Vertx-TCP-Server】 => TCP服务部署成功，端口：{}", netServerOptions.getPort());
			}
			else {
				Throwable ex = res.cause();
				log.error("【Vertx-TCP-Server】 => TCP服务部署失败，错误信息：{}", ex.getMessage(), ex);
			}
		});
	}

	@Override
	public Future<String> doUndeploy() {
		return deploymentIdFuture.onSuccess(deploymentId -> vertx.undeploy(deploymentId)).onComplete(res -> {
			if (res.succeeded()) {
				log.info("【Vertx-TCP-Server】 => TCP服务卸载成功，端口：{}", netServerOptions.getPort());
			}
			else {
				log.error("【Vertx-TCP-Server】 => TCP服务卸载失败，错误信息：{}", res.cause().getMessage(), res.cause());
			}
		});
	}

	@Override
	public Future<NetServer> doStart() {
		return vertx.createNetServer(netServerOptions)
			.connectHandler(
					socket -> socket.handler(buffer -> log.debug("【Vertx-TCP-Server】 => 服务端接收数据：{}", buffer.toString()))
						.closeHandler(_ -> log.info("【Vertx-TCP-Server】 => 客户端连接关闭")))
			.listen()
			.onComplete(result -> {
				if (result.succeeded()) {
					log.info("【Vertx-TCP-Server】 => TCP服务启动成功，端口：{}", result.result().actualPort());
				}
				else {
					Throwable ex = result.cause();
					log.error("【Vertx-TCP-Server】 => TCP服务启动失败，错误信息：{}", ex.getMessage(), ex);
				}
			});
	}

	@Override
	public Future<NetServer> doStop() {
		return serverFuture.onSuccess(NetServer::close).onComplete(result -> {
			if (result.succeeded()) {
				log.info("【Vertx-TCP-Server】 => HTTP服务停止成功，端口：{}", netServerOptions.getPort());
			}
			else {
				Throwable ex = result.cause();
				log.error("【Vertx-TCP-Server】 => HTTP服务停止失败，错误信息：{}", ex.getMessage(), ex);
			}
		});
	}

	private NetServerOptions getTcpServerOptions(TcpServerProperties tcpServerProperties) {
		NetServerOptions options = new NetServerOptions();
		options.setHost(tcpServerProperties.getHost());
		options.setPort(tcpServerProperties.getPort());
		options.setClientAuth(tcpServerProperties.getClientAuth());
		options.setSni(tcpServerProperties.isSni());
		options.setUseProxyProtocol(tcpServerProperties.isUseProxyProtocol());
		options.setProxyProtocolTimeout(tcpServerProperties.getProxyProtocolTimeout());
		options.setProxyProtocolTimeoutUnit(tcpServerProperties.getProxyProtocolTimeoutUnit());
		options.setRegisterWriteHandler(tcpServerProperties.isRegisterWriteHandler());
		options.setAcceptBacklog(tcpServerProperties.getAcceptBacklog());
		return options;
	}

}
