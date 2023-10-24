/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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
package org.laokou.common.sentinel.exception.handler;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.i18n.dto.Result;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.util.MimeTypeUtils;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import static org.laokou.common.i18n.common.StatusCode.TOO_MANY_REQUESTS;

/**
 * @author laokou
 */
@AutoConfiguration
@Slf4j
public class SentinelExceptionHandler implements BlockExceptionHandler {

	@Override
	public void handle(HttpServletRequest httpServletRequest, HttpServletResponse response, BlockException e) {
		// 限流
		if (e instanceof FlowException flowException) {
			log.error("请求太多，已被限流，请稍后再试");
			log.error("限流 FlowException，错误信息：{}",flowException.getMessage());
			response(response);
			return;
		}
		// 降级
		if (e instanceof DegradeException degradeException) {
			log.error("降级 DegradeException，错误信息：{}",degradeException.getMessage());
			response(response);
			return;
		}
		// 热点参数限流
		if (e instanceof ParamFlowException paramFlowException) {
			log.error("热点参数限流 ParamFlowException，错误信息：{}",paramFlowException.getMessage());
			response(response);
			return;
		}
		// 系统规则
		if (e instanceof SystemBlockException systemBlockException) {
			log.error("系统规则 SystemBlockException，错误信息：{}",systemBlockException.getMessage());
			response(response);
			return;
		}
		// 授权规则
		if (e instanceof AuthorityException authorityException) {
			log.error("授权规则 AuthorityException，错误信息：{}",authorityException.getMessage());
			response(response);
		}
	}

	@SneakyThrows
	private void response(HttpServletResponse response) {
		response.setStatus(HttpStatus.OK.value());
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
		try (PrintWriter writer = response.getWriter()) {
			writer.write(JacksonUtil.toJsonStr(Result.fail(TOO_MANY_REQUESTS)));
			writer.flush();
		}
	}

}
