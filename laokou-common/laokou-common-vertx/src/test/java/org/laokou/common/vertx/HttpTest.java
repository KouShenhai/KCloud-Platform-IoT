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

package org.laokou.common.vertx;

import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.laokou.common.i18n.util.JacksonUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatNoException;

/**
 * @author laokou
 */
@Slf4j
@SpringBootTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class HttpTest {

	private final Vertx vertx;

	@Test
	void testWebSocket() throws InterruptedException {
		for (int i = 2883; i <= 3000; i++) {
			WebSocketConnectOptions options = new WebSocketConnectOptions();
			options.setHost("127.0.0.1");
			options.setPort(i);
			options.setURI("/1/2/ws");
			WebSocketClient webSocketClient = vertx.createWebSocketClient();
			int finalI = i;
			webSocketClient.connect(options).onComplete(res -> {
				if (res.succeeded()) {
					log.info("【Vertx-WebSocket】 => 连接成功，端口：{}", finalI);
					WebSocket webSocket = res.result();
					webSocket.writeTextMessage("hello world");
					webSocket.textMessageHandler(msg -> log.info("【Vertx-WebSocket】 => 接收消息：{}", msg));
					webSocket.exceptionHandler(e -> log.error("【Vertx-WebSocket】 => 异常：{}", e.getMessage()));
					webSocket.closeHandler(close -> log.info("【Vertx-WebSocket】 => 关闭连接"));
				}
			});
			Thread.sleep(2000);
			assertThatNoException().isThrownBy(webSocketClient::close);
		}
	}

	@Test
	void testHttp() throws InterruptedException {
		Map<String, String> map = new HashMap<>();
		map.put("name", "laokou");
		map.put("age", "18");
		map.put("address", "北京上海深圳杭州");
		String jsonStr = JacksonUtils.toJsonStr(map);
		MultiMap headers = HttpHeaders.set("content-type", "application/json")
			.set("content-length", String.valueOf(jsonStr.getBytes(StandardCharsets.UTF_8).length));
		for (int i = 2883; i <= 3000; i++) {
			HttpClientOptions options = new HttpClientOptions();
			options.setDefaultHost("127.0.0.1");
			options.setDefaultPort(i);
			options.setKeepAlive(false);
			HttpClientAgent httpClient = vertx.createHttpClient(options);
			int finalI = i;
			httpClient.request(HttpMethod.POST, "/1/2/up/property/report").onSuccess(request -> {
				log.info("【Vertx-HTTP-Client】 => 连接成功，端口：{}", finalI);
				request.response()
					.onSuccess(response -> log.info("Received response with status code：{}", response.statusCode()));
				request.headers().addAll(headers);
				request.write(Buffer.buffer(jsonStr));
				// 确认请求可以结束
				request.end();
			});
			Thread.sleep(1000);
			assertThatNoException().isThrownBy(httpClient::close);
		}
	}

}
