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

import static org.laokou.common.core.constant.Constant.*;
import static org.laokou.common.mybatisplus.constant.Constant.IN;

/**
 * @author laokou
 */
@Aspect
@Slf4j
@Component
public class DataFilterAspect {

	@Before("@annotation(org.laokou.admin.domain.annotation.DataFilter)")
	public void doBefore(JoinPoint point) {
		Object param = Arrays.stream(point.getArgs()).filter(arg -> arg instanceof PageQuery).findFirst()
				.orElse(new PageQuery());
		if (param instanceof PageQuery pageQuery) {
			User user = UserUtil.user();
			// 超级管理员不过滤数据
			if (user.getSuperAdmin() == SuperAdmin.YES.ordinal()) {
				return;
			}
			try {
				// 数据过滤
				String sqlFilter = getSqlFilter(user, point);
				pageQuery.setSqlFilter(sqlFilter);
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
		String deptIdColumn = dataFilter.deptId();
		String userIdColumn = dataFilter.userId();
		List<Long> deptIds = user.getDeptIds();
		StringBuilder sqlFilter = new StringBuilder();
		if (StringUtil.isNotEmpty(alias)) {
			alias += DOT;
		}
		sqlFilter.append(LEFT);
		if (CollectionUtil.isNotEmpty(deptIds)) {
			sqlFilter.append(alias).append(deptIdColumn).append(SPACE).append(IN).append(SPACE).append(LEFT);
			sqlFilter.append(String.join(COMMA, deptIds.stream().map(String::valueOf).toArray(String[]::new)));
			sqlFilter.append(RIGHT).append(SPACE).append(OR).append(SPACE);
		}
		sqlFilter.append(alias).append(userIdColumn).append(SPACE).append(EQUAL).append(SPACE).append(user.getId());
		sqlFilter.append(RIGHT);
		String sql = sqlFilter.toString();
		after(sql);
		return sql;
	}

	private void after(String sql) {
		log.info("获取拼接后的SQL:{}", sql);
	}

}
