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

package org.laokou.admin.dict.convertor;

import org.laokou.admin.dict.dto.clientobject.DictCO;
import org.laokou.admin.dict.gatewayimpl.database.dataobject.DictDO;
import org.laokou.admin.dict.model.DictE;
import org.laokou.common.core.util.ConvertUtils;
import org.laokou.common.core.util.IdGenerator;
import org.laokou.common.i18n.util.ObjectUtils;

/**
 * 字典转换器.
 *
 * @author laokou
 */
public class DictConvertor {

	public static DictDO toDataObject(DictE dictE) {
		DictDO dictDO = ConvertUtils.sourceToTarget(dictE, DictDO.class);
		if (ObjectUtils.isNull(dictDO.getId())) {
			dictDO.setId(IdGenerator.defaultSnowflakeId());
		}
		return dictDO;
	}

	public static DictCO toClientObject(DictDO dictDO) {
		return ConvertUtils.sourceToTarget(dictDO, DictCO.class);
	}

	public static DictE toEntity(DictCO dictCO) {
		return ConvertUtils.sourceToTarget(dictCO, DictE.class);
	}

}
