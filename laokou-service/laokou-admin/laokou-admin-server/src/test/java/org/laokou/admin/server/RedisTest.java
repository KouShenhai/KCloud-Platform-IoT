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

package org.laokou.admin.server;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.laokou.common.redis.utils.RedisUtil;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

/**
 * @author laokou
 */
@Slf4j
@SpringBootTest(classes = AdminApplication.class)
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class RedisTest {

    private final RedisUtil redisUtil;

    @Test
    public void redisAtomicLongTest() {
        String key = "laokou";
        redisUtil.addAndGet(key,10);
        long l = redisUtil.incrementAndGet(key);
        long l1 = redisUtil.incrementAndGet(key);
        long l2 = redisUtil.decrementAndGet(key);
        long l3 = redisUtil.getAtomicValue(key);
        Object o = redisUtil.get(key);
        log.info("{}",l);
        log.info("{}",l1);
        log.info("{}",l2);
        log.info("{}",l3);
        log.info("{}",o);
    }

}
