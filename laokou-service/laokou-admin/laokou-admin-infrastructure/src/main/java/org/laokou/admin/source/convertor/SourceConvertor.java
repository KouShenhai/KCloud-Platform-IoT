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

package org.laokou.admin.source.convertor;

import org.laokou.admin.source.dto.clientobject.SourceCO;
import org.laokou.admin.source.model.SourceE;
import org.laokou.common.core.util.ConvertUtils;
import org.laokou.common.core.util.IdGenerator;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.tenant.mapper.SourceDO;

/**
 * 数据源转换器.
 *
 * @author laokou
 */
public class SourceConvertor {

	public static SourceDO toDataObject(SourceE sourceE) {
		SourceDO sourceDO = ConvertUtils.sourceToTarget(sourceE, SourceDO.class);
		if (ObjectUtils.isNull(sourceDO.getId())) {
			sourceDO.setId(IdGenerator.defaultSnowflakeId());
		}
		return sourceDO;
	}

	public static SourceCO toClientObject(SourceDO sourceDO) {
		return ConvertUtils.sourceToTarget(sourceDO, SourceCO.class);
	}

	public static SourceE toEntity(SourceCO sourceCO) {
		return ConvertUtils.sourceToTarget(sourceCO, SourceE.class);
	}

}
