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

package org.laokou.mqtt.client;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author loakou
 */
@Slf4j
final class NettyMqttClient implements MqttClient {

	volatile Channel channel;

	final AtomicReference<ClientState> state;

	final AtomicInteger reconnectAttempts;

	NettyMqttClient() {
		state = new AtomicReference<>(ClientState.DISCONNECTED);
		reconnectAttempts = new AtomicInteger(0);
	}

	@Override
	public void connect() {

	}

	@Override
	public void disconnect() {

	}

	private void doConnect() {

	}

}
