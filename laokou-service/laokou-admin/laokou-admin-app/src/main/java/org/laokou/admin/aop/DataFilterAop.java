/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.admin.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.laokou.admin.domain.annotation.DataFilter;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.security.utils.UserDetail;
import org.laokou.common.security.utils.UserUtil;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

import static org.laokou.common.i18n.common.DatasourceConstant.LIKE;
import static org.laokou.common.i18n.common.DatasourceConstant.OR;
import static org.laokou.common.i18n.common.StringConstant.*;

/**
 * 数据权限切面.
 *
 * @author laokou
 */
@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class DataFilterAop {

	@Before("@annotation(org.laokou.admin.domain.annotation.DataFilter)")
	public void doBefore(JoinPoint point) {
		Object param = Arrays.stream(point.getArgs()).filter(arg -> arg instanceof PageQuery).findFirst().orElse(null);
		if (param instanceof PageQuery pageQuery) {
			UserDetail userDetail = UserUtil.user();
			// 超级管理员不过滤数据
			if (userDetail.isSuperAdministrator()) {
				return;
			}
			try {
				// 数据过滤
				pageQuery.setSqlFilter(getSqlFilter(userDetail, point));
			}
			catch (Exception ex) {
				log.error("错误信息：{}，详情见日志", LogUtil.record(ex.getMessage()), ex);
			}
		}
	}

	/**
	 * 获取数据过滤的SQL.
	 * @param point 切面对象
	 * @param userDetail 用户
	 * @return 拼接的SQL
	 */
	private String getSqlFilter(UserDetail userDetail, JoinPoint point) {
		MethodSignature signature = (MethodSignature) point.getSignature();
		Method method = signature.getMethod();
		DataFilter dataFilter = AnnotationUtils.findAnnotation(method, DataFilter.class);
		Assert.isTrue(ObjectUtil.isNotNull(dataFilter), "@DataFilter is null");
		String tableAlias = dataFilter.tableAlias();
		String deptPathColumn = dataFilter.deptPath();
		String creatorColumn = dataFilter.creator();
		Set<String> deptPaths = userDetail.getDeptPaths();
		StringBuilder sqlFilter = new StringBuilder(300);
		if (StringUtil.isNotEmpty(tableAlias)) {
			tableAlias += DOT;
		}
		sqlFilter.append(LEFT);
		if (CollectionUtil.isNotEmpty(deptPaths)) {
			for (String deptPath : deptPaths) {
				sqlFilter.append(tableAlias)
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
		sqlFilter.append(tableAlias)
			.append(creatorColumn)
			.append(SPACE)
			.append(EQUAL)
			.append(SPACE)
			.append(DOUBLE_QUOT)
			.append(userDetail.getId())
			.append(DOUBLE_QUOT);
		sqlFilter.append(RIGHT);
		return afterAndResult(sqlFilter.toString());
	}

	private String afterAndResult(String sql) {
		if (log.isDebugEnabled()) {
			log.debug("获取拼接后的SQL:{}", sql);
		}
		return sql;
	}

}
