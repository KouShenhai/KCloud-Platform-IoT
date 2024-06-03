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

package org.laokou.common.netty.utils;

import io.netty.handler.logging.LogLevel;
import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LogUtil {

	private final Environment environment;

	public LogLevel getLogLevel() {
		String env = environment.getProperty("spring.profiles.active", "test");
		if (ObjectUtil.equals("prod", env)) {
			return LogLevel.ERROR;
		}
		return LogLevel.INFO;
	}

}
