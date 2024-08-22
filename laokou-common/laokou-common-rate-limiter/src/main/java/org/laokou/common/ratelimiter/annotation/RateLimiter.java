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

package org.laokou.common.ratelimiter.annotation;

import org.laokou.common.ratelimiter.driver.spi.Type;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;

import java.lang.annotation.*;

import static org.laokou.common.ratelimiter.driver.spi.Type.DEFAULT;
import static org.redisson.api.RateIntervalUnit.SECONDS;
import static org.redisson.api.RateType.OVERALL;

/**
 * @author laokou
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {

	/**
	 * 标识.
	 */
	String id();

	/**
	 * 令牌速率.
	 */
	long rate() default 1;

	/**
	 * 过期时间.
	 */
	long interval() default 1;

	/**
	 * 类型.
	 */
	Type type() default DEFAULT;

	/**
	 * 单位.
	 */
	RateIntervalUnit unit() default SECONDS;

	/**
	 * 样式 OVERALL -> 所有实例共享 PER_CLIENT -> 单个实例共享.
	 */
	RateType mode() default OVERALL;

}
