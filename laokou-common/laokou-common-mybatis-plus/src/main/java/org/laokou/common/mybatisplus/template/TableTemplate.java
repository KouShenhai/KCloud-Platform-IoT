/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.common.mybatisplus.template;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.ResourceUtil;
import org.laokou.common.core.utils.TemplateUtil;
import org.laokou.common.i18n.utils.DateUtil;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.laokou.common.i18n.common.Constant.UNDER;

/**
 * @author laokou
 */
@Slf4j
public class TableTemplate {

	public static List<String> getDynamicTables(String start, String end, String tableName) {
		LocalDate date1 = toDate(start);
		LocalDate date2 = toDate(end);
		int subMonths = (int) (DateUtil.getMonths(date1, date2) + 1);
		List<String> list = new ArrayList<>(subMonths);
		while (DateUtil.isBefore(date1, date2) || date1.equals(date2)) {
			list.add(tableName.concat(UNDER).concat(DateUtil.format(date1, DateUtil.YYYYMM)));
			date1 = DateUtil.plusMonths(date1, 1);
		}
		return list;
	}

	@SneakyThrows
	public static String getCreateLoginLogTableSqlScript(LocalDateTime localDateTime) {
		return getContent(localDateTime, "scripts/boot_sys_login_log.sql");
	}

	@SneakyThrows
	public static String getCreateTenantDBSqlScript() {
		return TemplateUtil.getContent("scripts/kcloud_platform_alibaba_tenant.sql", new HashMap<>(0));
	}

	@SneakyThrows
	private static String getContent(LocalDateTime localDateTime, String location) {
		Map<String, Object> params = new HashMap<>(1);
		params.put("suffix", DateUtil.format(localDateTime, DateUtil.YYYYMM));
		return getContent(location, params);
	}

	@SneakyThrows
	private static String getContent(String location, Map<String, Object> params) {
		try (InputStream inputStream = ResourceUtil.getResource(location).getInputStream()) {
			String template = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
			return TemplateUtil.getContent(template, params);
		}
	}

	private static LocalDate toDate(String dateStr) {
		int year = Integer.parseInt(dateStr.substring(0, 4));
		int month = Integer.parseInt(dateStr.substring(5, 7));
		return LocalDate.of(year, month, 1);
	}

}
