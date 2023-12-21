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
package org.laokou.common.ratelimiter.annotation;

import org.laokou.common.ratelimiter.enums.Type;

import java.lang.annotation.*;

import static org.laokou.common.ratelimiter.enums.Type.DEFAULT;

/**
 * @author laokou
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {

    /**
     * 标识
     */
    String id();

    /**
     * 令牌桶每秒填充平均速率
     */
    int replenishRate() default 1;

    /**
     * 令牌桶的桶容量
     */
    int burstCapacity() default 1;

    /**
     * 每次请求消耗令牌数
     */
    int requestedTokens() default 1;

    /**
     * 类型
     */
    Type type() default DEFAULT;

}
