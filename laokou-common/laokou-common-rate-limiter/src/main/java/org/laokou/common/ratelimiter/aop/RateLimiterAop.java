/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.common.ratelimiter.aop;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.ratelimiter.annotation.RateLimiter;
import org.laokou.common.ratelimiter.driver.KeyManager;
import org.laokou.common.redis.utils.RedisUtil;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.reflect.Method;

import static org.laokou.common.i18n.common.StatusCode.TOO_MANY_REQUESTS;
import static org.laokou.common.i18n.common.StringConstant.UNDER;

/**
 * 请查看 RequestRateLimiterGatewayFilterFactory.
 *
 * @author laokou
 */
@Component
@Aspect
@Slf4j
@RequiredArgsConstructor
public class RateLimiterAop {

	@Schema(name = "RATE_LIMITER_KEY", description = "限流Key")
	public static final String RATE_LIMITER_KEY = "___%s_KEY___";

	private final RedisUtil redisUtil;

	@Before("@annotation(org.laokou.common.ratelimiter.annotation.RateLimiter)")
	public void doBefore(JoinPoint point) {
		MethodSignature signature = (MethodSignature) point.getSignature();
		Method method = signature.getMethod();
		RateLimiter rateLimiter = AnnotationUtils.findAnnotation(method, RateLimiter.class);
		Assert.isTrue(ObjectUtil.isNotNull(rateLimiter), "@RateLimiter is null");
		String key = getKey(rateLimiter.id().concat(UNDER).concat(KeyManager.key(rateLimiter.type())));
		long rate = rateLimiter.rate();
		long interval = rateLimiter.interval();
		RateIntervalUnit unit = rateLimiter.unit();
		RateType mode = rateLimiter.mode();
		if (!redisUtil.rateLimiter(key, mode, rate, interval, unit)) {
			throw new RuntimeException(String.valueOf(TOO_MANY_REQUESTS));
		}
	}

	private String getKey(String id) {
		return "rate_limiter.{" + String.format(RATE_LIMITER_KEY, id) + "}.tokens";
	}

}
