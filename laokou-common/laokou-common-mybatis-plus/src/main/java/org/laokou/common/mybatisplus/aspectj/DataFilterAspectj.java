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

package org.laokou.common.mybatisplus.aspectj;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.laokou.common.context.util.UserUtils;
import org.laokou.common.core.util.CollectionExtUtils;
import org.laokou.common.i18n.common.constant.StringConstants;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.StringExtUtils;
import org.laokou.common.mybatisplus.annotation.DataFilter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Aspect
@Component
public class DataFilterAspectj {

	@Before("@annotation(dataFilter)")
	public void doBefore(JoinPoint joinPoint, DataFilter dataFilter) {
		if (UserUtils.isSuperAdmin()) {
			return;
		}
		Object arg = joinPoint.getArgs()[0];
		if (arg instanceof PageQuery pageQuery) {
			pageQuery.setSqlFilter(getSql(dataFilter));
		}
	}

	private String getSql(DataFilter dataFilter) {
		return Stream.of(getDeptFilterSql(dataFilter), getUserFilterSql(dataFilter))
			.filter(StringExtUtils::isNotEmpty)
			.collect(Collectors.joining(StringConstants.SPACE + StringConstants.AND_));
	}

	private String getTableAlias(DataFilter dataFilter) {
		String str = dataFilter.tableAlias();
		if (StringExtUtils.isNotEmpty(str)) {
			return str + StringConstants.DOT;
		}
		return str;
	}

	private String getDeptFilterSql(DataFilter dataFilter) {
		List<Long> deptIds = UserUtils.getDeptIds();
		if (CollectionExtUtils.isEmpty(deptIds)) {
			return StringConstants.EMPTY;
		}
		StringBuilder sql = new StringBuilder();
		String tableAlias = getTableAlias(dataFilter);
		return sql.append(StringConstants.SPACE)
			.append(tableAlias)
			.append(dataFilter.deptId())
			.append(StringConstants.SPACE)
			.append(StringConstants.IN_)
			.append(StringConstants.SPACE)
			.append(StringConstants.LEFT)
			.append(deptIds.stream().map(String::valueOf).collect(Collectors.joining(StringConstants.COMMA)))
			.append(StringConstants.RIGHT)
			.toString();
	}

	private String getUserFilterSql(DataFilter dataFilter) {
		Long creator = UserUtils.getCreator();
		if (ObjectUtils.isNull(creator)) {
			return StringConstants.EMPTY;
		}
		StringBuilder sql = new StringBuilder();
		String tableAlias = getTableAlias(dataFilter);
		return sql.append(StringConstants.SPACE)
			.append(tableAlias)
			.append(dataFilter.userId())
			.append(StringConstants.SPACE)
			.append(StringConstants.EQUAL)
			.append(StringConstants.SPACE)
			.append(creator)
			.toString();
	}

}
