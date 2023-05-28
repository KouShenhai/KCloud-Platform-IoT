/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.laokou.common.netty.tcp;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author laokou
 */
public class TcpChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        // 责任链模式
        ChannelPipeline pipeline = socketChannel.pipeline();
        // 解码
        pipeline.addLast("decoder",new TcpDecoder());
        // 编码
        pipeline.addLast("encoder",new TcpEncoder());
        // 心跳检测
        pipeline.addLast("heartbeat",new IdleStateHandler(60,0,0, TimeUnit.SECONDS));
        // 消息处理
        pipeline.addLast("handler",new TcpHandler());
    }

}
