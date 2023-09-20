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

package org.laokou.common.idempotent.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.laokou.common.core.utils.RequestUtil;
import org.laokou.common.core.utils.ResourceUtil;
import org.laokou.common.i18n.common.GlobalException;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.redis.utils.RedisKeyUtil;
import org.laokou.common.redis.utils.RedisUtil;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

/**
 * @author laokou
 */
@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class IdempotentAspect {

	private final RedisUtil redisUtil;

	private static final DefaultRedisScript<Boolean> REDIS_SCRIPT;

	private static final String REQUEST_ID = "request-id";

	static {
		try {
			Resource resource = ResourceUtil.getResource("idempotent.lua");
			REDIS_SCRIPT = new DefaultRedisScript<>(
					new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8), Boolean.class);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Before("@annotation(org.laokou.common.idempotent.annotation.Idempotent)")
	public void doBefore() {
		String requestId = getRequestId();
		if (StringUtil.isEmpty(requestId)) {
			throw new GlobalException("提交失败，令牌不能为空");
		}
		String apiIdempotentKey = RedisKeyUtil.getApiIdempotentKey(requestId);
		Boolean result = redisUtil.execute(REDIS_SCRIPT, Collections.singletonList(apiIdempotentKey));
		if (!result) {
			throw new GlobalException("不可重复提交请求");
		}
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
