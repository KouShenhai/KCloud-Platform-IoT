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

package org.laokou.common.trace;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.trace.util.MDCUtils;

/**
 * @author laokou
 */
class MDCUtilsTest {

	@Test
	void test_mdc() {
		MDCUtils.put("111", "222");
		Assertions.assertThat(MDCUtils.getTraceId()).isEqualTo("111");
		Assertions.assertThat(MDCUtils.getSpanId()).isEqualTo("222");
		Assertions.assertThatNoException().isThrownBy(MDCUtils::clear);
		Assertions.assertThat(MDCUtils.getTraceId()).isBlank();
		Assertions.assertThat(MDCUtils.getSpanId()).isBlank();
	}

}
