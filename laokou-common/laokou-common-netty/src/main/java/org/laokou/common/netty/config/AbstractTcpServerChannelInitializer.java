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

package org.laokou.common.netty.config;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author laokou
 */
public abstract class AbstractTcpServerChannelInitializer extends AbstractChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel channel) {
		ChannelPipeline pipeline = channel.pipeline();
		// 前置处理
		preHandler(pipeline);
		// 定长截取
		pipeline.addLast("fixedLengthFrameDecoder", new FixedLengthFrameDecoder(55));
		// 扩展处理
		extHandler(pipeline);
		// 心跳检测
		pipeline.addLast("idleStateHandler", new IdleStateHandler(60, 0, 0, TimeUnit.SECONDS));
		// 后置处理
		postHandler(pipeline);
	}

	abstract protected void extHandler(ChannelPipeline pipeline);

}
