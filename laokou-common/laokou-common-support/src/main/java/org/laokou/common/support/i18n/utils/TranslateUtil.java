/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.support.i18n.utils;

import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.redis.utils.RedisKeyUtil;
import org.laokou.common.redis.utils.RedisUtil;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class TranslateUtil {

	private final RedisUtil redisUtil;

	public String getMessage(String code, String language) {
		Object o = redisUtil.hGetNative(RedisKeyUtil.getI18nMessageKey(language), code);
		if (ObjectUtil.isNull(o)) {
			return code;
		}
		return o.toString();
	}

	public String getMessage(String code) {
		return getMessage(code, LocaleContextHolder.getLocale().getLanguage());
	}

}
