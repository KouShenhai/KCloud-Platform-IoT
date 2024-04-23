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

package org.laokou.common.mybatisplus.config;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.laokou.common.core.context.UserContextHolder;
import org.laokou.common.i18n.utils.ObjectUtil;

import java.util.Set;

import static org.laokou.common.i18n.common.TenantConstant.DEFAULT;

/**
 * @author laokou
 */
public class GlobalTenantLineHandler implements TenantLineHandler {

	private final Set<String> ignoreTables;

	public GlobalTenantLineHandler(Set<String> ignoreTables) {
		this.ignoreTables = ignoreTables;
	}

	@Override
	public boolean ignoreTable(String tableName) {
		return ignoreTables.contains(tableName) || ignoreTables.stream().anyMatch(tableName::contains);
	}

	@Override
	public Expression getTenantId() {
		return new LongValue(tenantId());
	}

	private Long tenantId() {
		Long tenantId = UserContextHolder.get().getTenantId();
		if (ObjectUtil.isNull(tenantId)) {
			return DEFAULT;
		}
		return tenantId;
	}

}
