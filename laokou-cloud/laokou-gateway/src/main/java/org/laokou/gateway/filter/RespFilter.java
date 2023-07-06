/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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
package org.laokou.gateway.filter;

import com.fasterxml.jackson.databind.JsonNode;
import io.micrometer.common.lang.NonNullApi;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.i18n.core.CustomException;
import org.laokou.common.i18n.core.HttpResult;
import org.laokou.common.i18n.core.StatusCode;
import org.laokou.common.i18n.utils.MessageUtil;
import org.laokou.gateway.constant.GatewayConstant;
import org.laokou.gateway.enums.ExceptionEnum;
import org.laokou.gateway.utils.ResponseUtil;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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
import java.util.Objects;

import static org.laokou.gateway.constant.GatewayConstant.OAUTH2_AUTH_URI;

/**
 * @author laokou
 */
@Component
@Slf4j
@NonNullApi
public class RespFilter implements GlobalFilter, Ordered {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		// 获取request对象
		ServerHttpRequest request = exchange.getRequest();
		// 获取uri
		String requestUri = request.getPath().pathWithinApplication().value();
		// 表单提交
		MediaType mediaType = request.getHeaders().getContentType();
		if (OAUTH2_AUTH_URI.contains(requestUri) && HttpMethod.POST.matches(request.getMethod().name())
				&& MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(mediaType)) {
			return oauth2Resp(exchange, chain);
		}
		else {
			return chain.filter(exchange);
		}
	}

	/**
	 * OAuth2响应
	 */
	private Mono<Void> oauth2Resp(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpResponse response = exchange.getResponse();
		DataBufferFactory dataBufferFactory = response.bufferFactory();
		ServerHttpResponseDecorator serverHttpResponseDecorator = new ServerHttpResponseDecorator(response) {
			@Override
			public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
				String contentType = getDelegate().getHeaders().getFirst(HttpHeaders.CONTENT_TYPE);
				assert contentType != null;
				if (contentType.contains(MediaType.APPLICATION_JSON_VALUE)
						&& Objects.requireNonNull(response.getStatusCode()).value() != StatusCode.OK
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
						JsonNode msgNode = node.get(GatewayConstant.ERROR_DESCRIPTION);
						JsonNode codeNode = node.get(GatewayConstant.ERROR);
						String msg = "";
						int code = 500;
						if (codeNode != null) {
							code = codeNode.asInt();
						}
						if (msgNode != null) {
							msg = msgNode.asText();
						}
						if (code == 0) {
							CustomException ex = getThrow(codeNode.asText());
							code = ex.getCode();
							msg = ex.getMsg();
						}
						HttpResult<?> result = ResponseUtil.response(code, msg);
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
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE + 1500;
	}

	private CustomException getThrow(String code) {
		ExceptionEnum instance = ExceptionEnum.getInstance(code.toUpperCase());
		return switch (instance) {
			case INVALID_CLIENT ->
				new CustomException(StatusCode.INVALID_CLIENT, MessageUtil.getMessage(StatusCode.INVALID_CLIENT));
		};
	}

}
