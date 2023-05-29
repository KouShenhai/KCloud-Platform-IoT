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

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @author laokou
 */
@Slf4j
public class TcpHandler extends SimpleChannelInboundHandler<BasePackage> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, BasePackage basePackage) {
        log.info("服务器接收到消息，长度：{}，消息体：{}",basePackage.getLen(),basePackage.getBody());
        System.out.println(new String(basePackage.getBody(), StandardCharsets.UTF_8));
    }
}
