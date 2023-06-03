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

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.laokou.auth.client.user.UserDetail;
import org.laokou.common.core.constant.Constant;
import org.laokou.common.core.utils.MapUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.redis.utils.RedisKeyUtil;
import org.laokou.common.redis.utils.RedisUtil;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author laokou
 */
@Component
@Slf4j
@ChannelHandler.Sharable
@RequiredArgsConstructor
public class WebsocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private static final String WS_HEADER_NAME = "Upgrade";
    private static final String WS_HEADER_VALUE = "websocket";
    private final RedisUtil redisUtil;
    private static final ChannelGroup CHANNEL_GROUP = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest request) {
            initInfo(ctx,request);
        }
        super.channelRead(ctx, msg);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) {

    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        CHANNEL_GROUP.add(ctx.channel());
        log.info("建立连接：{}",ctx.channel().id().asLongText());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        CHANNEL_GROUP.remove(ctx.channel());
        log.info("断开连接：{}",ctx.channel().id().asLongText());
    }

    private String getAuthorization(Map<String, String> paramMap) {
        String Authorization = paramMap.getOrDefault(Constant.AUTHORIZATION_HEAD, "");
        if (StringUtil.isNotEmpty(Authorization)) {
            return Authorization.substring(7);
        }
        return Authorization;
    }

    @SneakyThrows
    private void initInfo(ChannelHandlerContext ctx, FullHttpRequest request) {
        if (request.decoderResult().isFailure() || !WS_HEADER_VALUE.equals(request.headers().get(WS_HEADER_NAME))) {
            handleRequestError(ctx,HttpResponseStatus.BAD_REQUEST);
            return;
        }
        String uri = request.uri();
        int index = uri.indexOf(Constant.QUESTION_MARK);
        String param = uri.substring(index + 1);
        Map<String, String> paramMap = MapUtil.parseParamMap(param);
        String Authorization = getAuthorization(paramMap);
        request.setUri(uri.substring(0,index));
        if (StringUtil.isEmpty(Authorization)) {
            handleRequestError(ctx,HttpResponseStatus.UNAUTHORIZED);
            return;
        }
        String userInfoKey = RedisKeyUtil.getUserInfoKey(Authorization);
        Object obj = redisUtil.get(userInfoKey);
        if (obj == null) {
            handleRequestError(ctx,HttpResponseStatus.UNAUTHORIZED);
            return;
        }
        UserDetail userDetail = (UserDetail) obj;
        Channel channel = ctx.channel();
        ChannelId channelId = channel.id();
        Long userId = userDetail.getId();
        String userChannelKey = RedisKeyUtil.getUserChannelKey();
        serialize(channelId);
        //redisUtil.hSet(userChannelKey,userId.toString(),null,RedisUtil.HOUR_ONE_EXPIRE);
    }

    private byte[] serialize(Object obj)throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(outputStream);
        oos.writeObject(obj);
        oos.close();
        return outputStream.toByteArray();
    }

    private <T> T deserialize(Object obj,Class<T> clazz) {
        return null;
    }

    private void handleRequestError(ChannelHandlerContext ctx,HttpResponseStatus httpResponseStatus) {
        DefaultFullHttpResponse defaultFullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, httpResponseStatus);
        ByteBuf byteBuf = Unpooled.copiedBuffer(httpResponseStatus.toString(), StandardCharsets.UTF_8);
        defaultFullHttpResponse.content().writeBytes(byteBuf);
        // 释放资源
        ReferenceCountUtil.release(byteBuf);
        ctx.channel().writeAndFlush(defaultFullHttpResponse);
        ctx.close();
    }

}
