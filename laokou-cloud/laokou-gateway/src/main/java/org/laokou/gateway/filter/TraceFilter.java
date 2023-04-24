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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.constant.Constant;
import org.laokou.gateway.utils.ResponseUtil;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 请求链路
 * @author laokou
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class TraceFilter implements GlobalFilter,Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return Mono.fromRunnable(
                () -> {
                    ServerHttpRequest request = exchange.getRequest();
                    String userId = ResponseUtil.getUserId(request);
                    String tenantId = ResponseUtil.getTenantId(request);
                    String username = ResponseUtil.getUsername(request);
                    String traceId = ResponseUtil.getTraceId(request);
                    // EFK收集
                    try {
                        //
                    } catch (Exception e) {
                        log.error("消息发送失败，失败消息：{}",e.getMessage());
                    }
                }
        ).then(chain.filter(exchange)).doFinally(i -> MDC.remove(Constant.TRACE_ID));
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 500;
    }
}
