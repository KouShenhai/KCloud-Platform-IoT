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

package org.laokou.im.server.config;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.constant.Constant;
import org.laokou.common.core.utils.MapUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author laokou
 */
@Component
@Slf4j
@ChannelHandler.Sharable
@RequiredArgsConstructor
public class WebsocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private final ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest request) {
            String uri = request.uri();
            int index = uri.indexOf(Constant.QUESTION_MARK);
            String param = uri.substring(index + 1);
            Map<String, String> paramMap = MapUtil.parseParamMap(param);
            String Authorization = getAuthorization(paramMap);
            // 从redis响应式读取
            reactiveStringRedisTemplate.opsForValue().get("ddd");
            request.setUri(uri.substring(0,index));
        } else if (msg instanceof TextWebSocketFrame textWebSocketFrame) {
            System.out.println(11);
        }
        super.channelRead(ctx, msg);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("建立连接");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.info("断开连接");
    }

    private String getAuthorization(Map<String, String> paramMap) {
        String Authorization = paramMap.getOrDefault(Constant.AUTHORIZATION_HEAD, "");
        if (StringUtil.isNotEmpty(Authorization)) {
            return Authorization.substring(7);
        }
        return Authorization;
    }

}
