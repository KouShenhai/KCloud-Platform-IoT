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

package org.laokou.common.mybatisplus.template;

import com.google.common.base.CaseFormat;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.utils.DateUtil;
import org.laokou.common.i18n.utils.StringUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.laokou.common.i18n.common.DSConstant.INSERT_SQL_TEMPLATE;
import static org.laokou.common.i18n.common.constant.StringConstant.*;

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

	public static List<String> getInsertSqlScriptList(List<Map<String, String>> list, String tableName) {
		List<String> sqlList = new ArrayList<>(list.size());
		list.forEach(item -> {
			List<String> keys = item.keySet()
				.stream()
				// 转为下划线
				.map(i -> CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, i))
				.toList();
			List<String> values = item.values()
				.stream()
				.map(i -> SINGLE_QUOT + StringUtil.empty(i) + SINGLE_QUOT)
				.toList();
			String sql = String.format(INSERT_SQL_TEMPLATE, tableName,
					StringUtil.collectionToDelimitedString(keys, COMMA),
					StringUtil.collectionToDelimitedString(values, COMMA));
			sqlList.add(sql);
		});
		return sqlList;
	}

	private static LocalDate toDate(String dateStr) {
		int year = Integer.parseInt(dateStr.substring(0, 4));
		int month = Integer.parseInt(dateStr.substring(5, 7));
		return LocalDate.of(year, month, 1);
	}

}
