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

package org.laokou.common.mybatisplus.template;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.core.utils.ResourceUtil;
import org.laokou.common.core.utils.TemplateUtil;
import org.laokou.common.i18n.utils.DateUtil;

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

	public static final String MAX_TIME = "2099-12-31 23:59:59";

	public static final String MIN_TIME = "2022-01-01 00:00:00";

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

	public static String getDynamicTable(Long snowflakeId, String tableName) {
		return tableName + UNDER + DateUtil.format(IdGenerator.getLocalDateTime(snowflakeId), DateUtil.YYYYMM);
	}

	@SneakyThrows
	public static String getLoginLogSqlScript(LocalDateTime localDateTime) {
		return getContent(localDateTime, "scripts/boot_sys_login_log.ftl");
	}

	@SneakyThrows
	public static String getUserSqlScript(LocalDateTime localDateTime) {
		return getContent(localDateTime, "scripts/boot_sys_user.ftl");
	}

	@SneakyThrows
	private static String getContent(LocalDateTime localDateTime, String location) {
		Map<String, Object> params = new HashMap<>(1);
		params.put("suffix", DateUtil.format(localDateTime, DateUtil.YYYYMM));
		String template = new String(ResourceUtil.getResource(location).getInputStream().readAllBytes());
		return TemplateUtil.getContent(template, params);
	}

	private static LocalDate toDate(String dateStr) {
		int year = Integer.parseInt(dateStr.substring(0, 4));
		int month = Integer.parseInt(dateStr.substring(5, 7));
		return LocalDate.of(year, month, 1);
	}

}
