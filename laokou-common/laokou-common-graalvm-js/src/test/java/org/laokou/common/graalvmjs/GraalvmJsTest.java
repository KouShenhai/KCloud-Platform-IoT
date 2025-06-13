/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.graalvmjs;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.graalvmjs.config.Executor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import java.util.Map;

/**
 * @author laokou
 */
@SpringBootTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class GraalvmJsTest {

	private final Executor jsExecutor;

	@Test
	void test() {
		String script = "function processData(inputMap) { return inputMap['test']; } processData;";
		Map<String, Object> inputMap = Map.of("test", 123);
		Assertions.assertEquals(123, jsExecutor.execute(script, inputMap).asInt());
		String script2 = "function processData(s) { const p = JSON.parse(s); return p   } processData;";
		Map<?, ?> map = jsExecutor.execute(script2, "{\"test\":\"123\"}").as(Map.class);
		Assertions.assertEquals("123", map.get("test"));
		String script3 = "function processData(s) { return {}   } processData;";
		map = jsExecutor.execute(script3, "{\"test\":\"123\"}").as(Map.class);
		Assertions.assertTrue(map.isEmpty());
		String script4 = "function processData(s) { return parseInt(s, 16)   } processData;";
		Assertions.assertEquals(26, jsExecutor.execute(script4, "0x1a").asInt());
	}

}
