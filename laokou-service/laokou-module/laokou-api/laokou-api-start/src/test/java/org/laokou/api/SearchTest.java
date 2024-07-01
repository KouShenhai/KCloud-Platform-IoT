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

package org.laokou.api;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.core.utils.MapUtil;
import org.laokou.common.secret.utils.SecretUtil;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author laokou
 */
@Slf4j
@SpringBootTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class SearchTest {

	@Test
	@SneakyThrows
	void testApiSecret() {
		long timestamp = IdGenerator.SystemClock.now();
		String appKey = SecretUtil.APP_KEY;
		String appSecret = SecretUtil.APP_SECRET;
		String nonce = "laokou";
		Map<String, String> map = new LinkedHashMap<>(2);
		map.put("id", "1");
		map.put("name", "laokou");
		log.info("{}", timestamp);
		log.info("{}", SecretUtil.sign(appKey, appSecret, nonce, timestamp, MapUtil.parseParams(map, false)));
	}

}
