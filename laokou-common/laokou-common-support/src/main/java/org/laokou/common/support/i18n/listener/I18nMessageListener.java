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

package org.laokou.common.support.i18n.listener;

import io.micrometer.common.lang.NonNullApi;
import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.common.constants.StringConstant;
import org.laokou.common.mybatisplus.mapper.I18nMessageMapper;
import org.laokou.common.redis.utils.RedisUtil;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

import static org.laokou.common.redis.utils.RedisKeyUtil.getI18nMessageKey;

@NonNullApi
@AutoConfiguration
@RequiredArgsConstructor
public class I18nMessageListener implements ApplicationListener<ApplicationReadyEvent> {

	private final RedisUtil redisUtil;

	private final I18nMessageMapper i18nMessageMapper;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		i18nMessageMapper.selectObjects(StringConstant.EMPTY)
			.forEach(item -> redisUtil.hSetIfAbsentNative(getI18nMessageKey(item.getLang()), item.getCode(),
					item.getMessage()));
	}

}
