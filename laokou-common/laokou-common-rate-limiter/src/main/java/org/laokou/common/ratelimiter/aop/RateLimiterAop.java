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

package org.laokou.common.ratelimiter.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.laokou.common.core.utils.ResourceUtil;
import org.laokou.common.i18n.common.exception.ApiException;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.ratelimiter.annotation.RateLimiter;
import org.laokou.common.ratelimiter.driver.KeyManager;
import org.laokou.common.redis.utils.RedisUtil;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.laokou.common.i18n.common.Constant.EMPTY;
import static org.laokou.common.i18n.common.StatusCode.TOO_MANY_REQUESTS;

/**
 * 请查看 RequestRateLimiterGatewayFilterFactory
 * @author laokou
 */
@Component
@Aspect
@Slf4j
@RequiredArgsConstructor
public class RateLimiterAop {

    private static final String KEY = "___%s_KEY___";

    private static final DefaultRedisScript<Boolean> REDIS_SCRIPT;

    private final RedisUtil redisUtil;

    static {
        try (InputStream inputStream = ResourceUtil.getResource("META-INF/scripts/rate_limiter.lua").getInputStream()) {
            REDIS_SCRIPT = new DefaultRedisScript<>(new String(inputStream.readAllBytes(), StandardCharsets.UTF_8),
                    Boolean.class);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Before("@annotation(org.laokou.common.ratelimiter.annotation.RateLimiter)")
    public void doBefore(JoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        RateLimiter rateLimiter = AnnotationUtils.findAnnotation(method, RateLimiter.class);
        Assert.isTrue(ObjectUtil.isNotNull(rateLimiter), "@RateLimiter is null");

        // How many requests per second do you want a user to be allowed to do?
        int replenishRate = rateLimiter.replenishRate();

        // How much bursting do you want to allow?
        int burstCapacity = rateLimiter.burstCapacity();

        // How many tokens are requested per request?
        int requestedTokens = rateLimiter.requestedTokens();

        List<String> keys = getKeys(rateLimiter.id().concat(KeyManager.key(rateLimiter.type())));

        // The arguments to the LUA script. time() returns unixTime in seconds.
        List<String> scriptArgs = Arrays.asList(replenishRate + EMPTY, burstCapacity + EMPTY, EMPTY, requestedTokens + EMPTY);
        Boolean result = redisUtil.execute(REDIS_SCRIPT, keys, scriptArgs);
        if (!result) {
            throw new ApiException(TOO_MANY_REQUESTS);
        }
    }

    private List<String> getKeys(String id) {
        // use `{}` around keys to use Redis Key hashtags
        // this allows for using redis cluster

        // Make a unique key per user.
        String prefix = "rate_limiter.{" + String.format(KEY, id);

        // You need two Redis keys for Token Bucket.
        String tokenKey = prefix + "}.tokens";
        String timestampKey = prefix + "}.timestamp";
        return Arrays.asList(tokenKey, timestampKey);
    }

}
