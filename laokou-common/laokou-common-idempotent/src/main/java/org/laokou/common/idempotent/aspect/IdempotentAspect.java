/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.laokou.common.idempotent.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.laokou.common.core.constant.Constant;
import org.laokou.common.core.utils.RequestUtil;
import org.laokou.common.core.utils.ResourceUtil;
import org.laokou.common.i18n.core.CustomException;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.redis.utils.RedisUtil;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

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
    private static final DefaultRedisScript<Boolean> REDIS_SCRIPT = new DefaultRedisScript<>();

    @Before("@annotation(org.laokou.common.idempotent.annotation.Idempotent)")
    public void before() {
        HttpServletRequest request = RequestUtil.getHttpServletRequest();
        String requestId = request.getHeader(Constant.REQUEST_ID);
        if (StringUtil.isEmpty(requestId)) {
            throw new CustomException("令牌不为空");
        }
        REDIS_SCRIPT.setLocation(ResourceUtil.getResource("idempotent.lua"));
        Boolean result = redisUtil.execute(REDIS_SCRIPT, Collections.singletonList(requestId));
        if (result == null || !result) {
            throw new CustomException("不可重复提交请求");
        }
    }

}
