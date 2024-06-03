/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.im;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.netty.config.WebSocketClient;
import org.laokou.common.netty.config.WebSocketProperties;
import org.laokou.im.handler.WebSocketClientHandler;
import org.laokou.im.initializer.WebSocketClientChannelInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import java.net.URI;

@Slf4j
@SpringBootTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class WebSocketClientTest {

	public static void main(String[] args) {
		WebSocketProperties webSocketProperties = new WebSocketProperties();
		WebSocketProperties.Client client = new WebSocketProperties.Client();
		String uri = "wss://vue.laokou.org/im/ws";
		client.setUri(uri);
		webSocketProperties.setClient(client);
		WebSocketClientHandler webSocketClientHandler = new WebSocketClientHandler(WebSocketClientHandshakerFactory.newHandshaker(URI.create(uri), WebSocketVersion.V13, null, true, new DefaultHttpHeaders()));
		WebSocketClient webSocketClient = new WebSocketClient(webSocketProperties, new WebSocketClientChannelInitializer(webSocketProperties, webSocketClientHandler));
		webSocketClient.open();
	}

}
