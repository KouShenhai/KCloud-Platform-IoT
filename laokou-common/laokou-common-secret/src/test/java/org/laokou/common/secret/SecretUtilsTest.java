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

package org.laokou.common.secret;

import org.junit.jupiter.api.Test;
import org.laokou.common.secret.util.SecretUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author laokou
 */
class SecretUtilsTest {

	@Test
	void test() {
		String sign = SecretUtils.sign(SecretUtils.APP_KEY, SecretUtils.APP_SECRET, "1", String.valueOf(100000), "");
		assertThat(sign).isEqualTo("e1506abd8395b0763f08d2a0e56f6738");
	}

}
