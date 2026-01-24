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

package org.laokou.common.core;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.core.util.BigDecimalUtils;

/**
 * @author laokou
 */
class BigDecimalUtilsTest {

	@Test
	void test_add_withTwoDoubles_returnsSum() {
		Assertions.assertThat(BigDecimalUtils.add(2.0, 1.0)).isEqualTo(3.0);
		Assertions.assertThat(BigDecimalUtils.add(0.1, 0.2)).isEqualTo(0.3);
		Assertions.assertThat(BigDecimalUtils.add(-1.0, 1.0)).isEqualTo(0.0);
	}

	@Test
	void test_subtract_withTwoDoubles_returnsDifference() {
		Assertions.assertThat(BigDecimalUtils.subtract(2.0, 1.0)).isEqualTo(1.0);
		Assertions.assertThat(BigDecimalUtils.subtract(1.0, 2.0)).isEqualTo(-1.0);
		Assertions.assertThat(BigDecimalUtils.subtract(1.0, 1.0)).isEqualTo(0.0);
	}

	@Test
	void test_multiply_withTwoDoubles_returnsProduct() {
		Assertions.assertThat(BigDecimalUtils.multiply(2.0, 3.0)).isEqualTo(6.0);
		Assertions.assertThat(BigDecimalUtils.multiply(2.0, 0.0)).isEqualTo(0.0);
		Assertions.assertThat(BigDecimalUtils.multiply(-2.0, 3.0)).isEqualTo(-6.0);
	}

	@Test
	void test_divide_withTwoDoubles_returnsQuotient() {
		Assertions.assertThat(BigDecimalUtils.divide(6.0, 2.0)).isEqualTo(3.0);
		Assertions.assertThat(BigDecimalUtils.divide(6.0, 2.0, 0)).isEqualTo(3.0);
		Assertions.assertThat(BigDecimalUtils.divide(10.0, 3.0, 2)).isEqualTo(3.33);
	}

	@Test
	void test_divide_withNegativeScale_throwsException() {
		Assertions.assertThatThrownBy(() -> BigDecimalUtils.divide(10.0, 3.0, -1))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("scale");
	}

	@Test
	void test_round_withValidScale_returnsRoundedValue() {
		Assertions.assertThat(BigDecimalUtils.round(3.118, 2)).isEqualTo(3.12);
		Assertions.assertThat(BigDecimalUtils.round(3.115, 2)).isEqualTo(3.12);
		Assertions.assertThat(BigDecimalUtils.round(3.114, 2)).isEqualTo(3.11);
	}

	@Test
	void test_round_withNegativeScale_throwsException() {
		Assertions.assertThatThrownBy(() -> BigDecimalUtils.round(3.118, -1))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("scale");
	}

	@Test
	void test_returnMax_withTwoDoubles_returnsLargerValue() {
		Assertions.assertThat(BigDecimalUtils.returnMax(2.0, 1.0)).isEqualTo(2.0);
		Assertions.assertThat(BigDecimalUtils.returnMax(1.0, 2.0)).isEqualTo(2.0);
		Assertions.assertThat(BigDecimalUtils.returnMax(-1.0, -2.0)).isEqualTo(-1.0);
	}

	@Test
	void test_returnMin_withTwoDoubles_returnsSmallerValue() {
		Assertions.assertThat(BigDecimalUtils.returnMin(2.0, 1.0)).isEqualTo(1.0);
		Assertions.assertThat(BigDecimalUtils.returnMin(1.0, 2.0)).isEqualTo(1.0);
		Assertions.assertThat(BigDecimalUtils.returnMin(-1.0, -2.0)).isEqualTo(-2.0);
	}

	@Test
	void test_compareTo_withTwoDoubles_returnsComparisonResult() {
		Assertions.assertThat(BigDecimalUtils.compareTo(2.0, 1.0)).isGreaterThan(0);
		Assertions.assertThat(BigDecimalUtils.compareTo(1.0, 2.0)).isLessThan(0);
		Assertions.assertThat(BigDecimalUtils.compareTo(1.0, 1.0)).isEqualTo(0);
	}

}
