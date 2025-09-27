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

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.core.util.MapUtils;
import org.laokou.common.i18n.common.constant.StringConstants;
import org.springframework.util.MultiValueMap;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author laokou
 */
class MapUtilsTest {

	@Test
	void test_map() {
		Map<String, Set<String>> map = MapUtils.toUriMap(
				Map.of("POST",
						Set.of("/test=laokou-common-core", "/test2=laokou-common-core", "/test3=laokou-common-i18n")),
				"laokou-common-core");
		Set<String> set = map.get("POST");
		Assertions.assertThat(map).isNotNull();
		Assertions.assertThat(map.size()).isEqualTo(1);
		Assertions.assertThat(set.size()).isEqualTo(2);
		Assertions.assertThat(set.contains("/test")).isTrue();
		Assertions.assertThat(set.contains("/test2")).isTrue();
		Assertions.assertThat(set.contains("/test3")).isFalse();
		Assertions.assertThat(MapUtils.initialCapacity(75)).isEqualTo(100);
		Assertions.assertThat(MapUtils.isEmpty(map)).isFalse();
		Assertions.assertThat(MapUtils.isNotEmpty(map)).isTrue();
		map = MapUtils.toUriMap(Map.of("POST", Set.of("/test3=laokou-common-i18n")), "laokou-common-core",
				StringConstants.EQUAL);
		Assertions.assertThat(map).isNotNull();
		Assertions.assertThat(map.size()).isEqualTo(1);
		Assertions.assertThat(map.get("POST").size()).isEqualTo(0);
		Map<String, String> paramMap = MapUtils.getParameterMap("a=1&b=2", StringConstants.AND).asSingleValueMap();
		Assertions.assertThat(paramMap.size()).isEqualTo(2);
		Assertions.assertThat(paramMap.get("a")).isEqualTo("1");
		Assertions.assertThat(paramMap.get("b")).isEqualTo("2");
		Assertions.assertThat(MapUtils.parseParamterString(paramMap)).isEqualTo("a=1&b=2");
		Map<String, String> m = new LinkedHashMap<>(3);
		m.put("a", "哈哈哈");
		m.put("b", "嘻嘻");
		Assertions.assertThat(MapUtils.parseParamterString(m))
			.isEqualTo("a=%E5%93%88%E5%93%88%E5%93%88&b=%E5%98%BB%E5%98%BB");
		Assertions.assertThat(MapUtils.parseParamterString(m, false)).isEqualTo("a=哈哈哈&b=嘻嘻");
		MultiValueMap<String, String> multiValueMap = MapUtils.getParameterMap(Map.of("a", new String[] { "1", "2" }));
		Assertions.assertThat(multiValueMap.size()).isEqualTo(1);
		Assertions.assertThat(multiValueMap.get("a").size()).isEqualTo(2);
		Assertions.assertThat(multiValueMap.getFirst("a")).isEqualTo("1");
		Assertions.assertThat(multiValueMap.get("a").get(1)).isEqualTo("2");
	}

}
