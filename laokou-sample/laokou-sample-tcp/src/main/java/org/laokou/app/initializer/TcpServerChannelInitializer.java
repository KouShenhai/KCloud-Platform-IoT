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

package org.laokou.app.initializer;

import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import lombok.RequiredArgsConstructor;
import org.laokou.app.handler.TcpDecoder;
import org.laokou.app.handler.TcpEncoder;
import org.laokou.app.handler.TcpServerHandler;
import org.laokou.common.netty.annotation.TcpServer;
import org.laokou.common.netty.config.AbstractTcpServerChannelInitializer;
import org.laokou.common.netty.config.SpringTcpServerProperties;

/**
 * @author laokou
 */
@TcpServer
@RequiredArgsConstructor
public class TcpServerChannelInitializer extends AbstractTcpServerChannelInitializer {

	private final SpringTcpServerProperties springTcpServerProperties;

	private final TcpServerHandler tcpServerHandler;

	@Override
	protected void preHandler(ChannelPipeline pipeline) {
		// 定长截取
		pipeline.addLast("fixedLengthFrameDecoder", new FixedLengthFrameDecoder(2));
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
	protected SpringTcpServerProperties getProperties() {
		return springTcpServerProperties;
	}

}
