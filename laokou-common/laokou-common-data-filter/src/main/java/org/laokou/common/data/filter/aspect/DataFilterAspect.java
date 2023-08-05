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
package org.laokou.common.data.filter.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.laokou.auth.domain.user.SuperAdmin;
import org.laokou.auth.domain.user.User;
import org.laokou.common.core.enums.SuperAdminEnum;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.data.filter.annotation.DataFilter;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.security.utils.UserUtil;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author laokou
 */
@Aspect
@Slf4j
@Component
public class DataFilterAspect {

	@Before("@annotation(org.laokou.common.data.filter.annotation.DataFilter)")
	public void doBefore(JoinPoint point) {
		Object params = point.getArgs()[0];
		if (params instanceof Page page) {
			User user = UserUtil.user();
			// 超级管理员不过滤数据
			if (user.getSuperAdmin() == SuperAdmin.YES.ordinal()) {
				return;
			}
			try {
				// 否则数据过滤
				String sqlFilter = getSqlFilter(user, point);
				page.setSqlFilter(sqlFilter);
			}
			catch (Exception ex) {
				log.error("错误信息:{}", ex.getMessage());
			}
		}
	}

	/**
	 * 获取数据过滤的SQL
	 */
	private String getSqlFilter(User user, JoinPoint point) {
		MethodSignature signature = (MethodSignature) point.getSignature();
		Method method = signature.getMethod();
		DataFilter dataFilter = method.getAnnotation(DataFilter.class);
		if (dataFilter == null) {
			dataFilter = AnnotationUtils.findAnnotation(method, DataFilter.class);
		}
		// 获取表的别名
		assert dataFilter != null;
		String tableAlias = dataFilter.tableAlias();
		if (StringUtil.isNotEmpty(tableAlias)) {
			tableAlias += ".";
		}
		StringBuilder sqlFilter = new StringBuilder();
		// 用户列表
		List<Long> deptIds = user.getDeptIds();
		sqlFilter.append("(");
		if (CollectionUtil.isNotEmpty(deptIds)) {
			sqlFilter.append(tableAlias).append(dataFilter.deptId()).append(" in (");
			sqlFilter.append(String.join(",", deptIds.stream().map(String::valueOf).toArray(String[]::new)));
			sqlFilter.append(") or ");
		}
		sqlFilter.append(tableAlias).append(dataFilter.userId()).append(" = ").append(user.getId());
		sqlFilter.append(")");
		return sqlFilter.toString();
	}

}
