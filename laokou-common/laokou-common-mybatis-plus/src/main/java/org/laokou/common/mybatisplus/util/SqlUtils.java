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

package org.laokou.common.mybatisplus.util;

import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.laokou.common.core.util.CollectionExtUtils;
import org.laokou.common.i18n.common.constant.StringConstants;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.StringExtUtils;

import java.util.List;

/**
 * @author laokou
 */
@Slf4j
public class SqlUtils {

	public static String formatSql(String sql) {
		if (StringExtUtils.isEmpty(sql)) {
			throw new IllegalArgumentException("SQL cannot be empty");
		}
		return parseSql(sql).toString();
	}

	public static PlainSelect plainSelect(String sql) {
		return ((Select) parseSql(sql)).getPlainSelect();
	}

	public static String getCompleteSql(BoundSql boundSql) {
		String sql = boundSql.getSql().replaceAll("\\s+", StringConstants.SPACE);
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		if (CollectionExtUtils.isEmpty(parameterMappings)) {
			return sql;
		}
		Object parameterObject = boundSql.getParameterObject();
		MetaObject metaObject = ObjectUtils.isNotNull(parameterObject) ? SystemMetaObject.forObject(parameterObject)
				: null;
		for (ParameterMapping parameterMapping : parameterMappings) {
			Object value = getParameterValue(boundSql, parameterMapping, metaObject);
			sql = sql.replaceFirst("\\?", formatValue(value));
		}
		return sql;
	}

	/**
	 * 严谨获取参数值：考虑了附加参数、内置类型处理器等.
	 */
	private static Object getParameterValue(BoundSql boundSql, ParameterMapping pm, MetaObject metaObject) {
		String propertyName = pm.getProperty();
		if (boundSql.hasAdditionalParameter(propertyName)) {
			return boundSql.getAdditionalParameter(propertyName);
		}
		if (ObjectUtils.isNull(metaObject)) {
			return null;
		}
		if (metaObject.hasGetter(propertyName)) {
			return metaObject.getValue(propertyName);
		}
		else {
			return metaObject.getOriginalObject();
		}
	}

	/**
	 * 格式化参数值，使其符合 SQL 语法.
	 */
	private static String formatValue(Object obj) {
		return switch (obj) {
			case null -> "NULL";
			// 处理字符串中的单引号，防止SQL注入（虽然仅用于日志展示，也应严谨）
			case String str -> "'" + str.replace("'", "''") + "'";
			default -> obj.toString();
		};
	}

	private static Statement parseSql(String sql) {
		try {
			return CCJSqlParserUtil.parse(sql);
		}
		catch (Exception ex) {
			log.error("SQL解析失败，错误信息：{}", ex.getMessage(), ex);
			throw new SystemException("S_DS_SqlParseFailed", "SQL解析失败", ex);
		}
	}

}
