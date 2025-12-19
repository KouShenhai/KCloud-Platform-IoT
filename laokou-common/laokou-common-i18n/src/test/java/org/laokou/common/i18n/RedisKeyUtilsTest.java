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

package org.laokou.common.i18n;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.i18n.util.RedisKeyUtils;

class RedisKeyUtilsTest {

	@Test
	void test() {
		Assertions.assertThat(RedisKeyUtils.getMobileAuthCaptchaKey("test")).isEqualTo("auth:mobile:captcha:test");
		Assertions.assertThat(RedisKeyUtils.getMailAuthCaptchaKey("test")).isEqualTo("auth:mail:captcha:test");
		Assertions.assertThat(RedisKeyUtils.getUsernamePasswordAuthCaptchaKey("test"))
			.isEqualTo("auth:username-password:captcha:test");
		Assertions.assertThat(RedisKeyUtils.getApiIdempotentKey("test")).isEqualTo("api:idempotent:test");
		Assertions.assertThat(RedisKeyUtils.getRouteDefinitionHashKey()).isEqualTo("route:definition");
	}

}
