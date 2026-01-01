/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.algorithm;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.algorithm.template.select.HashAlgorithm;
import org.laokou.common.algorithm.template.select.RandomAlgorithm;
import org.laokou.common.algorithm.template.select.RoundRobinAlgorithm;
import java.util.List;

class AlgorithmTest {

	@Test
	void test_loadbalancer() {
		List<Integer> numbers = List.of(1, 2);
		// 负载均衡【哈希算法】
		Assertions.assertThat(new HashAlgorithm().select(numbers, 100)).isEqualTo(1);
		// 负载均衡【轮询算法】
		Assertions.assertThat(new RoundRobinAlgorithm().select(numbers, null)).isEqualTo(1);
		// 负载均衡【随机算法】
		Assertions.assertThat(new RandomAlgorithm().select(numbers, null))
			.satisfiesAnyOf(number -> Assertions.assertThat(number).isEqualTo(1),
					number -> Assertions.assertThat(number).isEqualTo(2));
	}

}
