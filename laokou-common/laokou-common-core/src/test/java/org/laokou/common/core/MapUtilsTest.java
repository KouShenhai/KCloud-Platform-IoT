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

package org.laokou.common.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.core.util.MapUtils;
import org.springframework.util.MultiValueMap;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static org.laokou.common.i18n.common.constant.StringConstants.AND;
import static org.laokou.common.i18n.common.constant.StringConstants.EQUAL;

/**
 * @author laokou
 */
class MapUtilsTest {

	@Test
	void testMap() {
		Map<String, Set<String>> map = MapUtils.toUriMap(
				Map.of("POST",
						Set.of("/test=laokou-common-core", "/test2=laokou-common-core", "/test3=laokou-common-i18n")),
				"laokou-common-core");
		Set<String> set = map.get("POST");
		Assertions.assertNotNull(map);
		Assertions.assertEquals(1, map.size());
		Assertions.assertEquals(2, set.size());
		Assertions.assertTrue(set.contains("/test"));
		Assertions.assertTrue(set.contains("/test2"));
		Assertions.assertFalse(set.contains("/test3"));
		Assertions.assertEquals(100, MapUtils.initialCapacity(75));
		Assertions.assertFalse(MapUtils.isEmpty(map));
		Assertions.assertTrue(MapUtils.isNotEmpty(map));
		map = MapUtils.toUriMap(Map.of("POST", Set.of("/test3=laokou-common-i18n")), "laokou-common-core", EQUAL);
		Assertions.assertNotNull(map);
		Assertions.assertEquals(1, map.size());
		Assertions.assertEquals(0, map.get("POST").size());
		Map<String, String> paramMap = MapUtils.getParameterMap("a=1&b=2", AND).asSingleValueMap();
		Assertions.assertEquals(2, paramMap.size());
		Assertions.assertEquals("1", paramMap.get("a"));
		Assertions.assertEquals("2", paramMap.get("b"));
		Assertions.assertEquals("a=1&b=2", MapUtils.parseParamterString(paramMap));
		Map<String, String> m = new LinkedHashMap<>(3);
		m.put("a", "哈哈哈");
		m.put("b", "嘻嘻");
		Assertions.assertEquals("a=%E5%93%88%E5%93%88%E5%93%88&b=%E5%98%BB%E5%98%BB", MapUtils.parseParamterString(m));
		Assertions.assertEquals("a=哈哈哈&b=嘻嘻", MapUtils.parseParamterString(m, false));
		MultiValueMap<String, String> multiValueMap = MapUtils.getParameterMap(Map.of("a", new String[] { "1", "2" }));
		Assertions.assertEquals(1, multiValueMap.size());
		Assertions.assertEquals(2, multiValueMap.get("a").size());
		Assertions.assertEquals("1", multiValueMap.getFirst("a"));
		Assertions.assertEquals("2", multiValueMap.get("a").get(1));
	}

}
