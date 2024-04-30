/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

package org.laokou.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.laokou.common.redis.utils.RedisUtil;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import java.time.Duration;

/**
 * @author laokou
 */
@Slf4j
@SpringBootTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class CacheTest {

	private final RedisUtil redisUtil;

	@Test
	void timeTest() {
		String value = "10s";
		Duration duration = DurationStyle.detectAndParse(value);
		log.info("获取值：{}", duration.toMillis());
	}

	@Test
	public void mapCacheTest() {
		redisUtil.hSet("tenantt", "ttt", "333");
		Object o = redisUtil.hGet("tenantt", "ttt");
		log.info("获取值：{}", o);
	}

}
