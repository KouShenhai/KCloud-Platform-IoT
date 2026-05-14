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

package org.laokou.common.idempotent.util;

import lombok.RequiredArgsConstructor;
import org.laokou.common.core.config.SystemSettingsProperties;
import org.laokou.common.i18n.common.exception.BizException;
import org.laokou.common.i18n.util.RedisKeyUtils;
import org.laokou.common.i18n.util.StringExtUtils;
import org.laokou.common.redis.util.RedisUtils;
import org.springframework.stereotype.Component;

/**
 * 幂等工具类.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public final class IdempotentUtils {

	private final RedisUtils redisUtils;

	private final SystemSettingsProperties systemSettingsProperties;

	public boolean isRepeatSubmit(String requestId) {
		if (StringExtUtils.isEmpty(requestId)) {
			throw new BizException("B_Idempotent_RequestIDIsNull", "请求ID不能为空");
		}
		String apiIdempotentKey = RedisKeyUtils.getApiIdempotentKey(requestId);
		return !redisUtils.setIfAbsent(apiIdempotentKey, 0, systemSettingsProperties.getIdempotentExpire().toSeconds());
	}

}
