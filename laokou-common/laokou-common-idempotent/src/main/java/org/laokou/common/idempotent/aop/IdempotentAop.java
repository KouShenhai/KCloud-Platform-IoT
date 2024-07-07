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

package org.laokou.common.idempotent.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.laokou.common.core.utils.RequestUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.idempotent.utils.IdempotentUtil;
import org.laokou.common.redis.utils.RedisKeyUtil;
import org.laokou.common.redis.utils.RedisUtil;
import org.springframework.stereotype.Component;

import static org.laokou.common.i18n.common.constant.TraceConstant.REQUEST_ID;
import static org.laokou.common.redis.utils.RedisUtil.MINUTE_FIVE_EXPIRE;

/**
 * 幂等性Aop.
 *
 * @author laokou
 */
@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class IdempotentAop {

	private final RedisUtil redisUtil;

	@Before("@annotation(org.laokou.common.idempotent.annotation.Idempotent)")
	public void doBefore() {
		String requestId = getRequestId();
		if (StringUtil.isEmpty(requestId)) {
			throw new RuntimeException("提交失败，令牌不能为空");
		}
		String apiIdempotentKey = RedisKeyUtil.getApiIdempotentKey(requestId);
		if (!redisUtil.setIfAbsent(apiIdempotentKey, 0, MINUTE_FIVE_EXPIRE)) {
			throw new RuntimeException("不可重复提交请求");
		}
		IdempotentUtil.openIdempotent();
	}

	@After("@annotation(org.laokou.common.idempotent.annotation.Idempotent)")
	public void doAfter() {
		IdempotentUtil.cleanIdempotent();
	}

	private String getRequestId() {
		HttpServletRequest request = RequestUtil.getHttpServletRequest();
		String requestId = request.getHeader(REQUEST_ID);
		if (StringUtil.isEmpty(requestId)) {
			return request.getParameter(REQUEST_ID);
		}
		return requestId;
	}

}
