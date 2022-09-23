/**
 * Copyright (c) 2022 KCloud-Platform Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.laokou.gateway.component;
import cn.hutool.http.HttpStatus;
import org.laokou.common.utils.HttpResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import java.util.Optional;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR;
@Component
@Slf4j
public class Resilience4jFallbackHandler implements HandlerFunction<ServerResponse> {
    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        Optional<Object> optionalUris = request.attribute(GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
        optionalUris.ifPresent((uri) -> log.error("网关执行请求：{}失败，resilience4j服务降级处理",uri));
        return ServerResponse.status(HttpStatus.HTTP_OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new HttpResultUtil<Boolean>().error("服务已被降级熔断")));
    }
}
