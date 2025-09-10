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
import org.laokou.common.core.util.BigDecimalUtils;

/**
 * @author laokou
 */
class BigDecimalUtilsTest {

	@Test
	void test_BigDecimal() {
		double a = 2.0;
		double b = 1.0;
		Assertions.assertThat(BigDecimalUtils.add(a, b)).isEqualTo(3.0);
		Assertions.assertThat(BigDecimalUtils.subtract(a, b)).isEqualTo(1.0);
		Assertions.assertThat(BigDecimalUtils.multiply(a, b)).isEqualTo(2.0);
		Assertions.assertThat(BigDecimalUtils.divide(a, b)).isEqualTo(2.0);
		Assertions.assertThat(BigDecimalUtils.divide(a, b, 0)).isEqualTo(2.0);
		Assertions.assertThat(BigDecimalUtils.round(3.118, 2)).isEqualTo(3.12);
		Assertions.assertThat(BigDecimalUtils.returnMax(a, b)).isEqualTo(a);
		Assertions.assertThat(BigDecimalUtils.returnMin(a, b)).isEqualTo(b);
		Assertions.assertThat(BigDecimalUtils.compareTo(a, b) > 0).isTrue();
		Assertions.assertThat(BigDecimalUtils.compareTo(a, b) < 0).isFalse();
	}

}
