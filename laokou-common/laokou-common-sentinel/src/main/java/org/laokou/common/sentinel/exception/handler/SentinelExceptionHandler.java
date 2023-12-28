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
import org.laokou.common.core.utils.ResponseUtil;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.common.i18n.utils.MessageUtil;
import org.springframework.boot.autoconfigure.AutoConfiguration;

import static org.laokou.common.i18n.common.ErrorCode.*;

/**
 * @author laokou
 */
@AutoConfiguration
@Slf4j
public class SentinelExceptionHandler implements BlockExceptionHandler {

	@Override
	@SneakyThrows
	public void handle(HttpServletRequest httpServletRequest, HttpServletResponse response, BlockException e) {
		// 限流
		if (e instanceof FlowException flowException) {
			log.error("请求已限流，限流 FlowException，错误信息：{}，详情见日志", LogUtil.error(flowException.getMessage()), flowException);
			ResponseUtil.response(response, REQUEST_FLOW, MessageUtil.getMessage(REQUEST_FLOW));
			return;
		}
		// 降级
		if (e instanceof DegradeException degradeException) {
			log.error("已降级，降级 DegradeException，错误信息：{}，详情见日志", LogUtil.error(degradeException.getMessage()),
					degradeException);
			ResponseUtil.response(response, DEGRADE, MessageUtil.getMessage(DEGRADE));
			return;
		}
		// 热点参数限流
		if (e instanceof ParamFlowException paramFlowException) {
			log.error("热点参数已限流，热点参数限流 ParamFlowException，错误信息：{}，详情见日志", LogUtil.error(paramFlowException.getMessage()),
					paramFlowException);
			ResponseUtil.response(response, PARAM_FLOW, MessageUtil.getMessage(PARAM_FLOW));
			return;
		}
		// 系统规则
		if (e instanceof SystemBlockException systemBlockException) {
			log.error("系统规则错误，系统规则 SystemBlockException，错误信息：{}，详情见日志",
					LogUtil.error(systemBlockException.getMessage()), systemBlockException);
			ResponseUtil.response(response, SYSTEM_BLOCK, MessageUtil.getMessage(SYSTEM_BLOCK));
			return;
		}
		// 授权规则
		if (e instanceof AuthorityException authorityException) {
			log.error("授权规则错误，授权规则 AuthorityException，错误信息：{}，详情见日志", LogUtil.error(authorityException.getMessage()),
					authorityException);
			ResponseUtil.response(response, AUTHORITY, MessageUtil.getMessage(AUTHORITY));
		}
	}

}
