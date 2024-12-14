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

package org.laokou.gateway.exception.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import io.micrometer.common.lang.NonNullApi;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.nacos.utils.ReactiveResponseUtil;
import org.laokou.gateway.utils.ReactiveI18nUtil;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.laokou.common.i18n.common.exception.StatusCode.*;

/**
 * 全局异常处理器.
 *
 * @author laokou
 */
@Component
@Slf4j
@NonNullApi
public class ExceptionHandler implements ErrorWebExceptionHandler, Ordered {

	/**
	 * 处理异常并响应.
	 * @param exchange 服务器网络交换机
	 * @param e 异常
	 * @return 响应结果
	 */
	@Override
	public Mono<Void> handle(ServerWebExchange exchange, Throwable e) {
		try {
			// 国际化
			ReactiveI18nUtil.set(exchange);
			if (e instanceof NotFoundException) {
				log.error("服务正在维护，请联系管理员，错误信息：{}，详情见日志", StringUtil.isEmpty(e.getMessage()) ? "暂无错误信息" : e.getMessage(),
						e);
				return ReactiveResponseUtil.responseOk(exchange, Result.fail(SERVICE_UNAVAILABLE));
			}
			if (e instanceof ResponseStatusException responseStatusException) {
				int statusCode = responseStatusException.getStatusCode().value();
				if (statusCode == HttpStatus.NOT_FOUND.value()) {
					log.error("状态码：{}，无法找到请求URL为{}的资源，错误信息：{}，详情见日志", statusCode,
							exchange.getRequest().getPath().pathWithinApplication().value(),
							StringUtil.isEmpty(e.getMessage()) ? "暂无错误信息" : e.getMessage(), e);
					return ReactiveResponseUtil.responseOk(exchange, Result.fail(NOT_FOUND));
				}
				else if (statusCode == HttpStatus.BAD_REQUEST.value()) {
					log.error("状态码：{}，错误请求，错误信息：{}，详情见日志", statusCode,
							StringUtil.isEmpty(e.getMessage()) ? "暂无错误信息" : e.getMessage(), e);
					return ReactiveResponseUtil.responseOk(exchange, Result.fail(BAD_REQUEST));
				}
				else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
					log.error("状态码：{}，服务器内部错误，无法完成请求，错误信息：{}，详情见日志", statusCode,
							StringUtil.isEmpty(e.getMessage()) ? "暂无错误信息" : e.getMessage(), e);
					return ReactiveResponseUtil.responseOk(exchange, Result.fail(INTERNAL_SERVER_ERROR));
				}
			}
			if (BlockException.isBlockException(e)) {
				// 思路来源于SentinelGatewayBlockExceptionHandler
				log.error("请求太频繁，错误信息：{}，详情见日志", StringUtil.isEmpty(e.getMessage()) ? "暂无错误信息" : e.getMessage(), e);
				return ReactiveResponseUtil.responseOk(exchange, Result.fail(TOO_MANY_REQUESTS));
			}
			log.error("错误网关，错误信息：{}，详情见日志", StringUtil.isEmpty(e.getMessage()) ? "暂无错误信息" : e.getMessage(), e);
			return ReactiveResponseUtil.responseOk(exchange, Result.fail(BAD_GATEWAY));
		}
		finally {
			ReactiveI18nUtil.reset();
		}
	}

	@Override
	public int getOrder() {
		return HIGHEST_PRECEDENCE;
	}

}
