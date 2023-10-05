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
package org.laokou.admin.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.laokou.admin.domain.annotation.DataFilter;
import org.laokou.auth.domain.user.SuperAdmin;
import org.laokou.auth.domain.user.User;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.security.utils.UserUtil;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.laokou.common.i18n.common.Constant.*;

/**
 * @author laokou
 */
@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class DataFilterAspect {

	@Before("@annotation(org.laokou.admin.domain.annotation.DataFilter)")
	public void doBefore(JoinPoint point) {
		Object param = Arrays.stream(point.getArgs())
			.filter(arg -> arg instanceof PageQuery)
			.findFirst()
			.orElse(new PageQuery());
		if (param instanceof PageQuery pageQuery) {
			User user = UserUtil.user();
			// 超级管理员不过滤数据
			if (user.getSuperAdmin() == SuperAdmin.YES.ordinal()) {
				return;
			}
			try {
				// 数据过滤
				pageQuery.setSqlFilter(getSqlFilter(user, point));
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
		assert dataFilter != null;
		String alias = dataFilter.alias();
		String deptPathColumn = dataFilter.deptPath();
		String userIdColumn = dataFilter.userId();
		List<String> deptPaths = user.getDeptPaths();
		StringBuilder sqlFilter = new StringBuilder(300);
		if (StringUtil.isNotEmpty(alias)) {
			alias += DOT;
		}
		sqlFilter.append(LEFT);
		if (CollectionUtil.isNotEmpty(deptPaths)) {
			for (String deptPath : deptPaths) {
				sqlFilter.append(alias)
					.append(deptPathColumn)
					.append(SPACE)
					.append(LIKE)
					.append(SPACE)
					.append(DOUBLE_QUOT)
					.append(deptPath.trim())
					.append(PERCENT)
					.append(DOUBLE_QUOT)
					.append(SPACE)
					.append(OR)
					.append(SPACE);
			}
		}
		sqlFilter.append(alias)
			.append(userIdColumn)
			.append(SPACE)
			.append(EQUAL)
			.append(SPACE)
			.append(DOUBLE_QUOT)
			.append(user.getId())
			.append(DOUBLE_QUOT);
		sqlFilter.append(RIGHT);
		String sql = sqlFilter.toString();
		after(sql);
		return sql;
	}

	private void after(String sql) {
		log.info("获取拼接后的SQL:{}", sql);
	}

}
