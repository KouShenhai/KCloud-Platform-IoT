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

package org.laokou.common.tenant.convertor;

import com.baomidou.dynamic.datasource.creator.DataSourceProperty;
import org.laokou.common.tenant.mapper.SourceDO;

/**
 * @author laokou
 */
public final class SourceConvertor {

	public static DataSourceProperty toDataSourceProperty(SourceDO sourceDO) {
		DataSourceProperty properties = new DataSourceProperty();
		properties.setUsername(sourceDO.getUsername());
		properties.setPassword(sourceDO.getPassword());
		properties.setUrl(sourceDO.getUrl());
		properties.setDriverClassName(sourceDO.getDriverClassName());
		return properties;
	}

}
