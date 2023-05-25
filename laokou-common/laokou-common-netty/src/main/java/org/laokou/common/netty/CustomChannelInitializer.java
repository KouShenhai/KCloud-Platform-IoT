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

package org.laokou.common.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

/**
 * @author laokou
 */
public class CustomChannelInitializer extends ChannelInitializer<SocketChannel> {

    /**
     * 结束标识
     */
    private static final short END_FLAG = 0xff;

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();
        // 粘包分隔
        ByteBuf delimiter = Unpooled.buffer(1);
        delimiter.writeByte(END_FLAG);
        pipeline.addLast(new DelimiterBasedFrameDecoder(1024,delimiter));
        // 解码
        pipeline.addLast();
        // 编码
        pipeline.addLast();
        // 心跳检测
        pipeline.addLast();
        // 消息处理
        pipeline.addLast();
    }

}
