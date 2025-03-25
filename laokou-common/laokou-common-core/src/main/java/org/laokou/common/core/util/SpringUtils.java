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

package org.laokou.common.core.util;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import static org.laokou.common.core.util.SpringContextUtils.APPLICATION_NAME;
import static org.laokou.common.core.util.SpringContextUtils.DEFAULT_SERVICE_ID;

/**
 * Spring工具类.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class SpringUtils {

	private final Environment environment;

	/**
	 * 获取服务ID.
	 */
	public String getServiceId() {
		return environment.getProperty(APPLICATION_NAME, DEFAULT_SERVICE_ID);
	}

}
