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
import io.netty.channel.*;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.auth.client.user.UserDetail;
import org.laokou.common.core.constant.Constant;
import org.laokou.common.core.utils.MapUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.redis.utils.RedisKeyUtil;
import org.laokou.common.redis.utils.RedisUtil;
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

    private static final String WS_HEADER_NAME = "Upgrade";
    private static final String WS_HEADER_VALUE = "websocket";
    private final RedisUtil redisUtil;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest request) {
            initInfo(ctx,request);
        } else if (msg instanceof PingWebSocketFrame pingWebSocketFrame) {
            System.out.println(22);
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

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }

    private String getAuthorization(Map<String, String> paramMap) {
        String Authorization = paramMap.getOrDefault(Constant.AUTHORIZATION_HEAD, "");
        if (StringUtil.isNotEmpty(Authorization)) {
            return Authorization.substring(7);
        }
        return Authorization;
    }

    private void initInfo(ChannelHandlerContext ctx, FullHttpRequest request) {
        if (request.decoderResult().isFailure() || !WS_HEADER_VALUE.equals(request.headers().get(WS_HEADER_NAME))) {
            handleRequestError(ctx,HttpResponseStatus.BAD_REQUEST);
        }
        String uri = request.uri();
        int index = uri.indexOf(Constant.QUESTION_MARK);
        String param = uri.substring(index + 1);
        Map<String, String> paramMap = MapUtil.parseParamMap(param);
        String Authorization = getAuthorization(paramMap);
        request.setUri(uri.substring(0,index));
        if (StringUtil.isEmpty(Authorization)) {
            handleRequestError(ctx,HttpResponseStatus.UNAUTHORIZED);
        }
        String userInfoKey = RedisKeyUtil.getUserInfoKey(Authorization);
        Object obj = redisUtil.get(userInfoKey);
        if (obj == null) {
            handleRequestError(ctx,HttpResponseStatus.UNAUTHORIZED);
        }
        UserDetail userDetail = (UserDetail) obj;
    }

    private void handleRequestError(ChannelHandlerContext ctx,HttpResponseStatus httpResponseStatus) {
        DefaultFullHttpResponse defaultFullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, httpResponseStatus);
        ChannelFuture channelFuture = ctx.channel().writeAndFlush(defaultFullHttpResponse);
        channelFuture.addListener(ChannelFutureListener.CLOSE);
    }

}
