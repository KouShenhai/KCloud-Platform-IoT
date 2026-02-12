/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.ratelimiter.aspectj;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.laokou.common.core.util.RequestUtils;
import org.laokou.common.i18n.common.constant.StringConstants;
import org.laokou.common.i18n.common.exception.StatusCode;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.ratelimiter.annotation.RateLimiter;
import org.laokou.common.redis.util.RedisUtils;
import org.redisson.api.RateType;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * 请查看 RequestRateLimiterGatewayFilterFactory.
 *
 * @author laokou
 */
@Aspect
@Component
@RequiredArgsConstructor
public class RateLimiterAspectj {

	private final RedisUtils redisUtils;

	@Around("@annotation(rateLimiter)")
	public Object doAround(ProceedingJoinPoint point, RateLimiter rateLimiter) throws Throwable {
		String key = getKey(rateLimiter.key()
			.concat(StringConstants.UNDER)
			.concat(rateLimiter.type().resolve(RequestUtils.getHttpServletRequest())));
		long rate = rateLimiter.rate();
		long ttl = rateLimiter.ttl();
		long interval = rateLimiter.interval();
		RateType mode = rateLimiter.mode();
		if (!redisUtils.rateLimiter(key, mode, rate, Duration.ofSeconds(interval), Duration.ofSeconds(ttl))) {
			throw new SystemException(StatusCode.TOO_MANY_REQUESTS);
		}
		return point.proceed();
	}

	private String getKey(String key) {
		return "rate_limiter.{" + String.format("___%s_KEY___", key) + "}.tokens";
	}

}
