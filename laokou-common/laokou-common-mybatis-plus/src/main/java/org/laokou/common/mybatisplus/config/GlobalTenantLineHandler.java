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

package org.laokou.common.mybatisplus.config;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.schema.Column;
import org.laokou.common.core.context.UserContextHolder;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.mybatisplus.context.IgnoreTableContextHolder;

import java.util.List;
import java.util.Set;

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
		try {
			String name = IgnoreTableContextHolder.get();
			return (StringUtil.isNotEmpty(name) && ObjectUtil.equals(tableName, name))
					|| ignoreTables.contains(tableName) || ignoreTables.stream().anyMatch(tableName::contains);
		}
		finally {
			IgnoreTableContextHolder.clear();
		}
	}

	@Override
	public Expression getTenantId() {
		Long tenantId = UserContextHolder.get().getTenantId();
		return ObjectUtil.isNull(tenantId) ? new LongValue(0L) : new LongValue(tenantId);
	}

	@Override
	public boolean ignoreInsert(List<Column> columns, String tenantIdColumn) {
		// https://baomidou.com/plugins/tenant/#_top
		return true;
	}

}
