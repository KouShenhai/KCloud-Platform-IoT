/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.network.config.http;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.util.RegexUtils;
import org.laokou.network.model.HttpMessageEnum;
import org.laokou.network.model.WebsocketMessageEnum;
import org.laokou.network.config.AbstractVertxService;

/**
 * @author laokou
 */
@Slf4j
final class VertxHttpServer extends AbstractVertxService<HttpServer> {

	private final HttpServerOptions httpServerOptions;

	VertxHttpServer(final Vertx vertx, HttpServerProperties httpServerProperties) {
		super(vertx);
		this.httpServerOptions = getHttpServerOptions(httpServerProperties);
	}

	@Override
	public Future<String> doDeploy() {
		return super.vertx.deployVerticle(this).onComplete(res -> {
			if (res.succeeded()) {
				log.info("【Vertx-HTTP-Server】 => HTTP服务部署成功，端口：{}", httpServerOptions.getPort());
			}
			else {
				Throwable ex = res.cause();
				log.error("【Vertx-HTTP-Server】 => HTTP服务部署失败，错误信息：{}", ex.getMessage(), ex);
			}
		});
	}

	@Override
	public Future<String> doUndeploy() {
		return deploymentIdFuture.onSuccess(deploymentId -> this.vertx.undeploy(deploymentId)).onComplete(res -> {
			if (res.succeeded()) {
				log.info("【Vertx-HTTP-Server】 => HTTP服务卸载成功，端口：{}", httpServerOptions.getPort());
			}
			else {
				log.error("【Vertx-HTTP-Server】 => HTTP服务卸载失败，错误信息：{}", res.cause().getMessage(), res.cause());
			}
		});
	}

	@Override
	public Future<HttpServer> doStart() {
		return super.vertx.createHttpServer(httpServerOptions).webSocketHandler(serverWebSocket -> {
			if (!RegexUtils.matches(WebsocketMessageEnum.UP_PROPERTY_REPORT.getPath(), serverWebSocket.path())) {
				serverWebSocket.close();
				return;
			}
			serverWebSocket.textMessageHandler(message -> log.info("【Vertx-WebSocket-Server】 => 收到消息：{}", message))
				.closeHandler(_ -> log.error("【Vertx-WebSocket-Server】 => 断开连接"))
				.exceptionHandler(err -> log.error("【Vertx-WebSocket-Server】 => 错误信息：{}", err.getMessage(), err))
				.endHandler(_ -> log.error("【Vertx-WebSocket-Server】 => 结束连接"));
		}).requestHandler(getRouter()).listen().onComplete(completionHandler -> {
			if (completionHandler.succeeded()) {
				log.info("【Vertx-HTTP-Server】 => HTTP服务启动成功，端口：{}", httpServerOptions.getPort());
			}
			else {
				Throwable ex = completionHandler.cause();
				log.error("【Vertx-HTTP-Server】 => HTTP服务启动失败，错误信息：{}", ex.getMessage(), ex);
			}
		});
	}

	@Override
	public Future<HttpServer> doStop() {
		return serverFuture.onSuccess(HttpServer::close).onComplete(result -> {
			if (result.succeeded()) {
				log.info("【Vertx-HTTP-Server】 => HTTP服务停止成功，端口：{}", httpServerOptions.getPort());
			}
			else {
				Throwable ex = result.cause();
				log.error("【Vertx-HTTP-Server】 => HTTP服务停止失败，错误信息：{}", ex.getMessage(), ex);
			}
		});
	}

	private Router getRouter() {
		Router router = Router.router(super.vertx);
		router.route().handler(BodyHandler.create());
		router.post(HttpMessageEnum.UP_PROPERTY_REPORT.getRouter()).handler(ctx -> {
			String body = ctx.body().asString();
			Long productId = Long.valueOf(ctx.pathParam("productId"));
			Long deviceId = Long.valueOf(ctx.pathParam("deviceId"));
			log.debug("productId:{}，deviceId:{}，body：{}", productId, deviceId, body);
			ctx.response().end();
		});
		return router;
	}

	private HttpServerOptions getHttpServerOptions(HttpServerProperties httpServerProperties) {
		HttpServerOptions options = new HttpServerOptions();
		options.setHost(httpServerProperties.getHost());
		options.setPort(httpServerProperties.getPort());
		options.setCompressionSupported(httpServerProperties.isCompressionSupported());
		options.setDecompressionSupported(httpServerProperties.isDecompressionSupported());
		options.setCompressionLevel(httpServerProperties.getCompressionLevel());
		options.setMaxWebSocketFrameSize(httpServerProperties.getMaxWebSocketFrameSize());
		options.setMaxWebSocketMessageSize(httpServerProperties.getMaxWebSocketMessageSize());
		options.setHandle100ContinueAutomatically(httpServerProperties.isHandle100ContinueAutomatically());
		options.setMaxChunkSize(httpServerProperties.getMaxChunkSize());
		options.setMaxInitialLineLength(httpServerProperties.getMaxInitialLineLength());
		options.setMaxHeaderSize(httpServerProperties.getMaxHeaderSize());
		options.setMaxFormAttributeSize(httpServerProperties.getMaxFormAttributeSize());
		options.setMaxFormFields(httpServerProperties.getMaxFormFields());
		options.setMaxFormBufferedBytes(httpServerProperties.getMaxFormBufferedBytes());
		options.setInitialSettings(httpServerProperties.getInitialSettings());
		options.setAlpnVersions(httpServerProperties.getAlpnVersions());
		options.setHttp2ClearTextEnabled(httpServerProperties.isHttp2ClearTextEnabled());
		options.setHttp2ConnectionWindowSize(httpServerProperties.getHttp2ConnectionWindowSize());
		options.setDecoderInitialBufferSize(httpServerProperties.getDecoderInitialBufferSize());
		options
			.setPerFrameWebSocketCompressionSupported(httpServerProperties.isPerFrameWebSocketCompressionSupported());
		options.setPerMessageWebSocketCompressionSupported(
				httpServerProperties.isPerMessageWebSocketCompressionSupported());
		options.setWebSocketCompressionLevel(httpServerProperties.getWebSocketCompressionLevel());
		options.setWebSocketAllowServerNoContext(httpServerProperties.isWebSocketAllowServerNoContext());
		options.setWebSocketPreferredClientNoContext(httpServerProperties.isWebSocketPreferredClientNoContext());
		options.setWebSocketClosingTimeout(httpServerProperties.getWebSocketClosingTimeout());
		options.setTracingPolicy(httpServerProperties.getTracingPolicy());
		options.setRegisterWebSocketWriteHandlers(httpServerProperties.isRegisterWebSocketWriteHandlers());
		options.setHttp2RstFloodMaxRstFramePerWindow(httpServerProperties.getHttp2RstFloodMaxRstFramePerWindow());
		options.setHttp2RstFloodWindowDuration(httpServerProperties.getHttp2RstFloodWindowDuration());
		options.setHttp2RstFloodWindowDurationTimeUnit(httpServerProperties.getHttp2RstFloodWindowDurationTimeUnit());
		return options;
	}

}
