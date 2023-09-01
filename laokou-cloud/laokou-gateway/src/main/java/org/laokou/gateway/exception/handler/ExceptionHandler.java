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
package org.laokou.gateway.exception.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import io.micrometer.common.lang.NonNullApi;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.dto.Result;
import org.laokou.gateway.utils.ResponseUtil;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.laokou.common.i18n.common.StatusCode.*;
import static org.laokou.gateway.exception.ErrorCode.*;

/**
 * @author laokou
 */
@Component
@Slf4j
@NonNullApi
public class ExceptionHandler implements ErrorWebExceptionHandler, Ordered {

	@Override
	public Mono<Void> handle(ServerWebExchange exchange, Throwable e) {
		log.error("网关全局处理异常，异常信息:{}", e.getMessage());
		if (e instanceof NotFoundException) {
			log.error("服务正在维护，请联系管理员");
			return ResponseUtil.response(exchange, Result.fail(SERVICE_UNAVAILABLE));
		}
		if (e instanceof ResponseStatusException) {
			int statusCode = ((ResponseStatusException) e).getStatusCode().value();
			log.info("状态码：{}", statusCode);
			if (statusCode == NOT_FOUND) {
				log.error("无法找到请求的资源");
				return ResponseUtil.response(exchange, Result.fail(NOT_FOUND));
			}
			else {
				log.error("服务器内部错误，无法完成请求");
				return ResponseUtil.response(exchange, Result.fail(INTERNAL_SERVER_ERROR));
			}
		}
		if (BlockException.isBlockException(e)) {
			// 思路来源于SentinelGatewayBlockExceptionHandler
			log.error("请求过于频繁，请稍后再试");
			return ResponseUtil.response(exchange, Result.fail(SERVICE_BLOCK_REQUEST));
		}
		else {
			log.error("请求已中断，请刷新页面");
			return ResponseUtil.response(exchange, Result.fail(SERVICE_REQUEST_CLOSE));
		}
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}

}
