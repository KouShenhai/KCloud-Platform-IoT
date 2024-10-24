/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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
 *
 */

package org.laokou.common.nacos.utils;

import org.laokou.common.core.utils.JacksonUtil;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * @author laokou
 */
public class ReactiveResponseUtil {

	public static Mono<Void> responseOk(ServerWebExchange exchange, Object data) {
		return responseOk(exchange, JacksonUtil.toJsonStr(data), MediaType.APPLICATION_JSON);
	}

	public static Mono<Void> responseOk(ServerWebExchange exchange, String str, MediaType contentType) {
		DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(str.getBytes(StandardCharsets.UTF_8));
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(HttpStatus.OK);
		response.getHeaders().setContentType(contentType);
		response.getHeaders().setContentLength(str.getBytes(StandardCharsets.UTF_8).length);
		return response.writeWith(Flux.just(buffer));
	}

}
