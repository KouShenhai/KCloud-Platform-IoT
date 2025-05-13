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

package org.laokou.http.server.config;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.vertx.model.HttpMessageEnum;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 * @author laokou
 */
@Slf4j
final class VertxHttpServer extends AbstractVerticle {

	private final HttpServerProperties properties;

	private final Vertx vertx;

	private final Router router;

	private volatile Flux<HttpServer> httpServer;

	private boolean isClosed = false;

	VertxHttpServer(Vertx vertx, HttpServerProperties properties) {
		this.vertx = vertx;
		this.properties = properties;
		this.router = getRouter();
	}

	@Override
	public synchronized void start() {
		httpServer = getHttpServerOptions()
			.map(httpServerOption -> vertx.createHttpServer(httpServerOption).requestHandler(router))
			.map(server -> server.listen(completionHandler -> {
				if (isClosed) {
					return;
				}
				if (completionHandler.succeeded()) {
					log.info("【Vertx-Http-Server】 => HTTP服务启动成功，端口：{}", server.actualPort());
				}
				else {
					Throwable ex = completionHandler.cause();
					log.error("【Vertx-Http-Server】 => HTTP服务启动失败，错误信息：{}", ex.getMessage(), ex);
				}
			}));
		httpServer.subscribeOn(Schedulers.boundedElastic()).subscribe();
	}

	@Override
	public synchronized void stop() {
		isClosed = true;
		httpServer.doOnNext(server -> server.close(result -> {
			if (result.succeeded()) {
				log.info("【Vertx-Http-Server】 => HTTP服务停止成功，端口：{}", server.actualPort());
			}
			else {
				Throwable ex = result.cause();
				log.error("【Vertx-Http-Server】 => HTTP服务停止失败，错误信息：{}", ex.getMessage(), ex);
			}
		})).subscribeOn(Schedulers.boundedElastic()).subscribe();
	}

	public void deploy() {
		// 部署服务
		vertx.deployVerticle(this);
		// 停止服务
		Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
	}

	private Router getRouter() {
		Router router = Router.router(vertx);
		router.route().handler(BodyHandler.create());
		router.post(HttpMessageEnum.UP_PROPERTY_REPORT.getRouter()).handler(ctx -> {
			String body = ctx.body().asString();
			Long deviceId = Long.valueOf(ctx.pathParam("deviceId"));
			Long productId = Long.valueOf(ctx.pathParam("productId"));
			log.info("productId:{}，deviceId:{}，body：{}", productId, deviceId, body);
			ctx.response().end();
		});
		return router;
	}

	private Flux<HttpServerOptions> getHttpServerOptions() {
		return Flux.fromIterable(properties.getPorts()).map(this::getHttpServerOption);
	}

	private HttpServerOptions getHttpServerOption(int port) {
		HttpServerOptions options = new HttpServerOptions();
		options.setHost(properties.getHost());
		options.setPort(port);
		options.setCompressionSupported(properties.isCompressionSupported());
		options.setDecompressionSupported(properties.isDecompressionSupported());
		options.setCompressionLevel(properties.getCompressionLevel());
		options.setMaxWebSocketFrameSize(properties.getMaxWebSocketFrameSize());
		options.setMaxWebSocketMessageSize(properties.getMaxWebSocketMessageSize());
		options.setHandle100ContinueAutomatically(properties.isHandle100ContinueAutomatically());
		options.setMaxChunkSize(properties.getMaxChunkSize());
		options.setMaxInitialLineLength(properties.getMaxInitialLineLength());
		options.setMaxHeaderSize(properties.getMaxHeaderSize());
		options.setMaxFormAttributeSize(properties.getMaxFormAttributeSize());
		options.setMaxFormFields(properties.getMaxFormFields());
		options.setMaxFormBufferedBytes(properties.getMaxFormBufferedBytes());
		options.setInitialSettings(properties.getInitialSettings());
		options.setAlpnVersions(properties.getAlpnVersions());
		options.setHttp2ClearTextEnabled(properties.isHttp2ClearTextEnabled());
		options.setHttp2ConnectionWindowSize(properties.getHttp2ConnectionWindowSize());
		options.setDecoderInitialBufferSize(properties.getDecoderInitialBufferSize());
		options.setPerFrameWebSocketCompressionSupported(properties.isPerFrameWebSocketCompressionSupported());
		options.setPerMessageWebSocketCompressionSupported(properties.isPerMessageWebSocketCompressionSupported());
		options.setWebSocketCompressionLevel(properties.getWebSocketCompressionLevel());
		options.setWebSocketAllowServerNoContext(properties.isWebSocketAllowServerNoContext());
		options.setWebSocketPreferredClientNoContext(properties.isWebSocketPreferredClientNoContext());
		options.setWebSocketClosingTimeout(properties.getWebSocketClosingTimeout());
		options.setTracingPolicy(properties.getTracingPolicy());
		options.setRegisterWebSocketWriteHandlers(properties.isRegisterWebSocketWriteHandlers());
		options.setHttp2RstFloodMaxRstFramePerWindow(properties.getHttp2RstFloodMaxRstFramePerWindow());
		options.setHttp2RstFloodWindowDuration(properties.getHttp2RstFloodWindowDuration());
		options.setHttp2RstFloodWindowDurationTimeUnit(properties.getHttp2RstFloodWindowDurationTimeUnit());
		return options;
	}

}
