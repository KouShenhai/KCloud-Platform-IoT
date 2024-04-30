/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import org.laokou.common.mybatisplus.context.DynamicTableSuffixContextHolder;

/**
 * @author laokou
 */
public class DynamicTableNameHandler implements TableNameHandler {

	@Schema(name = "PLACE_HOLDER", description = "分表标识符")
	public static final String PLACE_HOLDER = "$$";

	@Override
	public String dynamicTableName(String sql, String tableName) {
		if (tableName.endsWith(PLACE_HOLDER)) {
			return tableName.substring(0, tableName.length() - PLACE_HOLDER.length())
				.concat(DynamicTableSuffixContextHolder.get());
		}
		return tableName;
	}

}
