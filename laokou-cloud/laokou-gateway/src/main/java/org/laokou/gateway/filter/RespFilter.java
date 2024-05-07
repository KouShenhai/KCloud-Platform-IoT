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

package org.laokou.gateway.filter;

import com.fasterxml.jackson.databind.JsonNode;
import io.micrometer.common.lang.NonNullApi;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.i18n.utils.MessageUtil;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.gateway.exception.ExceptionEnum;
import org.laokou.gateway.utils.I18nReactiveUtil;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.laokou.common.i18n.common.constants.StringConstant.CHINESE_COMMA;
import static org.laokou.common.nacos.utils.ReactiveRequestUtil.*;
import static org.laokou.gateway.filter.AuthFilter.TOKEN_URL;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * 拦截请求响应的过滤器.
 *
 * @author laokou
 */
@Component
@Slf4j
@NonNullApi
public class RespFilter implements GlobalFilter, Ordered {

	@Schema(name = "ERROR", description = "错误")
	private static final String ERROR = "error";

	@Schema(name = "ERROR_DESCRIPTION", description = "错误信息")
	private static final String ERROR_DESCRIPTION = "error_description";

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		try {
			// 国际化
			I18nReactiveUtil.set(exchange);
			// 获取request对象
			ServerHttpRequest request = exchange.getRequest();
			// 获取uri
			String requestURL = getRequestURL(request);
			// 表单提交
			MediaType mediaType = getContentType(request);
			if (requestURL.contains(TOKEN_URL) && POST.matches(getMethodName(request))
					&& APPLICATION_FORM_URLENCODED.isCompatibleWith(mediaType)) {
				return response(exchange, chain);
			}
			else {
				return chain.filter(exchange);
			}
		}
		finally {
			I18nReactiveUtil.reset();
		}
	}

	/**
	 * OAuth2响应.
	 * @param chain 链式过滤器
	 * @param exchange 服务器对象
	 */
	private Mono<Void> response(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpResponse response = exchange.getResponse();
		DataBufferFactory dataBufferFactory = response.bufferFactory();
		ServerHttpResponseDecorator serverHttpResponseDecorator = new ServerHttpResponseDecorator(response) {
			@Override
			public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
				String contentType = getDelegate().getHeaders().getFirst(CONTENT_TYPE);
				Assert.isTrue(ObjectUtil.isNotNull(contentType), "content type is null");
				if (contentType.contains(APPLICATION_JSON_VALUE)
						&& ObjectUtil.requireNotNull(response.getStatusCode()).value() != OK.value()
						&& body instanceof Flux) {
					Flux<? extends DataBuffer> flux = Flux.from(body);
					return super.writeWith(flux.map(dataBuffer -> {
						// 修改状态码
						response.setStatusCode(OK);
						byte[] content = new byte[dataBuffer.readableByteCount()];
						dataBuffer.read(content);
						// 释放内容
						DataBufferUtils.release(dataBuffer);
						String str = new String(content, UTF_8);
						// str就是response的值
						JsonNode node = JacksonUtil.readTree(str);
						JsonNode msgNode = node.get(ERROR_DESCRIPTION);
						JsonNode codeNode = node.get(ERROR);
						if (msgNode == null || codeNode == null) {
							return dataBufferFactory.wrap(new byte[0]);
						}
						try {
							ExceptionEnum ee = getException(codeNode.asText());
							String code = ee.getCode();
							String msg = MessageUtil.getMessage(code) + CHINESE_COMMA + msgNode.asText();
							byte[] uppedContent = JacksonUtil.toJsonStr(Result.fail(code, msg)).getBytes(UTF_8);
							return dataBufferFactory.wrap(uppedContent);
						}
						catch (Exception ex) {
							byte[] uppedContent = JacksonUtil
								.toJsonStr(Result.fail(codeNode.asText(), msgNode.asText()))
								.getBytes(UTF_8);
							return dataBufferFactory.wrap(uppedContent);
						}
					}));
				}
				return super.writeWith(body);
			}
		};
		return chain.filter(exchange.mutate().response(serverHttpResponseDecorator).build());
	}

	@Override
	public int getOrder() {
		return HIGHEST_PRECEDENCE + 1500;
	}

	/**
	 * 通过编码获取自定义异常.
	 * @param code 编码
	 * @return 自定义异常
	 */
	private ExceptionEnum getException(String code) {
		return ExceptionEnum.getInstance(code.toUpperCase());
	}

}
