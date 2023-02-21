/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *   http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.gateway.filter;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.constant.Constant;
import org.laokou.common.i18n.core.StatusCode;
import org.laokou.gateway.utils.PasswordUtil;
import org.laokou.common.core.utils.StringUtil;
import org.laokou.gateway.constant.GatewayConstant;
import org.laokou.gateway.utils.ResponseUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
/**
 * 认证Filter
 * @author laokou
 */
@Component
@Slf4j
@RefreshScope
@Data
@ConfigurationProperties(prefix = "ignore")
public class AuthFilter implements GlobalFilter,Ordered {

    /**
     * 不拦截的urls
     */
    private List<String> uris;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取request对象
        ServerHttpRequest request = exchange.getRequest();
        // 获取uri
        String requestUri = request.getPath().pathWithinApplication().value();
        log.info("uri：{}", requestUri);
        // 请求放行，无需验证权限
        if (ResponseUtil.pathMatcher(requestUri,uris)){
            return chain.filter(exchange);
        }
        // 表单提交
        MediaType mediaType = request.getHeaders().getContentType();
        if (ResponseUtil.ANT_PATH_MATCHER.match(GatewayConstant.OAUTH2_AUTH_URI,requestUri) && MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(mediaType)) {
            return authDecode(exchange,chain);
        }
        // 获取token
        String token = ResponseUtil.getToken(request);
        if (StringUtil.isEmpty(token)) {
            return ResponseUtil.response(exchange, ResponseUtil.error(StatusCode.UNAUTHORIZED));
        }
        ServerHttpRequest build = exchange.getRequest().mutate()
                .header(Constant.AUTHORIZATION_HEAD, token).build();
        return chain.filter(exchange.mutate().request(build).build());
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    private Mono<Void> authDecode(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerRequest serverRequest = ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());
        Mono modifiedBody = serverRequest.bodyToMono(String.class).flatMap(decrypt());
        BodyInserter<Mono, ReactiveHttpOutputMessage> bodyInserter = BodyInserters.fromPublisher(modifiedBody, String.class);
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(exchange.getRequest().getHeaders());
        headers.remove(HttpHeaders.CONTENT_LENGTH);
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);
        return bodyInserter.insert(outputMessage, new BodyInserterContext()).then(Mono.defer(() -> {
            ServerHttpRequest decorator = decorate(exchange, headers, outputMessage);
            return chain.filter(exchange.mutate().request(decorator).build());
        }));
    }

    private Function decrypt() {
        return s -> {
            // 获取请求密码并解密
            Map<String, String> inParamsMap = HttpUtil.decodeParamMap((String) s, CharsetUtil.CHARSET_UTF_8);
            if (inParamsMap.containsKey(GatewayConstant.PASSWORD) && inParamsMap.containsKey(GatewayConstant.USERNAME)) {
                try {
                    String password = inParamsMap.get(GatewayConstant.PASSWORD);
                    String username = inParamsMap.get(GatewayConstant.USERNAME);
                    // 返回修改后报文字符
                    if (StringUtil.isNotEmpty(password)) {
                        inParamsMap.put(GatewayConstant.PASSWORD, PasswordUtil.decode(password));
                    }
                    if (StringUtil.isNotEmpty(username)) {
                        inParamsMap.put(GatewayConstant.USERNAME, PasswordUtil.decode(username));
                    }
                } catch (Exception e) {
                    log.error("错误信息：{}",e.getMessage());
                }
            }
            else {
                log.error("非法请求数据:{}", s);
            }
            return Mono.just(HttpUtil.toParams(inParamsMap, Charset.defaultCharset(), true));
        };
    }

    private ServerHttpRequestDecorator decorate(ServerWebExchange exchange, HttpHeaders headers, CachedBodyOutputMessage outputMessage) {
        return new ServerHttpRequestDecorator(exchange.getRequest()) {
            @Override
            public HttpHeaders getHeaders() {
                long contentLength = headers.getContentLength();
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.putAll(super.getHeaders());
                if (contentLength > 0) {
                    httpHeaders.setContentLength(contentLength);
                } else {
                    httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
                }
                return httpHeaders;
            }

            @Override
            public Flux<DataBuffer> getBody() {
                return outputMessage.getBody();
            }
        };
    }

}
