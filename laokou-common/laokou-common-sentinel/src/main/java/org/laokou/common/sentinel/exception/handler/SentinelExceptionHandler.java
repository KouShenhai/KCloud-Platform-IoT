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
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.i18n.utils.LogUtil;
import org.springframework.boot.autoconfigure.AutoConfiguration;

import static org.laokou.common.i18n.common.exception.SystemException.*;

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
			log.error("FlowException -> 已限流，错误信息：{}，详情见日志", LogUtil.record(flowException.getMessage()), flowException);
			ResponseUtil.response(response, Result.fail(FLOWED));
			return;
		}
		// 降级
		if (e instanceof DegradeException degradeException) {
			log.error("DegradeException -> 已降级，错误信息：{}，详情见日志", LogUtil.record(degradeException.getMessage()),
					degradeException);
			ResponseUtil.response(response, Result.fail(DEGRADED));
			return;
		}
		// 热点参数限流
		if (e instanceof ParamFlowException paramFlowException) {
			log.error("ParamFlowException -> 热点参数已限流，错误信息：{}，详情见日志", LogUtil.record(paramFlowException.getMessage()),
					paramFlowException);
			ResponseUtil.response(response, Result.fail(PARAM_FLOWED));
			return;
		}
		// 系统规则
		if (e instanceof SystemBlockException systemBlockException) {
			log.error("SystemBlockException -> 系统规则错误，错误信息：{}，详情见日志", LogUtil.record(systemBlockException.getMessage()),
					systemBlockException);
			ResponseUtil.response(response, Result.fail(SYSTEM_BLOCKED));
			return;
		}
		// 授权规则
		if (e instanceof AuthorityException authorityException) {
			log.error("AuthorityException -> 授权规则错误，错误信息：{}，详情见日志", LogUtil.record(authorityException.getMessage()),
					authorityException);
			ResponseUtil.response(response, Result.fail(AUTHORITY));
		}
	}

}
