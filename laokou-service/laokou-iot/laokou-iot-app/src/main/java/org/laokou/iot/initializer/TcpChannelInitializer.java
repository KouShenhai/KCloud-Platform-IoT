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

package org.laokou.iot.initializer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.EventExecutorGroup;
import lombok.RequiredArgsConstructor;
import org.laokou.iot.codec.TcpDecoder;
import org.laokou.iot.codec.TcpEncoder;
import org.laokou.iot.handler.MetricHandler;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class TcpChannelInitializer extends ChannelInitializer<SocketChannel> {

	private final SimpleChannelInboundHandler<?> tcpHandler;
	private final MetricHandler metricHandler;
	private final EventExecutorGroup eventExecutorGroup;

	@Override
	protected void initChannel(SocketChannel channel) {
		ChannelPipeline pipeline = channel.pipeline();
		// 日志
		pipeline.addLast("loggingHandler", new LoggingHandler(LogLevel.INFO));
		// 定长截取
		pipeline.addLast("fixedLengthFrameDecoder", new FixedLengthFrameDecoder(55));
		// 解码
		pipeline.addLast("tcpDecoder", new TcpDecoder());
		// 编码
		pipeline.addLast("tcpEncoder", new TcpEncoder());
		// 心跳检测
		pipeline.addLast("idleStateHandler", new IdleStateHandler(60, 0, 0, TimeUnit.SECONDS));
		// 度量
		pipeline.addLast("metricHandler", metricHandler);
		// 业务处理handler
		pipeline.addLast(eventExecutorGroup, tcpHandler);
	}

}
