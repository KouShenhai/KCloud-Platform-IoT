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
import org.laokou.common.core.utils.SecretUtil;
import org.laokou.common.i18n.core.CustomException;
import org.laokou.gateway.properties.CustomProperties;
import org.laokou.gateway.utils.ResponseUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class SecretFilter implements GlobalFilter, Ordered {

    private final CustomProperties customProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取request对象
        ServerHttpRequest request = exchange.getRequest();
        // 获取uri
        String requestUri = request.getPath().pathWithinApplication().value();
        // 请求放行，无需验证权限
        if (ResponseUtil.pathMatcher(requestUri, customProperties.getUris())){
            return chain.filter(exchange);
        }
        // 验签
        String userId = ResponseUtil.getUserId(request);
        String tenantId = ResponseUtil.getTenantId(request);
        String username = ResponseUtil.getUsername(request);
        String sign = ResponseUtil.getSign(request);
        String timestamp = ResponseUtil.getTimestamp(request);
        try {
            SecretUtil.verification(sign, Long.parseLong(timestamp), Long.parseLong(userId), username, Long.parseLong(tenantId));
        } catch (CustomException e) {
            return ResponseUtil.response(exchange, ResponseUtil.response(e.getCode(),e.getMsg()));
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 800;
    }
}
