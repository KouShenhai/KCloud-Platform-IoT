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
package org.laokou.gateway.filter;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.i18n.core.HttpResult;
import org.laokou.common.i18n.core.StatusCode;
import org.laokou.gateway.constant.GatewayConstant;
import org.laokou.gateway.utils.ResponseUtil;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.nio.charset.StandardCharsets;
/**
 * @author laokou
 */
@Component
@Slf4j
public class RespFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取request对象
        ServerHttpRequest request = exchange.getRequest();
        request.getHeaders();
        // 获取uri
        String requestUri = request.getPath().pathWithinApplication().value();
        // 表单提交
        MediaType mediaType = request.getHeaders().getContentType();
        if (ResponseUtil.ANT_PATH_MATCHER.match(GatewayConstant.OAUTH2_AUTH_URI,requestUri)
                && MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(mediaType)) {
            ServerHttpResponse response = exchange.getResponse();
            DataBufferFactory dataBufferFactory = response.bufferFactory();
            ServerHttpResponseDecorator serverHttpResponseDecorator = new ServerHttpResponseDecorator(response) {
                @Override
                public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                    String contentType = getDelegate().getHeaders().getFirst(HttpHeaders.CONTENT_TYPE);
                    if (contentType.contains(MediaType.APPLICATION_JSON_VALUE)
                     && response.getStatusCode().value() != StatusCode.OK
                     && body instanceof Flux) {
                        Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
                        return super.writeWith(fluxBody.map(dataBuffer -> {
                            byte[] content = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(content);
                            // 释放内容
                            DataBufferUtils.release(dataBuffer);
                            String str = new String(content, StandardCharsets.UTF_8);
                            // str就是response的值
                            JsonNode node = JacksonUtil.readTree(str);
                            String msg = node.get(GatewayConstant.ERROR_DESCRIPTION).asText();
                            int code = node.get(GatewayConstant.ERROR).asInt();
                            HttpResult result = ResponseUtil.response(code, msg);
                            byte[] uppedContent = JacksonUtil.toJsonStr(result).getBytes();
                            // 修改状态码
                            response.setStatusCode(HttpStatus.OK);
                            return dataBufferFactory.wrap(uppedContent);
                        }));
                    }
                    return super.writeWith(body);
                }
            };
            return chain.filter(exchange.mutate().response(serverHttpResponseDecorator).build());
        } else {
            return chain.filter(exchange);
        }
    }

    @Override
    public int getOrder() {
        return NettyWriteResponseFilter.HIGHEST_PRECEDENCE + 1500;
    }
}
