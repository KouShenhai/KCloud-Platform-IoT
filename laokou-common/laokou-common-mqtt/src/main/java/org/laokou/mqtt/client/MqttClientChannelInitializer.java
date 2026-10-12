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

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import java.util.concurrent.TimeUnit;

/**
 * @author laokou
 */
final class MqttClientChannelInitializer extends ChannelInitializer<SocketChannel> {

	// @formatter:off
	@Override
	protected void initChannel(SocketChannel channel)  {
		ChannelPipeline pipeline = channel.pipeline();
		pipeline.addLast("mqttEncoder", MqttEncoder.INSTANCE);
		pipeline.addLast("mqttDecoder", new MqttDecoder(64 * 1024 * 1024));
		pipeline.addLast("idleStateHandler", new IdleStateHandler(60, 0, 0, TimeUnit.SECONDS));
		pipeline.addLast("mqttHandler", new MqttClientHandler());
	}
	// @formatter:on

}
