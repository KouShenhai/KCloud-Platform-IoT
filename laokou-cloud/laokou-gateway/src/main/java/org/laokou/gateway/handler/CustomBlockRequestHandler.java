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

package org.laokou.gateway.handler;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import org.laokou.gateway.exception.GatewayException;
import org.laokou.gateway.utils.ResponseUtil;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;
/**
 * @author laokou
 */
@Component
public class CustomBlockRequestHandler implements BlockRequestHandler {
    @Override
    public Mono<ServerResponse> handleRequest(ServerWebExchange serverWebExchange, Throwable throwable) {
        GatewayException blockRequest = GatewayException.BLOCK_REQUEST;
        return ServerResponse.status(blockRequest.getCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromValue(ResponseUtil.error(blockRequest)));
    }
}
