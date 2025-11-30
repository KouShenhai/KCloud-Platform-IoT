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

package org.laokou.server.http.config;

import io.vertx.core.http.Http2Settings;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpVersion;
import io.vertx.core.tracing.TracingPolicy;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author laokou
 */
@Data
public class HttpServerProperties {

	private boolean auth = true;

	private String host = "0.0.0.0";

	private Integer port = 9070;

	private boolean compressionSupported = false;

	private int compressionLevel = 6;

	private int maxWebSocketFrameSize = 65536;

	private int maxWebSocketMessageSize = 65536 * 4;

	private boolean handle100ContinueAutomatically = false;

	private int maxChunkSize = 8192;

	private int maxInitialLineLength = 4096;

	private int maxHeaderSize = 8192;

	private int maxFormAttributeSize = 8192;

	private int maxFormFields = 512;

	private int maxFormBufferedBytes = 2048;

	private Http2Settings initialSettings = new Http2Settings()
		.setMaxConcurrentStreams(HttpServerOptions.DEFAULT_INITIAL_SETTINGS_MAX_CONCURRENT_STREAMS);

	private List<HttpVersion> alpnVersions = new ArrayList<>(HttpServerOptions.DEFAULT_ALPN_VERSIONS);

	private boolean http2ClearTextEnabled = true;

	private int http2ConnectionWindowSize = -1;

	private boolean decompressionSupported = false;

	private boolean acceptUnmaskedFrames = false;

	private int decoderInitialBufferSize = 256;

	private boolean perFrameWebSocketCompressionSupported = true;

	private boolean perMessageWebSocketCompressionSupported = true;

	private int webSocketCompressionLevel = 6;

	private boolean webSocketAllowServerNoContext = false;

	private boolean webSocketPreferredClientNoContext = false;

	private int webSocketClosingTimeout = 30;

	private TracingPolicy tracingPolicy = TracingPolicy.ALWAYS;

	private boolean registerWebSocketWriteHandlers = false;

	private int http2RstFloodMaxRstFramePerWindow = 400;

	private int http2RstFloodWindowDuration = 60;

	private TimeUnit http2RstFloodWindowDurationTimeUnit = TimeUnit.SECONDS;

}
