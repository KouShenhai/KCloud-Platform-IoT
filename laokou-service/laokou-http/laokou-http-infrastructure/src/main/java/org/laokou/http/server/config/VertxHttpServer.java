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

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import reactor.core.publisher.Flux;

import static org.laokou.common.vertx.constant.MqConstants.HTTP_ROUTER_RULE_PROPERTY_REPORT;

/**
 * @author laokou
 */
final class VertxHttpServer {

	private final HttpServerProperties properties;

	private final Vertx vertx;

	private final Router router;

	VertxHttpServer(Vertx vertx, HttpServerProperties properties) {
		this.vertx = vertx;
		this.properties = properties;
		this.router = getRouter();
	}

	Flux<HttpServer> start() {
		return getHttpServerOptions()
			.map(httpServerOption -> vertx.createHttpServer(httpServerOption).requestHandler(router));
	}

	private Router getRouter() {
		Router router = Router.router(vertx);
		router.post(HTTP_ROUTER_RULE_PROPERTY_REPORT).order(10).handler(handler -> {

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
