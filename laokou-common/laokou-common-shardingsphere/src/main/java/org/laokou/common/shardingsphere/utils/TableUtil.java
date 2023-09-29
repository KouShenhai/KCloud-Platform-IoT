/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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

package org.laokou.common.shardingsphere.utils;

import lombok.SneakyThrows;
import org.laokou.common.core.utils.DateUtil;
import org.laokou.common.core.utils.ResourceUtil;
import org.laokou.common.core.utils.TemplateUtil;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author laokou
 */
public class TableUtil {

	@SneakyThrows
	public static String getLoginLogSqlScript(LocalDateTime localDateTime) {
		return getContent(localDateTime, "script/boot_sys_login_log.sql");
	}

	@SneakyThrows
	public static String getUserSqlScript(LocalDateTime localDateTime) {
		return getContent(localDateTime, "script/boot_sys_user.sql");
	}

	@SneakyThrows
	private static String getContent(LocalDateTime localDateTime, String location) {
		Map<String, Object> params = new HashMap<>(1);
		params.put("suffix", DateUtil.format(localDateTime, DateUtil.YYYYMM));
		String template = new String(ResourceUtil.getResource(location).getInputStream().readAllBytes());
		return TemplateUtil.getContent(template, params);
	}

}
