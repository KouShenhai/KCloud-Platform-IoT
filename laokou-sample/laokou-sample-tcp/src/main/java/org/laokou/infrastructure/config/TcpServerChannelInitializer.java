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

package org.laokou.infrastructure.config;

import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import lombok.RequiredArgsConstructor;
import org.laokou.common.netty.annotation.TcpServer;
import org.laokou.common.netty.config.AbstractTcpServerChannelInitializer;
import org.laokou.common.netty.config.SpringTcpServerProperties;

import static org.laokou.infrastructure.config.TcpServerChannelInitializer.KEY;

/**
 * @author laokou
 */
@TcpServer(key = KEY)
@RequiredArgsConstructor
public class TcpServerChannelInitializer extends AbstractTcpServerChannelInitializer {

	protected static final String KEY = "tcp1";

	private final SpringTcpServerProperties springTcpServerProperties;

	private final TcpServerHandler tcpServerHandler;

	@Override
	protected void preHandler(ChannelPipeline pipeline) {
		// 定长截取
		pipeline.addLast("fixedLengthFrameDecoder", new FixedLengthFrameDecoder(4));
		// 解码
		pipeline.addLast(new TcpDecoder());
		// 编码
		pipeline.addLast(new TcpEncoder());
	}

	@Override
	protected void postHandler(ChannelPipeline pipeline) {
		pipeline.addLast("tcpServerHandler", tcpServerHandler);
	}

	@Override
	protected SpringTcpServerProperties.Config getConfig() {
		return springTcpServerProperties.getConfigs().get(KEY);
	}

}
