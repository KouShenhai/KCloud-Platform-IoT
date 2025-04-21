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
import org.laokou.common.core.util.BigDecimalUtils;

/**
 * @author laokou
 */
class BigDecimalUtilsTest {

	@Test
	void test() {
		double a = 2.0;
		double b = 1.0;
		Assertions.assertEquals(3.0, BigDecimalUtils.add(a, b));
		Assertions.assertEquals(1.0, BigDecimalUtils.subtract(a, b));
		Assertions.assertEquals(2.0, BigDecimalUtils.multiply(a, b));
		Assertions.assertEquals(2.0, BigDecimalUtils.divide(a, b));
		Assertions.assertEquals(2.0, BigDecimalUtils.divide(a, b, 0));
		Assertions.assertEquals(3.12, BigDecimalUtils.round(3.118, 2));
		Assertions.assertEquals(a, BigDecimalUtils.returnMax(a, b));
		Assertions.assertEquals(b, BigDecimalUtils.returnMin(a, b));
		Assertions.assertTrue(BigDecimalUtils.compareTo(a, b) > 0);
		Assertions.assertFalse(BigDecimalUtils.compareTo(a, b) < 0);
	}

}
